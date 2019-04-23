package io.github.alistairholmes.digitalnomadjobs.network;

import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.database.Job;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("/api")
    Call<List<Job>> getAllJobs();

    // For making search request
    @GET("/api?tags=android")
    Call<List<Job>> getAndroidJobs();

    @GET("/api?tags=frontend")
    Call<List<Job>> getFrontEndJobs();

    @GET("/api?tags=javascript")
    Call<List<Job>> getJavaScriptJobs();

    @GET("/api?tags=ruby")
    Call<List<Job>> getRubyJobs();

    @GET("/api?tags=devops")
    Call<List<Job>> getDevopsJobs();

    @GET("/api?tags=full-stack")
    Call<List<Job>> getFullStackJobs();

    @GET("/api?tags=marketing")
    Call<List<Job>> getMarketingJobs();

    @GET("/api?tags=ios")
    Call<List<Job>> getIosJobs();

    @GET("/api?tags=php")
    Call<List<Job>> getPhpJobs();
}
