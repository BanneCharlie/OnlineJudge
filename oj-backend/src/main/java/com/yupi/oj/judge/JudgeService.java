package com.yupi.oj.judge;

import com.yupi.oj.model.entity.QuestionSubmit;
import com.yupi.oj.model.vo.QuestionVO;
import org.springframework.stereotype.Service;

@Service
public interface JudgeService {
    QuestionSubmit doJudge(Long questionSubmitId);
}
