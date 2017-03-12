package com.example.ezeqzim.cuatro_en_linea.FrontEnd.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import com.example.ezeqzim.cuatro_en_linea.R;
import com.example.ezeqzim.cuatro_en_linea.SQLConnection.Contracts.SQLDatabaseContract;
import com.example.ezeqzim.cuatro_en_linea.SQLConnection.Helpers.PlayerProfileSQLDatabaseHelper;

public class PlayerProfileAdapter extends SimpleCursorAdapter {
    private static String[] columns = new String[] { SQLDatabaseContract.PlayerProfile.COLUMN_NAME_PLAYER_NAME };
    private static int[] to = new int[] { android.R.id.text1 };
    private static int flags = 0;

    public PlayerProfileAdapter(Context context) {
        super(context, R.layout.support_simple_spinner_dropdown_item, listPlayerProfiles(context), columns, to, flags);
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private static Cursor listPlayerProfiles(Context context) {
        return PlayerProfileSQLDatabaseHelper.getInstance(context).list(null, null, null, null, null, null, null);
    }

    public void updateProfileAdapter(Context context) {
        this.changeCursor(listPlayerProfiles(context));
        this.notifyDataSetChanged();
    }
}
