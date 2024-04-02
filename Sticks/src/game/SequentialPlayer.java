package game;

public class SequentialPlayer implements Player {
    @Override
    public Move move(final Position position, final Cell cell) {
        for (int x1 = 0; x1 <= 100; ++x1) {
            for (int y1 = 0; y1 <= 100; ++y1) {
                for (int x2 = 0; x2 <= 100; ++x2) {
                    for (int y2 = 0; y2 <= 100; ++y2) {
                        final Move move = new Move(x1, y1, x2, y2, cell);
                        if (position.isValid(move)) {
                            return move;
                        }
                    }
                }
            }
        }
        throw new IllegalStateException("No valid moves");
    }

    @Override
    public String getName() {
        return "Sequential";
    }
}
