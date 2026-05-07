import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    private static int[] highScores;

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

    public static void LoadHighScoreTable() {
        highScores = new int[100];
        try {            
            List<String> lines = Files.readAllLines(Path.of("./highscore.txt"));
            for (int i = 0; i < lines.size(); i++) {
                try {
                    highScores[i] = Integer.parseInt(lines.get(i));
                } catch (NumberFormatException e) {
                    highScores[i] = 0; // high score 0 = unknown
                }
            }
        } catch (IOException e) {
            highScores = new int[100];
        }
    }

    public static int GetHighScore(int level) {
        if (level <= 0 || level >= highScores.length)
            return -1;
        return highScores[level - 1]; // level 1 -> index 0
    }

    public static void TrySetHighScore(int level, int score) {
        int index = level - 1;
        if (index >= highScores.length) {
            int[] newHighscoreIndex = new int[index + 10];
            System.arraycopy(highScores, 0, newHighscoreIndex, 0, highScores.length);
            newHighscoreIndex[index] = score;
            highScores = newHighscoreIndex;
            SaveHighscore();
            return;
        }

        if (highScores[index] < score && highScores[index] > 0)
            return;
        highScores[index] = score;
        SaveHighscore();
    }

    private static void SaveHighscore() {
        ArrayList<String> lines = new ArrayList<>();
        
        for (int i = 0; i < highScores.length; i++) {
            lines.add(highScores[i] + "");
        }

        try {
            Files.write(Path.of("./highscore.txt"), lines);
        } catch (IOException e){
            System.err.println("Could not save highscore");
        }
    }
}
