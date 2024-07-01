package com.mpouce.swingy.model.character;

import com.mpouce.swingy.model.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;

public class CharacterRepository {
    private Connection conn;

    public CharacterRepository() {
        Dotenv dotenv = null;
        try {
            dotenv = Dotenv.configure().directory("..").load();
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
        
        String url = "jdbc:postgresql://localhost:5432/" + dotenv.get("DB_NAME");
        String username = dotenv.get("DB_USR");
        String password = dotenv.get("DB_PWD");

        try {
            conn = DriverManager.getConnection(url, username, password);
            if (conn != null) {
                System.out.println("Connected to the database!");
                System.out.println(DatabaseUtils.doesTableExist(conn, "classes"));
                if (!DatabaseUtils.doesTableExist(conn, "classes")) {
                    DatabaseUtils.initializeDatabase(conn);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            System.exit(1);
        }
    }

    public void getClasses() {
        if (conn == null){
            System.exit(1);
        }
        // try {
            DatabaseUtils.printAllTableRows(conn, "classes");
        // } catch (SQLException e) {
            // e.printStackTrace();
        // }
    }
}
