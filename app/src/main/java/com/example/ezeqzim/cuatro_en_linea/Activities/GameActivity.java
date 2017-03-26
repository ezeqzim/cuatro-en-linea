package com.example.ezeqzim.cuatro_en_linea.Activities;

import android.app.Activity;
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
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.Modes.BestOf;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.Modes.GameMode;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.Modes.JustPlay;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.Modes.Tournament;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.MoveType;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.WinStatus;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Player.Player;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Player.PlayerProfile;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Statistics.Statistic;
import com.example.ezeqzim.cuatro_en_linea.FrontEnd.Buttons.ButtonCoordinates;
import com.example.ezeqzim.cuatro_en_linea.R;

import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private GameMode gameMode;
    private LinearLayout BOARD;
    private int BTN_MAX;
    private final static int MARGIN = 1;
    private final int BTN_CIRCLE_RED_BORDER = R.drawable.button_circle_red_border;
    private final int BTN_CIRCLE_BLACK_BORDER = R.drawable.button_circle_black_border;
    private final int BTN_BLACK_BORDER = R.drawable.button_black_border;
    private final int LAYER_CIRCLE_RED_BORDER = R.id.circle_red_border;
    private final int LAYER_CIRCLE_BLACK_BORDER = R.id.circle_black_border;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent play = getIntent();
        initialize(play);
    }

    private void initialize(Intent play) {
        game = createGame(play);
        gameMode = createGameMode(play);
        BTN_MAX = getWidthMax();
        setTurnTextView();
        setResultTextView();
        getAndSetBoard();
    }

    private GameMode createGameMode(Intent gameMode) {
        switch (gameMode.getStringExtra("gameMode")) {
            case "bestOf":
                return new BestOf(
                        gameMode.getIntExtra("bestOf", 7),
                        game.getPlayers()
                );
            case "tournament":
                return new Tournament(
                        game.getPlayers()
                );
            default:
                return new JustPlay();
        }
    }

    @SuppressWarnings("unchecked")
    private Game createGame(Intent game) {
        return new Game(
                game.getIntExtra("rows", 7),
                game.getIntExtra("cols", 7),
                (List<PlayerProfile>) game.getBundleExtra("bundle").getSerializable("playerProfiles"),
                (int[]) game.getBundleExtra("bundle").getSerializable("colors")
        );
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
        btn.setBackground(BTN_BLACK_BORDER);
        return btn;
    }

    private void makePlay(int col) {
        if (!game.isEnded()) {
            Player activePlayer = game.getActivePlayer();
            Player lastTurnPlayer = game.getLastTurnPlayer();
            Map<MoveType, Cell> play = game.makePlay(col);
            WinStatus status = game.getWinStatus();
            if (play.size() == 0)
                Toast.makeText(this, R.string.full_column, Toast.LENGTH_SHORT).show();
            else {
                if (play.containsKey(MoveType.LAST_MOVE)) {
                    setShapeStrokeColor(BTN_CIRCLE_BLACK_BORDER, LAYER_CIRCLE_BLACK_BORDER, lastTurnPlayer.getColor());
                    getButtonCoordinates(play.get(MoveType.LAST_MOVE).getRow(), play.get(MoveType.LAST_MOVE).getCol()).setBackground(BTN_CIRCLE_BLACK_BORDER);
                }
                setShapeStrokeColor(BTN_CIRCLE_RED_BORDER, LAYER_CIRCLE_RED_BORDER, activePlayer.getColor());
                getButtonCoordinates(play.get(MoveType.NEW_MOVE).getRow(), play.get(MoveType.NEW_MOVE).getCol()).setBackground(BTN_CIRCLE_RED_BORDER);
                if (status == WinStatus.DRAW)
                    Toast.makeText(this, R.string.draw, Toast.LENGTH_SHORT).show();
                else if (status == WinStatus.WIN) {
                    markWin();
                    Toast.makeText(this, whoseWinString(), Toast.LENGTH_SHORT).show();
                    setResultTextView();
                    WinStatus winStatusGameMode = gameMode.informWinEvent(activePlayer);
                    if (winStatusGameMode == WinStatus.WIN) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("winPlayer", activePlayer.getPlayerProfile());
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
                setTurnTextView();
            }
        }
    }

    private void markWin() {
        List<Cell> places = game.getWinCells();
        for (Cell place : places) {
            setShapeStrokeColor(BTN_CIRCLE_RED_BORDER, LAYER_CIRCLE_RED_BORDER, game.getActivePlayer().getColor());
            getButtonCoordinates(place.getRow(), place.getCol()).setBackground(BTN_CIRCLE_RED_BORDER);
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
            Map<MoveType, Cell> undo = game.undoLastMove();
            Player lastTurnPlayer = game.getLastTurnPlayer();
            if (undo.size() == 0)
                Toast.makeText(this, R.string.no_more_mistakes, Toast.LENGTH_SHORT).show();
            else {
                getButtonCoordinates(undo.get(MoveType.CLEAR_MOVE).getRow(), undo.get(MoveType.CLEAR_MOVE).getCol()).setBackground(BTN_BLACK_BORDER);
                if (undo.size() == 2) {
                    setShapeStrokeColor(BTN_CIRCLE_RED_BORDER, LAYER_CIRCLE_RED_BORDER, lastTurnPlayer.getColor());
                    getButtonCoordinates(undo.get(MoveType.LAST_MOVE).getRow(), undo.get(MoveType.LAST_MOVE).getCol()).setBackground(BTN_CIRCLE_RED_BORDER);
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

    private String playerNamePlusString(Player player, String text){
        return player.getPlayerProfile().getName() + " " + text;
    }

    private String whosePlayingString(){
        Player activePlayer = game.getActivePlayer();
        return playerNamePlusString(activePlayer, "is playing");
    }

    private String whoseWinString(){
        Player activePlayer = game.getActivePlayer();
        return playerNamePlusString(activePlayer, "wins");
    }
}
