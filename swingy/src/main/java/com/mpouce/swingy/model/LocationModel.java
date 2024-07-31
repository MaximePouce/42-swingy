package com.mpouce.swingy.model;

import com.mpouce.swingy.controller.GameController;
import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.utils.DatabaseConnection;
import com.mpouce.swingy.model.utils.DatabaseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class LocationModel {

    private static LocationModel instance;

    private LocationModel() {}

    public static LocationModel getInstance() {
        if (instance == null) {
            instance = new LocationModel();
        }
        return instance;
    }

    public int createLocation(Location newLocation) {
        int newId = -1;
        String prepStatement = "INSERT INTO locations (x, y, map_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection()
                                    .prepareStatement(prepStatement, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, newLocation.getX());
            st.setInt(2, newLocation.getY());
            st.setInt(3, newLocation.getMapId());
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
        return newId;
    }

    public Location[][] readAllMapLocations(int mapId, int mapSize) {
        Location[][] locations = null;
        String prepStatement = "SELECT id, x, y FROM locations WHERE map_id=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, mapId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                locations = new Location[mapSize][mapSize];
                do {
                    int locationX = rs.getInt("x");
                    int locationY = rs.getInt("y");
                    locations[locationX][locationY] = new Location(locationX, locationY, mapId);
                    locations[locationX][locationY].setId(rs.getInt("id"));
                } while (rs.next());
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            locations = null;
        }
        return locations;
    }

    public Location readAllMapCharacters(Map map) {
        Location playerLocation = null;
        Location[][] locations = map.getLocations();
        String prepStatement = "SELECT locations.x, locations.y, character_location.character_id "
                                + "FROM locations INNER JOIN character_location "
                                + "ON locations.id = character_location.location_id "
                                + "WHERE locations.map_id=?;";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, map.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int characterId = rs.getInt("character_id");
                int locationX = rs.getInt("x");
                int locationY = rs.getInt("y");
                if (characterId == map.getPlayerId()) {
                    playerLocation = locations[locationX][locationY];
                }
                locations[locationX][locationY].setCharacterById(characterId);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return playerLocation;
    }

    public Character readCharacterFromId(int characterId) {
        Character character = null;
        String prepStatement = "SELECT id, name, experience, max_hitpoints, attack, defense "
                                + "FROM characters where id=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, characterId);
            ResultSet rs = st.executeQuery();
            int playerId = GameController.getInstance().getPlayerId();
            if (rs.next() && rs.getInt("id") != playerId) {
                character = new Character(
                    rs.getString("name"),
                    rs.getInt("experience"),
                    rs.getInt("max_hitpoints"),
                    rs.getInt("attack"),
                    rs.getInt("defense")
                );
            }
        } catch (SQLException e) {
            character = null;
            System.out.println("Database error: " + e.getMessage());
        }
        return character;
    }

    public void deleteLocation(Location location) {
        String prepStatement = "DELETE FROM locations WHERE id=?";
        try {
            PreparedStatement st = DatabaseConnection.getInstance().getConnection().prepareStatement(prepStatement);
            st.setInt(1, location.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}