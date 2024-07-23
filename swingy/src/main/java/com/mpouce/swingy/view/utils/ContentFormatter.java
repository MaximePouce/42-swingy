package com.mpouce.swingy.view.utils;

import java.awt.GridBagConstraints;
import javax.swing.JPanel;

public class ContentFormatter {
    private ContentFormatter() {}

    public static void setTitle(JPanel mainPanel, JPanel titlePanel) {
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.2;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.BOTH;

        mainPanel.add(titlePanel, c);
    }

    public static void setContent(JPanel mainPanel, JPanel contentPanel) {
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.75;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;

        mainPanel.add(contentPanel, c);
    }

    public static void setFooter(JPanel mainPanel, JPanel footPanel) {
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 0.05;
        c.anchor = GridBagConstraints.LINE_END;
        c.fill = GridBagConstraints.BOTH;

        mainPanel.add(footPanel, c);
    }
}