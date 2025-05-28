package com.zc.zcaiagent.chatmemory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.messages.GenericMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import org.springframework.ai.chat.messages.AssistantMessage; // 新增
import org.springframework.ai.chat.messages.UserMessage;    // 新增
import org.springframework.ai.chat.messages.SystemMessage; // 新增

import org.springframework.ai.model.Media;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.slf4j.Logger; // 引入 SLF4J Logger
import org.slf4j.LoggerFactory; // 引入 SLF4J LoggerFactory

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JdbcChatMemory implements ChatMemory {

    private static final Logger logger = LoggerFactory.getLogger(JdbcChatMemory.class); // 添加 SLF4J Logger

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    // 表名和列名常量
    private static final String TABLE_NAME = "chat_messages";
    private static final String COL_ID = "id";
    private static final String COL_CONVERSATION_ID = "conversation_id";
    private static final String COL_MESSAGE_CONTENT = "message_content";
    private static final String COL_MESSAGE_TYPE = "message_type";
    private static final String COL_MESSAGE_METADATA = "message_metadata";
    private static final String COL_CREATED_AT = "created_at";

    // SQL 语句 - 针对 MySQL 进行了调整
    // MySQL 通常可以直接处理 JSON 字符串的插入，如果列类型是 JSON
    // 如果 message_metadata 列是 TEXT，此语句也适用
    private static final String INSERT_MESSAGE_SQL = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
            TABLE_NAME, COL_CONVERSATION_ID, COL_MESSAGE_CONTENT, COL_MESSAGE_TYPE, COL_MESSAGE_METADATA, COL_CREATED_AT);

    private static final String SELECT_MESSAGES_SQL_TEMPLATE = String.format(
            "SELECT %s, %s, %s, %s FROM %s WHERE %s = ? ORDER BY %s DESC LIMIT ?",
            COL_MESSAGE_CONTENT, COL_MESSAGE_TYPE, COL_MESSAGE_METADATA, COL_CREATED_AT,
            TABLE_NAME, COL_CONVERSATION_ID, COL_CREATED_AT);

    private static final String CLEAR_MESSAGES_SQL = String.format(
            "DELETE FROM %s WHERE %s = ?",
            TABLE_NAME, COL_CONVERSATION_ID);

    /**
     * 构造函数.
     * @param jdbcTemplate 用于数据库操作的 JdbcTemplate.
     * @param objectMapper 用于序列化/反序列化消息元数据的 ObjectMapper.
     */
    public JdbcChatMemory(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 构造函数，使用默认的 ObjectMapper.
     * @param jdbcTemplate 用于数据库操作的 JdbcTemplate.
     */
    public JdbcChatMemory(JdbcTemplate jdbcTemplate) {
        this(jdbcTemplate, new ObjectMapper());
    }


    @Override
    public void add(String conversationId, List<Message> messages) {
        if (CollectionUtils.isEmpty(messages)) {
            return;
        }

        List<Object[]> batchArgs = new ArrayList<>();
        long currentTime = System.currentTimeMillis(); // 为批处理中的所有消息使用一致的时间戳

        for (Message message : messages) {
            String metadataJson = null;
            if (!CollectionUtils.isEmpty(message.getMetadata())) {
                try {
                    metadataJson = objectMapper.writeValueAsString(message.getMetadata());
                } catch (JsonProcessingException e) {
                    logger.error("序列化消息元数据时出错，会话ID: {}. 错误: {}", conversationId, e.getMessage(), e);
                    // 根据策略，可以选择将元数据设为 null 或一个表示错误的 JSON 对象，如 "{ \"error\": \"serialization_failed\" }"
                    // 这里我们简单地将其保留为 null，或者您可以选择抛出异常
                }
            }



            batchArgs.add(new Object[]{
                    conversationId,
                    message.getText(), // 改为使用 getText()
                    message.getMessageType().name(),
                    metadataJson,
                    new Timestamp(currentTime)
            });
//            batchArgs.add(new Object[]{
//                    conversationId,
//                    message.getContent(),
//                    message.getMessageType().name(), // 使用 MessageType 枚举的名称
//                    metadataJson, // 对于 MySQL 的 JSON 或 TEXT 类型，直接传递 JSON 字符串
//                    new Timestamp(currentTime) // 或者从 message 获取时间戳（如果可用）
//            });
        }
        jdbcTemplate.batchUpdate(INSERT_MESSAGE_SQL, batchArgs);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        if (lastN <= 0) {
            logger.warn("请求获取最后 {} 条消息，会话ID: {}. 返回空列表.", lastN, conversationId);
            return List.of();
        }
        return jdbcTemplate.query(
                SELECT_MESSAGES_SQL_TEMPLATE,
                new MessageRowMapper(objectMapper),
                conversationId,  // 第一个参数对应 SQL 中的 ?
                lastN            // 第二个参数对应 SQL 中的 ?
        );
    }
    @Override
    public void clear(String conversationId) {
        int deletedRows = jdbcTemplate.update(CLEAR_MESSAGES_SQL, conversationId);
        logger.info("已清除会话ID: {} 的聊天记录，删除了 {} 条消息.", conversationId, deletedRows);
    }

    private static class MessageRowMapper implements RowMapper<Message> {
        private final ObjectMapper objectMapper;

        public MessageRowMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            String content = rs.getString(COL_MESSAGE_CONTENT);
            String typeStr = rs.getString(COL_MESSAGE_TYPE);
            String metadataJson = rs.getString(COL_MESSAGE_METADATA);
            // Timestamp createdAt = rs.getTimestamp(COL_CREATED_AT); // 可选，如果需要将创建时间放回 Message 对象

            MessageType messageType;
            try {
                messageType = MessageType.valueOf(typeStr.toUpperCase()); // 转换为大写以增加兼容性
            } catch (IllegalArgumentException e) {
                logger.warn("在数据库中发现未知的消息类型: '{}'. 将其视为 SYSTEM 类型.", typeStr, e);
                messageType = MessageType.SYSTEM; // 或其他默认/回退类型
            }


            Map<String, Object> metadata = null;
            if (metadataJson != null && !metadataJson.trim().isEmpty() && !metadataJson.equalsIgnoreCase("null")) {
                try {
                    metadata = objectMapper.readValue(metadataJson, new TypeReference<Map<String, Object>>() {});
                } catch (JsonProcessingException e) {
                    logger.error("反序列化消息元数据时出错. 元数据内容: '{}'. 错误: {}", metadataJson, e.getMessage(), e);
                    // 可以选择将错误信息或原始 JSON 放入元数据
                    // metadata = Map.of("deserialization_error", e.getMessage(), "raw_metadata", metadataJson);
                }
            }

            if (!CollectionUtils.isEmpty(metadata)) {
                switch (messageType) {
                    case USER:
                        // 修复：直接传递 metadata Map 而不是强制转换为 List<Media>
                        UserMessage userMessage = new UserMessage(content);
                        if (metadata != null) {
                            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                                userMessage.getMetadata().put(entry.getKey(), entry.getValue());
                            }
                        }
                        return userMessage;

                    case ASSISTANT:
                        return new AssistantMessage(content, metadata);
                    case SYSTEM:
                        return new SystemMessage(content);
                    default:
                        return new SystemMessage(content);
                }
            } else {
                switch (messageType) {
                    case USER:
                        return new UserMessage(content);
                    case ASSISTANT:
                        return new AssistantMessage(content);
                    case SYSTEM:
                        return new SystemMessage(content);
                    default:
                        return new SystemMessage(content);
                }
            }
        }
    }
}