package io.github.alistairholmes.digitalnomadjobs.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface JobDao {

    @Query("SELECT * FROM remote_jobs ORDER BY date DESC LIMIT 300")
    Flowable<List<Job>> getJobs();

    @Query("SELECT * FROM remote_jobs WHERE id = :id")
    Single<Job> getJob(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveJobs(List<Job> jobList);

    @Update
    int updateJob(Job job);

    @Query("UPDATE remote_jobs SET favorite = :favorite WHERE id = :id")
    void update(int id, boolean favorite);

    @Query("DELETE FROM remote_jobs")
    void deleteJobs();

    @Query("DELETE FROM remote_jobs WHERE id =:id")
    int deleteJob(int id);

}
