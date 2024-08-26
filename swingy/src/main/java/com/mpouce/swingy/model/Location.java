package com.mpouce.swingy.model;

import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.utils.DatabaseConnection;
import com.mpouce.swingy.model.utils.DatabaseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.util.Random;
import java.util.HashMap;

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

    public void generateRandomEncounter(int mapSize, HashMap<Integer, Character> enemies) {
        if (this.x == 0 || this.x == mapSize - 1) {
            return ;
        }
        if (this.y == 0 || this.y == mapSize - 1) {
            return ;
        }
        if (this.x == mapSize / 2 && this.y == mapSize / 2) {
            return ;
        }
        Random rand = new Random();
        int randomValue = rand.nextInt(7);
        int experience = Math.max(
            (int) (Math.pow(this.x - mapSize / 2, 2) * 100),
            (int) (Math.pow(this.y - mapSize / 2, 2) * 100)
        );
        if ((randomValue) % 3 == 0) {
            int level = Character.getLevel(experience);
            while (enemies.get(level) == null) {
                level--;
            }
            this.character = new Character(enemies.get(level));
        }
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

    public Character getCharacter() {
        return this.character;
    }

    public void resolve(Character playerCharacter) {
        if (this.character == null) {
            return;
        }
        playerCharacter.battle(this.character);
        if(this.character.getHitPoints() == 0) {
            LocationModel.deleteCharacterLocation(this.locationId);
            this.character = null;
        }
    }

    public void setCharacter(Character newCharacter) {
        this.character = newCharacter;
    }

    public String getImageName(int mapSize) {
        if (isFinish(mapSize)) {
            return "icons/finish.png";
        }
        if (this.character != null) {
            return "icons/battle.png";
        }
        return "";
    }

    public boolean isFinish(int mapSize) {
        return (this.x == 0 || this.x == mapSize - 1 || this.y == 0 || this.y == mapSize - 1);
    }

    public void createLocation() {
        this.locationId = LocationModel.createLocation(this);
    }

    public void setCharacterById(int characterId) {
        this.character = LocationModel.readCharacterFromId(characterId);
    }
}