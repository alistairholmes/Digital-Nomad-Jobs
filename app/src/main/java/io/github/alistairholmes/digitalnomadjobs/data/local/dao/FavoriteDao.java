package io.github.alistairholmes.digitalnomadjobs.data.local.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT * FROM favorite_jobs")
    LiveData<List<FavoriteJob>> getFavoriteJobs();

    @Query("SELECT * FROM favorite_jobs")
    Flowable<List<FavoriteJob>> getFavoriteJobss();

    @Query("SELECT id FROM favorite_jobs")
    Cursor getFavoriteJobsId();

    @Update
    int update(FavoriteJob favoriteJob);

    @Query("SELECT * FROM favorite_jobs WHERE id=:id")
    LiveData<FavoriteJob> isFavoriteJob(int id);

    @Query("SELECT * from favorite_jobs where id = :id")
    LiveData<FavoriteJob> getFavoriteJob(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveFavoriteJob(FavoriteJob favoriteJob);

    @Delete
    void deleteFavoriteJob(FavoriteJob favoriteJob);

    @Query("DELETE FROM favorite_jobs WHERE id=:id")
    int deleteFavoriteJob(int id);
}
