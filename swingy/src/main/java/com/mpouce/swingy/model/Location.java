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
}