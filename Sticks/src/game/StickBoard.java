package game;

public class StickBoard implements Board {

    private StickPosition position;

    private final int side;
    private boolean[][] board;
    private Cell turn;
    private int pointsOfFirstPlayer = 0;
    private int pointsOfSecondPlayer = 0;

    public StickBoard(int side) {
        this.side = side;
        this.board = new boolean[side * side][4];
        for (int i = 0; i < side * side; ++i) {
            for (int j = 0; j < 4; ++j) {
                board[i][j] = false;
            }
        }
        this.turn = Cell.FIRST;
        this.position = new StickPosition(board, turn, side);
    }


    @Override
    public Position getPosition() {
        return position.copy();
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    // 0 - top
    // 1 - right
    // 2 - bottom
    // 3 - left

    private boolean isValidCoordinates(int x, int y) {
        return x >= 0 && x < side && y >= 0 && y < side;
    }

    private boolean checkBonus(int start, int x, int y) {
        int tmpX = x;
        int tmpY = y;
        boolean check = true;
        for (int i = start; i <= start + 3; ++i) {
            if (i % 4 == 0) {
                tmpY--;
            } else if (i % 4 == 1) {
                tmpX++;
            } else if (i % 4 == 2) {
                tmpY++;
            } else if (i % 4 == 3) {
                tmpX--;
            }
            check &= isValidCoordinates(tmpX, tmpY);
            if (!check) break;
            check = board[tmpX + tmpY * side][(i + 1) % 4];
        }
        if (check) return check;
        check = true;
        tmpX = x;
        tmpY = y;
        for (int i = start; i >= start - 3; --i) {
            if (i % 4 == 0) {
                tmpY--;
            } else if (i % 4 == 1) {
                tmpX++;
            } else if (i % 4 == 2) {
                tmpY++;
            } else if (i % 4 == 3) {
                tmpX--;
            }
            check &= isValidCoordinates(tmpX, tmpY);
            if (!check) break;
            check = board[tmpX + tmpY * side][(i + 7) % 4];
        }
        return check;
    }

    @Override
    public Result makeMove(Move move) {
        int x1 = move.getX1() - 1;
        int y1 = move.getY1() - 1;
        int x2 = move.getX2() - 1;
        int y2 = move.getY2() - 1;
        boolean isBonus = false;
        int countOfMarkedLines = 0;


        if (y2 > y1) {
            board[x1 + y1 * side][2] = true;
            board[x2 + y2 * side][0] = true;
            isBonus = checkBonus(2, x1, y1);

        } else if (x2 > x1) {
            board[x1 + y1 * side][1] = true;
            board[x2 + y2 * side][3] = true;
            isBonus = checkBonus(1, x1, y1);
        } else if (y1 > y2) {
            board[x1 + y1 * side][0] = true;
            board[x2 + y2 * side][2] = true;
            isBonus = checkBonus(0, x1, y1);
        } else if (x1 > x2) {
            board[x1 + y1 * side][3] = true;
            board[x2 + y2 * side][1] = true;
            isBonus = checkBonus(3, x1, y1);
        }

        if (isBonus) {
            if (turn == Cell.FIRST)
                pointsOfFirstPlayer++;
            else
                pointsOfSecondPlayer++;
        }

        for (int i = 0; i < side * side; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (board[i][j]) countOfMarkedLines++;
            }
        }


        if (countOfMarkedLines == side * side * 4 - side * 4) {
            if (pointsOfSecondPlayer == pointsOfFirstPlayer) return Result.DRAW;
            else if (turn == Cell.FIRST) {
                return pointsOfFirstPlayer > pointsOfSecondPlayer ? Result.WIN : Result.LOSE;
            } else {
                return pointsOfSecondPlayer > pointsOfFirstPlayer ? Result.WIN : Result.LOSE;
            }
        }

        turn = isBonus ? turn : turn == Cell.FIRST ? Cell.SECOND : Cell.FIRST;

        position = new StickPosition(board, turn, side);

        return Result.UNKNOWN;
    }
}
