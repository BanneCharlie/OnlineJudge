package com.yupi.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.oj.model.entity.Question;
import com.yupi.oj.mapper.QuestionMapper;
import com.yupi.oj.service.QuestionService;
import org.springframework.stereotype.Service;

/**
* @author 86188
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-02-29 19:44:56
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {

}




