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
            ResultSet rs = st.executeQuery(
                            "SELECT characters.characterid, characters.name, characters.experience, characters.classid "
                            + "FROM characters WHERE classid IS NOT NULL;");
            while (rs.next()) {
                CharacterClass newClass = characterClasses.get(rs.getInt("classid"));
                String newName = rs.getString("name");
                int newId = rs.getInt("characterid");
                int newExp = rs.getInt("experience");
                Character newCharacter = new Character(newName, newExp, newId, newClass);
                characters.add(newCharacter);
            }
        } catch (SQLException e) {
            System.out.println("Unable to connect to Database: " + e.getMessage());
        }
        return characters;
    }

    public int createCharacter(String name, int classId) {
        int newId = -1;
        String prepStatement = "INSERT INTO characters (name, experience, classid) VALUES (?, 0, ?);";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection()
                                    .prepareStatement(prepStatement, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, name);
            st.setInt(2, classId);
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
        String prepStatement = "DELETE FROM characters WHERE characterId=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, characterId);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void updateCharacter(Character character) {
        String prepStatement = "UPDATE characters SET experience=? WHERE characterid=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, character.getExperience());
            st.setInt(2, character.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
