package com.mpouce.swingy.model;

import com.mpouce.swingy.model.character.Character;

public class Location {
    private int x;
    private int y;
    private Character character;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
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
}