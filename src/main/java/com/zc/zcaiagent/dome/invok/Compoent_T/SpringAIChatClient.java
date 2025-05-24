package com.zc.zcaiagent.dome.invok.Compoent_T;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SpringAIChatClient implements CommandLineRunner {

    @Resource
    @Qualifier("ollamaChatModel")
    /* 有一个小报错   我的模型有多个(dashscopeChatModel   ollamaChatModel)没有指定这里要用哪一个 */
    private ChatModel chatModel;

    @Override
    public void run(String... args) throws Exception {


        // 基础用法(ChatModel)
        //ChatResponse response = chatModel.call(new Prompt("你好"));

        // 高级用法(ChatClient)
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultSystem("你是恋爱顾问")
                .build();

//        String response = chatClient.prompt().user("我叫什么名字").call().content();
//        System.out.println(response);


        //------------------------------------------------------------------------------
        System.out.println("-----------------------");
        /* 测试返回对象 */
        //call()--用来触发ai调用的

//        //1.ChatResponse返回的是元数据
//        ChatResponse response1 = chatClient.prompt()
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


        //2.返回实体对象
        ActorFilms actorFilms = chatClient.prompt()
                .user("你会唱歌吗？")
                .call()
                .entity(ActorFilms.class);


        System.out.println(actorFilms);



    }

    /* java16正式引入 record 表示这是一个记录类，专门用于存储不可变数据。*/
    public record ActorFilms(String actor, List<String> movies){}
}
