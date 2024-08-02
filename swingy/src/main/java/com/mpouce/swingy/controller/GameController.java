package com.mpouce.swingy.controller;

import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.character.CharacterRepository;
import com.mpouce.swingy.model.Location;
import com.mpouce.swingy.model.Map;
import com.mpouce.swingy.view.GameView;

public class GameController {
    private static GameController instance;
    private Character playerCharacter;
    private CharacterRepository characterModel;
    private GameView gameView;

    private GameController() {
        characterModel = CharacterRepository.getInstance();
    }

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public Character getPlayerCharacter() {
        return this.playerCharacter;
    }

    public void startGame(Character character) {
        this.playerCharacter = character;
        this.gameView = new GameView(this);
        Map.getInstance().initialize(this.playerCharacter);
        gameView.showGame(this.playerCharacter, Map.getInstance().getLocations());
    }

    public void createPlayerLocation(Location newLocation) {
        this.playerCharacter.setLocation(newLocation);
        characterModel.createCharacterLocation(this.playerCharacter);
    }

    public void playerMoveTo(Location newLocation) {
        this.playerCharacter.setLocation(newLocation);
        Map map = Map.getInstance();
        int mapSize = map.getSize();
        if (newLocation.isFinish(mapSize)) {
            System.out.println("Congrats on winning this round !");
            this.playerCharacter.addExp(mapSize * 100);
            characterModel.updateCharacter(this.playerCharacter);
            map.deleteMap();
            CharacterController.getInstance().startMenu();
        } else {
            newLocation.resolve(this.playerCharacter);
            characterModel.updateCharacterLocation(this.playerCharacter);
            characterModel.updateCharacter(this.playerCharacter);
            gameView.showGame(this.playerCharacter, map.getLocations());
        }
    }

    public int getPlayerId() {
        return this.playerCharacter.getId();
    }
}