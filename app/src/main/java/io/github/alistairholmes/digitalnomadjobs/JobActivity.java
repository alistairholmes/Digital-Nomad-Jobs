package io.github.alistairholmes.digitalnomadjobs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JobActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a fake list of earthquakes.
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("Front-End Dev", "San Francisco", "Feb 2, 2016"));
        jobs.add(new Job("Back-End Dev", "London", "July 20, 2015"));
        jobs.add(new Job("ios Dev", "Tokyo", "Nov 10, 2014"));
        jobs.add(new Job("android dev", "Mexico City", "May 3, 2014"));
        jobs.add(new Job("nodejs dev", "Moscow", "Jan 31, 2013"));
        jobs.add(new Job("rails dev", "Rio de Janeiro", "Aug 19, 2012"));
        jobs.add(new Job("block-chain dev", "Paris", "Oct 30, 2011"));

        RecyclerView mainRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_main);

        mainRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(layoutManager);

        // Create a new adapter that takes the list of jobs as input
        mAdapter = new JobAdapter(jobs);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mainRecyclerView.setAdapter(mAdapter);
    }
}


