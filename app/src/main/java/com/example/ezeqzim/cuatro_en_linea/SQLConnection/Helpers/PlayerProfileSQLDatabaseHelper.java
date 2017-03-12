package com.example.ezeqzim.cuatro_en_linea.SQLConnection.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Player.PlayerProfile;
import com.example.ezeqzim.cuatro_en_linea.SQLConnection.Contracts.SQLDatabaseContract;
import com.example.ezeqzim.cuatro_en_linea.SQLConnection.UtilsSQLDatabase;

public class PlayerProfileSQLDatabaseHelper extends SQLiteOpenHelper {
    private static PlayerProfileSQLDatabaseHelper instance;

    public static PlayerProfileSQLDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new PlayerProfileSQLDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private PlayerProfileSQLDatabaseHelper(Context context) {
        super(context, SQLDatabaseContract.DATABASE_NAME, null, SQLDatabaseContract.DATABASE_VERSION);
        instance = this;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLDatabaseContract.PlayerProfile.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQLDatabaseContract.PlayerProfile.DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public long upsert(PlayerProfile playerProfile) {
        String playerName = playerProfile.getName();
        Cursor cursor = getByName(new String[] {playerName});
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(SQLDatabaseContract.PlayerProfile.COLUMN_NAME_PLAYER_NAME, playerProfile.getName());
            return getWritableDatabase().insert(SQLDatabaseContract.PlayerProfile.TABLE_NAME, null, values);
        }
        return cursor.getColumnIndex(SQLDatabaseContract.PlayerProfile._ID);
    }

    public Cursor list(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return getReadableDatabase().query(
                SQLDatabaseContract.PlayerProfile.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy,
                limit
        );
    }

    public Cursor getByName(String[] playerNames) {
        return getReadableDatabase().query(
                SQLDatabaseContract.PlayerProfile.TABLE_NAME,
                null,
                SQLDatabaseContract.PlayerProfile.SELECT_BY_NAMES,
                new String[]{UtilsSQLDatabase.arrayToSQLSetString(playerNames)},
                null,
                null,
                null,
                null
        );
    }
}
