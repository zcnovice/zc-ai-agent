package com.zc.zcaiagent.rgb;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zcnovice
 * @data 2025/6/13 下午2:33
 */
@SpringBootTest
class PgVectorVectorStoreConfigTest {
    @Resource
    //注意注入依赖的时候，pgVectorVectorStore的参数要和@Bean的参数一致
    private VectorStore pgVectorVectorStore;

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Test
    void test() {
        //我自己的文档
        List<Document> documents = List.of(
                /* map是添加标签的意思 */
                new Document("Java基础：面向对象、集合框架、多线程编程", Map.of("category", "编程语言")),
                new Document("MySQL基础：SQL语法、索引优化、事务隔离级别", Map.of("category", "数据库")),
                new Document("HTML基础：标签语义化、表单验证、CSS选择器", Map.of("category", "前端技术"))
        );
        // 添加文档
        pgVectorVectorStore.add(documents);
        // 相似度查询
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("怎么学习编程").topK(3).build());
        Assertions.assertNotNull(results);
    }

    @Test
    void test2() {
        List<Document> doucuments = loveAppDocumentLoader.loadMarkdowns();
        pgVectorVectorStore.add(doucuments);
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("女朋友不高兴怎么办？").topK(3).build());
        Assertions.assertNotNull(results);
    }

}