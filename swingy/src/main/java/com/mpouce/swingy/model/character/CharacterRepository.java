package com.mpouce.swingy.model.character;

import com.mpouce.swingy.model.utils.DatabaseUtils;
import com.mpouce.swingy.model.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class CharacterRepository {

    public CharacterRepository() {

    }

    public List<Character> getCharacters() {
        List<Character> characters = new ArrayList<>();
        try {
            Statement st = DatabaseConnection.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(
                            "SELECT characters.characterid, characters.name, characters.experience, characters.classid "
                            + "FROM characters INNER JOIN classes ON characters.classid = classes.classid;");
            DatabaseUtils.printResultSet(rs);
        } catch (SQLException e) {
            System.out.println("Unable to connect to Database: " + e.getMessage());
        }
        return characters;
    }

    public void getClasses() {
        CharacterClassRepository test = new CharacterClassRepository();
        try {
            List<CharacterClass> classes = test.getClasses();
            System.out.println(classes.get(0).getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
