package io.github.alistairholmes.digitalnomadjobs.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.github.alistairholmes.digitalnomadjobs.data.provider.JobsProvider;
import io.github.alistairholmes.digitalnomadjobs.ui.jobs.JobActivity;

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract JobActivity contributeJobActivity();

    @ContributesAndroidInjector
    abstract JobsProvider contributeJobsProvider();

    /*@ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MovieDetailActivity contributeMovieDetailActivity();

    @ContributesAndroidInjector
    abstract SearchActivity contributeSearchActivity();*/
}
