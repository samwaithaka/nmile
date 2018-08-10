package com.nextramile.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * This program demonstrates how to resize an image.
 *
 * @author www.codejava.net
 *
 */
public class ImageResizer {

    public static BufferedImage resize(BufferedImage img, int widthAfter, int heightAfter) {
        if(img.getWidth() > widthAfter) {
            double ratio = (double)img.getWidth()/(double)widthAfter;
            int height = (int)(img.getHeight()/ratio);
            Image tmp = img.getScaledInstance(widthAfter, height, Image.SCALE_SMOOTH);
            BufferedImage resized = new BufferedImage(widthAfter, height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g2d = resized.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            if(height > heightAfter) {
                resized = cropImage(resized, widthAfter, heightAfter);
            }
            return resized;
        } else if(img.getHeight() > heightAfter) {
        	BufferedImage resized = cropImage(img, img.getWidth(), heightAfter);
        	return resized;
        } else {
            return img;
        }
    }
    
    private static BufferedImage cropImage(BufferedImage img, int width, int height) {
        BufferedImage dest = img.getSubimage(0, 0, width, height);
        return dest; 
    }
}