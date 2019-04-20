package io.github.alistairholmes.digitalnomadjobs.utils;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.squareup.leakcanary.LeakCanary;


import io.github.alistairholmes.digitalnomadjobs.BuildConfig;
import timber.log.Timber;

public class JobApplication extends Application {

    private  JobApplication instance;
    public Context context;

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        // Stetho init code
        Stetho.initializeWithDefaults(this);

        instance = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Timber.i("Creating our Application");

        context = getApplicationContext();

        // Required initialization logic here!
        new Instabug.Builder(this, "a2ffae571a0a8463b7cee78a1bf58d3c")
                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.SCREENSHOT)
                .build();
    }

    public  JobApplication getInstance() {
        return instance;
    }

    public Context getContext() {
        return context;
    }

}
