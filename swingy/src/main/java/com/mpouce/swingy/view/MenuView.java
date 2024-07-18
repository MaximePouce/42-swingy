package com.mpouce.swingy.view;

import com.mpouce.swingy.Settings;
import com.mpouce.swingy.controller.CharacterController;
import com.mpouce.swingy.view.utils.BackgroundPanel;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
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

            BackgroundPanel backgroundPanel = new BackgroundPanel("background.jpg");
            backgroundPanel.setLayout(new BorderLayout());
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(20, 20, 20, 20);

            buttonPanel.setOpaque(false);
            buttonPanel.add(btnStart, c);

            backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

            Window window = Window.getInstance();
            window.addContent(backgroundPanel);
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