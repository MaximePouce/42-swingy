package com.mpouce.swingy;

import com.mpouce.swingy.controller.CharacterController;
import com.mpouce.swingy.controller.GameController;
import com.mpouce.swingy.model.utils.DatabaseConnection;
import com.mpouce.swingy.model.utils.DatabaseUtils;

import java.util.Objects;

public class Main 
{
    public static void main( String[] args )
    {
        if (args.length > 0 && Objects.equals("gui", args[0])) {
            System.out.println("GUI mode enabled.");
            Settings.getInstance().setGui(true);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutting down.");
                CharacterController.getInstance().closeScanner();
                GameController.getInstance().closeScanner();
            }
        });

        DatabaseUtils.initializeDatabase(DatabaseConnection.getInstance().getConnection());
        CharacterController player = CharacterController.getInstance();
        player.startMenu();
    }
}
