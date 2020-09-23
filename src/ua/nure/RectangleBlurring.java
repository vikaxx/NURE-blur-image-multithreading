package ua.nure;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class RectangleBlurring {
    private int radius;
    private int squareLine;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        if (radius > 0) {
            this.radius = radius;
            setSquareLine();
        }
    }

    public int getSquareLine() {
        return squareLine;
    }

    private void setSquareLine() {
        this.squareLine = 2 * this.radius + 1;
    }

    public void multiThreadProcessing(BufferedImage source, BufferedImage result) throws IOException {
        File output;
        Thread thread = null;

        for (int k = 0; k < 10; k++) {
            int finalI = k;
            Runnable task = () -> {
                for (int x = source.getWidth() / 10 * finalI;
                     x < source.getWidth() / 10 + source.getWidth() / 10 * finalI;
                     x++) {
                    for (int y = 0; y < source.getHeight(); y++) {
                        blurAlgorithm(source, result, x, y);
                    }
                }
                Thread.currentThread().interrupt();
            };
            thread = new Thread(task);
            thread.start();
        }

        try {
            while (thread.isAlive()) {
                Thread.sleep(100);
            }
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        output = new File("src\\img\\1_blur" +
                (new Date().getTime()) +
                "_" +
                radius +
                "_radius" +
                "_multithread.png");
        ImageIO.write(result, "png", output);
    }

    public void oneThreadProcessing(BufferedImage source, BufferedImage result) throws IOException {
        for (int x = 0; x < source.getWidth(); x++) {
            progress(source, x);

            for (int y = 0; y < source.getHeight(); y++) {
                blurAlgorithm(source, result, x, y);
            }
        }

        File output = new File("src\\img\\1_blur" +
                (new Date().getTime()) +
                "_" +
                radius +
                "_radius.png");
        ImageIO.write(result, "png", output);
    }

    private void progress(BufferedImage source, int x) {
        for (int i = 0; i < 10; i++) {
            if (x == source.getWidth() / 100 * (10 + 10 * i)) {
                System.out.println((10 + 10 * i) + "% here");
            }
        }
    }

    private void blurAlgorithm(BufferedImage source, BufferedImage result, int x, int y) {
        double nearBlue = 0.0;
        double nearRed = 0.0;
        double nearGreen = 0.0;

        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                try {
                    Color nearColor = new Color(source.getRGB(x + i, y + j));

                    nearBlue += nearColor.getBlue();
                    nearRed += nearColor.getRed();
                    nearGreen += nearColor.getGreen();
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }

        double newRed = nearRed / (squareLine * squareLine);
        double newGreen = nearGreen / (squareLine * squareLine);
        double newBlue = nearBlue / (squareLine * squareLine);

        Color newColor = new Color((int) newRed, (int) newGreen, (int) newBlue);

        result.setRGB(x, y, newColor.getRGB());
    }

}
