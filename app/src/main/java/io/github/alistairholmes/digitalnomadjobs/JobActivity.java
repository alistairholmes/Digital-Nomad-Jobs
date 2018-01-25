package io.github.alistairholmes.digitalnomadjobs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JobActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public String url = "https://remoteok.io/remote-jobs.json";

    private static final String LOG_TAG = JobActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*// Create a fake list of earthquakes.
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("Front-End Dev", "San Francisco", "Feb 2, 2016"));
        jobs.add(new Job("Back-End Dev", "London", "July 20, 2015"));
        jobs.add(new Job("ios Dev", "Tokyo", "Nov 10, 2014"));
        jobs.add(new Job("android dev", "Mexico City", "May 3, 2014"));
        jobs.add(new Job("nodejs dev", "Moscow", "Jan 31, 2013"));
        jobs.add(new Job("rails dev", "Rio de Janeiro", "Aug 19, 2012"));
        jobs.add(new Job("block-chain dev", "Paris", "Oct 30, 2011"));
        jobs.add(new Job("Front-End Dev", "San Francisco", "Feb 2, 2016"));
        jobs.add(new Job("Back-End Dev", "London", "July 20, 2015"));
        jobs.add(new Job("ios Dev", "Tokyo", "Nov 10, 2014"));
        jobs.add(new Job("android dev", "Mexico City", "May 3, 2014"));
        jobs.add(new Job("nodejs dev", "Moscow", "Jan 31, 2013"));
        jobs.add(new Job("rails dev", "Rio de Janeiro", "Aug 19, 2012"));
        jobs.add(new Job("block-chain dev", "Paris", "Oct 30, 2011"));*/


        RecyclerView mainRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_main);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(layoutManager);

        mainRecyclerView.setHasFixedSize(true);

        // Create a new adapter that takes the list of jobs as input
        //mAdapter = new JobAdapter(jobs);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mainRecyclerView.setAdapter(mAdapter);

        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String jsonResponse = response.body().string();

                Log.d(LOG_TAG, jsonResponse);
            }
        });

    }
}


