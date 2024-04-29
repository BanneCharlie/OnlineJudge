package com.yupi.oj.judge;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.yupi.oj.common.ErrorCode;
import com.yupi.oj.exception.BusinessException;
import com.yupi.oj.judge.codesandbox.CodeSandbox;
import com.yupi.oj.judge.codesandbox.CodeSandboxFactory;
import com.yupi.oj.judge.codesandbox.model.ExecuteRequest;
import com.yupi.oj.judge.codesandbox.model.ExecuteResponse;
import com.yupi.oj.judge.strategy.JudgeContext;
import com.yupi.oj.model.dto.question.JudgeCase;
import com.yupi.oj.model.dto.question.JudgeInfo;
import com.yupi.oj.model.entity.Question;
import com.yupi.oj.model.entity.QuestionSubmit;
import com.yupi.oj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.oj.model.vo.QuestionVO;
import com.yupi.oj.service.QuestionService;
import com.yupi.oj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImp implements  JudgeService {

    // 从配置文件中通过 value注解读取数据
    @Value("${codesandbox.type}")
    private String type;

    @Resource
    private QuestionService questionService;


    @Resource
    private QuestionSubmitService questionSubmitService;


    @Resource
    @Lazy
    private JudgeManager judgeManager;


    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        // 1.根据提交题目id  获取当前题目id
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交信息不存在");
        }
        Question question = questionService.getById(questionSubmit.getQuestionId());
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        // 1.1 题目提交完成后,将题目状态设置为 判题中状态 防止重复提交
        // 判断是否为等待状态 等待状态进行更改
        if (! questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"题目判题中请耐心等待");
        }
        // 1.2 更新判题的状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmit);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"判题状态更新错误");
        }

        // 2.执行代码沙箱

        // 1. 通过简单工厂模式创建实例
        CodeSandbox codeSandbox = CodeSandboxFactory.createCodeSandbox(type);

        // 2. 实例调用方法   传递参数 (1) 语言 (2) 代码 (3) 输入的测试用例(JSON格式转换)
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 获取判题用例转换为json格式的数组, 进行传递
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        // 获取对应的判题用例
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteRequest executeRequest = ExecuteRequest.builder()
                .language(language)
                .code(code)
                .inputList(inputList).build();

        ExecuteResponse executeResponse = codeSandbox.execute(executeRequest);


        // 3.根据输出用例, 进行比较 判断代码的执行
        List<String> outputList = executeResponse.getOutputList();

        // 5）根据沙箱的执行结果，设置题目的判题状态和信息  判断是否正确 无论是否正确都将会进行状态的修改
        JudgeContext judgeContext = new JudgeContext();

        judgeContext.setJudgeInfo(executeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);

        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        // 6）修改数据库中的判题结果
        questionSubmit = new QuestionSubmit();
        questionSubmit.setId(questionSubmitId);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmit);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmitId);
        return questionSubmitResult;
    }
}
