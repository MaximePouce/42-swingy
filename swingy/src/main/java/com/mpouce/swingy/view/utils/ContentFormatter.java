package com.mpouce.swingy.view.utils;

import java.awt.GridBagConstraints;

import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class ContentFormatter {
    private ContentFormatter() {}

    public static void setTitle(JPanel mainPanel, JPanel titlePanel) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.BOTH;

        mainPanel.add(titlePanel, gbc);
    }

    public static void setContent(JPanel mainPanel, JPanel contentPanel) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.8;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;

        mainPanel.add(contentPanel, gbc);
    }

    public static void setFooter(JPanel mainPanel, JPanel footPanel) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.BOTH;

        mainPanel.add(footPanel, gbc);
    }

    public static JLabel newCenteredLabel(String text) {
        JLabel newLabel = new JLabel(text);

        newLabel.setHorizontalAlignment(SwingConstants.CENTER);
        newLabel.setVerticalAlignment(SwingConstants.CENTER);

        return newLabel;
    }
}