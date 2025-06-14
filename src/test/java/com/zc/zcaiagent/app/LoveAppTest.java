package com.zc.zcaiagent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员周豪";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我要制作一个AI相关的网页，你能给我一些建议吗？";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "重新帮我把你刚才说的简短的总结一下";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }


//    @Test
//    void doChatWithReport(){
//        String chatId = UUID.randomUUID().toString();
//        String message = "我想让另一半（瑞佳南）更爱我";
//        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
//        Assertions.assertNotNull(loveReport);
//    }


    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我已经结婚了，但是婚后关系不太亲密，怎么办？";
        String answer = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

}
