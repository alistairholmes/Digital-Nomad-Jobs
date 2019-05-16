package io.github.alistairholmes.digitalnomadjobs.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.github.alistairholmes.digitalnomadjobs.data.provider.JobsProvider;
import io.github.alistairholmes.digitalnomadjobs.ui.favorite.FavoriteActivity;
import io.github.alistairholmes.digitalnomadjobs.ui.jobs.JobActivity;

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract JobActivity contributeJobActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract FavoriteActivity contributeFavoriteActivity();

    @ContributesAndroidInjector
    abstract JobsProvider contributeJobsProvider();

    /*@ContributesAndroidInjector
    abstract SearchActivity contributeSearchActivity();*/
}
