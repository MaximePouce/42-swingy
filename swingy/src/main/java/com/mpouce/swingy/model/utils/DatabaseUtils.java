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
            initializeArtifacts(conn);
            initializeCharacterArtifacts(conn);
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
        String[] enemies = {"Scavenger", "Bandit", "Outlaw", "Marauder", "Raider", "Brigand", "Thief", "Enforcer", "Warlord", "Dark Knight"};
        for (int i = 0; i < enemies.length; i++) {
            insertCharacter(conn, enemies[i], i);
        }
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

    private static void initializeArtifacts(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String createTypeSQL = "DO $$ "
                                + "BEGIN "
                                + "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'artifact_type') THEN "
                                + "        CREATE TYPE artifact_type AS ENUM ('helmet', 'armor', 'weapon'); "
                                + "    END IF; "
                                + "END $$;";
        stmt.execute(createTypeSQL);

        String createTableSQL = "CREATE TABLE IF NOT EXISTS artifacts ("
                                + "id SERIAL PRIMARY KEY, "
                                + "name VARCHAR(255) UNIQUE NOT NULL, "
                                + "level INT NOT NULL, "
                                + "type artifact_type NOT NULL, "
                                + "bonus INT NOT NULL"
                                + ");";
        stmt.execute(createTableSQL);

        insertArtifact(conn, "Leather Armor", 1, "armor");
        insertArtifact(conn, "Hide Armor", 2, "armor");
        insertArtifact(conn, "Half Plate Armor", 3, "armor");
        insertArtifact(conn, "Watcher Helm", 1, "helmet");
        insertArtifact(conn, "Helm of Dread", 2, "helmet");
        insertArtifact(conn, "Helm of the Gods", 3, "helmet");
        insertArtifact(conn, "Longsword", 1, "weapon");
        insertArtifact(conn, "Hellfire Axe", 2, "weapon");
        insertArtifact(conn, "Moonblade", 3, "weapon");
    }

    private static void initializeCharacterArtifacts(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS character_artifacts ("
                                + "character_id INT REFERENCES characters(id) ON DELETE CASCADE, "
                                + "artifact_id INT REFERENCES artifacts(id) ON DELETE CASCADE, "
                                + "PRIMARY KEY (character_id, artifact_id)"
                                + ");";
        stmt.execute(createTableSQL);
    }

    private static void insertArtifact(Connection conn, String name, int level, String type) throws SQLException {
        String insertArtifact = "INSERT INTO artifacts ("
                                + "name, level, type, bonus) "
                                + "VALUES (?, ?, ?::artifact_type, ?) "
                                + "ON CONFLICT (name) DO NOTHING;";
        PreparedStatement stmt = conn.prepareStatement(insertArtifact);
        stmt.setString(1, name);
        stmt.setInt(2, level);
        stmt.setString(3, type);
        stmt.setInt(4, 10 * (int) Math.pow(2, level - 1));
        stmt.executeUpdate();
    }

    private static void insertCharacter(Connection conn, String name, int level) throws SQLException {
        String insertCharacter = "INSERT INTO characters ( "
                                + "name, experience, current_hitpoints, max_hitpoints, attack, defense) "
                                + "SELECT ?, ?, ?, ?, ?, ? "
                                + "WHERE NOT EXISTS ( "
                                + "SELECT 1 FROM characters WHERE name=? AND experience=?);";
        int experience = (level * 1000 + (level - 1) * (level - 1) * 450);
        int hitPoints = 20 + 20 * level;
        int attack = 10 + 10 * level;
        int defense = 10 * level;
        PreparedStatement stmt = conn.prepareStatement(insertCharacter);
        stmt.setString(1, name);
        stmt.setString(7, name);
        stmt.setInt(2, experience);
        stmt.setInt(8, experience);
        stmt.setInt(3, hitPoints);
        stmt.setInt(4, hitPoints);
        stmt.setInt(5, attack);
        stmt.setInt(6, defense);
        stmt.executeUpdate();
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