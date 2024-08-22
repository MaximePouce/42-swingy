package com.mpouce.swingy.view;

import com.mpouce.swingy.view.utils.BackgroundPanel;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class Window {
    private JFrame frame;
    private JPanel view;
    private static Window instance;

    private Window() {
        SwingUtilities.invokeLater(() -> {
            this.frame = new JFrame("Swingy");
            this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.frame.setSize(new Dimension(1536, 864));

            this.view = new BackgroundPanel("background2.jpg");
            view.setLayout(new GridBagLayout());
        });
    }

    public static Window getInstance() {
        if (instance == null) {
            instance = new Window();
        }
        return instance;
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public void displayWindow() {
        SwingUtilities.invokeLater(() -> {
            this.frame.setVisible(true);
        });
    }

    public void showView() {
        SwingUtilities.invokeLater(() -> {
            this.frame.add(view);
        });
    }

    public void closeWindow() {
        SwingUtilities.invokeLater(() -> {
            resetView();
            System.out.println("Making window disappear");
            this.frame.setVisible(false);
            // this.frame.dispose();
            System.out.println("Window has been disposed.");
        });
    }

    public void addSideColumns() {
        SwingUtilities.invokeLater(() -> {
            JPanel rightPanel = new JPanel();
            rightPanel.setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0.1;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.fill = GridBagConstraints.BOTH;

            this.view.add(rightPanel, gbc);

            JPanel leftPanel = new JPanel();
            leftPanel.setOpaque(false);

            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.weightx = 0.1;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.fill = GridBagConstraints.BOTH;

            this.view.add(leftPanel, gbc);
        });
    }

    public void addMenu(JPanel menuPanel) {
        SwingUtilities.invokeLater(() -> {
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0.1;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.fill = GridBagConstraints.BOTH;

            this.view.add(menuPanel, gbc);
        });
    }

    public void addContent(JPanel contentPanel) {
        SwingUtilities.invokeLater(() -> {
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 0.8;
            gbc.weighty = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.BOTH;

            this.view.add(contentPanel, gbc);
        });
    }

    public void resetView() {
        SwingUtilities.invokeLater(() -> {
            this.view.removeAll();
        });
    }
}