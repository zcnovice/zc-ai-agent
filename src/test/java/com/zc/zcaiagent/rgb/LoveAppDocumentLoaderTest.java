package com.zc.zcaiagent.rgb;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoveAppDocumentLoaderTest {
    @Autowired
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Test
    void loadMarkdowns() {
        loveAppDocumentLoader.loadMarkdowns();
    }

}