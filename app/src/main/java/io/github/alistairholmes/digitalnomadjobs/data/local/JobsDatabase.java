package io.github.alistairholmes.digitalnomadjobs.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import io.github.alistairholmes.digitalnomadjobs.data.local.converters.DateConverter;
import io.github.alistairholmes.digitalnomadjobs.data.local.dao.FavoriteDao;
import io.github.alistairholmes.digitalnomadjobs.data.local.dao.JobDao;
import io.github.alistairholmes.digitalnomadjobs.data.local.entity.FavoriteJob;
import io.github.alistairholmes.digitalnomadjobs.data.model.Job;

@Database(entities = {Job.class, FavoriteJob.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class JobsDatabase extends RoomDatabase {

    public abstract JobDao jobDao();

    public abstract FavoriteDao favoriteDao();

}
