package org.example;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();

        try (Connection conn = db.connect()) {
            System.out.println("SQLite 연결 성공: " + conn.getMetaData().getURL());

            db.executeSqlFile(conn, "sql/init.sql");
            System.out.println("테이블 생성 및 샘플 데이터 삽입 완료");

            System.out.println("\n[users 테이블 전체 조회]");
            db.printAll(conn, "sql/select_all.sql");

        } catch (Exception e) {
            System.err.println("오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
