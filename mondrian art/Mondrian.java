import java.util.*;
import java.awt.*;

//Sahana Sarangi
//11/14/2024

//This class represents a Mondrian Art object that will be painted.
public class Mondrian {
    private Color[] palette;
    private static final int MIN_RECTANGLE_DIMENSION = 10;

    //This constructor creates a new Mondrian Art object that will be painted.
    public Mondrian() {
        palette = new Color[]{Color.RED, Color.YELLOW, Color.CYAN, Color.WHITE};
    }

    //This method paints a basic Mondrian Art canvas. A basic canvas is created by coloring
    //each of the rectangles a random color.
    //Parameters: 
    //  - pixels: a non-null 2d array of Color objects representing the canvas. 
    public void paintBasicMondrian(Color[][] pixels) {
        int width = pixels[0].length;
        int height = pixels.length;
        divideCanvas(pixels, 0, 0, height, width, false);
        createBorders(pixels);
    }

    //This method paints a complex Mondrian Art canvas. A complex canvas is created by making
    //the rectangles on the left side of the canvas lighter colors than the right side of the 
    //canvas.
    //Parameters: 
    //  - pixels: a non-null 2d array of Color objects representing the canvas.
    public void paintComplexMondrian(Color[][] pixels) {
        int width = pixels[0].length;
        int height = pixels.length;
        divideCanvas(pixels, 0, 0, height, width, true);
        createBorders(pixels);
    }

    //It divides the canvas into several rectangles of random width and height to be colored. 
    //Rectangles are divided until the height of every one is less than one fourth of the height
    //of the total canvas, and the width of every one is less than one fourth of the width of the
    //total canvas. The height and width of every rectangle is at least 10 pixels.
    //Parameters:
    // - pixels: a non-null 2d array of Color objects representing the canvas
    // - yStart: the starting y-coordinate of the area to potentially divide (int)
    // - yEnd: the ending y-coordinate of the area to potentially divide (int)
    // - xStart: the starting x-coordinate of the area to potentially divide (int)
    // - xEnd: the ending x-coordinate of the area to potentially divide (int)
    // - complex: a non-null boolean value that is true if the user wants the art to be complex
    //            and false if the user wants the art to be basic
    private void divideCanvas(Color[][] pixels, int yStart, int xStart, int yEnd, int xEnd, 
            boolean complex) {
        int height = yEnd - yStart;
        int width = xEnd - xStart;
        if (width >= (pixels[0].length / 4) && height >= (pixels.length / 4)) {
            int splitHor = split(height);
            int splitVert = split(width);
            divideCanvas(pixels, yStart, xStart, yEnd - splitHor, xEnd - splitVert, complex);
            divideCanvas(pixels, yStart, xEnd - splitVert, yEnd - splitHor, xEnd, complex);
            divideCanvas(pixels, yEnd - splitHor, xStart, yEnd, xEnd - splitVert, complex);
            divideCanvas(pixels, yEnd - splitHor, xEnd - splitVert, yEnd, xEnd, complex);
        } else if (height >= (pixels.length / 4)) {
            int splitHor = split(height);
            divideCanvas(pixels, yStart, xStart, yEnd - splitHor, xEnd, complex);
            divideCanvas(pixels, yEnd - splitHor, xStart, yEnd, xEnd, complex);
        } else if (width >= (pixels[0].length / 4)) {
            int splitVert = split(width);
            divideCanvas(pixels, yStart, xEnd - splitVert, yEnd, xEnd, complex);
            divideCanvas(pixels, yStart, xStart, yEnd, xEnd - splitVert, complex);
        } else {
            if (complex) {
                paintComplex(pixels, xStart, yStart, xEnd, yEnd);
            } else {
                paintBasic(pixels, xStart, yStart, xEnd, yEnd);
            }
        }
    }
 
    //This method generates a random number at which a rectangle will be split at.
    //Return: the random number to split the rectangle at
    //Parameters:
    //  - length: the distance between the coordinates between which a split is needed (int)
    private int split(int length) {
        int minSplit = Math.max(MIN_RECTANGLE_DIMENSION, length / 4);
        int maxSplit = Math.max(MIN_RECTANGLE_DIMENSION, length - MIN_RECTANGLE_DIMENSION);
    
        if (minSplit >= maxSplit) {
            return minSplit;
        }

        return minSplit + (int) (Math.random() * (maxSplit - minSplit + 1));
    }

    //This method paints a complex Mondarin Art design by making the left half of the canvas
    //lighter colors than the other half of the canvas. 
    //Parameters:
    // - pixels: a non-null 2d array of Color objects representing the canvas
    // - yStart: the starting y-coordinate of the area to paint (int)
    // - yEnd: the ending y-coordinate of the area to paint (int)
    // - xStart: the starting x-coordinate of the area to paint (int)
    // - xEnd: the ending x-coordinate of the area to paint (int)
    private void paintComplex(Color[][] pixels, int xStart, int yStart, int xEnd, int yEnd) {
        if (xEnd < pixels[0].length / 2 && xStart < pixels[0].length / 2) {
            paintLeft(pixels, xStart, yStart, xEnd, yEnd);
        } else {
            paintBasic(pixels, xStart, yStart, xEnd, yEnd);
        }
    }

    //This method makes rectangles on the left half of the canvas more likely to be lighter 
    //colors than the right half. 
    //Parameters:
    // - pixels: a non-null 2d array of Color objects representing the canvas
    // - yStart: the starting y-coordinate of the area to paint (int)
    // - yEnd: the ending y-coordinate of the area to paint (int)
    // - xStart: the starting x-coordinate of the area to paint (int)
    // - xEnd: the ending x-coordinate of the area to paint (int)
    private void paintLeft(Color[][] pixels, int xStart, int yStart, int xEnd, int yEnd) {
        Color color = getRandomColor();
        int currRed = color.getRed();
        int currBlue = color.getBlue();
        int currGreen = color.getGreen();
        int newRed = Math.max(0, currRed - 40);
        int newBlue = Math.max(0, currBlue - 40);
        int newGreen = Math.max(0, currGreen - 40);
        Color newColor = new Color(newRed, newBlue, newGreen);
        for (int i = yStart; i < yEnd - 1; i++) {
            for (int j = xStart; j < xEnd - 1; j++) {
                pixels[i][j] = newColor;
           }
        }
    }

    //This method paints rectangles in the canvas one of the colors in the palette.  
    //Parameters:
    // - pixels: a non-null 2d array of Color objects representing the canvas
    // - yStart: the starting y-coordinate of the area to paint (int)
    // - yEnd: the ending y-coordinate of the area to paint (int)
    // - xStart: the starting x-coordinate of the area to paint (int)
    // - xEnd: the ending x-coordinate of the area to paint (int)
    private void paintBasic(Color[][] pixels, int xStart, int yStart, int xEnd, int yEnd) {
        Color color = getRandomColor();
        for (int i = yStart; i < yEnd - 1; i++) {
            for (int j = xStart; j < xEnd - 1; j++) {
                pixels[i][j] = color;
           }
        }
    }

    //This method generates a random color to fill a rectangle with from the palette
    //Return: the random color (Color object)
    private Color getRandomColor() {
        int randColorIndex = (int) (Math.random() * 4);
        return palette[randColorIndex];
    }

    //This method creates the left and top borders of the canvas and colors them black.
    //Parameters:
    // - pixels: a non-null 2d array of Color objects representing the canvas
    private void createBorders(Color[][] pixels) {
        for (int i = 0; i < pixels[0].length - 1; i++) {
            pixels[0][i] = Color.BLACK;
        }

        for (int i = 0; i < pixels.length; i++) {
            pixels[i][0] = Color.BLACK;
        }
    }

}
