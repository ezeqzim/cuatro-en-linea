package com.example.ezeqzim.cuatro_en_linea.BackEnd;

import java.util.ArrayList;
import java.util.List;

class Board {
    private final static int BLANK = -1;
    private final int rows, cols;
    private Cell[][] board;
    private List<Cell> win_cells;
    private Cell last_settled_cell;

    Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        initialize();
    }

    private void initialize() {
        board = new Cell[rows][cols];
        reset();
    }

    void reset() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                clearPosition(i, j);
        win_cells = new ArrayList<>();
        last_settled_cell = null; // new Cell();
    }

    void clearPosition(int row, int col) {
        board[row][col] = new Cell(row, col, BLANK);
    }

    int getCols() {
        return cols;
    }

    int getRows() {
        return rows;
    }

    WinStatus win(int player_id) {
        for (int row = rows - 1; row >= 0; row--)
            for (int col = 0; col < cols - 3; col++)
                if ((board[row][col].getContent().equals(player_id)) && (board[row][col + 1].getContent().equals(player_id)) && (board[row][col + 2].getContent().equals(player_id)) && (board[row][col + 3].getContent().equals(player_id))) {
                    win_cells.add(board[row][col]);
                    win_cells.add(board[row][col + 1]);
                    win_cells.add(board[row][col + 2]);
                    win_cells.add(board[row][col + 3]);
                    return WinStatus.WIN;
                }
        for (int col = 0; col < cols; col++)
            for (int row = rows - 1; row >= 3; row--)
                if ((board[row][col].getContent().equals(player_id)) && (board[row - 1][col].getContent().equals(player_id)) && (board[row - 2][col].getContent().equals(player_id)) && (board[row - 3][col].getContent().equals(player_id))) {
                    win_cells.add(board[row][col]);
                    win_cells.add(board[row - 1][col]);
                    win_cells.add(board[row - 2][col]);
                    win_cells.add(board[row - 3][col]);
                    return WinStatus.WIN;
                }
        for (int row = rows - 1; row >= 3; row--)
            for (int col = 0; col < cols - 3; col++)
                if ((board[row][col].getContent().equals(player_id)) && (board[row - 1][col + 1].getContent().equals(player_id)) && (board[row - 2][col + 2].getContent().equals(player_id)) && (board[row - 3][col + 3].getContent().equals(player_id))) {
                    win_cells.add(board[row][col]);
                    win_cells.add(board[row - 1][col + 1]);
                    win_cells.add(board[row - 2][col + 2]);
                    win_cells.add(board[row - 3][col + 3]);
                    return WinStatus.WIN;
                }
        for (int row = rows - 4; row >= 0; row--)
            for (int col = 0; col < cols - 3; col++)
                if ((board[row][col].getContent().equals(player_id)) && (board[row + 1][col + 1].getContent().equals(player_id)) && (board[row + 2][col + 2].getContent().equals(player_id)) && (board[row + 3][col + 3].getContent().equals(player_id))) {
                    win_cells.add(board[row][col]);
                    win_cells.add(board[row + 1][col + 1]);
                    win_cells.add(board[row + 2][col + 2]);
                    win_cells.add(board[row + 3][col + 3]);
                    return WinStatus.WIN;
                }
        for (int col = 0; col < cols; col++)
            if (board[0][col].getContent().equals(BLANK))
                return WinStatus.NONE;
        return WinStatus.DRAW;
    }

    List<Cell> getWinCells(){
        return win_cells;
    }

    Cell getLastSettledCell(){
        return last_settled_cell;
    }

    Boolean setPosition(int col, int player_id) {
        int row = rows - 1;
        while (row >= 0 && !board[row][col].getContent().equals(BLANK))
            row--;
        if (row < 0)
            return false;
        board[row][col].setContent(player_id);
        last_settled_cell = new Cell(board[row][col]);
        return true;
    }
}
