package schach2022.gameUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ResizePNGImage {
    public static void main(String[] args) {
        try {
            // Load the original PNG image
            BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\yassi\\IdeaProjects\\Schach\\src\\schach2022\\Icons\\King_White.png"));

            // Specify the desired width and height for the resized image
            int newWidth = 100;
            int newHeight = 100;

            // Create a new buffered image with transparency
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

            // Get the graphics context of the resized image
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // Draw the original image onto the resized image
            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            // Save the resized image as a PNG file
            ImageIO.write(resizedImage, "PNG", new File("C:\\Users\\yassi\\IdeaProjects\\Schach\\src\\schach2022\\Icons\\resized.png"));

            System.out.println("Resized image saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
