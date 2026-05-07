
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SnowmanStack extends GridItem {
    private static BufferedImage sprite;

    public static void LoadSprites() {
        try {
            sprite = ImageIO.read(new File("./resources/snowman_stack.png"));
        } catch (IOException e) {
            sprite = null;
        }
    }

    public SnowmanStack(int x, int y) {
        Position = new Point(x, y);
    }

    @Override
    public BufferedImage GetSprite() {
        return sprite;
    }
}