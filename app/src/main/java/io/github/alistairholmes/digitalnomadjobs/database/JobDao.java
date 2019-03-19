package io.github.alistairholmes.digitalnomadjobs.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JobDao {

    @Query("SELECT * FROM `Remote Jobs` ORDER BY date DESC ")
    LiveData<List<Job>> getJobs();

    @Insert
    void insertJob(Job[] jobs);

    @Delete
    void deleteJob(Job[] jobs);

    @Query("SELECT * FROM `Remote Jobs` WHERE id = :id")
    LiveData<Job> loadJobById(int id);

}
