package com.mpouce.swingy.model.character;

import com.mpouce.swingy.model.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class CharacterRepository {

    public CharacterRepository() {

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
