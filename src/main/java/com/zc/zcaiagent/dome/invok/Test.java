package com.zc.zcaiagent.dome.invok;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


//@Component
public class Test implements CommandLineRunner {
    @Resource
    @Qualifier("dashscopeChatModel")
    private ChatModel openaiChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage output = openaiChatModel.call(new Prompt("My name is Tom"))
                .getResult()
                .getOutput();
        System.out.println(output.getText());

    }
}
