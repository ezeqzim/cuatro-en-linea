package com.example.ezeqzim.cuatro_en_linea.BackEnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Game {
    private ArrayList<PlayerStatistic> players;
    private Board board;
    private int active_player_index;
    private Stack<Cell> moves = new Stack<>();

    public Game(int rows, int cols, ArrayList<String> names) {
        this.board = new Board(rows, cols);
        this.players = new ArrayList<>();
        for (String name : names) this.players.add(new PlayerStatistic(new Player(name), 0));
        this.active_player_index = 0;
    }

    public int getCols() {
        return board.getCols();
    }

    public int getRows() {
        return board.getRows();
    }


    private void passTurn() {
        active_player_index = (active_player_index + 1 + players.size()) % players.size();
    }

    private void retreatTurn() {
        active_player_index = (active_player_index - 1 + players.size()) % players.size();
    }

    public PlayerStatistic getPlayerStatistic(int player_index) {
        return players.get(player_index);
    }

    public List<PlayerStatistic> getPlayersStatistic() {
        return players;
    }

    public void restart() {
        board.reset();
        moves = new Stack<>();
    }

    public WinStatus winner(int player_index) {
        WinStatus status = board.win(player_index);
        if (status == WinStatus.WIN) {
            PlayerStatistic player = getPlayerStatistic(player_index);
            player.addWin();
            players.set(player_index, player);
        }
        return status;
    }

    public List<Cell> getWinCells(){
        return board.getWinCells();
    }

    public List<Cell> makePlay(int col) {
        List<Cell> res = new ArrayList<>(2);
        if (board.setPosition(col, active_player_index)) {
            Cell actual_move = board.getLastSettledCell();
            res.add(actual_move);
            if (!moves.empty())
                res.add(moves.peek());
            moves.push(actual_move);
        }
        passTurn();
        return res;
    }

    public List<Cell> undoLastMove() {
        List<Cell> res = new ArrayList<>();
        if (moves.isEmpty())
            return res;
        Cell last_move = moves.pop();
        board.clearPosition(last_move.getRow(), last_move.getCol());
        retreatTurn();
        res.add(last_move);
        if (!moves.isEmpty())
            res.add(moves.peek());
        return res;
    }
}
