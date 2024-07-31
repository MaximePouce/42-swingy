package com.mpouce.swingy.model;

import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.utils.DatabaseConnection;
import com.mpouce.swingy.model.utils.DatabaseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Map {
    private static Map instance;
    private int mapId = -1;
    private Location[][] locations;
    private int size;
    private int characterId;

    private Map() {

    }

    public static Map getInstance() {
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }

    public void initialize(Character character) {
        String prepStatement = "SELECT locationId FROM characters WHERE characterid = ?";
        Location playerLocation = null;
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, character.getId());
            ResultSet rs = st.executeQuery();
            DatabaseUtils.printResultSet(rs);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        
        if (playerLocation == null) {
            this.size = (character.getLevel() - 1) * 5 + 10;
            System.out.println("Creating new map with size " + this.size);
            this.createMap();
            locations = new Location[this.size][this.size];

            // Initialize empty map
            for (int x = 0; x < this.size; x++) {
                for  (int y = 0; y < this.size; y++) {
                    locations[x][y] = new Location(x, y, this.mapId);
                    locations[x][y].createLocation();
                }
            }
            character.setLocation(locations[this.size / 2][this.size / 2]);
        }
    }

    public int getId() {
        return this.mapId;
    }

    private void createMap() {
        System.out.println("Creating map");
        String prepStatement = "INSERT INTO maps (size) VALUES (?);";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection()
                                    .prepareStatement(prepStatement, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, this.size);
            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creation failed. No rows affected.");
            }
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                this.mapId = rs.getInt(1);
                System.out.println("id: " + this.mapId);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMap() {
        
    }

    public Location[][] getLocations() {
        return this.locations;
    }

    public int getSize() {
        return this.size;
    }
}
