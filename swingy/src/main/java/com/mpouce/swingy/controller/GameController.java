package com.mpouce.swingy.controller;

import com.mpouce.swingy.model.artifact.Artifact;
import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.character.CharacterModel;
import com.mpouce.swingy.model.Location;
import com.mpouce.swingy.model.Map;
import com.mpouce.swingy.view.GameView;

public class GameController {
    private static GameController instance;
    private Character playerCharacter;
    private GameView gameView;

    private GameController() {
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
        this.gameView = new GameView();
        Map.getInstance().initialize(this.playerCharacter);
        gameView.showGame(this.playerCharacter, Map.getInstance().getLocations());
    }

    public void createPlayerLocation(Location newLocation) {
        this.playerCharacter.setLocation(newLocation);
        CharacterModel.createCharacterLocation(this.playerCharacter);
    }

    public void playerMoveTo(Location newLocation) {
        this.playerCharacter.setLocation(newLocation);
        Map map = Map.getInstance();
        int mapSize = map.getSize();
        if (newLocation.isFinish(mapSize)) {
            System.out.println("Congrats on winning this round !");
            this.playerCharacter.addExp(mapSize * 100);
            this.playerCharacter.healDamage(this.playerCharacter.getMaxHitPoints());
            CharacterModel.updateCharacter(this.playerCharacter);
            map.deleteMap();
            CharacterController.getInstance().startMenu();
        } else {
            newLocation.resolve(this.playerCharacter);
            if (this.playerCharacter.isDead()) {
                playerDeath();
            }
            CharacterModel.updateCharacterLocation(this.playerCharacter);
            CharacterModel.updateCharacter(this.playerCharacter);
            gameView.showGame(this.playerCharacter, map.getLocations());
        }
    }

    private void playerDeath() {
        Map.getInstance().deleteMap();
        CharacterController.getInstance().deleteCharacter(this.playerCharacter.getId());
        gameView.showDeathScreen(this.playerCharacter);
    }

    public void lootArtifact(Artifact lootedArtifact) {
        if (lootedArtifact == null) {
            System.out.println("Artifact is null, returning.");
            return ;
        }
        System.out.println("looting artifact " + lootedArtifact.getName());
        gameView.showArtifactDialog(this.playerCharacter, lootedArtifact);
    }

    public void equipArtifact(Artifact lootedArtifact) {
        if (lootedArtifact == null) {
            System.out.println("Artifact is null, returning.");
            return ;
        }
        System.out.println("equiping artifact " + lootedArtifact.getName());
        this.playerCharacter.equipArtifact(lootedArtifact);
    }

    public int getPlayerId() {
        return this.playerCharacter.getId();
    }

    public void closeScanner() {
        if (this.gameView != null) {
            this.gameView.closeScanner();
        }
    }
}