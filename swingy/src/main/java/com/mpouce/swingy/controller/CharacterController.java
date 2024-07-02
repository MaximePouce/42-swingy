package com.mpouce.swingy.controller;

import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.character.CharacterRepository;
import com.mpouce.swingy.view.CharacterView;

public class CharacterController {
    private Character player;
    private CharacterView characterView;
    private CharacterRepository characterModel;

    public CharacterController() {
        characterModel = new CharacterRepository();
        characterModel.getClasses();
    }

    public void newCharacter(String name, int classId) {
        int charId = characterModel.createCharacter(name, classId);
        if (charId == -1) {
            System.out.println("An error occured during the character creation.");
            return;
        }
        selectCharacter(charId);
    }
}