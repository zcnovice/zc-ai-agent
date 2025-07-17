package com.zc.zcaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WebSearchToolTest {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    public void testSearchWeb() {
        /* 构造函数里面传入APIkey */
        WebSearchTool tool = new WebSearchTool(searchApiKey);
        String query = "蜗牛学院";
        String result = tool.searchWeb(query);
        assertNotNull(result);
    }
}