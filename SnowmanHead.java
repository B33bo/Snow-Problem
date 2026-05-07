import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SnowmanHead extends MovableItem {
    public SnowmanColor snowColor;

    private static BufferedImage redSprite, blueSprite, yellowSprite;

    public static void LoadSprites() throws IOException {
        redSprite = ImageIO.read(new File("./resources/head_red.png"));
        blueSprite = ImageIO.read(new File("./resources/head_blue.png"));
        yellowSprite = ImageIO.read(new File("./resources/head_yellow.png"));
    }
    
    public SnowmanHead(int x, int y, SnowmanColor color) {
        Position = new Point(x, y);
        snowColor = color;
    }

    @Override
    public BufferedImage GetSprite() {
        switch (snowColor) {
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

    @Override
    public boolean CanMerge(GridItem stationary) {
        return stationary instanceof SnowmanStack;
    }

    @Override
    public GridItem MergeWith(GridItem stationary) {
        return new Snowman(stationary.Position.X, stationary.Position.Y, snowColor);
    }
}
