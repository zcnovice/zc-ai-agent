package com.zc.zcaiagent.app;



import com.zc.zcaiagent.advisor.MyLoggerAdvisor;
import com.zc.zcaiagent.chatmemory.FileBasedChatMemory;
import com.zc.zcaiagent.chatmemory.FileBasedChatMemory1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "你是一个情感分析助手，你会分析用户的情感，并且给出一个情感分析结果";


    //构造函数
    public LoveApp(ChatModel dashscopeChatModel) {
        //初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/char-memory";
        ChatMemory chatMemory = new FileBasedChatMemory1(fileDir);

        //构建聊天记忆初始化
        //ChatMemory chatMemory = new InMemoryChatMemory();



        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                //添加MessageChatMemoryAdvisor顾问，将chatMemory绑定到客户端。
                .defaultAdvisors(
                        //对话记忆的拦截器
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
                )
                .build();
    }


    public String doChat(String msg,String chatId){
        ChatResponse chatResponse = chatClient
                // 创建一个新的对话请求构造器（Prompt对象或类似结构），表示开始定义一次对话请求的配置。
                .prompt()
                .user(msg)
                //也相当于是一个拦截器
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();

        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",content);
        return content;
    }


    //定义一个record 类(写过想相关笔记)
    record LoveReport(String title, List<String> suggestions) {
    }



    /* AI恋爱报告功能(实战结构化输出) */
    public LoveReport doChatWithReport(String msg,String chatId){
        LoveReport loveReport = chatClient
                // 创建一个新的对话请求构造器（Prompt对象或类似结构），表示开始定义一次对话请求的配置。
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(msg)
                //也相当于是一个拦截器
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);

        log.info("loveReport:{}",loveReport);
        return loveReport;
    }
}
