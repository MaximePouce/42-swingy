package com.mpouce.swingy.controller;

import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.character.CharacterClass;
import com.mpouce.swingy.model.character.CharacterModel;
import com.mpouce.swingy.model.character.CharacterClassModel;
import com.mpouce.swingy.view.CharacterView;
import com.mpouce.swingy.view.MenuView;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashMap;

public class CharacterController {
    private static CharacterController instance;
    private Character player;
    private CharacterView characterView;
    private MenuView menuView;

    private CharacterController() {
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
        HashMap<Integer, CharacterClass> characterClasses = CharacterClassModel.getClasses();
        List<Character> characters = CharacterModel.getCharacters(characterClasses);
        characterView.showCharacters(characters);
    }

    public void newCharacter(String name, int classId) {
        // Dummy Character to validate name
        Character newCharacter = new Character(name, 0, 0, 0, 0);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        jakarta.validation.Validator validator = factory.getValidator();
        Set<ConstraintViolation<Character>> violations = validator.validate(newCharacter);
        if (!violations.isEmpty())
        {
            String errorString = "";
            for (ConstraintViolation<Character> violation : violations)
            {
                errorString += violation.getMessage();
                errorString += "\n";
            }
            characterView.showCreationError(errorString);
        }
        else
        {
            int charId = CharacterModel.createCharacter(name, classId);
            if (charId == -1) {
                System.out.println("An error occured during the character creation.");
                return;
            }
            getCharacters();
        }
    }

    public void selectCharacter(Character character) {
        System.out.println("Selected character #" + character.getId());
        if (character.isDead()) {
            return ;
        }
        GameController.getInstance().startGame(character);
    }

    public void createCharacter() {
        HashMap<Integer, CharacterClass> characterClasses = CharacterClassModel.getClasses();
        characterView.createCharacter(characterClasses);
    }

    public void startMenu() {
        menuView.showMenu();
    }

    public void deleteCharacter(int characterId) {
        CharacterModel.deleteCharacter(characterId);
        this.getCharacters();
    }

    public void closeScanner() {
        if (this.characterView != null) {
            this.characterView.closeScanner();
        }
    }
}