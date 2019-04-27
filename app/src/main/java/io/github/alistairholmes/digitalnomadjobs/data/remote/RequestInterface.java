package io.github.alistairholmes.digitalnomadjobs.data.remote;

import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("/api")
    Observable<List<Job>> getAllJobs();

    // Search request
    @GET("/api?tags=android")
    Single<List<Job>> getAndroidJobs();

    @GET("/api?tags=frontend")
    Single<List<Job>> getFrontEndJobs();

}
