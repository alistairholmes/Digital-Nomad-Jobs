package io.github.alistairholmes.digitalnomadjobs;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

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

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(layoutManager);

        mainRecyclerView.setHasFixedSize(true);

        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() throws IOException {

        mProgressBar.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                mProgressBar.setVisibility(View.INVISIBLE);
                mNoInternetConnectionTv.setText(R.string.no_internet_connection);
                mNoWifiConnectionIv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String jsonResponse = response.body().string();

                JobActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.INVISIBLE);

                            String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.setDateFormat(ISO_FORMAT);

                            Gson gson = new GsonBuilder().create();
                            List<Job> jobs = new ArrayList<Job>();
                            jobs = Arrays.asList(gson.fromJson(jsonResponse, Job[].class));

                        final List<Job> finalJobs = jobs;
                        mAdapter = new JobAdapter(jobs, new JobAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Job clickedItem = finalJobs.get(position);
                                    Uri jobURL = Uri.parse(clickedItem.getUrl());
                                    Intent intent =  new Intent(Intent.ACTION_VIEW);
                                    intent.setData(jobURL);

                                    startActivity(intent);
                                }
                            });

                            mainRecyclerView.setAdapter(mAdapter);
                    }
                });


                Log.d(LOG_TAG, String.valueOf(mAdapter));
            }
        });

    }
}
