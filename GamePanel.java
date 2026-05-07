import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

class GamePanel extends JPanel implements MouseListener {
    private final int tileSize = 100;
    private final GameManager gameManager;
    private JButton nextLevelButton;
    private JLabel moveCountText;

    public GamePanel() {
        gameManager = new GameManager(5, 4);
        gameManager.LoadLevel(1);
    }

    public void ResetButtonClicked() {
        gameManager.ResetLevel();
        repaint();
    }

    public void NextLevelButtonClicked() {
        gameManager.NextLevel();
        repaint();
    }

    public void SetNextLevelButton(JButton button) {
        nextLevelButton = button;
    }

    public void SetMoveCountText(JLabel moveCountText) {
        this.moveCountText = moveCountText;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // i did it using this instead of JFrames i hope thats okay
        super.paintComponent(g);

        // Draw grid
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x < gameManager.GetWidth(); x++) {
            for (int y = 0; y < gameManager.GetHeight(); y++) {
                var windowPos = MapGridToWindow(x, y);

                if (gameManager.ItemAt(x, y) == null)
                    DrawBlank(g, new Point(x, y), windowPos);
                else
                    DrawItem(g, gameManager.ItemAt(x, y), windowPos);

                // this bit just draws the grid bit so needs to come last as to draw over everything
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(windowPos.X, windowPos.Y, tileSize, tileSize);
            }
        }

        if (gameManager.LevelFailed()) {
            moveCountText.setText(" LEVEL FAILED");
            moveCountText.setForeground(Color.red);
        }
        else {
            String movesText = " Moves: " + gameManager.GetMoves();
            int currentHighscore = gameManager.GetHighscore();

            if (currentHighscore > 0)
                movesText += " Highscore: " + currentHighscore;

            moveCountText.setText(movesText);
            moveCountText.setForeground(Color.BLACK);
        }

        nextLevelButton.setEnabled(gameManager.LevelCompleted());
    }

    private void DrawBlank(Graphics g, Point gridPosition, Point windowPos) {
        var selectionPoint = gameManager.GetSelectedPoint();

        boolean isAdjacentToSelection = selectionPoint != null && (gridPosition.X == selectionPoint.X || gridPosition.Y == selectionPoint.Y);
        boolean isHighlighted = gameManager.GetSelectedItem() instanceof MovableItem && isAdjacentToSelection;

        g.setColor(isHighlighted ? Color.LIGHT_GRAY : Color.WHITE);
        g.fillRect(windowPos.X, windowPos.Y, tileSize, tileSize);

        // didnt like it sorry mr professor
        //g.drawImage(holeSprite, windowPos.X, windowPos.Y, tileSize, tileSize, null);
    }

    private void DrawItem(Graphics g, GridItem item, Point windowPos) {        
        var spr = item.GetSprite();

        if (spr == null)
            throw new NullPointerException();

        g.setColor(Color.GREEN);
        g.drawImage(spr, windowPos.X, windowPos.Y, tileSize, tileSize, null);
    }

    private Point MapWindowToGrid(int x, int y) {
        int xOffset = (getWidth() - (gameManager.GetWidth() * tileSize)) / 2;
        int yOffset = 20;

        int newX = (x - xOffset) / tileSize;
        int newY = (y - yOffset) / tileSize;

        if (newX < 0 || newX >= gameManager.GetWidth() || newY < 0 || newY >= gameManager.GetHeight()){
            newX = 0;
            newY = 0;
        }

        return new Point(newX, newY);
    }

    private Point MapGridToWindow(int x, int y) {
        int xOffset = (getWidth() - (gameManager.GetWidth() * tileSize)) / 2;
        int yOffset = 20;//(getHeight() - (gameManager.GetHeight() * tileSize)) / 2;

        return new Point(x * tileSize + xOffset, y * tileSize + yOffset);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        var gridPos = MapWindowToGrid(e.getX(), e.getY());
        gameManager.Select(gridPos);
        repaint();
    }
    
    // this is dumb. whoops
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}
}