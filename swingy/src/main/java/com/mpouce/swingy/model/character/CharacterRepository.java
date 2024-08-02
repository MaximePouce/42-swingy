package com.mpouce.swingy.model.character;

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

public class CharacterRepository {
    private static CharacterRepository instance;

    private CharacterRepository() {}

    public static CharacterRepository getInstance() {
        if (instance == null) {
            instance = new CharacterRepository();
        }
        return instance;
    }

    public List<Character> getCharacters(HashMap<Integer, CharacterClass> characterClasses) {
        List<Character> characters = new ArrayList<>();
        try {
            Statement st = DatabaseConnection.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM characters WHERE class_id IS NOT NULL;");
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
                characters.add(newCharacter);
            }
        } catch (SQLException e) {
            System.out.println("Unable to connect to Database: " + e.getMessage());
        }
        return characters;
    }

    public void createAllEnemies(Location[][] locations) {
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
            for (int x = 0; x < locations.length; x++) {
                for (int y = 0; y < locations.length; y++) {
                    Character character = locations[x][y].getCharacter();
                    if (character != null) {
                        if (rs.next()) {
                            character.setId(rs.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createEnemy(Character enemy, int locationId) {
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
            this.createCharacterLocation(newId, locationId);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void createCharacterLocation(int characterId, int locationId) {
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

    public int createCharacter(String name, int classId) {
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

    public void deleteCharacter(int characterId) {
        String prepStatement = "DELETE FROM characters WHERE id=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, characterId);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public Location readCharacterLocation(Location[][] locations, int characterId) {
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

    public void updateCharacter(Character character) {
        String prepStatement = "UPDATE characters SET "
                                + "experience=?, current_hitpoints=?, "
                                + "max_hitpoints=?, attack=?, defense=? "
                                + "WHERE id=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, character.getExperience());
            st.setInt(2, character.getHitPoints());
            st.setInt(3, character.getMaxHitPoints());
            st.setInt(4, character.getAttack());
            st.setInt(5, character.getDefense());
            st.setInt(6, character.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void createCharacterLocation(Character character) {
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

    public void updateCharacterLocation(Character character) {
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
}
