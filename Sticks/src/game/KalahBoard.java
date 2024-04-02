//package game;
//
//public class KalahBoard implements Board{
//    private KalahPosition position;
//    private final int valueOfStones;
//    private final int[][] board;
//
//    private Cell turn;
//
//    public KalahBoard(int valueOfStones) {
//        this.valueOfStones = valueOfStones;
//        this.board = new int[2][7];
//        for (int i=0; i<6; ++i){
//            board[0][i] = valueOfStones/12;
//            board[1][i] = valueOfStones/12;
//        }
//        turn = Cell.FIRST;
//        position = new KalahPosition(board, turn);
//    }
//
//
//    @Override
//    public Position getPosition() {
//        return position.copy();
//    }
//
//    @Override
//    public Cell getCell() {
//        return turn;
//    }
//
//    @Override
//    public Result makeMove(Move move) {
//        if(move.getHole() == 1192387){
//            return Result.WIN;
//        }
//        final int pl = move.getPlayer();
//        final int hole = move.getHole() - 1;
//        int cur = pl, pos = hole;
////        for(int i=0; i<2; ++i){
////            for(int j=0; j<7; ++j){
////                System.out.print(board[i][j] + " ");
////            }
////            System.out.println();
////        }
//
//        final int num = board[pl][hole];
//        board[pl][hole] = 0;
//        boolean isSecondMove = false;
//        for(int i=1; i <= num; ++i){
//            pos++;
//            if(pos >= 7){
//                cur = (cur + 1) % 2;
//                pos %= 7;
//            }
//            if(pos == 6 && pl != cur) continue;
//            if(i == num && pl == cur && pos != 6 && board[cur][pos] == 0 && board[(cur + 1) % 2][5 - pos] != 0){
//                board[cur][6] += 1 + board[(cur + 1) % 2][5 - pos];
//                board[(cur + 1) % 2][5 - pos]  = 0;
//                continue;
//            }
//            if(cur == pl && pos == 6 && i == num){
//                isSecondMove = true;
//            }
//            board[cur][pos]++;
//        }
//        int s1 = 0, s2 = 0;
//        for(int i=0; i<6; ++i){
//            s1 += board[pl][i];
//            s2 += board[(pl + 1)%2][i];
//        }
//        if(s1 == 0 || s2 == 0){
//            int v1 = s1 + board[pl][6],
//                v2 = s2 + board[(pl + 1)%2][6];
//            if(v1 > v2) return Result.WIN;
//            else if(v1 < v2) return Result.LOSE;
//            else return Result.DRAW;
//        }
//
//        turn = isSecondMove ? turn: turn == Cell.FIRST ? Cell.SECOND: Cell.FIRST;
//
//        position = new KalahPosition(board, turn);
//
//        return Result.UNKNOWN;
//    }
//}
