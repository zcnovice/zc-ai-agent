package com.zc.zcaiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于文件持久化的对话记忆
 */
public class FileBasedChatMemory implements ChatMemory {

    //指定存储路径
    private final String BASE_DIR;
    //Kryo序列化工具
    private static final Kryo kryo = new Kryo();

    //可以直接粘贴的
    static {
        kryo.setRegistrationRequired(false);
        // 设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }



    // 构造对象时，指定文件保存目录
    public FileBasedChatMemory(String dir) {
        //用户输入的路径
        this.BASE_DIR = dir;

        //创建目录
        File baseDir = new File(dir);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    /* 保存一条消息 */
//    @Override
//    public void add(String conversationId, Message message) {
//        saveConversation(conversationId,List.of(message));
//    }

    /* 保存多条消息 */
    @Override
    public void add(String conversationId, List<Message> messages) {
        /* 获取之前的消息 */
        List<Message> messageList = getOrCreateConversation(conversationId);
        messageList.addAll(messages);
        /* 保存消息 */
        saveConversation(conversationId, messageList);
    }

    /* 查询追后几条消息 */
    @Override
    public List<Message> get(String conversationId, int lastN) {
        /* 获取所有消息 */
        List<Message> messageList = getOrCreateConversation(conversationId);
        /* 跳过特定消息数(比如有7条，我只要3条，就是跳过4条) */
        return messageList.stream()
                .skip(Math.max(0, messageList.size() - lastN))
                .toList();
    }

    /* 清空所有消息 */
    @Override
    public void clear(String conversationId) {
        /* 通过会话id得到当前的会话文件 */
        File file = getConversationFile(conversationId);
        /* 如果文件存在，删除文件 */
        if (file.exists()) {
            file.delete();
        }

    }





    /**
     *获取对话信息
     * @param conversationId 会话id
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        /* 通过会话id得到当前的会话文件 */
        File file = getConversationFile(conversationId);

        List<Message> messages = new ArrayList<>();
        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
                /* 反序列化 (把会话信息装换成消息列表[ ArrayList.class])*/
                messages = kryo.readObject(input, ArrayList.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }


    /**
     *保存对话信息
     * @param conversationId 会话id
     * @param messages 消息列表
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        /* 通过会话id得到当前的会话文件 */
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 每个会话文件要单独保存
     * @param conversationId 会话id
     * @return
     */
    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }
}
