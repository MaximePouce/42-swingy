package com.mpouce.swingy.controller;

import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.character.CharacterRepository;
import com.mpouce.swingy.model.Location;
import com.mpouce.swingy.model.GameModel;
import com.mpouce.swingy.view.GameView;

public class GameController {
    private static GameController instance;
    private Character playerCharacter;
    private Location[][] map;
    private GameModel gameModel;
    private GameView gameView;

    private GameController() {}

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public void startGame(Character character) {
        this.playerCharacter = character;
        this.gameModel = new GameModel();
        this.gameView = new GameView(this);
        this.map = gameModel.getMap(character);
        System.out.println("Map is ready, starting GAME");
        gameView.showGame(this.playerCharacter, this.map);
        // gameView.displaySideMenu(this.playerCharacter);
    }

    public void playerMoveTo(Location newLocation) {
        this.playerCharacter.setLocation(newLocation);
        int mapSize = map.length;
        if (newLocation.isFinish(mapSize)) {
            System.out.println("Congrats on winning this round !");
            this.playerCharacter.addExp(mapSize * 100);
            CharacterRepository characterModel = new CharacterRepository();
            characterModel.updateCharacter(this.playerCharacter);
            CharacterController.getInstance().startMenu();
        } else {
            gameView.showGame(this.playerCharacter, this.map);
        }
    }
}