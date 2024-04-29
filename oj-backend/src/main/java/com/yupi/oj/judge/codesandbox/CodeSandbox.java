package com.yupi.oj.judge.codesandbox;

import com.yupi.oj.judge.codesandbox.model.ExecuteRequest;
import com.yupi.oj.judge.codesandbox.model.ExecuteResponse;

public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param request 执行请求
     * @return 执行结果
     */
    ExecuteResponse execute(ExecuteRequest request);
}
