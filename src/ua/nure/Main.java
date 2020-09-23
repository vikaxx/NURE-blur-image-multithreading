package ua.nure;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Scanner input = new Scanner(System.in);
    private static File file = new File("src\\img\\1.png");

    public static void main(String[] args) {
        RectangleBlurring goBlur = new RectangleBlurring();

        goBlur.setRadius(newRadius());
        input.close();

        try {
            BufferedImage source = ImageIO.read(file);
            BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());

            long start = System.currentTimeMillis();
            System.out.println("start...");
            goBlur.oneThreadProcessing(source, result);

            double sec = ((System.currentTimeMillis() - start) / 1000.0);
            System.out.println("done by 1 thread in " + sec + " sec");

            long newStart = System.currentTimeMillis();
            goBlur.multiThreadProcessing(source, result);

            double newSec = ((System.currentTimeMillis() - newStart) / 1000.0);
            System.out.println("done by " + 10 + " threads in " + newSec + " sec");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int newRadius() {
        int radius;
        System.out.println("Input radius of image blurring: ");
        if (!input.hasNextInt()) {
            System.out.println("Incorrect number. Input radius. ");
            input.next();
            return newRadius();
        }
        radius = input.nextInt();
        if (radius < 1) {
            System.out.println("Incorrect number. Input radius bigger than 0. ");
            return newRadius();
        }
        return radius;
    }

}
