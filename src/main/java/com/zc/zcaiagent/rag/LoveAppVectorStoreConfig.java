package com.zc.zcaiagent.rag;


import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 实现初始化向量数据库并且保存文档的方法
 */

/* 初始化基于内存的向量数据库 */
//@Configuration
public class LoveAppVectorStoreConfig {
    /* 引入文档前面(刚才)定义的加载器 */
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Resource
    private MyKeywordEnricher myKeywordEnricher;
    
    /* (loveAppVectorStore就是Bean的名字)   开始嵌入大模型dashscopeEmbeddingModel*/
    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {

        //使用构建器模式创建内存向量存储实例
        /* SimpleVectorStore.builder() 会创建一个内存向量存储的构建器
           .dashscopeEmbeddingModel 参数用于注入文本嵌入模型（这里用的是阿里云 DashScope 的嵌入模型）
            .build() 会最终创建向量存储实例， */
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        // 加载文档
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        //把加载的文档切割了
        //List<Document> splitDocuments =myTokenTextSplitter.splitDocuments(documents);

        //元数据增强器
        List<Document> enrichDocuments = myKeywordEnricher.enrichDocuments(documents);
        //存放文档切割后的结果到向量数据库中
        simpleVectorStore.add(enrichDocuments);
        return simpleVectorStore;
    }

}
