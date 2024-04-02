package game;

import java.util.Random;

public class RandomPlayer implements Player {
    private final Random random;

    public RandomPlayer(final Random random) {
        this.random = random;
    }

    public RandomPlayer() {
        this(new Random());
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            int x1 = random.nextInt(100);
            int y1 = random.nextInt(100);
            int x2 = random.nextInt(100);
            int y2 = random.nextInt(100);

            final Move move = new Move(x1, y1, x2, y2, cell);
            if (position.isValid(move)) {
                return move;
            }
        }
    }

    @Override
    public String getName() {
        return "Random";
    }
}
