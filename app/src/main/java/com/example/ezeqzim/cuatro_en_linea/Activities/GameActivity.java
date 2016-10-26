package com.example.ezeqzim.cuatro_en_linea.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezeqzim.cuatro_en_linea.BackEnd.*;
import com.example.ezeqzim.cuatro_en_linea.FrontEnd.*;
import com.example.ezeqzim.cuatro_en_linea.R;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private int[][] SHAPES;
    private final static int MARGIN = 1;
    private int BTN_MAX, active_player_index, players_count;
    private boolean ended;
    private LinearLayout BOARD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent play = getIntent();
        setContentView(R.layout.activity_game);
        initialize(play);
    }

    private void initialize(Intent play) {
        ArrayList<String> players = play.getStringArrayListExtra("players");
        game = new Game(
                play.getIntExtra("rows", 7),
                play.getIntExtra("cols", 7),
                players
        );
        SHAPES = (int[][]) play.getBundleExtra("shapes").getSerializable("shapes");
        BTN_MAX = getWidthMax();
        setTurnTextView();
        setResultTextView();
        getAndSetBoard();
        active_player_index = 0;
        players_count = players.size();
    }

    private int getWidthMax() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        return size.x / (game.getCols() + 1);
    }

    private void setTurnTextView() {
        TextView tv = (TextView) findViewById(R.id.isPlaying);
        tv.setText(whosePlayingString());
    }

    private void setResultTextView() {
        TextView tv = (TextView) findViewById(R.id.resultsView);
        tv.setText(resultsString());
    }

    private void getAndSetBoard() {
        BOARD = (LinearLayout) findViewById(R.id.board);
        BOARD.setWeightSum(game.getRows());
        initializeBoard();
    }

    private void initializeBoard() {
        BOARD.removeAllViews();
        for (int row = 0; row < game.getRows(); ++row)
            addLinearLayoutWithButtons(row);
        game.restart();
        ended = false;
    }

    private void addLinearLayoutWithButtons(int row) {
        LinearLayout LL = new LinearLayout(this);
        LL.setOrientation(LinearLayout.HORIZONTAL);
        LL.setBackgroundColor(Color.BLACK);
        LL.setWeightSum(1);
        for (int col = 0; col < game.getCols(); ++col) {
            ButtonCoordinates btn = createButton(row, col);
            LL.addView(btn);
        }
        BOARD.addView(LL);
    }

    private ButtonCoordinates createButton(int r, int c) {
        ButtonCoordinates btn = new ButtonCoordinates(this, r, c, BTN_MAX);
        btn.setLayoutParams(MARGIN);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonCoordinates btn = (ButtonCoordinates) v;
                makePlay(btn.getCol());
            }
        });
        btn.setBackground(R.drawable.button_black_border);
        return btn;
    }

    private int getLastPlayerPlayIndex(){
        return (active_player_index - 1 + players_count) % players_count;
    }

    private void passTurn() {
        active_player_index = (active_player_index + 1 + players_count) % players_count;
    }

    private void retreatTurn() {
        active_player_index = getLastPlayerPlayIndex();
    }

    private void makePlay(int col) {
        if (!ended) {
            List<Cell> play = game.makePlay(col);
            if (play.size() == 0)
                Toast.makeText(this, R.string.full_column, Toast.LENGTH_SHORT).show();
            else {
                if (play.size() == 2)
                    ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(play.get(1).getRow())).getChildAt(play.get(1).getCol())).setBackground(SHAPES[getLastPlayerPlayIndex()][1]);
                ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(play.get(0).getRow())).getChildAt(play.get(0).getCol())).setBackground(SHAPES[active_player_index][0]);

                WinStatus status = game.winner(active_player_index);
                if (status == WinStatus.DRAW) {
                    Toast.makeText(this, R.string.draw, Toast.LENGTH_SHORT).show();
                    ended = true;
                }
                else if (status == WinStatus.WIN) {
                    markWin();
                    Toast.makeText(this, whoseWinString(), Toast.LENGTH_SHORT).show();
                    setResultTextView();
                    ended = true;
                }
                passTurn();
                setTurnTextView();
            }
        }
    }

    private void markWin() {
        List<Cell> places = game.getWinCells();
        for (Cell place : places)
            ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(place.getRow())).getChildAt(place.getCol())).setBackground(SHAPES[active_player_index][0]);
    }

    public void restart(View view) {
        if (ended) {
            setTurnTextView();
            setResultTextView();
            initializeBoard();
        }
    }

    public void undoLastMove(View view) {
        if (!ended) {
            List<Cell> undo = game.undoLastMove();
            if (undo.size() == 0)
                Toast.makeText(this, R.string.no_more_mistakes, Toast.LENGTH_SHORT).show();
            else {
                ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(undo.get(0).getRow())).getChildAt(undo.get(0).getCol())).setBackground(R.drawable.button_black_border);
                if (undo.size() == 2)
                    ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(undo.get(1).getRow())).getChildAt(undo.get(1).getCol())).setBackground(SHAPES[active_player_index][0]);
                setTurnTextView();
            }
            retreatTurn();
        }
    }

    private String resultsString() {
        List<PlayerStatistic> playersStatistics = game.getPlayersStatistic();
        String s = "";
        for (int i = 0; i < playersStatistics.size(); ++i){
            PlayerStatistic player_stat = playersStatistics.get(i);
            s += (i == 0 ? "" : " ") + player_stat.getPlayer().getName() + ": " + player_stat.getWins();
        }
        return s;
    }

    private String playerNamePlusString(int player_index, String text){
        return game.getPlayerStatistic(player_index).getPlayer().getName() + " " + text;
    }

    private String whosePlayingString(){
        return playerNamePlusString(active_player_index, "is playing");
    }

    private String whoseWinString(){
        return playerNamePlusString(active_player_index, "wins");
    }
}
