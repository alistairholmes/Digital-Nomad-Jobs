package io.github.alistairholmes.digitalnomadjobs.data.remote;

import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("/api")
    Observable<List<Job>> getAllJobs();

    // For making search request
    @GET("/api?tags=android")
    Single<List<Job>> getAndroidJobs();

    @GET("/api?tags=frontend")
    Single<List<Job>> getFrontEndJobs();

    @GET("/api?tags=javascript")
    Single<List<Job>> getJavaScriptJobs();

    @GET("/api?tags=ruby")
    Single<List<Job>> getRubyJobs();

    @GET("/api?tags=devops")
    Single<List<Job>> getDevopsJobs();

    @GET("/api?tags=full-stack")
    Single<List<Job>> getFullStackJobs();

    @GET("/api?tags=marketing")
    Single<List<Job>> getMarketingJobs();

    @GET("/api?tags=ios")
    Single<List<Job>> getIosJobs();

    @GET("/api?tags=php")
    Single<List<Job>> getPhpJobs();

}
