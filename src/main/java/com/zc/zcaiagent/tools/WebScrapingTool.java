package com.zc.zcaiagent.tools;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

public class WebScrapingTool {

    @Tool(description = "Scrape the content of a web page")
    /* 注意这里的返回值是String */
    public String scrapeWebPage(@ToolParam(description = "URL of the web page to scrape") String url) {
        try {
        // Document是文档对象
            Document doc = Jsoup.connect(url).get();
            //转换为html 
            return doc.html();
        } catch (IOException e) {
            return "Error scraping web page: " + e.getMessage();
        }
    }
}