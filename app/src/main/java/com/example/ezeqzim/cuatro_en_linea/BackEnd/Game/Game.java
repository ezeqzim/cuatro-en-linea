package com.example.ezeqzim.cuatro_en_linea.BackEnd.Game;

import com.example.ezeqzim.cuatro_en_linea.BackEnd.Player.*;

import java.util.*;

public class Game {
    private ArrayList<Player> players;
    private Board board;
    private int activePlayerIndex;
    private Stack<Cell> moves = new Stack<>();
    private WinStatus winStatus;

    public Game(int rows, int cols, List<PlayerProfile> playerProfiles, int[] colors) {
        this.board = new Board(rows, cols);
        this.players = new ArrayList<>();
        for (int i = 0; i < playerProfiles.size(); ++i) {
            PlayerStatistics playerStatistics = new PlayerStatistics();
            playerStatistics = new PlayerStatisticsWinDecorator(playerStatistics);
            Player player = new Player(playerProfiles.get(i), playerStatistics, colors[i]);
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

    public Player getLastTurnPlayer() {
        return players.get(getLastTurnPlayerIndex());
    }

    public boolean isEnded() {
        return winStatus != WinStatus.NONE;
    }

    public Map<MoveType, Cell> makePlay(int col) {
        Map<MoveType, Cell> res = new HashMap<>();
        if (board.setPosition(col, activePlayerIndex)) {
            Cell actual_move = board.getLastSettledCell();
            res.put(MoveType.NEW_MOVE, actual_move);
            if (!moves.empty())
                res.put(MoveType.LAST_MOVE, moves.peek());
            moves.push(actual_move);
            winStatus = winner();
            if (winStatus == WinStatus.NONE)
                passTurn();
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
        activePlayerIndex = getLastTurnPlayerIndex();
    }

    private int getLastTurnPlayerIndex() {
        return (activePlayerIndex - 1 + players.size()) % players.size();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void restart() {
        board.reset();
        moves = new Stack<>();
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

    public Map<MoveType, Cell> undoLastMove() {
        Map<MoveType, Cell> res = new HashMap<>();
        if (moves.isEmpty())
            return res;
        Cell last_move = moves.pop();
        board.clearPosition(last_move.getRow(), last_move.getCol());
        retreatTurn();
        res.put(MoveType.CLEAR_MOVE, last_move);
        if (!moves.isEmpty())
            res.put(MoveType.LAST_MOVE, moves.peek());
        return res;
    }
}
