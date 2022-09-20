package ru.practicum.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataBaseCleaner {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public DataBaseCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clean() {
        String sql =
                "DELETE FROM users;" +
                        "DELETE FROM bookings;" +
                        "ALTER TABLE bookings ALTER COLUMN id RESTART START WITH 1;" +
                        "DELETE FROM comments;" +
                        "ALTER TABLE comments ALTER COLUMN id RESTART START WITH 1;" +
                        "DELETE FROM items;" +
                        "ALTER TABLE items ALTER COLUMN id RESTART START WITH 1;" +
                        "DELETE FROM requests;" +
                        "ALTER TABLE requests ALTER COLUMN id RESTART START WITH 1;" +
                        "DELETE FROM users;" +
                        "ALTER TABLE users ALTER COLUMN id RESTART START WITH 1;";
        jdbcTemplate.update(sql);
    }
}