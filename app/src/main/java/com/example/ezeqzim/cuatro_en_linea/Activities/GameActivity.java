package com.example.ezeqzim.cuatro_en_linea.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
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
import com.example.ezeqzim.cuatro_en_linea.FrontEnd.Buttons.ButtonCoordinates;
import com.example.ezeqzim.cuatro_en_linea.R;

import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private Player activePlayer;
    private LinearLayout BOARD;
    private final static int MARGIN = 1;
    private int BTN_MAX;
    private final int btn_circle_red_border = R.drawable.button_circle_red_border;
    private final int btn_circle_black_border = R.drawable.button_circle_black_border;
    private final int btn_black_border = R.drawable.button_black_border;
    private final int layer_circle_red_border = R.id.circle_red_border;
    private final int layer_circle_black_border = R.id.circle_black_border;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent play = getIntent();
        initialize(play);
    }

    @SuppressWarnings("unchecked")
    private void initialize(Intent play) {
        game = new Game(
                play.getIntExtra("rows", 7),
                play.getIntExtra("cols", 7),
                (List<PlayerProfile>) play.getBundleExtra("bundle").getSerializable("playerProfiles"),
                (int[]) play.getBundleExtra("bundle").getSerializable("colors")
        );
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
        btn.setBackground(btn_black_border);
        return btn;
    }

    private void makePlay(int col) {
        if (!game.isEnded()) {
            List<Cell> play = game.makePlay(col);
            WinStatus status = game.getWinStatus();
            if (play.size() == 0)
                Toast.makeText(this, R.string.full_column, Toast.LENGTH_SHORT).show();
            else {
                if (play.size() == 2) {
                    setShapeStrokeColor(btn_circle_black_border, layer_circle_black_border, game.getLastTurnPlayer().getColor());
                    getButtonCoordinates(play.get(1).getRow(), play.get(1).getCol()).setBackground(btn_circle_black_border);
                }
                setShapeStrokeColor(btn_circle_red_border, layer_circle_red_border, game.getActivePlayer().getColor());
                getButtonCoordinates(play.get(0).getRow(), play.get(0).getCol()).setBackground(btn_circle_red_border);
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
        for (Cell place : places) {
            //TODO: Check player to highlight
            setShapeStrokeColor(btn_circle_red_border, layer_circle_red_border, game.getLastTurnPlayer().getColor());
            getButtonCoordinates(place.getRow(), place.getCol()).setBackground(btn_circle_red_border);
        }
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
                getButtonCoordinates(undo.get(0).getRow(), undo.get(0).getCol()).setBackground(btn_black_border);
                if (undo.size() == 2) {
                    setShapeStrokeColor(btn_circle_red_border, layer_circle_red_border, game.getActivePlayer().getColor());
                    getButtonCoordinates(undo.get(1).getRow(), undo.get(1).getCol()).setBackground(btn_circle_red_border);
                }
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
            s += (i == 0 ? "" : " ") + player.getPlayerProfile().getName() + ": " + stats.get("wins").getValue();
        }
        return s;
    }

    private ButtonCoordinates getButtonCoordinates(int row, int col) {
        return (ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(row)).getChildAt(col);
    }

    private void setShapeStrokeColor(int drawable, int layerId, int color) {
        ((GradientDrawable) ((LayerDrawable) getResources().getDrawable(drawable)).findDrawableByLayerId(layerId)).setStroke(6, color);
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
        return player.getPlayerProfile().getName() + " " + text;
    }

    private String whosePlayingString(){
        return playerNamePlusString(activePlayer, "is playing");
    }

    private String whoseWinString(){
        return playerNamePlusString(activePlayer, "wins");
    }
}
