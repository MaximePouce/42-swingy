package com.mpouce.swingy.view;

import com.mpouce.swingy.model.character.Character;
import com.mpouce.swingy.model.Location;
import com.mpouce.swingy.controller.GameController;
import com.mpouce.swingy.view.utils.ImageUtil;
import com.mpouce.swingy.view.utils.ContentFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.ImageIcon;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;


public class GameView {
    private GameController controller;

    public GameView(GameController gameController) {
        this.controller = gameController;
    }

    public void displaySideMenu(Character player) {
        displaySideMenuGui(player);
    }

    public void displaySideMenuGui(Character player) {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        // Display Character Name
        JLabel lblName = ContentFormatter.newCenteredLabel("<html>" + player.getName() + "</html>");
        // Show Character image
        String imageName = player.getCharacterClass().getName().toLowerCase() + ".jpg";
        Image scaledImage = ImageUtil.getImage(imageName, 190, 190);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel lblImage = new JLabel(scaledIcon);
        // Display HP Bar
        JPanel healthPanel = new JPanel(new GridLayout(2, 1));
        JProgressBar healthBar = new JProgressBar(0, player.getMaxHitPoints());
        healthBar.setForeground(Color.green);
        healthBar.setValue(50);
        healthPanel.add(healthBar);
        JLabel healthLabel = ContentFormatter.newCenteredLabel("<html>" + player.getHitPoints() + "/" + player.getMaxHitPoints() + "</html>");
        healthPanel.add(healthLabel);
        // Display ATK / DEF
        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        JLabel lblAtk = ContentFormatter.newCenteredLabel("<html>" + player.getAttack() + " ATK</html>");
        JLabel lblDef = ContentFormatter.newCenteredLabel("<html>" + player.getDefense() + " DEF</html>");
        statsPanel.add(lblAtk);
        statsPanel.add(lblDef);
        // Buttons
        JButton btnMenu = new JButton("Back to Menu");

        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.PAGE_END;
        menuPanel.add(lblName, gbc);

        gbc.weighty = 0.2;
        gbc.gridy = 1;
        menuPanel.add(lblImage, gbc);

        gbc.weighty = 0.2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.PAGE_START;
        menuPanel.add(healthPanel, gbc);

        gbc.weighty = 0.2;
        gbc.gridy = 3;
        menuPanel.add(statsPanel, gbc);

        gbc.weighty = 0.2;
        gbc.gridy = 4;
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
        JProgressBar expBar = new JProgressBar(player.getRequiredExp(playerLevel), player.getRequiredExp(playerLevel + 1));
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

        JPanel contentPanel = new JPanel(new GridLayout(3, 3));

        int playerX = player.getLocation().getX();
        int playerY = player.getLocation().getY();

        for (int i = 1; i > -2; i--) {
            for (int j = 1; j > -2; j--) {
                boolean clickable = (i == 0) ^ (j == 0);
                JLabel locationLabel = createLocationPanel(map[i + playerX][j + playerY], player, clickable);
                contentPanel.add(locationLabel);
            }
        }

        ContentFormatter.setContent(mainPanel, contentPanel);

        
        
        // window.addMenu(menuPanel);
        window.addContent(mainPanel);
        window.showView();
        window.displayWindow();
    }

    private JLabel createLocationPanel(Location location, Character player, boolean clickable) {
        String imageName;

        if (location.getX() == player.getLocation().getX() && location.getY() == player.getLocation().getY()) {
            imageName = player.getCharacterClass().getName().toLowerCase() + ".jpg";
        } else {
            imageName = location.getImageName();
        }
        Image scaledImage = ImageUtil.getImage(imageName, 250, 250);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel locationLabel = new JLabel(scaledIcon);

        if (clickable) {
            locationLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    System.out.println("clicked.");
                }
            });
        }

        return locationLabel;
    }
}