package com.mpouce.swingy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class Main 
{
    public static void main( String[] args )
    {
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
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                if (connection != null) {
                    System.out.println("Connected to the database!");
                } else {
                    System.out.println("Failed to make connection!");
                }
            } catch (SQLException e) {
                System.out.println("Error connecting to the database");
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
