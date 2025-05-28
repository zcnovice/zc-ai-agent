package com.zc.zcaiagent.JDBCServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MyService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        // 现在你可以使用 jdbcTemplate 了
    }
}