package com.yupi.oj.judge.codesandbox;

import com.yupi.oj.judge.codesandbox.imp.ExampleCodeSandBox;
import com.yupi.oj.judge.codesandbox.imp.RemoteCodeSandBox;
import com.yupi.oj.judge.codesandbox.imp.ThirdpartyCodeSandBox;

/**
 * 简单工厂模式
 */
public class CodeSandboxFactory {

    public static CodeSandbox createCodeSandbox(String type) {
        switch(type) {
            // case后的内容 可以封装成静态变量  确定不存在线程安全问题 可以使用 单例模式(单例模式推荐 静态内部类单例模式)
            case "exampe":
                return new ExampleCodeSandBox();
            case "remote":
                return  new RemoteCodeSandBox();
            case "third_party":
                return new ThirdpartyCodeSandBox();
            default:
                return new ExampleCodeSandBox();
        }
    }

}
