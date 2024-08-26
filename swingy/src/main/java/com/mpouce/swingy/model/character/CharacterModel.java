package com.mpouce.swingy.model.character;

import com.mpouce.swingy.model.artifact.Artifact;
import com.mpouce.swingy.model.artifact.ArtifactFactory;
import com.mpouce.swingy.model.utils.DatabaseUtils;
import com.mpouce.swingy.model.utils.DatabaseConnection;
import com.mpouce.swingy.model.Location;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class CharacterModel {

    private CharacterModel() {}

    public static List<Character> getCharacters(HashMap<Integer, CharacterClass> characterClasses) {
        List<Character> characters = new ArrayList<>();
        try {
            Statement st = DatabaseConnection.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM characters WHERE class_id IS NOT NULL ORDER BY id;");
            while (rs.next()) {
                CharacterClass newClass = characterClasses.get(rs.getInt("class_id"));
                String newName = rs.getString("name");
                int newId = rs.getInt("id");
                int newExp = rs.getInt("experience");
                Character newCharacter = new Character(newName, newExp, newId, newClass);
                newCharacter.setStats(
                    rs.getInt("max_hitpoints"),
                    rs.getInt("current_hitpoints"),
                    rs.getInt("attack"),
                    rs.getInt("defense")
                    );
                readCharacterArtifacts(newCharacter);
                characters.add(newCharacter);
            }
        } catch (SQLException e) {
            System.out.println("Unable to connect to Database: " + e.getMessage());
        }
        return characters;
    }

    public static HashMap<Integer, Character> readAllEnemies() {
        String query = "SELECT * FROM characters WHERE class_id IS NULL";
        HashMap<Integer, Character> enemies = new HashMap<Integer, Character>();
        try {
            Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Character newCharacter = new Character(
                    rs.getString("name"),
                    rs.getInt("experience"),
                    rs.getInt("max_hitpoints"),
                    rs.getInt("attack"),
                    rs.getInt("defense")
                );
                newCharacter.setId(rs.getInt("id"));
                enemies.put(newCharacter.getLevel(), newCharacter);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
        return enemies;
    }

    public static void createAllEnemies(Location[][] locations) {
        String prepStatement = "INSERT INTO characters "
                                + "(name, experience, current_hitpoints, max_hitpoints, attack, defense) "
                                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            PreparedStatement st = conn.prepareStatement(prepStatement, Statement.RETURN_GENERATED_KEYS);

            for (int x = 0; x < locations.length; x++) {
                for (int y = 0; y < locations.length; y++) {
                    Character character = locations[x][y].getCharacter();
                    if (character != null) {
                        st.setString(1, character.getName());
                        st.setInt(2, character.getExperience());
                        st.setInt(3, character.getHitPoints());
                        st.setInt(4, character.getMaxHitPoints());
                        st.setInt(5, character.getAttack());
                        st.setInt(6, character.getDefense());
                        st.addBatch();
                    }
                }
            }
            st.executeBatch();
            conn.commit();

            ResultSet rs = st.getGeneratedKeys();
            conn.setAutoCommit(true);
            for (int x = 0; x < locations.length; x++) {
                for (int y = 0; y < locations.length; y++) {
                    Character character = locations[x][y].getCharacter();
                    if (character != null) {
                        if (rs.next()) {
                            character.setId(rs.getInt(1));
                            createCharacterLocation(rs.getInt(1), locations[x][y].getId());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createEnemy(Character enemy, int locationId) {
        int newId = -1;
        String prepStatement = "INSERT INTO characters "
                                + "(name, experience, current_hitpoints, max_hitpoints, attack, defense) "
                                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, enemy.getName());
            st.setInt(2, enemy.getExperience());
            st.setInt(3, enemy.getHitPoints());
            st.setInt(4, enemy.getMaxHitPoints());
            st.setInt(5, enemy.getAttack());
            st.setInt(6, enemy.getDefense());
            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Create operation failed");
            }
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                newId = rs.getInt(1);
            }
            rs.close();
            enemy.setId(newId);
            createCharacterLocation(newId, locationId);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void createCharacterLocation(int characterId, int locationId) {
        String prepStatement = "INSERT INTO character_location "
                            + "(character_id, location_id) "
                            + "VALUES (?, ?)";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, characterId);
            st.setInt(2, locationId);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static int createCharacter(String name, int classId) {
        int newId = -1;
        String prepStatement = "INSERT INTO characters (name, experience, current_hitpoints, max_hitpoints, attack, defense, class_id) VALUES (?, 0, ?, ?, ?, ?, ?);";
        try {
            Statement classSt = DatabaseConnection.getInstance().getConnection().createStatement();
            ResultSet classRs = classSt.executeQuery("SELECT * from classes WHERE id=" + classId + ";");
            PreparedStatement st = DatabaseConnection.getInstance().getConnection()
                                    .prepareStatement(prepStatement, Statement.RETURN_GENERATED_KEYS);
            if (!classRs.next()) {
                throw new SQLException("Invalid class_id value");
            }
            st.setString(1, name);
            st.setInt(2, classRs.getInt("health"));
            st.setInt(3, classRs.getInt("health"));
            st.setInt(4, classRs.getInt("attack"));
            st.setInt(5, classRs.getInt("defense"));
            st.setInt(6, classId);
            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creation failed. No rows affected.");
            }
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                newId = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return newId;
    }

    public static void deleteCharacter(int characterId) {
        String prepStatement = "DELETE FROM characters WHERE id=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, characterId);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static Location readCharacterLocation(Location[][] locations, int characterId) {
        Location newLocation = null;
        String prepStatement = "SELECT x, y FROM locations INNER JOIN character_location "
                                + "ON character_location.character_id=? "
                                + "WHERE locations.id=character_location.location_id;";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, characterId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                newLocation = locations[rs.getInt("x")][rs.getInt("y")];
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return newLocation;
    }

    public static void updateCharacter(Character character) {
        String prepStatement = "UPDATE characters SET "
                                + "experience=?, current_hitpoints=?, "
                                + "max_hitpoints=?, attack=?, defense=? "
                                + "WHERE id=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, character.getExperience());
            st.setInt(2, character.getBaseHitPoints());
            st.setInt(3, character.getBaseMaxHitPoints());
            st.setInt(4, character.getBaseAttack());
            st.setInt(5, character.getBaseDefense());
            st.setInt(6, character.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void createCharacterLocation(Character character) {
        String prepStatement = "INSERT INTO character_location(character_id, location_id) VALUES(?, ?)";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, character.getId());
            st.setInt(2, character.getLocation().getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createAllCharacterLocations(Location[][] locations) {
        String prepStatement = "INSERT INTO character_location (character_id, location_id) VALUES (?, ?)";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            PreparedStatement st = conn.prepareStatement(prepStatement);

            for (int x = 0; x < locations.length; x++) {
                for (int y = 0; y < locations.length; y++) {
                    Character character = locations[x][y].getCharacter();
                    if (character != null) {
                        st.setInt(1, character.getId());
                        st.setInt(2, locations[x][y].getId());
                        st.addBatch();
                    }
                }
            }
            st.executeBatch();
            conn.commit();

            ResultSet rs = st.getGeneratedKeys();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void updateCharacterLocation(Character character) {
        String prepStatement = "UPDATE character_location SET location_id=? WHERE character_id = ?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, character.getLocation().getId());
            st.setInt(2, character.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void readCharacterArtifacts(Character character) {
        String query = "SELECT id, name, level, type, bonus FROM artifacts INNER JOIN character_artifacts "
                        + "ON character_artifacts.character_id=? "
                        + "WHERE artifacts.id = character_artifacts.artifact_id";
        try {
            PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            stmt.setInt(1, character.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String name = rs.getString("name");
                int level = rs.getInt("level");
                int bonus = rs.getInt("bonus");
                Artifact newArtifact = ArtifactFactory.createArtifact(id, type, name, level, bonus);
                character.equipArtifact(newArtifact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createCharacterArtifact(int characterId, int artifactId) {
        String query = "INSERT INTO character_artifacts (character_id, artifact_id) VALUES (?, ?)"
                        + "ON CONFLICT (character_id, artifact_id) DO NOTHING;";
        try {
            PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            stmt.setInt(1, characterId);
            stmt.setInt(2, artifactId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCharacterArtifact(int characterId, int artifactId) {
        String query = "DELETE FROM character_artifacts WHERE character_id=? AND artifact_id=?";
        try {
            PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            stmt.setInt(1, characterId);
            stmt.setInt(2, artifactId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
