package com.mpouce.swingy.model.character;

import com.mpouce.swingy.model.utils.DatabaseUtils;
import com.mpouce.swingy.model.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class CharacterRepository {

    public CharacterRepository() {

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

    public void updateCharacter(Character character) {
        String prepStatement = "UPDATE characters SET experience=? WHERE id=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, character.getExperience());
            st.setInt(2, character.getId());
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
