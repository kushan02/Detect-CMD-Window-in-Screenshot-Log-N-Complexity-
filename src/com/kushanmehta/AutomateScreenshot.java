package com.kushanmehta;/* Author: Kushan Mehta
 * Date: 16-07-2018
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class AutomateScreenshot {

    private static Robot robot;

   /* public AutomateScreenshot() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new IllegalArgumentException("No robot");
        }
    }*/

    public static void captureScreen(String fileName) {

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new IllegalArgumentException("No robot");
        }


        // Press Alt + PrintScreen
        // (Windows shortcut to take a screen shot of the active window)
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_PRINTSCREEN);
        robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
        robot.keyRelease(KeyEvent.VK_ALT);

        saveFromClipboard(fileName);
    }

    private static void saveFromClipboard(String filename) {

        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor[] flavors = cb.getAvailableDataFlavors();
        for (DataFlavor flavor : flavors) {
            if (flavor.toString().indexOf("java.awt.Image") <= 0) {
                continue;
            }
            try {
                Image i = (Image) cb.getData(flavor);
                BufferedImage bi = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = bi.createGraphics();
                g.drawImage(i, 0, 0, null);
                g.dispose();
                ImageIO.write(bi, "png", new File(filename));
            } catch (Exception e) {
                throw new IllegalArgumentException("Error in saving screenshot!");
            }
        }
    }


}
