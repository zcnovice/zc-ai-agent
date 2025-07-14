package com.zc.zcaiagent.dome.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zcnovice
 * @data 2025/6/14 下午3:19
 */
@SpringBootTest
class MultiQueryExpanderDomeTest {

    @Resource
    private MultiQueryExpanderDome multiQueryExpanderDome;

    @Test
    void expand() {
        List<Query> queries = multiQueryExpanderDome.expand("谁是程序员鱼皮啊啊啊啊啊啊啊啊？，哈哈哈哈");
        System.out.println(queries);
    }
}