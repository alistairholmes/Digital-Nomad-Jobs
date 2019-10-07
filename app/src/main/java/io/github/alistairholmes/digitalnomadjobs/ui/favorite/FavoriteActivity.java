package io.github.alistairholmes.digitalnomadjobs.ui.favorite;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.data.local.entity.FavoriteJob;
import io.github.alistairholmes.digitalnomadjobs.ui.adapter.FavoriteAdapter;
import io.github.alistairholmes.digitalnomadjobs.ui.jobdetail.DetailActivity;

public class FavoriteActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        FavoriteAdapter.OnFavoriteJobClickListener {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @BindView(R.id.app_bar) Toolbar toolbar;

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.favorite_container, FavoriteFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof FavoriteFragment) {
            FavoriteFragment favoriteFragment = (FavoriteFragment) fragment;
            favoriteFragment.setOnFavoriteJobClickListener(this);
        }
    }

    @Override
    public void onJobClick(FavoriteJob favoriteJob) {
        //create a Bundle object
        Bundle extras = new Bundle();

        //Adding key value pairs to this bundle
        extras.putString("JOB_TITLE", favoriteJob.getPosition());
        extras.putString("COMPANY_NAME", favoriteJob.getCompany());
        extras.putString("COMPANY_LOGO", favoriteJob.getLogo());
        extras.putString("JOB_DESCRIPTION", favoriteJob.getDescription());
        extras.putInt("JOB_ID", favoriteJob.getId());
        //create and initialize an intent
        Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
        //attach the bundle to the Intent object
        intent.putExtras(extras);
        //finally start the activity
        startActivity(intent);
    }

    @Override
    public void onFavoredClicked(@NonNull FavoriteJob favoriteJob, boolean isFavorite, int position) {

    }

}
