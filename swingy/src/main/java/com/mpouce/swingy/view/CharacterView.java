package com.mpouce.swingy.view;

import com.mpouce.swingy.Settings;
import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.character.CharacterClass;
import com.mpouce.swingy.controller.CharacterController;
import com.mpouce.swingy.view.utils.BackgroundPanel;
import com.mpouce.swingy.view.utils.ContentFormatter;

import java.util.List;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.io.File;
import java.io.IOException;

import javax.swing.*;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
            JPanel mainPanel = new JPanel();
            mainPanel.setOpaque(false);
            BackgroundPanel charactersPanel = new BackgroundPanel("wood_texture.jpg");
            charactersPanel.setLayout(new BoxLayout(charactersPanel, BoxLayout.X_AXIS));
            charactersPanel.setOpaque(false);
            
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
            JScrollPane scrollPanel = new JScrollPane(charactersPanel);
            scrollPanel.setBackground(Color.red);
            scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPanel.getHorizontalScrollBar().setUnitIncrement(16);

            JLabel lblTitle = new JLabel("Select your character");
            lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
            lblTitle.setVerticalAlignment(SwingConstants.CENTER);

            JPanel titlePane = new JPanel();
            titlePane.setOpaque(false);

            titlePane.add(lblTitle);
            Window window = Window.getInstance();

            JButton createButton = new JButton("Create new Character");
            createButton.setPreferredSize(new Dimension(200, 20));
            createButton.addActionListener(e -> {
                this.controller.createCharacter();
            });

            JButton menuButton = new JButton("Back to Menu");
            menuButton.setPreferredSize(new Dimension(200, 20));
            menuButton.addActionListener(e -> {
                this.controller.startMenu();
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(false);
            // buttonPanel.setLayout(new BorderLayout());
            buttonPanel.add(menuButton);//, BorderLayout.LINE_START);
            buttonPanel.add(createButton);//, BorderLayout.LINE_END);

            JPanel contentPanel = new JPanel();
            // Using a Layout manager to ensure the ScrollPanel takes the entire JPanel
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(scrollPanel, BorderLayout.CENTER);

            mainPanel.setLayout(new GridBagLayout());

            ContentFormatter.setTitle(mainPanel, titlePane);
            ContentFormatter.setContent(mainPanel, contentPanel);
            ContentFormatter.setFooter(mainPanel, buttonPanel);

            window.resetView();
            window.addSideColumns();
            window.addContent(mainPanel);
            window.showView();
            window.displayWindow();
        });
    }

    private JPanel createCharacterPanel(Character character) {
        JPanel characterPanel = new JPanel();
        characterPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        characterPanel.setLayout(new BorderLayout());
        characterPanel.setOpaque(false);

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

        Image scaledImage = image.getScaledInstance(380, 380, Image.SCALE_SMOOTH);

        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JPanel imagePanel = new JPanel();
        JLabel lblImage = new JLabel(scaledIcon);
        lblImage.setOpaque(false);
        imagePanel.add(lblImage);
        imagePanel.setOpaque(false);
        characterPanel.add(lblImage, BorderLayout.CENTER);
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

    public void createCharacter(List<CharacterClass> characterClasses) {
        if (Settings.getInstance().getUseGui()) {
            createCharacterGui(characterClasses);
        }
        else {
            createCharacterConsole(characterClasses);
        }
    }

    private void createCharacterGui(List<CharacterClass> characterClasses) {
        SwingUtilities.invokeLater(() -> {
            JPanel mainPanel = new JPanel();

            JPanel namePanel = new JPanel();
            JLabel lblName = new JLabel("Enter a name");
            final JTextField textName = new JTextField("Unknown Adventurer");
            textName.setBounds(50, 100, 200, 30);
            namePanel.add(lblName);
            namePanel.add(textName);

            JPanel classesPanel = new JPanel();
            classesPanel.setLayout(new BoxLayout(classesPanel, BoxLayout.X_AXIS));
            classesPanel.setBackground(Color.blue);
            ButtonGroup bg = new ButtonGroup();

            JButton createButton = new JButton("Create Character");
            createButton.setEnabled(false);
            for (CharacterClass charClass : characterClasses) {
                classesPanel.add(createClassPanel(charClass, bg, createButton));
            }
            JScrollPane scrollPanel = new JScrollPane(classesPanel);
            scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPanel.getHorizontalScrollBar().setUnitIncrement(16);

            createButton.addActionListener(e -> {
                int classId = Integer.parseInt(bg.getSelection().getActionCommand());
                String charName = textName.getText().trim();
                System.out.println("Selected class ID :" + classId);
                System.out.println("Name : " + charName);
                this.controller.newCharacter(charName, classId);
            });

            mainPanel.setBackground(Color.red);
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(namePanel, BorderLayout.NORTH);
            mainPanel.add(scrollPanel, BorderLayout.CENTER);
            mainPanel.add(createButton, BorderLayout.SOUTH);

            Window window = Window.getInstance();
            window.resetView();
            window.addMenu();
            window.addContent(mainPanel);
            window.showView();
            window.displayWindow();
        });
    }

    private JPanel createClassPanel(CharacterClass charClass, ButtonGroup bg, JButton button) {
        JPanel classPanel = new JPanel();
        classPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        classPanel.setBackground(Color.green);
        classPanel.setLayout(new BorderLayout());

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        classPanel.setBorder(border);

        JLabel lblName = new JLabel("<html>" + charClass.getName() + "</html>");
        JLabel lblStats = new JLabel("<html>Level 0: " + charClass.getHitPoints() + "HP " + charClass.getAttack() + "ATK " + charClass.getDefense() + "DEF</html>");
        JLabel lblGrowth = new JLabel("<html>Level 0: " + charClass.getHitPointsGrowth() + "HP " + charClass.getAttackGrowth() + "ATK " + charClass.getDefenseGrowth() + "DEF</html>");

        lblName.setHorizontalAlignment(SwingConstants.CENTER);
        lblName.setVerticalAlignment(SwingConstants.CENTER);

        lblStats.setHorizontalAlignment(SwingConstants.CENTER);
        lblStats.setVerticalAlignment(SwingConstants.CENTER);

        lblGrowth.setHorizontalAlignment(SwingConstants.CENTER);
        lblGrowth.setVerticalAlignment(SwingConstants.CENTER);
    
        JPanel labelPanel = new JPanel(new GridLayout(3, 1));
        labelPanel.add(lblName);
        labelPanel.add(lblStats);
        labelPanel.add(lblGrowth);

        JRadioButton radio = new JRadioButton("Select");
        radio.setActionCommand(String.valueOf(charClass.getId()));
        radio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                button.setEnabled(true);
            }
        });
        bg.add(radio);

        classPanel.add(labelPanel, BorderLayout.CENTER);
        classPanel.add(radio, BorderLayout.SOUTH);

        return classPanel;
    }

    private void createCharacterConsole(List<CharacterClass> characterClasses) {
        System.out.println("Select a class from the list below:");
        for (CharacterClass charClass : characterClasses) {
            System.out.println("class #" + charClass.getId() + ": " + charClass.getName());
            System.out.print("starting stats: ");
            System.out.print(charClass.getHitPoints() + " HP ");
            System.out.print(charClass.getAttack() + " ATK ");
            System.out.println(charClass.getDefense() + " DEF");
            System.out.print("stat growth: ");
            System.out.print(charClass.getHitPointsGrowth() + " HP ");
            System.out.print(charClass.getAttackGrowth() + " ATK ");
            System.out.println(charClass.getDefenseGrowth() + " DEF");
        }
        // classId = Read input to select class
        // name = Read input
        // this.controller.newCharacter(name, classId);
    }
}
