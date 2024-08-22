package com.mpouce.swingy.controller;

import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.character.CharacterClass;
import com.mpouce.swingy.model.character.CharacterRepository;
import com.mpouce.swingy.model.character.CharacterClassRepository;
import com.mpouce.swingy.view.CharacterView;
import com.mpouce.swingy.view.MenuView;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class CharacterController {
    private static CharacterController instance;
    private Character player;
    private CharacterView characterView;
    private MenuView menuView;
    private CharacterRepository characterModel;
    private CharacterClassRepository characterClassModel;

    private CharacterController() {
        characterModel = CharacterRepository.getInstance();
        characterClassModel = new CharacterClassRepository();
        characterView = new CharacterView();
        menuView = new MenuView();
    }

    public static CharacterController getInstance() {
        if (instance == null) {
            instance = new CharacterController();
        }
        return instance;
    }

    public void getCharacters() {
        HashMap<Integer, CharacterClass> characterClasses = characterClassModel.getClasses();
        List<Character> characters = characterModel.getCharacters(characterClasses);
        characterView.showCharacters(characters);
    }

    public void newCharacter(String name, int classId) {
        if (name.isEmpty()) {
            name = "Unknown Adventurer";
        } else if (name.length() > 15) {
            name = name.substring(0, 14);
        }
        int charId = characterModel.createCharacter(name, classId);
        if (charId == -1) {
            System.out.println("An error occured during the character creation.");
            return;
        }
        getCharacters();
    }

    public void selectCharacter(Character character) {
        System.out.println("Selected character #" + character.getId());
        if (character.isDead()) {
            return ;
        }
        GameController.getInstance().startGame(character);
    }

    public void createCharacter() {
        HashMap<Integer, CharacterClass> characterClasses = characterClassModel.getClasses();
        characterView.createCharacter(characterClasses);
    }

    public void startMenu() {
        menuView.showMenu();
    }

    public void deleteCharacter(int characterId) {
        characterModel.deleteCharacter(characterId);
        this.getCharacters();
    }

    public void closeScanner() {
        if (this.characterView != null) {
            this.characterView.closeScanner();
        }
    }
}