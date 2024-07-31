package com.mpouce.swingy.model;

import com.mpouce.swingy.controller.GameController;
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
    private int playerId;

    private Map() {

    }

    public static Map getInstance() {
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }

    public void initialize(Character character) {
        Location playerLocation = null;
        this.locations = null;
        this.playerId = character.getId();
        this.size = (character.getLevel() - 1) * 5 + 10;
        readMap(this.playerId);
        if (this.locations != null) {
            Location playerFoundLocation = LocationModel.getInstance().readAllMapCharacters(this);
            if (playerFoundLocation == null) {
                playerFoundLocation = this.locations[this.size / 2][this.size / 2];
            }
            character.setLocation(playerFoundLocation);
        } else {
            System.out.println("Creating new map with size " + this.size);
            this.createMap();
            this.locations = new Location[this.size][this.size];

            for (int x = 0; x < this.size; x++) {
                for  (int y = 0; y < this.size; y++) {
                    this.locations[x][y] = new Location(x, y, this.mapId);
                    this.locations[x][y].createLocation();
                }
            }
            GameController.getInstance().createPlayerLocation(this.locations[this.size / 2][this.size / 2]);
        }
    }

    public int getId() {
        return this.mapId;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    private void readMap(int characterId) {
        System.out.println("Reading map info");
        String prepStatement = "SELECT locations.map_id "
                                + "FROM character_location "
                                + "JOIN locations ON character_location.location_id = locations.id "
                                + "WHERE character_location.character_id=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, characterId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                this.mapId = rs.getInt("map_id");
                System.out.println("map id: " + this.mapId + " with size " + this.size);
                this.locations = LocationModel.getInstance().readAllMapLocations(this.mapId, this.size);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
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

    public void deleteMap() {
        System.out.println("Deleting map");
        String prepStatement = "DELETE FROM maps WHERE id=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection(). prepareStatement(prepStatement);
            st.setInt(1, this.mapId);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
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
