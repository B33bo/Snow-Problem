
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Snowball extends MovableItem {
    public boolean Large;
    private static BufferedImage smallSprite, largeSprite;

    public static void LoadSprites() throws IOException {
        smallSprite = ImageIO.read(new File("./resources/snowball_small.png"));
        largeSprite = ImageIO.read(new File("./resources/snowball_large.png"));
    }

    public Snowball(int x, int y, boolean large) {
        Position = new Point(x, y);
        Large = large;
    }

    @Override
    public BufferedImage GetSprite() {
        return Large ? largeSprite : smallSprite;
    }

    @Override
    public boolean CanMerge(GridItem stationary) {
        // this function means this is moving and stationary is.. stationary

        if (!(stationary instanceof Snowball))
            // snowballs can only merge with each other
            return false;
        
        if (Large)
            // large snowballs can't really merge with anything while it is moving
            return false;

        var otherSnowball = (Snowball)stationary;

        // small snowballs can merge with large ones
        return otherSnowball.Large;
    }

    @Override
    public GridItem MergeWith(GridItem stationary) {
        // we know it must be a large snowball we are merging into so js create the stack
        return new SnowmanStack(stationary.Position.X, stationary.Position.Y);
    }
}