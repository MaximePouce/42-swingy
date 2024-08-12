package com.mpouce.swingy.view.utils;

import com.mpouce.swingy.model.artifact.Artifact;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;

import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

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

    public static JPanel newArtifactPanel(String type, Artifact artifact) {
        JPanel artifactPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        ImageIcon artifactIcon = new ImageIcon(ImageUtil.getImage(type + ".png", 50, 50));
        JLabel lblArtifactIcon = new JLabel(artifactIcon);

        String artifactInfo = (artifact == null) ? "No " + type + " equipped." : artifact.getName();
        JLabel lblArtifactInfo = newCenteredLabel("<html>" + artifactInfo + "</html>");

        int rarity = (artifact == null) ? 0 : artifact.getLevel();
        Border artifactBorder = BorderFactory.createLineBorder(getRarityColor(rarity), 3);
        artifactPanel.setBorder(artifactBorder);

        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.LINE_START;
        artifactPanel.add(lblArtifactIcon, gbc);

        gbc.weightx = 0.7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        artifactPanel.add(lblArtifactInfo, gbc);

        return artifactPanel;
    }

    public static Color getRarityColor(int rarity) {
        Color rarityColor;
        switch (rarity) {
            case 1:
                rarityColor = Color.BLUE;
                break;
            case 2:
                rarityColor = Color.MAGENTA;
                break;
            case 3:
                rarityColor = Color.YELLOW;
                break;
            default:
                rarityColor = Color.GRAY;
            }
        return rarityColor;
    }
}