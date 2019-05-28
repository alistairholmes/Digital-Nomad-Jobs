package io.github.alistairholmes.digitalnomadjobs.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import io.github.alistairholmes.digitalnomadjobs.JobApplication;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityModule.class,
        ContentProviderBuilderModule.class,
        FragmentBuildersModule.class,
        ServiceBuilderModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    // Where the dependency injection has to be used.
    void inject(JobApplication app);

}
