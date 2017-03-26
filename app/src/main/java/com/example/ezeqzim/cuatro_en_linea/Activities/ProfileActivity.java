package com.example.ezeqzim.cuatro_en_linea.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Player.PlayerProfile;
import com.example.ezeqzim.cuatro_en_linea.R;
import com.example.ezeqzim.cuatro_en_linea.SQLConnection.Helpers.PlayerProfileSQLDatabaseHelper;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void createPlayerProfile(View view) {
        String s_playerName = ((EditText) findViewById(R.id.profileName)).getText().toString();
        PlayerProfile pp = new PlayerProfile(s_playerName);
        long id = PlayerProfileSQLDatabaseHelper.getInstance(getApplicationContext()).upsert(pp);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("playerProfileId", id);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
