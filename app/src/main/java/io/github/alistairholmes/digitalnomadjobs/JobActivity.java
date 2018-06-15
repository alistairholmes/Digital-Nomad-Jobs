package io.github.alistairholmes.digitalnomadjobs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JobActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private TextView mNoInternetConnectionTv;
    private ImageView mNoWifiConnectionIv;

    private SwipeRefreshLayout swipeContainer;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public RecyclerView.Adapter mAdapter;
    public RecyclerView mainRecyclerView;
    public String url = "https://remoteok.io/remote-jobs.json";
    public static final String LOGO_URL = "logoUrl";

    public List<Job> jobs;

    private static final String LOG_TAG = JobActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_loader);
        mNoInternetConnectionTv = findViewById(R.id.no_internet_connection_tv);
        mNoWifiConnectionIv = findViewById(R.id.no_wifi_connection_imageview);
        mainRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_main);

        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeContainer);

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
                    loadJobData();
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
                    loadJobData();
                    mProgressBar.setVisibility(View.INVISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorAccent);


        try {
            loadJobData();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void loadJobData() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                mProgressBar.setVisibility(View.INVISIBLE);
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
                        mProgressBar.setVisibility(View.INVISIBLE);

                            Gson gson = new GsonBuilder().create();
                            List<Job> jobs = new ArrayList<Job>();
                            jobs = Arrays.asList(gson.fromJson(jsonResponse, Job[].class));

                        mAdapter = new JobAdapter(jobs, new JobAdapter.OnJobClickListener() {
                            @Override
                            public void onJobClick(Job job) {
                                if (job.getUrl() != null) {
                                    Uri jobURL = Uri.parse(job.getUrl());
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(jobURL);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(JobActivity.this, "Sorry no URL is available for this job at the moment. Please try again later",
                                            Toast.LENGTH_SHORT).show();
                                }
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
}
