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
        /* 创建一个JSON对象 */
        JSONObject requestBody = JSONUtil.createObj()
                /* 对参数进行配置 */
                .put("model", "deepseek-ai/DeepSeek-R1")
                /* messages里面还有JSON对象 */
                .put("messages", JSONUtil.createArray()
                        .put(JSONUtil.createObj()
                                .put("role", "user")
                                .put("content", "我叫什么名字")))
                /* 流式输出 */
                .put("stream", false)
                /* 生成的最大token数，控制回复长度。 */
                .put("max_tokens", 512)
                /* 可能启用模型的“思考”模式 */
                .put("enable_thinking", true)
                /* 思考预算，控制“思考”模式下的最大生成长度。 */
                .put("thinking_budget", 4096)
                .put("min_p", 0.05)
                .put("stop", null) // Hutool 会自动处理 null 值，或者您可以选择不设置该键
                /* 控制生成的文本的多样性。 */
                .put("temperature", 0.7)
                /* 核采样概率，控制生成多样性*/
                .put("top_p", 0.7)
                /* 仅从top_k个token中选择。 */
                .put("top_k", 50)
                /* 频率惩罚，避免重复内容（正值减少重复）。 */
                .put("frequency_penalty", 0.5)
                /* 生成回复条数 */
                .put("n", 1)
                /* 指定响应格式为纯文本。 */
                .put("response_format", JSONUtil.createObj().put("type", "text"))
                /* 定义了一个空的函数工具 */
                .put("tools", JSONUtil.createArray()
                        .put(JSONUtil.createObj()
                                .put("type", "function")
                                .put("function", JSONUtil.createObj()
                                        .put("description", "<string>")
                                        .put("name", "<string>")
                                        .put("parameters", JSONUtil.createObj()) // 空的 parameters 对象
                                        .put("strict", false))));

        // 发送 POST 请求
        /* HttpRequest.post(apiUrl)  初始化一个POST对象  并且目标地址是apiUrl */
        String responseBody = HttpRequest.post(apiUrl)
                /* 添加请求头  秘钥就是token(之前设置了的) */
                .header(Header.AUTHORIZATION, "Bearer " + token)
                .header(Header.CONTENT_TYPE, "application/json")
                /* 将之前构建的 JSONObject 转换为字符串，并作为请求体发送。 */
                .body(requestBody.toString())
                /* 发送请求到服务器，并返回一个 HttpResponse 对象。 */
                .execute()
                /* 从 HttpResponse 中提取响应体的字符串内容（即服务器返回的 JSON 数据）。 */
                .body();


        // 输出响应结果
        System.out.println(responseBody);
    }
}