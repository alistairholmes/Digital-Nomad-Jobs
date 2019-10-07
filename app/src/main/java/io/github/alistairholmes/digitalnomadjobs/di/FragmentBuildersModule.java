package io.github.alistairholmes.digitalnomadjobs.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.github.alistairholmes.digitalnomadjobs.ui.favorite.FavoriteFragment;

/*
 * @author Osaigbovo Odiase.
 * */
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract FavoriteFragment contributeFavoriteFragment();

}
