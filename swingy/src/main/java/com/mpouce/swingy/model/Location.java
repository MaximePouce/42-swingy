package com.mpouce.swingy.model;

import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.utils.DatabaseConnection;
import com.mpouce.swingy.model.utils.DatabaseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.util.Random;

public class Location {
    private int locationId;
    private int x;
    private int y;
    private Character character;
    private int mapId;

    public Location(int x, int y, int mapId, int size) {
        this.x = x;
        this.y = y;
        this.mapId = mapId;
        this.character = null;
        generateRandomEncounter(size);
    }

    private void generateRandomEncounter(int mapSize) {
        if (this.x == 0 || this.x == mapSize - 1) {
            return ;
        }
        if (this.y == 0 || this.y == mapSize = 1) {
            return ;
        }
        if (this.x == mapSize / 2 && this.y == mapSize / 2) {
            return ;
        }
        // random check to populate or not
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

    public int getId() {
        return this.locationId;
    }

    public void setId(int locationId) {
        this.locationId = locationId;
    }

    public int getMapId() {
        return this.mapId;
    }

    public String getImageName(int mapSize) {
        if (isFinish(mapSize)) {
            return "finish.png";
        }
        if (this.character != null) {
            return "battle.png";
        }
        return "";
    }

    public boolean isFinish(int mapSize) {
        return (this.x == 0 || this.x == mapSize - 1 || this.y == 0 || this.y == mapSize - 1);
    }

    public void createLocation() {
        this.locationId = LocationModel.getInstance().createLocation(this);
    }


    public void setCharacterById(int characterId) {
        this.character = LocationModel.getInstance().readCharacterFromId(characterId);
    }
}