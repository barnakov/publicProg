import game.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        testOneByOne();
        //testRandom();
        //testSequential();
        //testTour();
    }

    public static void testOneByOne() {
        Game game = new Game(true, new HumanPlayer("biba"), new HumanPlayer("boba"));
        game.play(new StickBoard(2));
    }

    public static void testRandom() {
        Game game = new Game(false, new HumanPlayer("biba"), new RandomPlayer());
        game.play(new StickBoard(3));
    }

    public static void testSequential() {
        Game game = new Game(false, new HumanPlayer("biba"), new SequentialPlayer());
        game.play(new StickBoard(3));
    }

    public static void testTour() {
        StickTour stickTour = new StickTour(List.of(new HumanPlayer("biba"), new HumanPlayer("boba"),
                new RandomPlayer(), new SequentialPlayer()), 3);
    }
}