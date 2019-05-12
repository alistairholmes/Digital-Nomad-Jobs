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
import io.github.alistairholmes.digitalnomadjobs.data.local.dao.FavoriteDao;
import io.github.alistairholmes.digitalnomadjobs.data.local.entity.FavoriteJob;
import timber.log.Timber;

public class JobsProvider extends ContentProvider {

    public static final String AUTHORITY = "io.github.alistairholmes.digitalnomadjobs.data.provider";
    public static final Uri URI_JOB = Uri.parse("content://" + AUTHORITY + "/" + "favorite_jobs");
    private static final String TAG = JobsProvider.class.getSimpleName();
    private static final int FAVORITE_JOB_DIR = 1;
    private static final int FAVORITE_JOB_ID = 2;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, "favorite_jobs", FAVORITE_JOB_DIR);
        MATCHER.addURI(AUTHORITY, "favorite_jobs" + "/#", FAVORITE_JOB_ID); // *
    }

    @Inject
    JobsDatabase jobsDatabase;
    @Inject
    FavoriteDao favoriteDao;

    @Override
    public boolean onCreate() {
        AndroidInjection.inject(this);
        //favoriteDao = jobsDatabase.favoriteDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final Cursor cursor;
        final int match = MATCHER.match(uri);

        Timber.tag(TAG).v("uri=" + uri + " match=" + match + " proj=" + Arrays.toString(projection) +
                " selection=" + selection + " args=" + Arrays.toString(selectionArgs) + ")");

        if (match == FAVORITE_JOB_DIR || match == FAVORITE_JOB_ID) {

            if (match == FAVORITE_JOB_DIR) {
                cursor = favoriteDao
                        .getFavoriteJobsId(
                                new SimpleSQLiteQuery("SELECT * FROM favorite_jobs"));

            } else {
                cursor = favoriteDao
                        .getFavoriteJobId(
                                new SimpleSQLiteQuery("SELECT * FROM favorite_jobs WHERE id = ?",
                                        new Object[]{ContentUris.parseId(uri)}));
            }

            cursor.setNotificationUri(this.getContext().getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case FAVORITE_JOB_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + "favorite_jobs";
            case FAVORITE_JOB_ID:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + "favorite_jobs";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Timber.tag(TAG).v("insert(uri=" + uri + ", values=" + contentValues.toString() + ")");

        switch (MATCHER.match(uri)) {
            case FAVORITE_JOB_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = favoriteDao
                        .insertFavoriteJob(FavoriteJob.fromContentValues(contentValues));

                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case FAVORITE_JOB_ID:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Timber.tag(TAG).v("delete(uri=" + uri + ")");
        switch (MATCHER.match(uri)) {
            case FAVORITE_JOB_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case FAVORITE_JOB_ID:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = favoriteDao.deleteFavoriteJob((int) ContentUris.parseId(uri));
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
            case FAVORITE_JOB_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case FAVORITE_JOB_ID:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final FavoriteJob favoriteJob = FavoriteJob.fromContentValues(contentValues);
                favoriteJob.id = (int) ContentUris.parseId(uri);
                final int count = favoriteDao.updateFavoriteJob(favoriteJob);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}
