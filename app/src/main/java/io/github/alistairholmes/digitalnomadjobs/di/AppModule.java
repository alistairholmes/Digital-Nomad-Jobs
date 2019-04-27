package io.github.alistairholmes.digitalnomadjobs.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.alistairholmes.digitalnomadjobs.data.local.JobsDatabase;
import io.github.alistairholmes.digitalnomadjobs.data.local.dao.JobDao;
import io.github.alistairholmes.digitalnomadjobs.data.remote.RequestInterface;
import io.github.alistairholmes.digitalnomadjobs.data.remote.ServiceGenerator;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Osaigbovo Odiase.
 */
@Module(includes = ViewModelModule.class)
class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    RequestInterface provideJobService() {
        return ServiceGenerator.createService(RequestInterface.class);
    }

    @Provides
    @Singleton
    JobDao provideJobDao(JobsDatabase db) {
        return db.jobDao();
    }

    @Provides
    @Singleton
    JobsDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, JobsDatabase.class, "RemoteJobs.db")
                //.addMigrations(PopularMoviesDatabase.MIGRATION_1_2)
                .build();

        /*
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "jobs";
    private static JobsDatabase sInstance;

    public static JobsDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        JobsDatabase.class, JobsDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }*/
    }

    /*
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    CheckConnectionBroadcastReceiver providesCheckConnectionBroadcastReceiver() {
        return new CheckConnectionBroadcastReceiver();
    }*/

    @Provides
    @Singleton
    CompositeDisposable providesCompositeDisposable() {
        return new CompositeDisposable();
    }

}
