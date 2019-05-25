package io.github.alistairholmes.digitalnomadjobs.data.local.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.data.local.entity.FavoriteJob;
import io.reactivex.Flowable;

/**
 * Data Access Object that contains methods used for accessing the database.
 *
 * @author Osaigbovo Odiase.
 */
@Dao
public interface FavoriteDao {

    // Repository access to database
    @Query("SELECT * FROM favorite_jobs")
    LiveData<List<FavoriteJob>> getFavoriteJobss();

    @Query("SELECT * FROM favorite_jobs ORDER BY date")
    Flowable<List<FavoriteJob>> getFavoriteJobs();

    @Query("SELECT * FROM favorite_jobs WHERE id = :id")
    LiveData<FavoriteJob> isFavoriteJob(int id);

    @Query("SELECT * FROM favorite_jobs WHERE id = :id")
    LiveData<FavoriteJob> getFavoriteJob(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveFavoriteJob(FavoriteJob favoriteJob);

    @Query("DELETE FROM favorite_jobs WHERE id = :id")
    void deleteFavoriteJoba(int id);

    @Delete
    void deleteFavoriteJob(FavoriteJob favoriteJob);


    // ContentProvider access to database
    @RawQuery
    Cursor getFavoriteJobsId(SupportSQLiteQuery query);

    @RawQuery
    Cursor getFavoriteJobId(SupportSQLiteQuery query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFavoriteJob(FavoriteJob favoriteJob);

    @Query("DELETE FROM favorite_jobs WHERE id = :id")
    int deleteFavoriteJob(int id);

    @Update
    int updateFavoriteJob(FavoriteJob favoriteJob);


}
