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

}