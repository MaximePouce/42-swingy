package com.mpouce.swingy.view;

import com.mpouce.swingy.Settings;
import com.mpouce.swingy.controller.CharacterController;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.BorderLayout;

public class MenuView {
    private CharacterController controller;

    public MenuView(CharacterController ctrl) {
        this.controller = ctrl;
    }

    public void showMenu() {
        if (Settings.getInstance().getUseGui()) {
            showMenuGui();
        }
        else {
            showMenuConsole();
        }
    }

    private void showMenuGui() {
        SwingUtilities.invokeLater(() -> {
            JButton btnStart = new JButton("Start Game");
            btnStart.addActionListener(e -> {
                this.controller.getCharacters();
            });

            JPanel menuPanel = new JPanel();
            menuPanel.setBackground(Color.cyan);
            menuPanel.setLayout(new BorderLayout());
            menuPanel.add(btnStart, BorderLayout.CENTER);

            Window window = Window.getInstance();
            window.addContent(menuPanel);
            window.showView();
            window.displayWindow();
        });
    }

    private void showMenuConsole() {
        System.out.println("Welcome to Swingy !");
        System.out.println("For your safety, please put your seatbelt.");
        this.controller.getCharacters();
    }
}