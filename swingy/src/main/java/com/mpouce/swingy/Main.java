package com.mpouce.swingy;

import com.mpouce.swingy.controller.CharacterController;

import java.util.Objects;

public class Main 
{
    public static void main( String[] args )
    {
        if (args.length > 0 && Objects.equals("gui", args[0])) {
            System.out.println("GUI mode enabled.");
            Settings.getInstance().setGui(true);
        }
        CharacterController player = CharacterController.getInstance();
        player.startMenu();
    }
}
