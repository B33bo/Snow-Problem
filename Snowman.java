
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Snowman extends GridItem {
    private static BufferedImage redSprite, yellowSprite, blueSprite;
    public SnowmanColor snowmanColor;

    public static void LoadSprites() {
        try {
            redSprite = ImageIO.read(new File("./resources/snowman_red.png"));
            yellowSprite = ImageIO.read(new File("./resources/snowman_yellow.png"));
            blueSprite = ImageIO.read(new File("./resources/snowman_blue.png"));
        } catch (IOException e) {
            redSprite = null;
            yellowSprite = null;
            blueSprite = null;
        }
    }

    public Snowman(int x, int y, SnowmanColor color) {
        Position = new Point(x, y);
        snowmanColor = color;
    }

    @Override
    public BufferedImage GetSprite() {
        switch (snowmanColor) {
            case red -> {
                return redSprite;
            }
            case blue -> {
                return blueSprite;
            }
            case yellow -> {
                return yellowSprite;
            }
            default -> throw new AssertionError();
        }
    }
}