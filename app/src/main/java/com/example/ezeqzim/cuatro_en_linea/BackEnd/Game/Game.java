package com.example.ezeqzim.cuatro_en_linea.BackEnd.Game;

import com.example.ezeqzim.cuatro_en_linea.BackEnd.Player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Game {
    private ArrayList<Player> players;
    private Board board;
    private int activePlayerIndex;
    private boolean gameEnded;
    private Stack<Cell> moves = new Stack<>();
    private WinStatus winStatus;

    public Game(int rows, int cols, ArrayList<String> names) {
        this.board = new Board(rows, cols);
        this.players = new ArrayList<>();
        for (String name : names) {
            PlayerStatistics playerStatistics = new PlayerStatistics();
            playerStatistics = new PlayerStatisticsWinDecorator(playerStatistics);
            Player player = new Player(name, playerStatistics);
            this.players.add(player);
        }
        activePlayerIndex = 0;
        restart();
    }

    public int getCols() {
        return board.getCols();
    }

    public int getRows() {
        return board.getRows();
    }

    public Player getActivePlayer() {
        return players.get(activePlayerIndex);
    }

    public boolean isEnded() {
        return gameEnded;
    }

    public List<Cell> makePlay(int col) {
        List<Cell> res = new ArrayList<>(2);
        if (board.setPosition(col, activePlayerIndex)) {
            Cell actual_move = board.getLastSettledCell();
            res.add(actual_move);
            if (!moves.empty())
                res.add(moves.peek());
            moves.push(actual_move);
            winStatus = winner();
            if (winStatus == WinStatus.NONE)
                passTurn();
            else
                gameEnded = true;
        }
        return res;
    }

    public WinStatus getWinStatus() {
        return winStatus;
    }

    private void passTurn() {
        activePlayerIndex = (activePlayerIndex + 1 + players.size()) % players.size();
    }

    private void retreatTurn() {
        activePlayerIndex = (activePlayerIndex - 1 + players.size()) % players.size();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void restart() {
        board.reset();
        moves = new Stack<>();
        gameEnded = false;
        winStatus = WinStatus.NONE;
    }

    private WinStatus winner() {
        WinStatus status = board.win(activePlayerIndex);
        if (status == WinStatus.WIN) {
            Player player = getActivePlayer();
            player.getPlayerStatistics().addGameResult();
            players.set(activePlayerIndex, player);
        }
        return status;
    }

    public List<Cell> getWinCells(){
        return board.getWinCells();
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
