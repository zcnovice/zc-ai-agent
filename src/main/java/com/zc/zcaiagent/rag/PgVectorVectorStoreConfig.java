package com.zc.zcaiagent.rag;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

/* 配置和初始化一个基于PostgreSQL的向量存储(Vector Store) */
//可以区分一下LoveAppVectorStoreConfig和PgVectorVectorStoreConfig
//LoveAppVectorStoreConfig是基于内存的向量存储，PgVectorVectorStoreConfig是基于PostgreSQL的向量存储
//@Configuration
public class PgVectorVectorStoreConfig {

    @Bean
    /**
     * @Description: 
     * @return: VectorStore
     * @Author:  zcnovice
     * @date:  2025/6/13 下午2:09
     */
    //这里是要有自动注入的JdbcTemplate和EmbeddingModel   (官方文档：https://docs.spring.io/spring-ai/reference/api/vectordbs/pgvector.html#_manual_configuration)
    /* 旁边绿叶可以跳转的 */
    //注意注入依赖的时候，pgVectorVectorStore的参数要和下面的方法的参数一致
    public VectorStore pgVectorVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel) {
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
                .dimensions(1536)                    // Optional: defaults to model dimensions or 1536
                .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
                .indexType(HNSW)                     // 带HNSW索引加速
                .initializeSchema(true)              // Optional: defaults to false
                .schemaName("public")                // Optional: defaults to "public"
                .vectorTableName("vector_store")     // Optional: defaults to "vector_store"
                .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
                .build();
        return vectorStore;
    }
}