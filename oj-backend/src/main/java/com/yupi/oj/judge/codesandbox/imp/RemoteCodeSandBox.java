package com.yupi.oj.judge.codesandbox.imp;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.oj.common.ErrorCode;
import com.yupi.oj.exception.BusinessException;
import com.yupi.oj.judge.codesandbox.CodeSandbox;
import com.yupi.oj.judge.codesandbox.model.ExecuteRequest;
import com.yupi.oj.judge.codesandbox.model.ExecuteResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
/**
 * 远程代码沙箱
 */
public class RemoteCodeSandBox implements CodeSandbox {
    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";


    @Override
    public ExecuteResponse execute(ExecuteRequest executeRequest) {
        log.info("远程代码沙箱");
        String url = "http://localhost:8090/executeCode";
        String json = JSONUtil.toJsonStr(executeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();

        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }

        return JSONUtil.toBean(responseStr, ExecuteResponse.class);
    }
}
