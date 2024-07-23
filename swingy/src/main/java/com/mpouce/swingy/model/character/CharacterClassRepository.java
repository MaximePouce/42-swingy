package com.mpouce.swingy.model.character;

import com.mpouce.swingy.model.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CharacterClassRepository {

    public CharacterClassRepository() {

    }

    public List<CharacterClass> getClasses() {
        List<CharacterClass> classes = new ArrayList<>();
        try {
            Statement st = DatabaseConnection.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM classes");
            while (rs.next()) {
                CharacterClass newClass = new CharacterClass(rs);
                classes.add(newClass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }
}