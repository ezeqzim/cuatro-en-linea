package com.example.ezeqzim.cuatro_en_linea.SQLConnection;

import android.text.TextUtils;

public class UtilsSQLDatabase {
    public static String arrayToSQLSetString(String[] array) {
        return TextUtils.join(",", array);
    }
}
