package com.zc.zcaiagent.dome.invok.Compoent_T;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


// 取消注释即可在 SpringBoot 项目启动时执行
//@Component
public class SpringAiAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel  dashscopeChatModel;


    /* 链接Redis */
        //    @Autowired
        //    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void run(String... args) throws Exception {
        AssistantMessage assistantMessage = dashscopeChatModel.call(new Prompt("你是什么模型"))
                .getResult()
                .getOutput();
        System.out.println(assistantMessage.getText());


        /* 链接Redis */
//        redisTemplate.opsForValue().set("ai_test", assistantMessage.getText());
//        Object value = redisTemplate.opsForValue().get("ai_test");
//        System.out.println("从Redis读取的值: " + value);
    }
}
