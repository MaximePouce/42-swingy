package com.mpouce.swingy.view;

import com.mpouce.swingy.model.artifact.Artifact;
import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.Location;
import com.mpouce.swingy.model.artifact.Helmet;
import com.mpouce.swingy.model.artifact.Armor;
import com.mpouce.swingy.model.artifact.Weapon;
import com.mpouce.swingy.model.artifact.Artifact;
import com.mpouce.swingy.controller.CharacterController;
import com.mpouce.swingy.controller.GameController;
import com.mpouce.swingy.view.utils.ImageUtil;
import com.mpouce.swingy.view.utils.ContentFormatter;
import com.mpouce.swingy.view.utils.BackgroundPanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.ImageIcon;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

import java.util.Random;
import java.util.Scanner;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;


public class GameView {
    private Scanner scanner = null;

    public GameView() {
        scanner = new Scanner(System.in);
    }

    public void displaySideMenu(Character player) {
        displaySideMenuGui(player);
    }

    public void displaySideMenuGui(Character player) {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblName = ContentFormatter.newCenteredLabel("<html>" + player.getName() + "</html>");

        String imageName = player.getCharacterClass().getName().toLowerCase() + ".jpg";
        Image scaledImage = ImageUtil.getImage(imageName, 190, 190);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel lblImage = new JLabel(scaledIcon);

        JPanel healthPanel = new JPanel(new GridLayout(2, 1));
        JProgressBar healthBar = new JProgressBar(0, player.getMaxHitPoints());
        healthBar.setForeground(Color.green);
        healthBar.setValue(player.getHitPoints());
        healthPanel.add(healthBar);
        JLabel healthLabel = ContentFormatter.newCenteredLabel("<html>" + player.getHitPoints() + "/" + player.getMaxHitPoints() + "</html>");
        healthPanel.add(healthLabel);

        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        JLabel lblAtk = ContentFormatter.newCenteredLabel("<html>" + player.getAttack() + " ATK</html>");
        JLabel lblDef = ContentFormatter.newCenteredLabel("<html>" + player.getDefense() + " DEF</html>");
        statsPanel.add(lblAtk);
        statsPanel.add(lblDef);

        GridLayout equipLayout = new GridLayout(3, 1);
        equipLayout.setHgap(10);
        JPanel equipPanel = new JPanel(equipLayout);
        JPanel helmetPanel = ContentFormatter.newArtifactPanel("helmet", player.getHelmet());
        JPanel armorPanel = ContentFormatter.newArtifactPanel("armor", player.getArmor());
        JPanel weaponPanel = ContentFormatter.newArtifactPanel("weapon", player.getWeapon());

        equipPanel.add(helmetPanel);
        equipPanel.add(armorPanel);
        equipPanel.add(weaponPanel);

        JButton btnMenu = new JButton("Back to Menu");
        btnMenu.addActionListener(e -> {
            CharacterController.getInstance().startMenu();
        });

        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.PAGE_END;
        menuPanel.add(lblName, gbc);

        gbc.weighty = 0.2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.PAGE_START;
        menuPanel.add(lblImage, gbc);

        gbc.weighty = 0.1;
        gbc.gridy = 2;
        menuPanel.add(healthPanel, gbc);

        gbc.weighty = 0.1;
        gbc.gridy = 3;
        menuPanel.add(statsPanel, gbc);

        gbc.weighty = 0.3;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        menuPanel.add(equipPanel, gbc);

        gbc.weighty = 0.2;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        menuPanel.add(btnMenu, gbc);

        Window window = Window.getInstance();
        // window.resetView();
        window.addMenu(menuPanel);
        // window.addContent();
        // window.showView();
        // window.displayWindow();
    }

    public void displaySideMenuConsole(Character player) {
        // Just show player info
    }

    public void showGame(Character player, Location[][] map) {
        Window window = Window.getInstance();
        window.resetView();
        displaySideMenuGui(player);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        JPanel expPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        int playerLevel = player.getLevel();
        JProgressBar expBar = new JProgressBar(
            player.getRequiredExp(playerLevel),
            player.getRequiredExp(playerLevel + 1)
            );
        expBar.setValue(player.getExperience());

        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.CENTER;
        expPanel.add(expBar, gbc);

        JLabel lblLevel = ContentFormatter.newCenteredLabel("<html>Level " + playerLevel + "</html>");

        gbc.weighty = 0.3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.PAGE_END;
        expPanel.add(lblLevel, gbc);

        ContentFormatter.setTitle(mainPanel, expPanel);

        JPanel contentPanel = new BackgroundPanel("green_tile.jpg");
        GridLayout layout = new GridLayout(3, 3);
        layout.setVgap(10);
        layout.setHgap(10);
        contentPanel.setLayout(layout);
        int playerX = player.getLocation().getX();
        int playerY = player.getLocation().getY();
        int mapSize = map.length;

        for (int i = 1; i > -2; i--) {
            for (int j = 1; j > -2; j--) {
                boolean clickable = (i == 0) ^ (j == 0);
                JPanel locationPanel = createLocationPanel(map[i + playerX][j + playerY], player, mapSize, clickable);
                contentPanel.add(locationPanel);
            }
        }

        ContentFormatter.setContent(mainPanel, contentPanel);
        window.addContent(mainPanel);
        window.showView();
        window.displayWindow();
    }

    private JPanel createLocationPanel(Location location, Character player, int mapSize, boolean clickable) {
        JPanel locationPanel = new JPanel();
        locationPanel.setLayout(new GridBagLayout());
        locationPanel.setOpaque(false);
        String imageName = "";
        GridBagConstraints gbc = new GridBagConstraints();

        if (location.getX() == player.getLocation().getX() && location.getY() == player.getLocation().getY()) {
            imageName = player.getCharacterClass().getName().toLowerCase() + ".jpg";
            Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
            locationPanel.setBorder(border);
        } else {
            imageName = location.getImageName(mapSize);
            if (location.getCharacter() != null) {
                JLabel enemyLabel = ContentFormatter.newCenteredLabel("<html>" + location.getCharacter().getName() + ", Level " + location.getCharacter().getLevel() + "</html>");
                gbc.gridy = 0;
                gbc.weighty = 0.3;
                gbc.anchor = GridBagConstraints.PAGE_START;
                locationPanel.add(enemyLabel, gbc);
            }
        }
        if (!imageName.isEmpty()) {
            Image scaledImage = ImageUtil.getImage(imageName, 200, 200);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel locationLabel = new JLabel(scaledIcon);
            gbc.gridy = 1;
            gbc.weighty = 0.7;
            locationPanel.add(locationLabel, gbc);
        }

        if (clickable) {
            Border border = BorderFactory.createLineBorder(Color.GREEN, 2);
            locationPanel.setBorder(border);
            locationPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int dialogResult = -1;
                    boolean battle = location.getCharacter() != null;
                    if (battle) {
                        Image image = ImageUtil.getImage("battle.png", 100, 100);
                        ImageIcon icon = new ImageIcon(image);
                        dialogResult = JOptionPane.showConfirmDialog(Window.getInstance().getFrame(),
                        "Engage battle against " + location.getCharacter().getName() + " ?",
                        "Fighting the good fight",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, icon);
                    }
                    if (!battle || dialogResult == JOptionPane.YES_OPTION || new Random().nextInt(42) % 2 == 0) {
                        if (dialogResult == JOptionPane.NO_OPTION) {
                            System.out.println("You weren't able to flee. Fight for your life!");
                        }
                        System.out.println("Moving to " + location.getX() + ":" + location.getY());
                        GameController.getInstance().playerMoveTo(location);
                    }
                }
            });
        }

        return locationPanel;
    }

    public void showArtifactDialog(Character playerCharacter, Artifact lootedArtifact) {
        Image image = ImageUtil.getImage("notfound.jpg", 100, 100);
        ImageIcon icon = new ImageIcon(image);
        String message = "Would you like to equip the artifact " 
                        + lootedArtifact.getName() + ", level "
                        + lootedArtifact.getLevel() + " "
                        + lootedArtifact.getType() + " ?";

        System.out.println(message);
        int dialogResult = JOptionPane.showConfirmDialog(
                                        Window.getInstance().getFrame(),
                                        message,
                                        "Artifact equipment", JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE, icon);
        if (dialogResult == JOptionPane.YES_OPTION) {
            GameController.getInstance().equipArtifact(lootedArtifact);
        }
    }

    public void showDeathScreen(Character player) {
        String message = player.getName() + " died fighting a " + player.getLocation().getCharacter().getName() + ".\n And will be remembered for atleast a day.";
        JOptionPane.showMessageDialog(
                    Window.getInstance().getFrame(),
                    message,
                    "You died.",
                    JOptionPane.INFORMATION_MESSAGE);
        CharacterController.getInstance().startMenu();
    }
}