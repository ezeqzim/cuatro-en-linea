package com.example.ezeqzim.cuatro_en_linea;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {
    Game game;
    private final static int MARGIN = 1;
    private int BTN_MAX;
    private LinearLayout BOARD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent play = getIntent();
        setContentView(R.layout.activity_game);
        init(play);
    }

    private void init(Intent play) {
        game = new Game(
                play.getIntExtra("rows", 7),
                play.getIntExtra("cols", 7),
                play.getStringArrayListExtra("players"),
                (int[][]) play.getBundleExtra("shapes").getSerializable("shapes")
        );
        BTN_MAX = getWidthMax();
        Button btn_rst = (Button) findViewById(R.id.restart);
        btn_rst.setText(R.string.concede);
        setTurnTextView();
        setResultTextView();
        getAndSetBoard();
    }

    public void restart(View view) {
        Button btn = (Button) findViewById(R.id.restart);
        if (btn.getText() == getString(R.string.concede))
            game.concede();
        setTurnTextView();
        setResultTextView();
        btn.setText(R.string.concede);
        initBoard();
    }

    public void undoLastMove(View view) {
        Pair<Pair<Boolean, Player>, int[][]> undo = game.undoLastMove();
        if (undo.first.first)
            if (undo.second.length == 0)
                Toast.makeText(this, R.string.no_more_mistakes, Toast.LENGTH_SHORT).show();
            else {
                ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(undo.second[0][0])).getChildAt(undo.second[0][1])).setBackground(R.drawable.button_black_border);
                ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(undo.second[1][0])).getChildAt(undo.second[1][1])).setBackground(undo.first.second.getLastMove());
                setTurnTextView();
            }
    }

    private void getAndSetBoard() {
        BOARD = (LinearLayout) findViewById(R.id.board);
        BOARD.setWeightSum(game.getRows());
        initBoard();
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

    private void initBoard() {
        BOARD.removeAllViews();
        for (int row = 0; row < game.getRows(); ++row)
            addLinearLayoutWithButtons(row);
        game.restart();
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

    private void makePlay(int col) {
        Pair<Pair<Boolean, Player>, int[][]> play = game.makePlay(col);
        if (play.first.first && play.second.length == 0)
            Toast.makeText(this, R.string.full_column, Toast.LENGTH_SHORT).show();
        else if (play.first.first) {
            if (play.second.length == 2)
                ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(play.second[1][0])).getChildAt(play.second[1][1])).setBackground(play.first.second.getNormalMove());
            ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(play.second[0][0])).getChildAt(play.second[0][1])).setBackground(play.first.second.getMove());

            Pair<Pair<Boolean, Player>, int[][]> win = game.winner();
            if (!win.first.first) {
                game.passTurn();
                setTurnTextView();
            } else {
                if (win.second.length == 0)
                    Toast.makeText(this, R.string.draw, Toast.LENGTH_SHORT).show();
                else {
                    markWin(win.second, win.first.second);
                    Toast.makeText(this, game.whoWinsString(), Toast.LENGTH_SHORT).show();
                    game.passTurn();
                    setTurnTextView();
                    setResultTextView();
                }
                Button btn = (Button) findViewById(R.id.restart);
                btn.setText(R.string.play_again);
            }
        }
    }

    private void markWin(int[][] places, Player player) {
        for (int[] place : places) {
            int row = place[0], col = place[1];
            ((ButtonCoordinates) ((LinearLayout) BOARD.getChildAt(row)).getChildAt(col)).setBackground(player.getWinMove());
        }
    }

    private void setTurnTextView() {
        TextView tv = (TextView) findViewById(R.id.isPlaying);
        tv.setText(game.whoseTurnString());
    }

    private void setResultTextView() {
        TextView tv = (TextView) findViewById(R.id.resultsView);
        tv.setText(game.resultsString());
    }

    private int getWidthMax() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        return size.x / (game.getCols() + 1);
    }
}
