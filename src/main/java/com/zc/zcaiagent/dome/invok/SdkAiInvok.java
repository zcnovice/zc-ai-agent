package com.zc.zcaiagent.dome.invok;// 建议dashscope SDK的版本 >= 2.12.0
import java.util.Arrays;
import java.lang.System;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;

/**
 * 阿里云灵积大模型服务调用
 */
public class SdkAiInvok {
    public static GenerationResult callWithMessage() throws ApiException, NoApiKeyException, InputRequiredException {
        /* Generation 用于调用生成式 AI 模型的主入口 */
        Generation gen = new Generation();
        /* 构建系统消息 */
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                /* 告诉AI他是的角色 */
                .content("You are a helpful assistant.")
                .build();

        /* 构建用户消息 */
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("你好讲一下sentinel哨兵的配置了解配置哪些点。以及投票机制。")
                .build();

        /* 配置模型的参数 */
        /* GenerationParam: 配置模型调用的参数 */
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(TestApiKey.API_KEY)
                // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-plus")
                /* 消息列表   systemMsg- 系统信息  userMsg-用户信息 */
                .messages(Arrays.asList(systemMsg, userMsg))
                /* 控制返回结果的格式（MESSAGE 表示结构化消息）。 */
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }

    public static void main(String[] args) {
        try {
            GenerationResult result = callWithMessage();
            System.out.println(JsonUtils.toJson(result));
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            // 使用日志框架记录异常信息
            System.err.println("An error occurred while calling the generation service: " + e.getMessage());
        }
        System.exit(0);
    }
}