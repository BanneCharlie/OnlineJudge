package com.yupi.oj.judge.codesandbox.imp;

import com.yupi.oj.judge.codesandbox.CodeSandbox;
import com.yupi.oj.judge.codesandbox.model.ExecuteRequest;
import com.yupi.oj.judge.codesandbox.model.ExecuteResponse;
import com.yupi.oj.model.dto.question.JudgeInfo;
import com.yupi.oj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 测试代码沙箱  (跑通业务流程)
 */
public class ExampleCodeSandBox implements CodeSandbox {
    @Override
    public ExecuteResponse execute(ExecuteRequest request) {
        List<String> inputList = request.getInputList();


        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemory(1000L);
        judgeInfo.setTime(200L);
        judgeInfo.setMessage("哈哈哈哈");

        ExecuteResponse executeResponse = ExecuteResponse.builder()
                .outputList(inputList)
                .message("代码沙箱成功")
                .status(QuestionSubmitStatusEnum.SUCCEED.getValue())
                .judgeInfo(judgeInfo).build();

        return executeResponse;
    }
}
