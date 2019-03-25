package io.github.alistairholmes.digitalnomadjobs.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.adapters.JobAdapter;
import io.github.alistairholmes.digitalnomadjobs.database.Job;
import io.github.alistairholmes.digitalnomadjobs.network.ApiClient;
import io.github.alistairholmes.digitalnomadjobs.network.GetDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobActivity extends AppCompatActivity {

    private TextView mNoInternetConnectionTv;
    private ImageView mNoWifiConnectionIv;
    private SearchView searchView;

    private SwipeRefreshLayout swipeContainer;

    //OkHttpClient client = new OkHttpClient();

    private RecyclerView.LayoutManager layoutManager;
    public JobAdapter mAdapter;
    public RecyclerView mainRecyclerView;
    //public String remoteJobUrl = "https://remoteok.io/api";

    private static final String LOG_TAG = JobActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNoInternetConnectionTv = findViewById(R.id.no_internet_connection_tv);
        mNoWifiConnectionIv = findViewById(R.id.no_wifi_connection_imageview);
        mainRecyclerView =  findViewById(R.id.recyclerView_main);

        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeRefreshLayout);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mainRecyclerView.addItemDecoration(itemDecoration);

        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setAdapter(mAdapter);

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = ApiClient.getRetrofitInstance().create(GetDataService.class);
        Call<List<Job>> call = service.getAllJobs();
        call.enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                    generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                Toast.makeText(JobActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Job> jobList) {
        mainRecyclerView = findViewById(R.id.recyclerView_main);
        mAdapter = new JobAdapter(this, jobList, new JobAdapter.OnJobClickListener() {
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
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(JobActivity.this);
        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.setAdapter(mAdapter);
    }


    /**
     * This is where we inflate and set up the menu for this Activity.
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.drawer_view, menu);

        // Associate searchable configuration with the SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }
}