package com.mpouce.swingy.model;

public class GameModel {
    public GameModel() {}

    public Location[][] getMap(int playerId, int playerLevel) {
        // Look for map saved
        // If not found, start a new one
        int mapSize = (playerLevel - 1) * 5 + 10;
        System.out.println("Creating new map with size " + mapSize);
        Location[][] map = new Location[mapSize][mapSize];

        // Initialize empty map
        for (int x = 0; x < mapSize; x++) {
            for  (int y = 0; y < mapSize; y++) {
                map[x][y] = new Location(x, y);
            }
        }

        return map;
    }
}