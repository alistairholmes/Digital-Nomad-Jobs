package io.github.alistairholmes.digitalnomadjobs.network;

import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.database.Job;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("/api")
    Call<List<Job>> getAllJobs();
}
