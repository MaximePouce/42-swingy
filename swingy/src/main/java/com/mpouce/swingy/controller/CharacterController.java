package com.mpouce.swingy.controller;

import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.character.CharacterClass;
import com.mpouce.swingy.model.character.CharacterRepository;
import com.mpouce.swingy.model.character.CharacterClassRepository;
import com.mpouce.swingy.view.CharacterView;
import com.mpouce.swingy.view.MenuView;

import java.util.ArrayList;
import java.util.List;

public class CharacterController {
    private Character player;
    private CharacterView characterView;
    private MenuView menuView;
    private CharacterRepository characterModel;
    private CharacterClassRepository characterClassModel;

    public CharacterController() {
        characterModel = new CharacterRepository();
        characterClassModel = new CharacterClassRepository();
        characterView = new CharacterView(this);
        menuView = new MenuView(this);
    }

    public void getCharacters() {
        List<Character> characters = characterModel.getCharacters();
        characterView.showCharacters(characters);
    }

    public void newCharacter(String name, int classId) {
        int charId = characterModel.createCharacter(name, classId);
        if (charId == -1) {
            System.out.println("An error occured during the character creation.");
            return;
        }
        selectCharacter(charId);
    }

    public void selectCharacter(int characterId) {
        System.out.println("Selected character #" + characterId);
    }

    public void createCharacter() {
        List<CharacterClass> characterClasses = characterClassModel.getClasses();
        characterView.createCharacter(characterClasses);
    }

    public void startMenu() {
        menuView.showMenu();
    }
}