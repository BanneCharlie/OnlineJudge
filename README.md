# 前端框架构架

- 前端接口代码生成器 (通用的代码生成插件)

  - https://github.com/ferdikoomen/openapi-typescript-codegen

  ```shell
  # 进行安装
  npm install openapi-typescript-codegen --save-dev
  
  # 进行配置
  openapi --input ./spec.json(接口文档 / 或者本地路径)   --output ./generated(生成的目录)  --client axios (生成的客户端为axios) 
  
  # npx 是一个用于运行项目依赖中的可执行文件的工具，可以确保正确查找和运行 openapi
  npx openapi --input http://localhost:8121/api/v2/api-docs --output ./generated  --client axios
  ```

  ![image-20240423190235613](https://banne.oss-cn-shanghai.aliyuncs.com/Java/image-20240423190235613.png) 

  `OpenAPI.ts` 

  ```ts
  export const OpenAPI: OpenAPIConfig = {
      BASE: 'http://localhost:8121',
      VERSION: '1.0',
      WITH_CREDENTIALS: true,  // 修改为 ture
      CREDENTIALS: 'include',
      TOKEN: undefined,
      USERNAME: undefined,
      PASSWORD: undefined,
      HEADERS: undefined,
      ENCODE_PATH: undefined,
  };
  ```

- 前端代码实现  

  Markdown编辑器 (https://github.com/bytedance/bytemd)   

  ```shell
  npm install  @bytemd/vue-next
  
  # 导入样式文件  main.ts中导入
  import 'bytemd/dist/index.css'
  ```

  ```vue
  
  <template>
    <Editor {value} {plugins} on:change={handleChange} />
  </template>
  
  <script setup>
    // 导入对应的vue组件  
    import { Editor, Viewer } from 'bytemd'
    import gfm from '@bytemd/plugin-gfm'
  
    let value
    const plugins = [
      gfm(),
      // Add more plugins here
    ]
  
    function handleChange(e) {
      value = e.detail.value
    }
  </script>
  ```

  ```shell
   # 安装两个插件
   npm install @bytemd/plugin-gfm @bytemd/plugin-highlight
  ```

  代码编辑器   微软代码编辑器


![image-20240429103337299](https://banne.oss-cn-shanghai.aliyuncs.com/Java/image-20240429103337299.png) 

![image-20240429104411253](https://banne.oss-cn-shanghai.aliyuncs.com/Java/image-20240429104411253.png) 



判题目模块: 调用代码沙箱, 把代码和输入交给代码沙箱去完成

代码沙箱模块 : 只接受代码和输入, 返回编译的结果, 不负负判断题目

两个模块之间完全解耦



代码沙箱模块  对象的创建 --> 简单工厂模式 + 构建者模式    将必要的参数设置为配置文件中, 更改方便  代理模式添加日志

判断题目的完整业务流程



# 前端优化

- 优化前端页面 样式的修改   对于提交的代码 返回的数据信息可以 在前端获取之后进行转换 再展示到前端页面中

![image-20240426133033079](https://banne.oss-cn-shanghai.aliyuncs.com/Java/image-20240426133033079.png)



`优化之后`  查看当前题目的信息 

![image-20240426220401275](https://banne.oss-cn-shanghai.aliyuncs.com/Java/image-20240426220401275.png)   

- 出现的BUG 前端页面中的 提交数目和判题数目 一直为 0 需要进行修改  

![image-20240427152730150](https://banne.oss-cn-shanghai.aliyuncs.com/Java/image-20240427152730150.png) 

- 展示题目生成的结果 

# 代码沙箱

通常指定的是一种安全机制, 用于在 Java 虚拟机中运行或不信任的代码时提供的保护, java的安全模型允许在一个`受信任的环境下运行未受信任的代码`, 通过实施严格的安全限制和访问控制来保证系统的安全

OJ 判题系统中的代码不一定绝对是可靠的 需要单独写一个服务 服务中实现代码沙箱安全机制, 去运行这些代码 防止对系统的恶意影响, 提高java应用程序的安全性.

![image-20240426120718632](https://banne.oss-cn-shanghai.aliyuncs.com/Java/image-20240426120718632.png) 

![image-20240426120659992](https://banne.oss-cn-shanghai.aliyuncs.com/Java/image-20240426120659992.png) 

- 手动实现 Java代码沙箱 功能     通过 maven 进行管理, 手动配置 JDK8 + Springboot 2.7 版本 (稳定可靠) 
  - 代码沙箱是另一个服务, 只实现代码的判断, 并不做具体的判断   需要重新创建一个项目单独来实现
    - 将用户输入的代码 存放到固定的文件夹  tempCode  通过UUID为每个文件生成对应的文件
    - Process 进程 执行命令行的代码  首先进行编译  接收返回的结果  
    - 执行命令行代码  执行程序
    - 整理输出 返回需要的结果    执行结果分为多种 创建具体的枚举类进行分析 / 前端根据 设置的属性 手动进行配置

- 代码沙箱的方法执行 流程为

  - QuestionController.doQuestionSubmit
  - QuestionSubmitService.doQuestionSubmit  -->   添加到题目提交表中(方便JudgeService服务获取)  -->judgeService.doJudge(questionSubmitId)
  - JudgeServiceImp.doJudge 获取到提交的题目信息 和 题目的判题用例 --> 选择代码沙箱(RemoteCodeSandbox) 代码 语言 输出用例进行传递
  - 进行换成后 返回执行的结果

  ![image-20240427150654615](https://banne.oss-cn-shanghai.aliyuncs.com/Java/image-20240427150654615.png) 

  - 测试用例, 进行判断

- 更改规则 针对控制台 输入进行判断题目 

- 编译代码

![image-20240427200215315](https://banne.oss-cn-shanghai.aliyuncs.com/Java/image-20240427200215315.png) 

- 运行代码

![image-20240427200242297](https://banne.oss-cn-shanghai.aliyuncs.com/Java/image-20240427200242297.png) 

- 仅仅返回输出的结果
