package com.zc.zcaiagent.dome.invok;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class SiliconflowAPI {

    public static void main(String[] args) {
        String apiUrl = "https://api.siliconflow.cn/v1/chat/completions";
        String token = TestApiKey.API_DeepSeek_R1; // 请替换为您的有效 token

        // 构建请求体 JSON 对象
        JSONObject requestBody = JSONUtil.createObj()
                .put("model", "deepseek-ai/DeepSeek-R1")
                .put("messages", JSONUtil.createArray()
                        .put(JSONUtil.createObj()
                                .put("role", "user")
                                .put("content", "我叫什么名字")))
                .put("stream", false)
                .put("max_tokens", 512)
                .put("enable_thinking", true)
                .put("thinking_budget", 4096)
                .put("min_p", 0.05)
                .put("stop", null) // Hutool 会自动处理 null 值，或者您可以选择不设置该键
                .put("temperature", 0.7)
                .put("top_p", 0.7)
                .put("top_k", 50)
                .put("frequency_penalty", 0.5)
                .put("n", 1)
                .put("response_format", JSONUtil.createObj().put("type", "text"))
                .put("tools", JSONUtil.createArray()
                        .put(JSONUtil.createObj()
                                .put("type", "function")
                                .put("function", JSONUtil.createObj()
                                        .put("description", "<string>")
                                        .put("name", "<string>")
                                        .put("parameters", JSONUtil.createObj()) // 空的 parameters 对象
                                        .put("strict", false))));

        // 发送 POST 请求
        String responseBody = HttpRequest.post(apiUrl)
                .header(Header.AUTHORIZATION, "Bearer " + token)
                .header(Header.CONTENT_TYPE, "application/json")
                .body(requestBody.toString())
                .execute()
                .body();

        // 输出响应结果
        System.out.println(responseBody);
    }
}