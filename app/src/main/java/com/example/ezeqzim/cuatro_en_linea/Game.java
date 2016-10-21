package com.example.ezeqzim.cuatro_en_linea;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Ezequiel on 8/10/2016.
 */
public class Game {
    private Player[] players;
    private Board board;
    private int active_player_id;
    private int[] wins;
    private Stack<int[]> moves = new Stack<>();
    private boolean waiting = false;

    public Game(int rows, int cols, ArrayList<String> names, int[][] shapes) {
        this.board = new Board(rows, cols);
        this.players = new Player[names.size() + 1];
        for (int i = 0; i < names.size(); ++i)
            this.players[i + 1] = new Player(names.get(i), i + 1, shapes[0][i], shapes[1][i], shapes[2][i], shapes[3][i]);
        this.active_player_id = 1;
        this.wins = new int[names.size() + 1];
    }

    public int getCols() {
        return board.getCols();
    }

    public int getRows() {
        return board.getRows();
    }

    public void passTurn() {
        active_player_id = (active_player_id % (players.length - 1)) + 1;
    }

    private int otherPlayer() {
        return (active_player_id % (players.length - 1)) + 1;
    }

    private Player active_player() {
        return players[active_player_id];
    }

    public void restart() {
        board.reset();
        moves = new Stack<>();
        waiting = false;
    }

    public void concede() {
        wins[otherPlayer()]++;
    }

    public Pair<Pair<Boolean, Player>, int[][]> winner() {
        Pair<Boolean, int[][]> win = board.win(active_player_id);
        if (win.first) {
            waiting = true;
            if (win.second.length > 0)
                wins[active_player_id]++;
        }
        return Pair.create(Pair.create(win.first, active_player()), win.second);
    }

    public Pair<Pair<Boolean, Player>, int[][]> makePlay(int col) {
        if (!waiting) {
            int[] play = board.setPosition(col, active_player_id);
            if (play.length == 0) {
                return Pair.create(Pair.create(true, active_player()), new int[][]{});
            }
            else {
                int[] last_move = {};
                if (!moves.empty())
                     last_move = moves.peek();
                moves.push(play);
                if (last_move.length == 0)
                    return Pair.create(Pair.create(true, active_player()), new int[][]{play});
                return Pair.create(Pair.create(true, active_player()), new int[][]{play, last_move});
            }
        }
        return Pair.create(Pair.create(false, active_player()), new int[][]{});
    }

    public Pair<Pair<Boolean, Player>, int[][]> undoLastMove() {
        if (!waiting) {
            if (moves.isEmpty())
                return Pair.create(Pair.create(true, active_player()), new int[][]{});
            int[] last_move = moves.pop();
            board.clearPosition(last_move[0], last_move[1]);
            passTurn();
            return Pair.create(Pair.create(true, active_player()), new int[][]{last_move, moves.peek()});
        }
        return Pair.create(Pair.create(false, active_player()), new int[][]{});
    }

    public final String whoseTurnString() {
        return active_player().getName() + " is playing";
    }

    public final String whoWinsString() {
        return active_player().getName() + " wins";
    }

    public final String resultsString() {
        String s = "";
        for (int i = 1; i < players.length; ++i)
            s += (i == 1 ? "" : " ") + players[i].getName() + ": " + wins[i];
        return s;
    }
}
