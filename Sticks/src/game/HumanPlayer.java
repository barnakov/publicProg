package game;

import java.io.PrintStream;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;
    private final String name;

    public HumanPlayer(final PrintStream out, final Scanner in, final String name) {
        this.out = out;
        this.in = in;
        this.name = name;
    }

    public HumanPlayer(String name) {
        this(System.out, new Scanner(System.in), name);
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            out.println("Position");
            out.println(position);
            if (name.isEmpty()) {
                out.println(cell + "'s move");
            } else {
                out.println(name + "'s move");
            }
            out.println("Enter the coordinates of start and end positions");

            final Move move = new Move(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Move is invalid");
        }
    }

    public String getName() {
        return name;
    }

}
