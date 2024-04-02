package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StickTour {
    private final int side;
    private List<Player> players;

    public StickTour(List<Player> players, int side) {
        this.players = players;
        this.side = side;
    }

    public String play() {
        int p = 1;
        while (p * 2 <= players.size()) p *= 2;
        if (players.size() != p) {
            playOff(players.size() - p);
        }

        System.out.println("Players in the tournament:");
        for (Player pl : players) {
            System.out.println(pl.getName());
        }

        return "";
    }

    private void playOff(int cnt) {
        System.out.println("Start play off of " + cnt * 2 + " gamers");
        List<Integer> playOff = new ArrayList<>();
        while (playOff.size() < 2 * cnt) {
            int minValue = 0;
            int maxValue = 2 * cnt;
            int k = minValue + (int) (Math.random() * (maxValue - minValue + 1));
            System.out.println(k);
            boolean ok = true;
            for (Integer player : playOff) {
                if (player.equals(k)) {
                    ok = false;
                    break;
                }
            }
            if (ok) playOff.add(k);
        }
        Game game;
        for (int i = 0; i < playOff.size(); i += 2) {
            int res = 0;
            while (res == 0) {
                System.out.println("Game between " + players.get(playOff.get(i)).getName() + " and " + players.get(playOff.get(i + 1)).getName());
                game = new Game(false, players.get(playOff.get(i)), players.get(playOff.get(i + 1)));
                res = game.play(new StickBoard(side));
                if (res == 0) System.out.println("Draw. Replay...");
            }
            if (res == 1) {
                players.set(playOff.get(i + 1), new HumanPlayer(System.out, new Scanner(System.in), "del"));
                System.out.println(players.get(playOff.get(i)).getName() + "win");
            } else {
                players.set(playOff.get(i), new HumanPlayer(System.out, new Scanner(System.in), "del"));
                System.out.println(players.get(playOff.get(i + 1)).getName() + "win");
            }
        }

        List<Player> newList = new ArrayList<>();

        for (Player pl : players) {
            if (!(pl.getName().equals("del"))) {
                newList.add(pl);
            }
        }

        players = newList;
    }
}
