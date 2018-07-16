package com.kushanmehta;/* Author: Kushan Mehta
 * Date: 16-07-2018
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Coordinate {
    int x;
    int y;

    public Coordinate() {
        x = 0;
        y = 0;
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Square {

    // These will define the boundaries for a square (in terms of rows and columns)

    public static final int SIZE = 25;
    public static final int LEFT_COLUMN = 25;

    public Square() {
    }

    int topRow;
    int bottomRow;
    int leftColumn;
    int rightColumn;

    public Square(int topRow, int bottomRow, int leftColumn, int rightColumn) {
        this.topRow = topRow;
        this.bottomRow = bottomRow;
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }
}

public class ProcessImage extends Component {

    private BufferedImage image;
    private int imageWidth;
    private int imageHeight;

    public ProcessImage(String fileName) {
        try {
            // get the BufferedImage, using the ImageIO class
//            BufferedImage image = ImageIO.read(this.getClass().getResource(fileName));
            // image = ImageIO.read(this.getClass().getResource(fileName));

            image = ImageIO.read(new File(fileName));

            imageWidth = image.getWidth();
            imageHeight = image.getHeight();

            //  marchThroughImage(image);

            int ans = getCropRow();

            System.out.println("The pixel at which crop is to be made is " + ans);

            cropImage(ans);

            imageWidth = image.getWidth();
            imageHeight = image.getHeight();

            //TODO: Add crop detection for image horizontally too! (use concept of strip)

//            rotateImageClockwise();
//
//            imageWidth = image.getWidth();
//            imageHeight = image.getHeight();
//
//            cropImage(getCropRow());
//
//            imageWidth = image.getWidth();
//            imageHeight = image.getHeight();
//
//            rotateImageAntiClockwise();
//
//            imageWidth = image.getWidth();
//            imageHeight = image.getHeight();

            saveImage(fileName);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void cropImage(int cropHeight) {

        if (cropHeight < imageHeight)
            image = image.getSubimage(0, 0, imageWidth, cropHeight);
    }

    private void rotateImageClockwise() {
        image = new AffineTransformOp(
                AffineTransform.getQuadrantRotateInstance(
                        1, imageWidth / 2, imageHeight / 2),
                AffineTransformOp.TYPE_BILINEAR).filter(image, null);
    }

    private void rotateImageAntiClockwise() {
        image = new AffineTransformOp(
                AffineTransform.getQuadrantRotateInstance(
                        3, imageWidth / 2, imageHeight / 2),
                AffineTransformOp.TYPE_BILINEAR).filter(image, null);
    }

    private void saveImage(String outputFileName) {

        String newFileName = outputFileName.substring(0, outputFileName.lastIndexOf('.'));
        newFileName += "-newOutput.png";


        File outputfile = new File(newFileName);
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPixelARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
//        int red = (pixel >> 16) & 0xff;
//        int green = (pixel >> 8) & 0xff;
//        int blue = (pixel) & 0xff;

        int[] rgbArray = getRGBArray(pixel);
        System.out.println("argb: " + alpha + ", " + rgbArray[0] + ", " + rgbArray[1] + ", " + rgbArray[2]);
    }

    private int[] getRGBArray(int pixel) {
//        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        int[] rgbAr = new int[3];
        rgbAr[0] = red;
        rgbAr[1] = green;
        rgbAr[2] = blue;

        return rgbAr;
    }


    private int getCropRow() {

        System.out.println("width, height: " + imageWidth + ", " + imageHeight);

        int upperRow = 0;
        int lowerRow = imageHeight - 1;

        Square square = new Square();

        int midRow = 0;

        while (upperRow <= lowerRow) {


            midRow = (int) (upperRow + lowerRow) >> 1;

            System.out.println("UpperRow = " + upperRow + ", lowerRow=" + lowerRow + ", Mid = " + midRow);


            square.topRow = midRow;
            square.bottomRow = Square.SIZE + midRow;
            square.leftColumn = Square.LEFT_COLUMN;
            square.rightColumn = Square.SIZE + Square.LEFT_COLUMN;

            if (hasTextPresence(square)) {
                upperRow = midRow + 1;
            } else {
                lowerRow = midRow - 1;
            }

        }


        // return lowerRow;

        return lowerRow + (int) (1.5 * Square.SIZE);
    }

    private boolean hasTextPresence(Square square) {

        int count = 0;

        for (int i = square.topRow; i <= square.bottomRow && i < imageHeight; ++i) {

            for (int j = square.leftColumn; j <= square.rightColumn; ++j) {

                int pixel = image.getRGB(j, i);
                int[] rgbArray = getRGBArray(pixel);

                if (count > 5)
                    return true;

                // if (rgbArray[0] == rgbArray[1] && rgbArray[1] == rgbArray[2] && rgbArray[0] == 255) {
                if (rgbArray[0] != 0 || rgbArray[1] != 0 || rgbArray[2] != 0) {
                    ++count;
                    //  System.out.println("i = " + i + ", j = " + j + " - white found");
                    printPixelARGB(pixel);
                }

            }

        }

        return false;
    }

    private void marchThroughImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        System.out.println("width, height: " + w + ", " + h);

        int gapWidth = w / 30;

      /*  for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                System.out.println("x,y: " + j + ", " + i);
                int pixel = image.getRGB(j, i);
                printPixelARGB(pixel);
                System.out.println("");
            }
        }*/

    }


}
