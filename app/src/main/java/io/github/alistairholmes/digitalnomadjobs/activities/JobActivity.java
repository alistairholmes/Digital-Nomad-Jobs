package io.github.alistairholmes.digitalnomadjobs.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.adapters.JobAdapter;
import io.github.alistairholmes.digitalnomadjobs.database.Job;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JobActivity extends AppCompatActivity {

    private TextView mNoInternetConnectionTv;
    private ImageView mNoWifiConnectionIv;
    private SearchView searchView;

    private SwipeRefreshLayout swipeContainer;

    OkHttpClient client = new OkHttpClient();

    private RecyclerView.LayoutManager layoutManager;
    public JobAdapter mAdapter;
    public RecyclerView mainRecyclerView;
    public String remoteJobUrl = "https://remoteok.io/api";

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

        // Refresh the data
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
                // Fetching data from server
                try {
                    loadJobData(remoteJobUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // Code to refresh the list here.
                try {
                    loadJobData(remoteJobUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorAccent);

        try {
            loadJobData(remoteJobUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void loadJobData(String url) throws IOException {

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                mNoInternetConnectionTv.setText(R.string.no_internet_connection);
                mNoWifiConnectionIv.setVisibility(View.VISIBLE);

                // Stopping swipe refresh
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String jsonResponse = response.body().string();
                Log.d(LOG_TAG, String.valueOf(jsonResponse));

                JobActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new GsonBuilder().create();
                        List<Job> jobs = new ArrayList<Job>(Arrays.asList(gson.fromJson(jsonResponse, Job[].class)));

                        mAdapter = new JobAdapter(jobs, new JobAdapter.OnJobClickListener() {
                            @Override
                            public void onJobClick(Job job) {

                                //create a Bundle object
                                Bundle extras = new Bundle();

                                //Adding key value pairs to this bundle
                                extras.putString("JOB_TITLE", job.getJobTitle());
                                extras.putString("COMPANY_NAME", job.getCompanyName());
                                extras.putString("COMPANY_LOGO", job.getCompanyLogo());
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

                        mainRecyclerView.setAdapter(mAdapter);
                        Log.d(LOG_TAG, String.valueOf(mAdapter));

                        // Stopping swipe refresh
                        swipeContainer.setRefreshing(false);
                    }
                });
            }
});
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