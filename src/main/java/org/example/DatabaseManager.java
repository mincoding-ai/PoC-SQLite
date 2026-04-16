package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:./data.db";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void executeSqlFile(Connection conn, String resourcePath) throws SQLException, IOException {
        String sql = loadResource(resourcePath);
        // 세미콜론 기준으로 구문 분리 후 순차 실행
        for (String statement : sql.split(";")) {
            String trimmed = statement.trim();
            if (!trimmed.isEmpty()) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(trimmed);
                }
            }
        }
    }

    public void printAll(Connection conn, String resourcePath) throws SQLException, IOException {
        String sql = loadResource(resourcePath).trim();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("┌────┬──────────┐");
            System.out.println("│ ID │ Name     │");
            System.out.println("├────┼──────────┤");
            while (rs.next()) {
                System.out.printf("│ %-2d │ %-8s │%n", rs.getInt("id"), rs.getString("name"));
            }
            System.out.println("└────┴──────────┘");
        }
    }

    private String loadResource(String resourcePath) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("리소스를 찾을 수 없습니다: " + resourcePath);
            }
            return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        }
    }
}
