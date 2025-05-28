package com.zc.zcaiagent.dome.invok.Compoent_T;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;

import reactor.core.publisher.Flux;


//@Component
public class SpringAIChatClient implements CommandLineRunner {

    @Resource
    @Qualifier("dashscopeChatModel")
    //
    /* 有一个小报错   我的模型有多个(dashscopeChatModel   ollamaChatModel)没有指定这里要用哪一个 */
    private ChatModel chatModel;

    @Override
    public void run(String... args) throws Exception {


        // 基础用法(ChatModel)
        //ChatResponse response = chatModel.call(new Prompt("你好"));

        // 高级用法(ChatClient)
        //这段代码是在创建并配置一个AI聊天客户端
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultSystem("你是编程专家")
                .build();

//        String response = chatClient.prompt().user("我叫什么名字").call().content();
//        System.out.println(response);


        //------------------------------------------------------------------------------
        System.out.println("-----------------------");
        /* 测试返回对象 */
        //call()--用来触发ai调用的

//        //1.ChatResponse返回的是元数据
//        ChatResponse response1 = chatClient
//        //创建一个新的对话请求构造器（Prompt对象或类似结构），表示开始定义一次对话请求的配置。
//                .prompt()
//                .user("你会一些什么")
//                .call()
//                .chatResponse();
//        System.out.println(response1);
//
//
//        //测试打印输入的token
//        System.out.println(response1.getMetadata().getUsage().getPromptTokens());
//        //测试打印输出的token
//        //测试打印输入的token
//        System.out.println(response1.getMetadata().getUsage().getCompletionTokens());
//
//        System.out.println("-----------------------");


        //------------------------------------------------------------------------------


//        //2.返回实体对象
//        ActorFilms actorFilms = chatClient.prompt()
//                .user("你会唱歌吗？")
//                .call()
//                .entity(ActorFilms.class);
//
//        System.out.println("----"+actorFilms+"----");
//        System.out.println("-----------------------");


        //3.返回流式对象
        Flux<String> stream = chatClient.prompt()
                .user("人为什么不能用二进制交流")
                .stream()
                .content();


/*
        Flux可以从Stream中创建()
        上面的代码就是流式对象
*/

        //下面使用简单的订阅模式来打印
        stream.subscribe(
                data -> System.out.print(data), // 收到每个数据块时处理
                error -> System.err.println("Error: " + error), // 错误处理
                () -> System.out.println("\nStream completed!") // 完成回调
        );

// 需要阻塞主线程（测试用）
        //Thread.sleep(5000);

    }

    /* java16正式引入 record 表示这是一个记录类，专门用于存储不可变数据。*/
    public record ActorFilms(boolean additionalProperties){

    }
}
