package com.mpouce.swingy.view;

import com.mpouce.swingy.Settings;
import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.character.CharacterClass;
import com.mpouce.swingy.controller.CharacterController;
import com.mpouce.swingy.view.utils.BackgroundPanel;
import com.mpouce.swingy.view.utils.ContentFormatter;
import com.mpouce.swingy.view.utils.ImageUtil;

import java.util.List;
import java.util.HashMap;
import java.util.Scanner;

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
    private Scanner scanner = null;
    public CharacterView() {
        scanner = new Scanner(System.in);
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

            if (characters.isEmpty()) {
                JLabel lblCharacters = ContentFormatter.newCenteredLabel("<html>No character found. Please create one to continue.</html>");
                lblCharacters.setFont(new Font("Serif", Font.BOLD, 42));
                lblCharacters.setForeground(Color.cyan);
                charactersPanel.add(lblCharacters);
            } else {
                for (Character character : characters) {
                    charactersPanel.add(createCharacterPanel(character));
                }
            }
            JScrollPane scrollPanel = new JScrollPane(charactersPanel);
            scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPanel.getHorizontalScrollBar().setUnitIncrement(16);

            JLabel lblTitle = ContentFormatter.newCenteredLabel("<html>Select your character</html>");
            lblTitle.setFont(new Font("Serif", Font.BOLD, 21));

            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            titlePanel.setOpaque(false);
            titlePanel.add(lblTitle, gbc);

            Window window = Window.getInstance();

            JButton createButton = new JButton("Create new Character");
            createButton.setPreferredSize(new Dimension(200, 20));
            createButton.addActionListener(e -> {
                CharacterController.getInstance().createCharacter();
            });

            JButton menuButton = new JButton("Back to Menu");
            menuButton.setPreferredSize(new Dimension(200, 20));
            menuButton.addActionListener(e -> {
                CharacterController.getInstance().startMenu();
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.setOpaque(false);

            gbc.weightx = 0.5;
            buttonPanel.add(menuButton, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.5;
            buttonPanel.add(createButton, gbc);

            JPanel contentPanel = new JPanel();
            // Using a Layout manager to ensure the ScrollPanel takes the entire JPanel
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(scrollPanel, BorderLayout.CENTER);

            mainPanel.setLayout(new GridBagLayout());

            ContentFormatter.setTitle(mainPanel, titlePanel);
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
        characterPanel.setOpaque(false);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        characterPanel.setBorder(border);

        JLabel lblName = ContentFormatter.newCenteredLabel("<html>" + character.getName() + "</html>");
        JLabel lblClass = ContentFormatter.newCenteredLabel("<html>Level " + character.getLevel() + " " + character.getCharacterClass().getName() + "</html>");
        JLabel lblExp = ContentFormatter.newCenteredLabel("<html>" + character.getExperience() + " XP</html>");
        
        JPanel labelPanel = new JPanel(new GridLayout(3, 1));
        labelPanel.add(lblName);
        labelPanel.add(lblClass);
        labelPanel.add(lblExp);

        JPanel statsPanel = new JPanel(new GridLayout(3, 1));
        JLabel lblAtk = ContentFormatter.newCenteredLabel("<html>" + character.getAttack() + " ATK</html>");
        JLabel lblHP = ContentFormatter.newCenteredLabel("<html>" + character.getHitPoints() + " / " + character.getMaxHitPoints() + " HP</html>");
        JLabel lblDef = ContentFormatter.newCenteredLabel("<html>" + character.getDefense() + " DEF</html>");
        statsPanel.add(lblHP);
        statsPanel.add(lblAtk);
        statsPanel.add(lblDef);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton btnSelect = new JButton("Select");
        btnSelect.addActionListener(e -> {
            CharacterController.getInstance().selectCharacter(character);
        });

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            CharacterController.getInstance().deleteCharacter(character.getId());
        });

        gbc.weightx = 0.5;
        buttonPanel.add(btnDelete, gbc);

        gbc.weightx = 0.5;
        gbc.gridx = 1;
        buttonPanel.add(btnSelect, gbc);


        String imageName = character.getCharacterClass().getName().toLowerCase() + ".jpg";
        Image scaledImage = ImageUtil.getImage(imageName, 380, 380);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JPanel imagePanel = new JPanel();
        JLabel lblImage = new JLabel(scaledIcon);
        lblImage.setOpaque(false);
        imagePanel.add(lblImage);
        imagePanel.setOpaque(false);

        characterPanel.setLayout(new GridBagLayout());

        gbc.weighty = 0.2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        characterPanel.add(labelPanel, gbc);

        gbc.weighty = 0.5;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        characterPanel.add(lblImage, gbc);

        gbc.weighty = 0.2;
        gbc.gridy = 2;
        characterPanel.add(statsPanel, gbc);

        gbc.weighty = 0.1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.PAGE_END;
        characterPanel.add(buttonPanel, gbc);

        return characterPanel;
    }

    private void showCharactersConsole(List<Character> characters) {
        System.out.println("Available options:");
        System.out.println("GUI: Open the GUI interface");
        System.out.println("New: create a new Character");
        System.out.println("You can select a character by entering its id.");
        if (characters.isEmpty()) {
            System.out.println("No character found.");
        }
        else {
            System.out.println("Listing characters.");
            for (int i = 0; i < characters.size(); i++) {
                Character character = characters.get(i);
                System.out.print("character #" + i + ": ");
                System.out.print(character.getName());
                System.out.print(", Level " + character.getLevel() + " ");
                System.out.println(character.getCharacterClass().getName());
            }
        }
        while (true) {
            try {
                System.out.print("Selection: ");
                String input = scanner.next();
                if (input.equalsIgnoreCase("gui")) {
                    System.out.println("switching to GUI");
                    Settings.getInstance().setGui(true);
                    showCharactersGui(characters);
                    break;
                } else if (input.equalsIgnoreCase("new")) {
                    CharacterController.getInstance().createCharacter();
                    break;
                } else {
                    try {
                        int number = Integer.parseInt(input);
                        if (characters.isEmpty()) {
                            System.out.println("No character currently created. Type 'new' to create a new one.");
                        } else if (number < 0 || number >= characters.size()) {
                            System.out.println("Invalid character ID provided.");
                        } else {
                            CharacterController.getInstance().selectCharacter(characters.get(number));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
            } catch (java.util.NoSuchElementException e) {
                System.err.println("Ctrl + D detected");
                System.exit(1);
            }
        }
    }

    public void createCharacter(HashMap<Integer, CharacterClass> characterClasses) {
        if (Settings.getInstance().getUseGui()) {
            createCharacterGui(characterClasses);
        }
        else {
            createCharacterConsole(characterClasses);
        }
    }

    private void createCharacterGui(HashMap<Integer, CharacterClass> characterClasses) {
        SwingUtilities.invokeLater(() -> {
            JPanel mainPanel = new JPanel();

            JPanel namePanel = new JPanel();
            JLabel lblName = new JLabel("Name: ");
            final JTextField textName = new JTextField("Unknown Adventurer");
            textName.setBounds(50, 100, 200, 30);
            namePanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            namePanel.add(lblName, gbc);
            namePanel.add(textName, gbc);
            namePanel.setOpaque(false);

            BackgroundPanel classesPanel = new BackgroundPanel("wood_texture.jpg");
            classesPanel.setLayout(new BoxLayout(classesPanel, BoxLayout.X_AXIS));
            ButtonGroup buttonGroup = new ButtonGroup();

            JPanel buttonPanel = new JPanel();
            JButton createButton = new JButton("Create Character");
            createButton.setEnabled(false);
            createButton.setPreferredSize(new Dimension(200, 20));
            createButton.addActionListener(e -> {
                int classId = Integer.parseInt(buttonGroup.getSelection().getActionCommand());
                String charName = textName.getText().trim();
                System.out.println("Selected class ID :" + classId);
                System.out.println("Name : " + charName);
                CharacterController.getInstance().newCharacter(charName, classId);
            });

            JButton backButton = new JButton("Back");
            backButton.setPreferredSize(new Dimension(200, 20));
            backButton.addActionListener(e -> {
                CharacterController.getInstance().getCharacters();
            });

            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.setOpaque(false);

            gbc.weightx = 0.5;
            buttonPanel.add(backButton, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.5;
            buttonPanel.add(createButton, gbc);

            for (CharacterClass charClass : characterClasses.values()) {
                classesPanel.add(createClassPanel(charClass, buttonGroup, createButton));
            }

            JScrollPane scrollPanel = new JScrollPane(classesPanel);
            scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPanel.getHorizontalScrollBar().setUnitIncrement(16);

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(scrollPanel, BorderLayout.CENTER);
            
            mainPanel.setOpaque(false);
            mainPanel.setLayout(new GridBagLayout());
            ContentFormatter.setTitle(mainPanel, namePanel);
            ContentFormatter.setContent(mainPanel, contentPanel);
            ContentFormatter.setFooter(mainPanel, buttonPanel);

            Window window = Window.getInstance();
            window.resetView();
            window.addSideColumns();
            window.addContent(mainPanel);
            window.showView();
            window.displayWindow();
        });
    }

    private JPanel createClassPanel(CharacterClass charClass, ButtonGroup buttonGroup, JButton button) {
        JPanel classPanel = new JPanel();
        classPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        classPanel.setLayout(new GridBagLayout());
        classPanel.setOpaque(false);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        classPanel.setBorder(border);

        JLabel lblName = ContentFormatter.newCenteredLabel("<html>" + charClass.getName() + "</html>");
        JLabel lblStats = ContentFormatter.newCenteredLabel("<html>Stats : " + charClass.getHitPoints() + " HP / " + charClass.getAttack() + " ATK / " + charClass.getDefense() + " DEF</html>");
        JLabel lblGrowth = ContentFormatter.newCenteredLabel("<html>Growth: " + charClass.getHitPointsGrowth() + " HP / " + charClass.getAttackGrowth() + " ATK / " + charClass.getDefenseGrowth() + " DEF</html>");

        JPanel labelPanel = new JPanel(new GridLayout(3, 1));
        labelPanel.add(lblName);
        labelPanel.add(lblStats);
        labelPanel.add(lblGrowth);

        String imageName = charClass.getName().toLowerCase() + ".jpg";
        Image scaledImage = ImageUtil.getImage(imageName, 380, 380);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel lblImage = new JLabel(scaledIcon);

        JRadioButton radio = new JRadioButton("Select");
        radio.setActionCommand(String.valueOf(charClass.getId()));
        radio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                button.setEnabled(true);
            }
        });
        buttonGroup.add(radio);

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();

        radioPanel.add(radio, gbc);

        gbc.weighty = 0.2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        classPanel.add(labelPanel, gbc);

        gbc.weighty = 0.7;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        classPanel.add(lblImage, gbc);

        gbc.weighty = 0.1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.PAGE_END;
        classPanel.add(radioPanel, gbc);

        return classPanel;
    }

    private void createCharacterConsole(HashMap<Integer, CharacterClass> characterClasses) {
        System.out.println("Creating a new Character !");
        System.out.println("Available options:");
        System.out.println("GUI: Open the GUI interface");
        System.out.println("Menu: Go back to character selection");
        System.out.println("Select a class from the list below by entering its id:");
        int selectedClassId = -1;
        for (CharacterClass charClass : characterClasses.values()) {
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
        
        while (true) {
            try {
                String input = scanner.next();
                if (input.equalsIgnoreCase("gui")) {
                    System.out.println("switching to GUI");
                    Settings.getInstance().setGui(true);
                    createCharacterGui(characterClasses);
                    break;
                } else if (input.equalsIgnoreCase("menu")) {
                    CharacterController.getInstance().startMenu();
                    return;
                } else {
                    try {
                        int number = Integer.parseInt(input);
                        if (!characterClasses.containsKey(number)) {
                            System.out.println("Invalid class ID provided.");
                        } else {
                            selectedClassId = number;
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
            } catch (java.util.NoSuchElementException e) {
                System.err.println("Ctrl + D detected");
                System.exit(1);
            }
        }
        System.out.print(characterClasses.get(selectedClassId).getName() + " selected.");
        System.out.println("Available options:");
        System.out.println("GUI: Open the GUI interface");
        System.out.println("Back: Go back to the class selection");
        System.out.print("Enter a name. Names longer than 15 characters will be truncated: ");
        while (true) {
            try {
                String input = scanner.next();
                if (input.equalsIgnoreCase("gui")) {
                    System.out.println("switching to GUI");
                    Settings.getInstance().setGui(true);
                    createCharacterGui(characterClasses);
                    break;
                } else if (input.equalsIgnoreCase("back")) {
                    createCharacterConsole(characterClasses);
                    break;
                } else {
                    CharacterController.getInstance().newCharacter(input, selectedClassId);
                    break;
                }
            } catch (java.util.NoSuchElementException e) {
                System.err.println("Ctrl + D detected");
                System.exit(1);
            }
        }
    }

    public void closeScanner() {
        this.scanner.close();
    }
}
