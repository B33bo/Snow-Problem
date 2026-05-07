
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Tree extends GridItem {
    private static BufferedImage sprite;

    public Tree(int x, int y) {
        Position = new Point(x, y);
    }

    public static void LoadSprites() throws IOException {
        sprite = ImageIO.read(new File("./resources/tree.png"));
    }

    @Override
    public BufferedImage GetSprite() {
        return sprite;
    }
}
