package game;

public final class Move {
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final Cell player;

    public Move(final int x1, final int y1, final int x2, final int y2, final Cell player) {
        this.player = player;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public int getPlayer() {
        if (player == Cell.FIRST) return 0;
        else return 1;
    }

    @Override
    public String toString() {
        return "start: " + x1 + " " + y1 + ", end: " + x2 + " " + y2 + ", player=" + player;
    }
}
