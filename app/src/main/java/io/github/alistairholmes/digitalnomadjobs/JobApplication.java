package io.github.alistairholmes.digitalnomadjobs;

import android.app.Activity;
import android.app.Service;
import android.content.ContentProvider;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasContentProviderInjector;
import dagger.android.HasServiceInjector;
import io.github.alistairholmes.digitalnomadjobs.di.AppInjector;
import timber.log.Timber;

public class JobApplication extends MultiDexApplication implements HasActivityInjector, HasContentProviderInjector, HasServiceInjector {

    private static JobApplication instance;
    public Context context;

    @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject DispatchingAndroidInjector<ContentProvider> contentProviderInjector;
    @Inject DispatchingAndroidInjector<Service> dispatchingServiceInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Stetho init code
        Stetho.initializeWithDefaults(this);


        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Timber.i("Creating our Application");

        context = getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        AppInjector.init(this);

    }

    public static JobApplication getInstance() {
        return instance;
    }

    public static boolean hasNetwork() {
        return instance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<ContentProvider> contentProviderInjector() {
        return contentProviderInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }

}
