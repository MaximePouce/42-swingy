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

    public void addSideColumns() {
        SwingUtilities.invokeLater(() -> {
            JPanel rightPanel = new JPanel();
            rightPanel.setOpaque(false);

            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.1;
            c.weighty = 1.0;
            c.anchor = GridBagConstraints.LINE_START;
            c.fill = GridBagConstraints.BOTH;

            this.view.add(rightPanel, c);

            JPanel leftPanel = new JPanel();
            leftPanel.setOpaque(false);

            c.gridx = 2;
            c.gridy = 0;
            c.weightx = 0.1;
            c.weighty = 1.0;
            c.anchor = GridBagConstraints.LINE_END;
            c.fill = GridBagConstraints.BOTH;

            this.view.add(leftPanel, c);
        });
    }

    public void addMenu() {
        SwingUtilities.invokeLater(() -> {
            JPanel menuPanel = new JPanel();
            JLabel lblTest = new JLabel("Test");
            menuPanel.setOpaque(false);
            menuPanel.add(lblTest);

            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.1;
            c.weighty = 1.0;
            c.anchor = GridBagConstraints.LINE_START;
            c.fill = GridBagConstraints.BOTH;

            this.view.add(menuPanel, c);
        });
    }

    public void addContent(JPanel contentPanel) {
        SwingUtilities.invokeLater(() -> {
            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0.8;
            c.weighty = 1;
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;

            this.view.add(contentPanel, c);
        });
    }

    public void resetView() {
        SwingUtilities.invokeLater(() -> {
            this.view.removeAll();
        });
    }
}