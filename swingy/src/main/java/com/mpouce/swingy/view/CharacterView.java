package com.mpouce.swingy.view;

import com.mpouce.swingy.Settings;
import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.controller.CharacterController;

import java.util.List;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.swing.*;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import javax.imageio.ImageIO;

public class CharacterView {

    private CharacterController controller;

    public CharacterView(CharacterController ctrl) {
        this.controller = ctrl;
    }

    public void showCharacters(List<Character> characters) {
        if (Settings.getInstance().getUseGui()) {
            showCharactersGui(characters);
        }
        else {
            showCharactersConsole(characters);
        }
    }

    private void showCharactersGui(List<Character> characters) {
        SwingUtilities.invokeLater(() -> {
            JPanel charactersPanel = new JPanel();
            charactersPanel.setLayout(new BoxLayout(charactersPanel, BoxLayout.X_AXIS));
            charactersPanel.setBackground(Color.blue);
            charactersPanel.setMaximumSize(new Dimension(1336, 600));
            if (characters.isEmpty()) {
                JLabel lblCharacters = new JLabel("No character found. Please create one to continue.", SwingConstants.CENTER);
                lblCharacters.setFont(new Font("Serif", Font.BOLD, 42));
                lblCharacters.setForeground(Color.cyan);
                charactersPanel.add(lblCharacters, BorderLayout.CENTER);
            } else {
                for (Character character : characters) {
                    charactersPanel.add(createCharacterPanel(character));
                }
            }

            JScrollPane scrollPane = new JScrollPane(charactersPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

            ImageIcon scaledIcon = new ImageIcon("CharacterSelectBanner.png");
            JLabel lblTitle = new JLabel(scaledIcon);

            JPanel titlePane = new JPanel();
            titlePane.setBackground(Color.cyan);
            titlePane.setPreferredSize(new Dimension(1336, 214));

            titlePane.add(lblTitle);
            Window window = Window.getInstance();

            JButton createButton = new JButton("Create new Character");
            createButton.addActionListener(e -> {
                System.out.println("Create new character button clicked");
            });

            JPanel mainPanel = new JPanel();

            mainPanel.setBackground(Color.red);
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(titlePane, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(createButton, BorderLayout.SOUTH);

            window.resetView();
            window.addMenu();
            window.addContent(mainPanel);
            window.showView();
            window.displayWindow();
        });
    }

    private JPanel createCharacterPanel(Character character) {
        JPanel characterPanel = new JPanel();
        characterPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        characterPanel.setBackground(Color.green);
        characterPanel.setLayout(new BorderLayout());

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        characterPanel.setBorder(border);

        JLabel lblName = new JLabel("<html>" + character.getName() + "</html>");
        JLabel lblClass = new JLabel("<html>Level " + character.getLevel() + " " + character.getCharacterClass().getName() + "</html>");

        lblName.setHorizontalAlignment(SwingConstants.CENTER);
        lblName.setVerticalAlignment(SwingConstants.CENTER);

        lblClass.setHorizontalAlignment(SwingConstants.CENTER);
        lblClass.setVerticalAlignment(SwingConstants.CENTER);

        JButton btnSelect = new JButton("Select");

        btnSelect.addActionListener(e -> {
            this.controller.selectCharacter(character.getId());
        });

        JPanel labelPanel = new JPanel(new GridLayout(2, 1));
        labelPanel.add(lblName);
        labelPanel.add(lblClass);

        ImageIcon imageIcon = new ImageIcon("fighter.jpg");
        Image image = imageIcon.getImage();

        int newWidth = 380;
        int newHeight = (int)(image.getHeight(null) * ((double) newWidth / image.getWidth(null)));
        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JPanel imagePanel = new JPanel();
        JLabel lblImage = new JLabel(scaledIcon);
        imagePanel.add(lblImage);
        imagePanel.setBackground(Color.cyan);
        characterPanel.add(imagePanel, BorderLayout.CENTER);

        imagePanel.setBackground(Color.cyan);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnSelect);
        characterPanel.add(labelPanel, BorderLayout.NORTH);
        
        characterPanel.add(btnSelect, BorderLayout.SOUTH);

        return characterPanel;
    }

    private void showCharactersConsole(List<Character> characters) {
        if (characters.isEmpty()) {
            System.out.println("No character found.");
        }
        else {
            System.out.println("Listing characters.");
            for (Character character : characters) {
                System.out.print("character #" + character.getId() + ": ");
                System.out.print(character.getName());
                System.out.print(", Level " + character.getLevel() + " ");
                System.out.println(character.getCharacterClass().getName());
            }
        }
        System.out.println("Create New Character");
    }
}
