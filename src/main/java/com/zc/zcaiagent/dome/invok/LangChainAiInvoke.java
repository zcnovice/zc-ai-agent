package com.zc.zcaiagent.dome.invok;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

import java.util.Scanner;

public class LangChainAiInvoke {

    public static void main(String[] args) {
        /* 与SpringAI很相识  */
        /*  */
        ChatLanguageModel qwenModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen-max")
                .build();

        String answer = qwenModel.chat("你是什么模型？");
        System.out.println(answer);
    }
}
