package io.github.alistairholmes.digitalnomadjobs.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.alistairholmes.digitalnomadjobs.data.local.entity.FavoriteJob;
import io.reactivex.functions.Function;

import static io.github.alistairholmes.digitalnomadjobs.widget.FavoriteJobsAppWidget.FAVORITE_DATA_UPDATED;

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

    public static String[] FAVORITE_WIDGET_PROJECTION = {
            "id",
            "position",
            "company",
            "company_logo",
            "date",
            "logo",
            "description"
    };

    public static Function<Optional<Cursor>, Set<Integer>> ID_PROJECTION_MAP = cursorOptional -> {
        try (Cursor cursor = cursorOptional.get()) {
            Set<Integer> idSet = new HashSet<>(cursor.getCount());
            while (cursor.moveToNext()) {
                idSet.add(getInt(cursor, "id"));
            }
            return idSet;
        }
    };

    public static Function<Optional<Cursor>, List<FavoriteJob>> WIDGET_PROJECTION_MAP = cursorOptional -> {
        try (Cursor cursor = cursorOptional.get()) {
            List<FavoriteJob> favoriteJobs = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                int id = getInt(cursor, "id");
                String position = getString(cursor, "position");
                String company = getString(cursor, "company");
                String company_logo = getString(cursor, "company_logo");
                long date = getLong(cursor, "date");
                String logo = getString(cursor, "logo");
                String description = getString(cursor, "description");

                favoriteJobs
                        .add(new FavoriteJob(id, position, company,
                                company_logo, new Date(date), logo, description, true));
            }
            return favoriteJobs;
        }
    };


    // TODO: When the favorite button is clicked send broadcast.
    // but for now put it
    private void updateWidgets(Context context) {
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(FAVORITE_DATA_UPDATED).setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }


}
