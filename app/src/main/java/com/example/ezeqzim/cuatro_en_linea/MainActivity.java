package com.example.ezeqzim.cuatro_en_linea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void play(View view){
        String s_rows = ((EditText) findViewById(R.id.rows)).getText().toString();
        String s_cols = ((EditText) findViewById(R.id.cols)).getText().toString();
        String s_player1 = ((EditText) findViewById(R.id.player1)).getText().toString();
        String s_player2 = ((EditText) findViewById(R.id.player2)).getText().toString();

        int rows, cols;
        try {
            rows = Integer.parseInt(s_rows);
        }
        catch (Exception e){
            rows = 7;
        }
        try {
            cols = Integer.parseInt(s_cols);
        }
        catch (Exception e){
            cols = 7;
        }

        Intent play = new Intent(this, GameActivity.class);
        play.putExtra("rows", rows);
        play.putExtra("cols", cols);
        ArrayList<String> players = new ArrayList<>();
        players.add(s_player1);
        players.add(s_player2);
        play.putStringArrayListExtra("players", players);
        Bundle bundle = new Bundle();
        bundle.putSerializable("shapes", new int[][] {
                {R.drawable.button_circle_red_back, R.drawable.button_cross_red_back},
                {R.drawable.button_cross_black_border, R.drawable.button_circle_black_border},
                {R.drawable.button_cross_red_back, R.drawable.button_circle_red_back},
                {R.drawable.button_circle_red_border, R.drawable.button_cross_red_border}
        });
        play.putExtra("shapes", bundle);
        startActivity(play);
    }
}
