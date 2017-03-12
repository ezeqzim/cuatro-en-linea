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

import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.Cell;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.Game;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.WinStatus;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Player.*;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Statistics.*;
import com.example.ezeqzim.cuatro_en_linea.FrontEnd.*;
import com.example.ezeqzim.cuatro_en_linea.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private Player activePlayer;
    private int[][] SHAPES;
    private LinearLayout BOARD;
    private final static int MARGIN = 1;
    private int BTN_MAX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent play = getIntent();
        initialize(play);
    }

    private void initialize(Intent play) {
        game = new Game(play.getIntExtra("rows", 7), play.getIntExtra("cols", 7), play.getStringArrayListExtra("players"));
        SHAPES = (int[][]) play.getBundleExtra("shapes").getSerializable("shapes");
        BTN_MAX = getWidthMax();
        activePlayer = game.getActivePlayer();
        setTurnTextView();
        setResultTextView();
        getAndSetBoard();
    }

    private int getWidthMax() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        return size.x / (game.getCols() + 1);
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

    private ButtonCoordinates createButton(int row, int col) {
        ButtonCoordinates btn = new ButtonCoordinates(this, row, col, BTN_MAX);
        btn.setLayoutParams(MARGIN);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonCoordinates btn = (ButtonCoordinates) view;
                makePlay(btn.getCol());
            }
        });
        btn.setBackground(R.drawable.button_black_border);
        return btn;
    }

    private void makePlay(int col) {
        if (!game.isEnded()) {
            List<Cell> play = game.makePlay(col);
            WinStatus status = game.getWinStatus();
            if (play.size() == 0)
                Toast.makeText(this, R.string.full_column, Toast.LENGTH_SHORT).show();
            else {
//                // GET SHAPES FROM PLAYER
//                if (play.size() == 2)
//                    ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(play.get(1).getRow())).getChildAt(play.get(1).getCol())).setBackground(SHAPES[game.getLastPlayerPlayIndex()][1]);
//                ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(play.get(0).getRow())).getChildAt(play.get(0).getCol())).setBackground(SHAPES[game.activePlayerIndex][0]);
                if (status == WinStatus.DRAW)
                    Toast.makeText(this, R.string.draw, Toast.LENGTH_SHORT).show();
                else if (status == WinStatus.WIN) {
                    markWin();
                    Toast.makeText(this, whoseWinString(), Toast.LENGTH_SHORT).show();
                    setResultTextView();
                }
                activePlayer = game.getActivePlayer();
                setTurnTextView();
            }
        }
    }

    private void markWin() {
        List<Cell> places = game.getWinCells();
//        GET SHAPES FROM PLAYER
//        for (Cell place : places)
//            ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(place.getRow())).getChildAt(place.getCol())).setBackground(SHAPES[activePlayerIndex][0]);
    }

    public void restart(View view) {
        if (game.isEnded()) {
            setTurnTextView();
            setResultTextView();
            initializeBoard();
            game.restart();
        }
    }

    public void undoLastMove(View view) {
        if (!game.isEnded()) {
            List<Cell> undo = game.undoLastMove();
            activePlayer = game.getActivePlayer();
            if (undo.size() == 0)
                Toast.makeText(this, R.string.no_more_mistakes, Toast.LENGTH_SHORT).show();
            else {
                ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(undo.get(0).getRow())).getChildAt(undo.get(0).getCol())).setBackground(R.drawable.button_black_border);
//                GET SHAPES FROM PLAYER
//                if (undo.size() == 2)
//                    ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(undo.get(1).getRow())).getChildAt(undo.get(1).getCol())).setBackground(SHAPES[activePlayerIndex][0]);
                setTurnTextView();
            }
        }
    }

    private String resultsString() {
        List<Player> players = game.getPlayers();
        String s = "";
        for (int i = 0; i < players.size(); ++i){
            Player player = players.get(i);
            Map<String, Statistic> stats = player.getPlayerStatistics().getStatistics();
            s += (i == 0 ? "" : " ") + player.getName() + ": " + stats.get("wins").getValue();
        }
        return s;
    }

    private void setTurnTextView() {
        TextView tv = (TextView) findViewById(R.id.isPlaying);
        tv.setText(whosePlayingString());
    }

    private void setResultTextView() {
        TextView tv = (TextView) findViewById(R.id.resultsView);
        tv.setText(resultsString());
    }

    private String playerNamePlusString(Player player, String text){
        return player.getName() + " " + text;
    }

    private String whosePlayingString(){
        return playerNamePlusString(activePlayer, "is playing");
    }

    private String whoseWinString(){
        return playerNamePlusString(activePlayer, "wins");
    }
}
