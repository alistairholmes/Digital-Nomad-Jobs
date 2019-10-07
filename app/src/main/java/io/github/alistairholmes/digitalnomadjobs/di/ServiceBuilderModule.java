package io.github.alistairholmes.digitalnomadjobs.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.github.alistairholmes.digitalnomadjobs.widget.FavoriteJobWidgetService;

@Module
public abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    abstract FavoriteJobWidgetService contributeMyService();
}
