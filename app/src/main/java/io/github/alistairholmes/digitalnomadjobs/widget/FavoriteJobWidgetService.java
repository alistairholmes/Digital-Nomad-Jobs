package io.github.alistairholmes.digitalnomadjobs.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.android.AndroidInjection;
import io.github.alistairholmes.digitalnomadjobs.data.repository.JobRepository;

public class FavoriteJobWidgetService extends RemoteViewsService {

    @Inject
    JobRepository jobRepository;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavoriteRemoteViewsFactory(this.getApplicationContext(), intent, jobRepository);
    }
}