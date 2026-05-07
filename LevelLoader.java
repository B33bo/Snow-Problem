import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LevelLoader {
    public static void Load(GridItem[][] map, int level) {
        String levelName = "./levels/" + level + ".txt";
        List<String> lines;

        try {
            lines = Files.readAllLines(Path.of(levelName));
        }
        catch (IOException e) {
            // handle later
            return;
        }

        LoadLines(map, lines);
    }

    private static void LoadLines(GridItem[][] map, List<String> lines) {
        for (int y = 0; y < lines.size(); y++) {
            var line = lines.get(y);

            for (int x = 0; x < line.length(); x++) {
                var letter = line.charAt(x);
                map[x][y] = GetItem(letter, x, y);
            }
        }
    }

    private static GridItem GetItem(char c, int x, int y) {
        switch (c) {
            case '.' -> {
                return null;
            }
            case 's' -> {
                return new Snowball(x, y, false);
            }
            case 'S' -> {
                return new Snowball(x, y, true);
            }
            case 'r' -> {
                return new SnowmanHead(x, y, SnowmanColor.red);
            }
            case 'y' -> {
                return new SnowmanHead(x, y, SnowmanColor.yellow);
            }
            case 'b' -> {
                return new SnowmanHead(x, y, SnowmanColor.blue);
            }
            case '#' -> {
                return new Tree(x, y);
            }
            case 'x' -> {
                return new SnowmanStack(x, y);
            }
            default -> throw new AssertionError("Character " + c + " not recognised");
        }
    }
}
