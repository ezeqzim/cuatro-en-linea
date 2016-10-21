package com.example.ezeqzim.cuatro_en_linea;

import android.util.Pair;

/**
 * Created by Ezequiel on 8/10/2016.
 */
public class Board {
    private final int rows, cols;
    private int[][] board;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        initialize();
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public Pair<Boolean, int[][]> win(int player_id) {
        for (int row = rows - 1; row >= 0; row--)
            for (int col = 0; col < cols - 3; col++)
                if ((board[row][col] == player_id) && (board[row][col + 1] == player_id) && (board[row][col + 2] == player_id) && (board[row][col + 3] == player_id))
                    return Pair.create(true, new int[][]{{row, col}, {row, col + 1}, {row, col + 2}, {row, col + 3}});
        for (int col = 0; col < cols; col++)
            for (int row = rows - 1; row >= 3; row--)
                if ((board[row][col] == player_id) && (board[row - 1][col] == player_id) && (board[row - 2][col] == player_id) && (board[row - 3][col] == player_id))
                    return Pair.create(true, new int[][]{{row, col}, {row - 1, col}, {row - 2, col}, {row - 3, col}});
        for (int row = rows - 1; row >= 3; row--)
            for (int col = 0; col < cols - 3; col++)
                if ((board[row][col] == player_id) && (board[row - 1][col + 1] == player_id) && (board[row - 2][col + 2] == player_id) && (board[row - 3][col + 3] ==
                        player_id))
                    return Pair.create(true, new int[][]{{row, col}, {row - 1, col + 1}, {row - 2, col + 2}, {row - 3, col + 3}});
        for (int row = rows - 4; row >= 0; row--)
            for (int col = 0; col < cols - 3; col++)
                if ((board[row][col] == player_id) && (board[row + 1][col + 1] == player_id) && (board[row + 2][col + 2] == player_id) && (board[row + 3][col + 3] ==
                        player_id))
                    return Pair.create(true, new int[][]{{row, col}, {row + 1, col + 1}, {row + 2, col + 2}, {row + 3, col + 3}});
        for (int col = 0; col < cols; col++)
            if (board[0][col] == 0)
                return Pair.create(false, new int[][]{});
        return Pair.create(true, new int[][]{});
    }

    public void clearPosition(int row, int col) {
        board[row][col] = 0;
    }

    public int[] setPosition(int col, int player_id) {
        int row = rows - 1;
        while (row >= 0 && board[row][col] != 0) {
            row--;
        }
        if (row < 0) {
            return new int[]{};
        }
        board[row][col] = player_id;
        return new int[]{row, col};
    }

    public void reset() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = 0;
            }
        }
    }

    private void initialize() {
        board = new int[rows][cols];
        reset();
    }
}
