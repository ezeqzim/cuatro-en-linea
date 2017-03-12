package com.example.ezeqzim.cuatro_en_linea.SQLConnection.Contracts;

import android.provider.BaseColumns;

public final class SQLDatabaseContract {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "four_in_a_row.database";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";

    private SQLDatabaseContract() {}

    public static class PlayerProfile implements BaseColumns {
        public static final String TABLE_NAME = "playerProfile";
        public static final String COLUMN_NAME_PLAYER_NAME = "playerName";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                COLUMN_NAME_PLAYER_NAME + TEXT_TYPE +
                " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SELECT_BY_NAMES = COLUMN_NAME_PLAYER_NAME + " in (?)";
    }
}
