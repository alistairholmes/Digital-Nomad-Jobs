package io.github.alistairholmes.digitalnomadjobs.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.Arrays;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.github.alistairholmes.digitalnomadjobs.data.local.JobsDatabase;
import io.github.alistairholmes.digitalnomadjobs.data.local.dao.JobDao;
import timber.log.Timber;

public class JobsProvider extends ContentProvider {

    private static final String TAG = JobsProvider.class.getSimpleName();

    public static final String AUTHORITY = "io.github.alistairholmes.digitalnomadjobs.provider";
    public static final Uri URI_JOB = Uri.parse("content://" + AUTHORITY + "/" + "remote_jobs");

    private static final int REMOTE_JOB = 1; // TODO Rename CODE_CHEESE_DIR
    private static final int REMOTE_JOB_ID = 2;  // TODO Rename CODE_CHEESE_ITEM

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    @Inject
    JobsDatabase jobsDatabase;
    @Inject
    JobDao jobDao;

    static {
        MATCHER.addURI(AUTHORITY, "remote_jobs", REMOTE_JOB);
        MATCHER.addURI(AUTHORITY, "remote_jobs" + "/*", REMOTE_JOB_ID);
    }

    @Override
    public boolean onCreate() {
        //AndroidInjection.inject(this);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final int match = MATCHER.match(uri);

        Timber.tag(TAG).v("uri=" + uri + " match=" + match + " proj=" + Arrays.toString(projection) +
                " selection=" + selection + " args=" + Arrays.toString(selectionArgs) + ")");

        if (match == REMOTE_JOB || match == REMOTE_JOB_ID) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            final Cursor cursor;
            if (match == REMOTE_JOB) {
                cursor = jobsDatabase.query(new SimpleSQLiteQuery("SELECT * FROM remote_jobs ORDER BY date DESC LIMIT 300"));
            } else {
                cursor = jobsDatabase.query(new SimpleSQLiteQuery("SELECT * FROM remote_jobs WHERE id = ?",
                        new Integer[]{(int) ContentUris.parseId(uri)}));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case REMOTE_JOB:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + "remote_jobs";
            case REMOTE_JOB_ID:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + "remote_jobs";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        Timber.tag(TAG).v("insert(uri=" + uri + ", values=" + contentValues.toString() + ")");

//        switch (MATCHER.match(uri)) {
//            case REMOTE_JOB:
//                final Context context = getContext();
//                if (context == null) {
//                    return null;
//                }
//                jobsDatabase.jobDao()
//                final int id = SampleDatabase.getInstance(context).cheese().insert(Cheese.fromContentValues(contentValues));
//
//                context.getContentResolver().notifyChange(uri, null);
//                return ContentUris.withAppendedId(uri, id);
//            case REMOTE_JOB_ID:
//                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
        return null;


//        switch (match) {
//            case GENRES: {
//                db.insertOrThrow(Tables.GENRES, null, values);
//                notifyChange(uri);
//                return Genres.buildGenreUri(values.getAsString(Genres.GENRE_ID));
//            }
//            case MOVIES: {
//                db.insertOrThrow(Tables.MOVIES, null, values);
//                notifyChange(uri);
//                return Movies.buildMovieUri(values.getAsString(Movies.MOVIE_ID));
//            }
//            case MOVIES_ID_GENRES: {
//                db.insertOrThrow(Tables.MOVIES_GENRES, null, values);
//                notifyChange(uri);
//                return Genres.buildGenreUri(values.getAsString(Genres.GENRE_ID));
//            }
//            default: {
//                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
//            }
//        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Timber.tag(TAG).v("delete(uri=" + uri + ")");
        switch (MATCHER.match(uri)) {
            case REMOTE_JOB:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case REMOTE_JOB_ID:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = jobDao.deleteJob((int) ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s,
                      @Nullable String[] strings) {
        Timber.tag(TAG).v("update(uri=" + uri + ", values=" + contentValues.toString() + ")");
        switch (MATCHER.match(uri)) {
            case REMOTE_JOB:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case REMOTE_JOB_ID:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }

                /*jobsDatabase.favoriteDao()
                final Cheese cheese = Cheese.fromContentValues(contentValues);
                cheese.id = ContentUris.parseId(uri);
                final int count = SampleDatabase.getInstance(context).cheese()
                        .update(cheese);
                context.getContentResolver().notifyChange(uri, null);*/
                return 0 /*count*/;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}
