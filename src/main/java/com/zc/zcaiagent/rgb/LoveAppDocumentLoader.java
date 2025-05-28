package com.zc.zcaiagent.rgb;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用文档加载器
 */

@Component
@Slf4j
public class LoveAppDocumentLoader {
    /* 这是 Spring 框架提供的资源模式解析接口，主要用于解析类路径下的资源文件
    * 使用 final 修饰符确保依赖不可变，保证线程安全*/
    private final ResourcePatternResolver resourcePatternResolver;

    public LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }


    /**
     * 这段 Java 代码定义了一个名为 loadMarkdowns 的公共方法，
     * 其目的是从类路径（classpath）下的特定目录加载多个 Markdown 文件，
     * 并将它们解析为 Document 对象列表。
     * @return
     */
    public List<Document> loadMarkdowns() {
        /* 存储从所有 Markdown 文件中读取和解析出的 Document 对象。 */
        List<Document> allDocuments = new ArrayList<>();
        try {
            // 这里可以修改为你要加载的多个 Markdown 文件的路径模式
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            for (Resource resource : resources) {
                /* 获取文件名 */
                String fileName = resource.getFilename();
                /* 配置 Markdown 文档读取器: */  //可以直接拷贝
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        /* 给文档加上额外的信息 (这里加的是文件名) */
                        .withAdditionalMetadata("filename", fileName)
                        .build();

                /*                                                          资源对象   配置对象 */
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("Markdown 文档加载失败", e);
        }

        /* 最终返回所有文档列表 */
        return allDocuments;
    }
}
