package io.github.alistairholmes.digitalnomadjobs.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import io.github.alistairholmes.digitalnomadjobs.ui.jobs.JobViewModel;
import io.github.alistairholmes.digitalnomadjobs.utils.JobsViewModelFactory;

/*
 * @author Osaigbovo Odiase.
 * */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(JobViewModel.class)
    abstract ViewModel bindJobViewModel(JobViewModel jobViewModel);

    /*
    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel.class)
    abstract ViewModel bindMovieDetailViewModel(MovieDetailViewModel movieDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel searchViewModel);*/

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(JobsViewModelFactory factory);
}
