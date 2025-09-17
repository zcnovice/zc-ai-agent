package com.zc.zcaiagent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DataSourceConfigTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDatabaseConnection() {
        assertDoesNotThrow(() -> {
            // 尝试获取数据库连接
            try (var connection = dataSource.getConnection()) {
                assertTrue(connection.isValid(1));
            }
        }, "数据库连接失败，请检查配置");
    }

    @Test
    void testSimpleQuery() {
        // 执行简单查询验证数据库可用性
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertEquals(1, result, "数据库查询失败");
    }
}