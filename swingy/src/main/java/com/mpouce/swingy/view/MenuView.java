package com.mpouce.swingy.view;

import com.mpouce.swingy.Settings;
import com.mpouce.swingy.controller.CharacterController;
import com.mpouce.swingy.view.utils.BackgroundPanel;
import com.mpouce.swingy.view.utils.ContentFormatter;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import java.awt.Font;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.BorderLayout;
import java.io.IOException;
import java.lang.UnsupportedOperationException;

public class MenuView {
    public MenuView() {
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
                CharacterController.getInstance().getCharacters();
            });

            JButton btnGitHub = new JButton("SwinGitHub");
            btnGitHub.addActionListener(e -> {
                try {
                    String url = "https://github.com/MaximePouce/42-swingy";
                    Desktop dt = Desktop.getDesktop();
                    URI uri = new URI(url);
                    dt.browse(uri.resolve(uri));
                } catch (IOException | URISyntaxException | UnsupportedOperationException urlException) {
                    // TODO: Maybe make this a nice pop-up window ?
                    System.out.println("An error occured when trying to open your browser.");
                }
            });

            JButton btnConsole = new JButton("Console View");
            btnConsole.addActionListener(e -> {
                System.out.println("Switching to Console view");
                SwingUtilities.invokeLater(() -> {
                    Window.getInstance().closeWindow();
                    SwingUtilities.invokeLater(() -> {
                        Settings.getInstance().setGui(false);
                        showMenuConsole();
                    });
                });
            });

            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BorderLayout());
            menuPanel.setOpaque(false);
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(20, 20, 20, 20);

            buttonPanel.setOpaque(false);
            buttonPanel.add(btnStart, c);

            c.gridx = 0;
            c.gridy = 1;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(20, 20, 20, 20);
            buttonPanel.add(btnGitHub, c);

            c.gridx = 0;
            c.gridy = 2;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(20, 20, 100, 20);
            buttonPanel.add(btnConsole, c);

            JLabel lblTitle = ContentFormatter.newCenteredLabel("<html>SWINGY</html>");
            lblTitle.setFont(new Font("Serif", Font.BOLD, 56));
            lblTitle.setForeground(Color.cyan);
            lblTitle.setBorder(BorderFactory.createEmptyBorder(200, 20, 20, 20));

            menuPanel.add(lblTitle, BorderLayout.PAGE_START);
            menuPanel.add(buttonPanel, BorderLayout.PAGE_END);

            Window window = Window.getInstance();
            window.resetView();
            window.addContent(menuPanel);
            window.showView();
            window.displayWindow();
        });
    }

    private void showMenuConsole() {
        System.out.println("Welcome to Swingy !");
        System.out.println("For your safety, please wear your seatbelt.");
        CharacterController.getInstance().getCharacters();
    }
}