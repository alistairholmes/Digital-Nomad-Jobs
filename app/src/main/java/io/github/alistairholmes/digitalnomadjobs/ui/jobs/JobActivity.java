package io.github.alistairholmes.digitalnomadjobs.ui.jobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.github.alistairholmes.digitalnomadjobs.ui.adapter.JobAdapter;
import io.github.alistairholmes.digitalnomadjobs.ui.favorite.FavoriteActivity;
import io.github.alistairholmes.digitalnomadjobs.ui.jobdetail.DetailActivity;
import io.github.alistairholmes.digitalnomadjobs.utils.JobListItemDecoration;

public class JobActivity extends AppCompatActivity implements JobAdapter.OnJobClickListener {

    private static final String LOG_TAG = JobActivity.class.getName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private JobViewModel jobViewModel;

    private TextView mNoInternetConnectionTv;
    private ImageView mNoWifiConnectionIv;
    private SearchView searchView;
    private SwipeRefreshLayout swipeContainer;
    private Toolbar toolbar;
    private RecyclerView.LayoutManager layoutManager;
    public JobAdapter mAdapter;
    public RecyclerView mainRecyclerView;

    private List<Job> jobList;

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

        // Lottie Loader
        final LottieAnimationView loaderLottie = findViewById(R.id.loader);

        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeRefreshLayout);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(layoutManager);

        int smallPadding = getResources().getDimensionPixelSize(R.dimen.job_item_spacing_small);
        mainRecyclerView.addItemDecoration(new JobListItemDecoration(smallPadding));
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setAdapter(mAdapter);

        jobViewModel.getJobsLiveData().observe(this, resource -> {
            if (resource != null && resource.data.size() > 0) {
                jobList = resource.data;
                setupRecyclerView(resource.data);
                loaderLottie.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void setupRecyclerView(List<Job> jobList) {
        mAdapter = new JobAdapter(this, jobList, this);
        mainRecyclerView.setAdapter(mAdapter);
    }


    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.dnj_toolbar_menu, menu);

        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.favorites:
                Intent intent = new Intent(JobActivity.this, FavoriteActivity.class);
                //finally start the activity
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
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

}