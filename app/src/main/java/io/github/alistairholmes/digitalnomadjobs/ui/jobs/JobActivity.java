package io.github.alistairholmes.digitalnomadjobs.ui.jobs;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.github.alistairholmes.digitalnomadjobs.ui.about.AboutActivity;
import io.github.alistairholmes.digitalnomadjobs.ui.adapter.JobAdapter;
import io.github.alistairholmes.digitalnomadjobs.ui.favorite.FavoriteActivity;
import io.github.alistairholmes.digitalnomadjobs.ui.jobdetail.DetailActivity;
import io.github.alistairholmes.digitalnomadjobs.ui.search.SearchActivity;
import io.github.alistairholmes.digitalnomadjobs.utils.DbUtil;
import io.github.alistairholmes.digitalnomadjobs.utils.JobListItemDecoration;

public class JobActivity extends AppCompatActivity implements JobAdapter.OnJobClickListener {

    private static final String LOG_TAG = JobActivity.class.getName();
    private static final int RC_SEARCH = 0;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private JobViewModel jobViewModel;

    private TextView mNoInternetConnectionTv;
    private ImageView mNoWifiConnectionIv;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private RecyclerView.LayoutManager layoutManager;
    public JobAdapter mAdapter;
    public RecyclerView mainRecyclerView;
    public LinearLayout linearLayout;
    public TextView mCategoriesTv;
    public TextView mJobsTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        jobViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(JobViewModel.class);

        mNoInternetConnectionTv = findViewById(R.id.no_internet_connection_tv);
        mNoWifiConnectionIv = findViewById(R.id.no_wifi_connection_imageview);
        mainRecyclerView = findViewById(R.id.recyclerView_main);
        linearLayout = findViewById(R.id.linearLayout);
        mCategoriesTv = findViewById(R.id.tv_categories);
        mJobsTv = findViewById(R.id.tv_jobs);

        // Lottie Loader
        final LottieAnimationView loaderLottie = findViewById(R.id.loader);

        // Swipe refresh
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(layoutManager);

        int smallPadding = getResources().getDimensionPixelSize(R.dimen.job_item_spacing_small);
        mainRecyclerView.addItemDecoration(new JobListItemDecoration(smallPadding));
        mainRecyclerView.setHasFixedSize(true);


       /* jobViewModel.getJobsLiveData().observe(this, resource -> {
            if (resource != null && resource.data.size() > 0) {
                jobList = resource.data;
                setupRecyclerView(resource.data);
            }
        });*/

        mAdapter = new JobAdapter(this, this);
        jobViewModel.jobsLiveData.observe(this, resource -> mAdapter.submitList(resource.data));
        loaderLottie.setVisibility(View.INVISIBLE);
        initSwipeToRefresh();
        mainRecyclerView.setAdapter(mAdapter);

        if(!haveNetworkConnection()) {
            mNoInternetConnectionTv.setText(R.string.no_internet_connection);
            mNoWifiConnectionIv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.dnj_toolbar_menu, menu);

        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    // When an Icon is clicked in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                this.startActivity(new Intent(JobActivity.this, AboutActivity.class));
                break;
            case R.id.favorites:
                this.startActivity(new Intent(JobActivity.this, FavoriteActivity.class));
                break;
            case R.id.menu_search:
                View searchMenuView = toolbar.findViewById(R.id.menu_search);
                Bundle options = ActivityOptions
                        .makeSceneTransitionAnimation(this, searchMenuView,
                                getString(R.string.transition_search_back)).toBundle();

                ActivityCompat.startActivityForResult(this,
                        new Intent(this, SearchActivity.class),
                        RC_SEARCH,
                        options);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_SEARCH:
                // reset the search icon which we hid
                View searchMenuView = toolbar.findViewById(R.id.menu_search);
                if (searchMenuView != null) {
                    searchMenuView.setAlpha(1f);
                }
                if (resultCode == SearchActivity.RESULT_CODE_SAVE) {
                    String query = data.getStringExtra(SearchActivity.EXTRA_QUERY);
                    if (TextUtils.isEmpty(query)) return;
                }
                break;
        }
    }

    @Override
    public void onJobClick(Job job) {
        //create a Bundle object
        Bundle extras = new Bundle();

        //Adding key value pairs to this bundle
        extras.putString("JOB_TITLE", job.getPosition());
        extras.putString("COMPANY_NAME", job.getCompany());
        extras.putString("COMPANY_LOGO", job.getLogo());
        extras.putString("JOB_DESCRIPTION", job.getDescription());
        extras.putInt("JOB_ID", job.getId());
        //create and initialize an intent
        Intent intent = new Intent(JobActivity.this, DetailActivity.class);
        //attach the bundle to the Intent object
        intent.putExtras(extras);
        //finally start the activity
        startActivity(intent);
    }

    @Override
    public void onFavoredClicked(@NonNull Job job, boolean isFavorite, int position) {
        jobViewModel.setJobFavored(job, isFavorite);

        //Timber.e("onFavoredClicked: favored=%s", isFavorite);

        /*if (isFavorite) {
            Snackbar.make(findViewById(R.id.container),
                    job.getPosition().substring(0, 10) + "... is removed to Favorites!",
                    Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(findViewById(R.id.container),
                    job.getPosition().substring(0, 10) + "... is added from Favorites!",
                    Snackbar.LENGTH_SHORT).show();
        }*/

        DbUtil.updateWidgets(this);
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void initSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            jobViewModel.refresh();
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}