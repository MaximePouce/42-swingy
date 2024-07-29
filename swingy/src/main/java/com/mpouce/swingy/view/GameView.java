package com.mpouce.swingy.view;

import com.mpouce.swingy.model.character.Character;
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
        JLabel lblName = ContentFormatter.newCenteredLabel("<html>" + player.getName() + "<html>");
        // Show Character image
        String imageName = player.getCharacterClass().getName().toLowerCase() + ".jpg";
        Image scaledImage = ImageUtil.getImage(imageName, 190, 190);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel lblImage = new JLabel(scaledIcon);
        // Display HP Bar
        JProgressBar healthBar = new JProgressBar(0, player.getMaxHitPoints());
        healthBar.setForeground(Color.green);
        healthBar.setValue(player.getHitPoints());
        // Displays ATK / DEF
        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        JLabel lblAtk = ContentFormatter.newCenteredLabel("<html>" + player.getAttack() + " ATK</html>");
        JLabel lblDef = ContentFormatter.newCenteredLabel("<html>" + player.getDefense() + " DEF</html>");
        statsPanel.add(lblAtk);
        statsPanel.add(lblDef);
        // Buttons
        JButton btnMenu = new JButton("Back to Menu");

        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.PAGE_START;
        menuPanel.add(lblName, gbc);

        gbc.weighty = 0.4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 1;
        menuPanel.add(lblImage, gbc);

        gbc.weighty = 0.1;
        gbc.gridy = 2;
        menuPanel.add(healthBar, gbc);

        gbc.weighty = 0.3;
        gbc.gridy = 3;
        menuPanel.add(statsPanel, gbc);

        gbc.weighty = 0.1;
        gbc.gridy = 4;
        menuPanel.add(btnMenu, gbc);

        Window window = Window.getInstance();
        window.resetView();
        window.addMenu(menuPanel);
        // window.addContent();
        window.showView();
        window.displayWindow();
    }

    public void displaySideMenuConsole(Character player) {

    }
}