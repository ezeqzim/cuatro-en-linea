package com.example.ezeqzim.cuatro_en_linea;

/**
 * Created by Ezequiel on 8/10/2016.
 */
public class Player {
    private String name;
    private int id, move, normal_move, last_move, win_move;

    public Player(String name, int id, int move, int normal_move, int last_move, int win_move) {
        this.name = name;
        this.id = id;
        this.move = move;
        this.normal_move = normal_move;
        this.last_move = last_move;
        this.win_move = win_move;
    }

    public final String getName() {
        return name;
    }

    public final int getId() {
        return id;
    }

    public final int getMove() {
        return move;
    }

    public final int getNormalMove() {
        return normal_move;
    }

    public final int getLastMove() {
        return last_move;
    }

    public final int getWinMove() {
        return win_move;
    }
}
