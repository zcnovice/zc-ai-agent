package com.zc.zcaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

@Slf4j
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    /* 为这个增强器（Advisor）提供一个唯一标识名称。 */
    @Override
    public String getName() {
        /* 获取当前类的简单类名 */
        return this.getClass().getSimpleName();
    }

    /*  定义优先级  数值越小，优先级越高*/
    @Override
    public int getOrder() {
        return 0;
    }

    private AdvisedRequest before(AdvisedRequest request) {
        log.info("请求参数：{}", request.userText());
        return request;
    }

    private void observeAfter(AdvisedResponse response) {
        log.info("响应结果：{}", response.response().getResult().getOutput().getText());
        //输入的token数
        log.info("输入的token数：{}", response.response().getMetadata().getUsage().getPromptTokens());
        //输出的token数
        log.info("输出的token数：{}", response.response().getMetadata().getUsage().getCompletionTokens());
    }

    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        advisedRequest = this.before(advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        this.observeAfter(advisedResponse);
        return advisedResponse;
    }

    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        advisedRequest = this.before(advisedRequest);
        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);
        return (new MessageAggregator()).aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }

}
