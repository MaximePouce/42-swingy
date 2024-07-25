package com.mpouce.swingy.view.utils;

import java.awt.Image;
import javax.swing.ImageIcon;

public class ImageUtil {
    private ImageUtil() {}

    public static Image getImage(String imageName, int width, int height) {
        ImageIcon imageIcon;
        Image image;
        try {
            imageIcon = new ImageIcon(imageName);
            image = imageIcon.getImage();

            if (image.getWidth(null) == -1) {
                throw new Exception("Image not found: " + imageName);
            }
        } catch (Exception e) {
            e.getMessage();
            imageIcon = new ImageIcon("notfound.jpg");
            image = imageIcon.getImage();
        }
        return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
}