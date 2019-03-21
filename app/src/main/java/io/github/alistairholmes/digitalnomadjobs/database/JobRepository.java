package io.github.alistairholmes.digitalnomadjobs.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class JobRepository {

    private JobDao mJobDao;
    private LiveData<List<Job>> mJobEntries;

    public JobRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mJobDao = db.jobDao();
        mJobEntries = mJobDao.getJobs();
    }

    public LiveData<List<Job>> getJobs() {
        return mJobEntries;
    }

    public void insert(Job jobEntry) {
        new insertAsyncTask(mJobDao).execute(jobEntry);
    }

    private static class insertAsyncTask extends AsyncTask<Job, Void, Void> {

        private JobDao mAsyncTaskDao;

        insertAsyncTask(JobDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Job... jobEntries) {
            mAsyncTaskDao.insertJob(jobEntries);
            return null;
        }
    }

}
