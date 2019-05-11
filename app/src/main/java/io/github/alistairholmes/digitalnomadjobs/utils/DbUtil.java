package io.github.alistairholmes.digitalnomadjobs.utils;

import android.database.Cursor;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.functions.Function;

public final class DbUtil {

    public static final int BOOLEAN_FALSE = 0;
    public static final int BOOLEAN_TRUE = 1;


    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static boolean getBoolean(Cursor cursor, String columnName) {
        return getInt(cursor, columnName) == BOOLEAN_TRUE;
    }

    public static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }

    public static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public static double getDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndexOrThrow(columnName));
    }

    public static String[] ID_PROJECTION = {
            "id"
    };

    public static Function<Cursor, Set<Integer>> ID_PROJECTION_MAP = new Function<Cursor, Set<Integer>>() {
        @Override
        public Set<Integer> apply(Cursor cursor) throws Exception {
            try {
                Set<Integer> idSet = new HashSet<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    idSet.add(getInt(cursor, "id"));
                }
                return idSet;
            } finally {
                cursor.close();
            }
        }
    };


}
