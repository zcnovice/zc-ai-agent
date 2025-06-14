package com.zc.zcaiagent;

import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* 避免Spring启动时自动加载PgVectorStoreAutoConfiguration */
@SpringBootApplication(exclude = PgVectorStoreAutoConfiguration.class)
public class ZcAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZcAiAgentApplication.class, args);
    }

}
