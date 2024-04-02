//package game;
//
//public class KalahPosition implements Position{
//
//    private final int[][] board;
//    private final Cell turn;
//
//    public KalahPosition(int[][] board) {
//        this.board = board;
//        this.turn = Cell.FIRST;
//    }
//
//    public KalahPosition(int[][] board, Cell turn) {
//        this.board = board;
//        this.turn = turn;
//    }
//
//    @Override
//    public boolean isValid(Move move) {
//        return move.getHole() > 0 && move.getHole() < 7
//        && board[move.getPlayer()][move.getHole() - 1] != 0 || move.getHole() == 1192387;
//    }
//
//    @Override
//    public Cell getCell(int r, int c) {
//        return turn;
//    }
//
//    public Position copy() {
//        return new KalahPosition(board, turn);
//    }
//
//    @Override
//    public String toString(){
//        final int pl = turn == Cell.FIRST ? 0: 1;
//        StringBuilder out = new StringBuilder();
//        out.append(" ".repeat(7));
//        for(int i=6; i>=1; --i){
//            out.append(i).append(" ".repeat(3));
//        }
//        out.append("\n");
//        out.append("|").append("-".repeat(4)).append("|");
//        for(int i=5; i>=0; --i){
//            out.append(" ").append(board[(pl + 1) % 2][i]);
//            if(board[(pl + 1) % 2][i] < 10){
//                out.append(" ");
//            }
//            out.append("|");
//        }
//        out.append("-".repeat(4)).append("|\n").append("| ");
//        if(board[(pl + 1) % 2][6] < 10){
//            out.append(" ");
//        }
//        out.append(board[(pl + 1)%2][6]).append(" |").append("---|".repeat(6)).append(" ");
//        if(board[pl][6] < 10){
//            out.append(" ");
//        }
//        out.append(board[pl][6]).append(" |\n").append("|").append("-".repeat(4)).append("|");;
//        for(int i=0; i<6; ++i){
//            out.append(" ").append(board[pl][i]);
//            if(board[pl][i] < 10){
//                out.append(" ");
//            }
//            out.append("|");
//        }
//        out.append("-".repeat(4)).append("|\n");
//        out.append(" ".repeat(7));
//        for(int i=1; i<=6; ++i){
//            out.append(i).append(" ".repeat(3));
//        }
//        return out.toString();
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
