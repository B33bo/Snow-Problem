public class GameManager {
    private final GridItem[][] items;
    private final int width, height;

    private Point selectionPoint;
    private GridItem selected;
    private int level = 1;
    private int moves = 0;
    private boolean failed = false;

    public GameManager(int width, int height) {
        this.width = width;
        this.height = height;

        items = new GridItem[width][height];
    }

    public int GetWidth() { return width; }
    public int GetHeight() { return height; }
    public int GetMoves() { return moves; }

    public int GetHighscore() { return LevelLoader.GetHighScore(level); }

    public void LoadLevel(int level) {
        LevelLoader.Load(items, level);
    }

    public static void VerifyAllLevels() {
        GameManager dummy = new GameManager(5, 4);
        for (int i = 1; i <= 80; i++) {
            System.out.println("Level " + i);
            LevelLoader.Load(dummy.items, i);
            if (!dummy.VerifyLevel())
                System.err.println("Level " + i + " not working");
        }
    }

    public boolean VerifyLevel() {
        // for debugging purposes. making sure the levels load properly
        int redSnowmanHeads = 0;
        int blueSnowmanHeads = 0;
        int yellowSnowmanHeads = 0;
        int smallSnowballs = 0;
        int bigSnowballs = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (items[x][y] instanceof Snowball snowball){
                    if (snowball.Large)
                        bigSnowballs++;
                    else
                        smallSnowballs++;
                }
                else if (items[x][y] instanceof SnowmanHead snowmanHead) {
                    if (snowmanHead.snowColor == SnowmanColor.red)
                        redSnowmanHeads++;
                    if (snowmanHead.snowColor == SnowmanColor.blue)
                        blueSnowmanHeads++;
                    if (snowmanHead.snowColor == SnowmanColor.yellow)
                        yellowSnowmanHeads++;
                }
            }
        }

        int totalHeads = redSnowmanHeads + blueSnowmanHeads + yellowSnowmanHeads;
        if (redSnowmanHeads > 1 || blueSnowmanHeads > 1 || yellowSnowmanHeads > 1)
            return false;

        return smallSnowballs == totalHeads && bigSnowballs == totalHeads;
    }

    public void SpawnItem(GridItem item) {
        items[item.Position.X][item.Position.Y] = item;
    }

    public GridItem ItemAt(Point p){
        return items[p.X][p.Y];
    }

    public GridItem ItemAt(int x, int y){
        return items[x][y];
    }

    public GridItem GetSelectedItem() {
        return selected;
    }

    public Point GetSelectedPoint() {
        return selectionPoint;
    }

    public void Deselect() {
        selected = null;
        selectionPoint = null;
    }
    
    public boolean LevelCompleted() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // if any of these are in the grid then its not completed
                if (items[x][y] instanceof MovableItem)
                    return false;
            }
        }
        return true;
    }

    public boolean LevelFailed() {
        return failed;
    }

    public void NextLevel() {
        level++;
        ResetLevel();
    }

    public void ResetLevel() {
        selected = null;
        selectionPoint = null;
        moves = 0;
        failed = false;
        LevelLoader.Load(items, level);
    }

    public void Select(Point position) {
        if (position == null) {
            Deselect();
            return;
        }

        if (LevelFailed())
            return;

        if (selectionPoint != null && selectionPoint.X == position.X && selectionPoint.Y == position.Y) {
            Deselect();
            return;
        }

        boolean isMovementVector = selectionPoint != null && (position.X == selectionPoint.X || position.Y == selectionPoint.Y);
        if (selected instanceof MovableItem && isMovementVector)
        {
            int dx = Normalise(position.X - selectionPoint.X);
            int dy = Normalise(position.Y - selectionPoint.Y);
            if (selected instanceof SnowmanHead) {
                SnowmanHeadMove(dx, dy);
                Deselect(); // called here incase it fails to move
            }
            else{
                MoveInDirection(dx, dy);
            }

            if (LevelCompleted())
                LevelLoader.TrySetHighScore(level, moves);
            return;
        }

        selectionPoint = position;
        selected = items[selectionPoint.X][selectionPoint.Y];
    }

    private static int Normalise(int direction) {
        if (direction == 0)
            return 0;
        return direction > 0 ? 1 : -1;
    }

    private void SnowmanHeadMove(int dx, int dy) {
        SnowmanHead head = (SnowmanHead)selected;
        Point nextPos = new Point(head.Position.X + dx, head.Position.Y + dy);
        if (nextPos.X < 0 || nextPos.X >= width || nextPos.Y < 0 || nextPos.Y >= height)
            return;
        if (!head.CanMerge(items[nextPos.X][nextPos.Y]))
            return;
        moves++;
        var snowman = head.MergeWith(items[nextPos.X][nextPos.Y]);
        items[selected.Position.X][selected.Position.Y] = null;
        items[nextPos.X][nextPos.Y] = snowman;
    }

    private void MoveInDirection(int dx, int dy) {
        moves++;
        MovableItem selectedMovable = (MovableItem)selected;
        boolean firstLoop = true;

        while (true) { 
            int nextX = selected.Position.X + dx;
            int nextY = selected.Position.Y + dy;

            if (nextX < 0 || nextX >= width || nextY < 0 || nextY >= height) {
                // A null position signals a game loss
                MoveItem(selected.Position, null);
                failed = true;
                break;
            }
            
            if (items[nextX][nextY] != null) {
                if (firstLoop && selectedMovable.CanMerge(items[nextX][nextY]))
                    Merge(selectedMovable, items[nextX][nextY]);
                break;
            }

            firstLoop = false;
            MoveItem(selectedMovable.Position, new Point(nextX, nextY));
        }

        Deselect();
    }

    private void Merge(MovableItem moving, GridItem stationary) {
        items[selected.Position.X][selected.Position.Y] = null;

        var newItem = moving.MergeWith(stationary);
        items[newItem.Position.X][newItem.Position.Y] = newItem;
    }
    
    private void MoveItem(Point from, Point to) {
        if (to == null){
            items[from.X][from.Y].Position = null;
            items[from.X][from.Y] = null;
            return;
        }

        items[from.X][from.Y].Position = to;

        items[to.X][to.Y] = items[from.X][from.Y];
        items[from.X][from.Y] = null;
    }
}
