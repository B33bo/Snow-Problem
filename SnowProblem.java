import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.*;

public class SnowProblem {

    public static void main(String[] args) {
        LevelLoader.LoadHighScoreTable();
        JFrame frame = new JFrame("Snow Problem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        GamePanel panel = new GamePanel();
        panel.addMouseListener(panel); // lol

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton resetButton = new JButton("Reset");

        resetButton.addActionListener(e -> {
            panel.ResetButtonClicked();
        });

        JButton nextLevelButton = new JButton("Next Level");
        nextLevelButton.setEnabled(false);

        nextLevelButton.addActionListener(e -> {
            panel.NextLevelButtonClicked();
        });

        JLabel moveCount = new JLabel();
        moveCount.setText(" Loading... "); // i doubt this will ever even be seen
        bottomPanel.add(moveCount, BorderLayout.CENTER);

        panel.SetMoveCountText(moveCount);
        panel.SetNextLevelButton(nextLevelButton);
        
        bottomPanel.add(resetButton, BorderLayout.WEST);
        bottomPanel.add(nextLevelButton, BorderLayout.EAST);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(panel, BorderLayout.CENTER);

        InitSprites();

        frame.setVisible(true);
    }

    private static void InitSprites() {
        try {
            SnowmanHead.LoadSprites();
            SnowmanStack.LoadSprites();
            Tree.LoadSprites();
            Snowball.LoadSprites();
            Snowman.LoadSprites();
        }
        catch (IOException e) {
            // HANDLE LATER
            System.err.println(e);
        }
    }
}