package io.github.alistairholmes.digitalnomadjobs.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.github.alistairholmes.digitalnomadjobs.data.provider.JobsProvider;

@Module
public abstract class ContentProviderBuilderModule {

    @ContributesAndroidInjector
    abstract JobsProvider contributeJobsProvider();
}
