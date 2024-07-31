package com.mpouce.swingy.model.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DatabaseUtils {
    public static boolean doesTableExist(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, tableName, null);
        return tables.next();
    }

    public static void initializeDatabase(Connection conn) {
        try {
            initializeClasses(conn);
            initializeMap(conn);
            initializeLocation(conn);
            initializeCharacters(conn);
            initializeCharacterLocation(conn);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeClasses(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS classes ("
                             + "id SERIAL PRIMARY KEY, "
                             + "name VARCHAR(255) UNIQUE NOT NULL, "
                             + "health INT NOT NULL, "
                             + "attack INT NOT NULL, "
                             + "defense INT NOT NULL, "
                             + "health_growth INT NOT NULL, "
                             + "attack_growth INT NOT NULL, "
                             + "defense_growth INT NOT NULL"
                             + ");";

        stmt.execute(createTableSQL);

        String insertFighter = "INSERT INTO classes ("
                            + "name, health, attack, defense, health_growth, attack_growth, defense_growth)"
                            + "VALUES ('Fighter', 100, 10, 10, 20, 5, 5) "
                            + "ON CONFLICT (name) DO NOTHING;";

        String insertRogue = "INSERT INTO classes ("
                            + "name, health, attack, defense, health_growth, attack_growth, defense_growth)"
                            + "VALUES ('Rogue', 80, 15, 5, 15, 10, 0) "
                            + "ON CONFLICT (name) DO NOTHING;";

        stmt.execute(insertFighter);
        stmt.execute(insertRogue);
    }

    private static void initializeMap(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS maps ("
                                + "id SERIAL PRIMARY KEY, "
                                + "size INT NOT NULL"
                                + ");";
        stmt.execute(createTableSQL);
    }

    private static void initializeLocation(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS locations ("
                                + "id SERIAL PRIMARY KEY, "
                                + "x INT NOT NULL, "
                                + "y INT NOT NULL, "
                                + "map_id INT NOT NULL, "
                                + "FOREIGN KEY (map_id) REFERENCES maps(id) ON DELETE CASCADE"
                                + ");";
        stmt.execute(createTableSQL);
    }

    private static void initializeCharacters(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS characters ("
                                + "id SERIAL PRIMARY KEY, "
                                + "name VARCHAR(255) NOT NULL, "
                                + "experience BIGINT NOT NULL, "
                                + "current_hitpoints INT NOT NULL, "
                                + "max_hitpoints INT NOT NULL, "
                                + "attack INT NOT NULL, "
                                + "defense INT NOT NULL, "
                                + "class_id INT, "
                                + "FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE"
                                + ");";

        stmt.execute(createTableSQL);
    }

    private static void initializeCharacterLocation(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS character_location ("
                                + "id SERIAL PRIMARY KEY, "
                                + "character_id INT NOT NULL, "
                                + "location_id BIGINT NOT NULL, "
                                + "FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE, "
                                + "FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE"
                                + ");";

        stmt.execute(createTableSQL);
    }

    public static void printAllTableRows(Connection conn, String tableName) {
        try {
            System.out.println("current " + tableName + " is:");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + tableName);
            if (!rs.isBeforeFirst()) {
                System.out.println("no rows found");
            }
            else {
                System.out.println("types:");
                for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
                    System.out.print(rs.getMetaData().getColumnName(i) + ":" + rs.getMetaData().getColumnTypeName(i) + " ");
                }
                System.out.println("");
                printResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void printResultSet(ResultSet rs) {
        try {
            while (rs.next()) {
                for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
                    System.out.print(rs.getMetaData().getColumnName(i) + ": " + rs.getObject(i));
                    if (i < rs.getMetaData().getColumnCount()) {
                        System.out.print("; ");
                    }
                }
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}