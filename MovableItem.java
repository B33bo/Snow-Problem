public abstract class MovableItem extends GridItem {
    public abstract boolean CanMerge(GridItem stationary);

    public abstract GridItem MergeWith(GridItem stationary);
}