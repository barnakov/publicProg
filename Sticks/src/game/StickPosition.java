package game;

public class StickPosition implements Position {

    private final boolean[][] board;
    private final Cell turn;
    private final int side;

    public StickPosition(boolean[][] board, Cell turn, int side) {
        this.board = board;
        this.turn = turn;
        this.side = side;
    }


    public Position copy() {
        return new StickPosition(board, turn, side);
    }

    // 0 - top
    // 1 - right
    // 2 - bottom
    // 3 - left


    private boolean check(int coordinate) {
        return coordinate >= 0 && coordinate < side;
    }

    @Override
    public boolean isValid(Move move) {
        int x1 = move.getX1() - 1;
        int y1 = move.getY1() - 1;
        int x2 = move.getX2() - 1;
        int y2 = move.getY2() - 1;
        int delta = Math.abs(x1 - x2) + Math.abs(y1 - y2);
        if (delta > 1 || delta == 0) return false;
        if (!check(x1) || !check(x2) || !check(y1) || !check(y2)) return false;
        if (y2 > y1) {
            return !board[x1 + y1 * side][2];
        }
        if (x2 > x1) {
            return !board[x1 + y1 * side][1];
        }
        if (y1 > y2) {
            return !board[x1 + y1 * side][0];
        }
        if (x1 > x2) {
            return !board[x1 + y1 * side][3];
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < side * side; ++i) {
            if (i != 0 && i % side == 0) {
                sb.append(System.lineSeparator());
            }
            if (board[i][0]) {
                sb.append("|");
            } else {
                sb.append(".");
            }
            if (board[i][1]) {
                sb.append("__");
            } else {
                sb.append("  ");
            }
        }
        return sb.toString();
    }
}
