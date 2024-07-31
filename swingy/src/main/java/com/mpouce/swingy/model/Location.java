package com.mpouce.swingy.model;

import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.utils.DatabaseConnection;
import com.mpouce.swingy.model.utils.DatabaseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Location {
    private int locationId;
    private int x;
    private int y;
    private Character character;
    private int mapId;

    public Location(int x, int y, int mapId) {
        this.x = x;
        this.y = y;
        this.mapId = mapId;
        this.character = null;
    }

    public Location(int x, int y, Character character) {
        this.x = x;
        this.y = y;
        this.character = character;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String getImageName(int mapSize) {
        if (isFinish(mapSize)) {
            return "finish.png";
        }
        if (this.character != null) {
            return "enemy.png";
        }
        return "";
    }

    public boolean isFinish(int mapSize) {
        return (this.x == 0 || this.x == mapSize - 1 || this.y == 0 || this.y == mapSize - 1);
    }

    public void createLocation() {
        int newId = -1;
        String prepStatement = "INSERT INTO locations (x, y, mapId) VALUES (?, ?, ?)";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection()
                                    .prepareStatement(prepStatement, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, this.x);
            st.setInt(2, this.y);
            st.setInt(3, this.mapId);
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
            System.out.println(e.getMessage());
        }
        this.locationId = newId;
    }
}