package io.github.alistairholmes.digitalnomadjobs.data.remote;

import java.util.List;

import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.github.alistairholmes.digitalnomadjobs.data.model.SearchResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestInterface {

    @GET("/api")
    Observable<List<Job>> getAllJobs();

    /*@GET("/api?tags=android")
    Single<List<Job>> getAndroidJobs();*/

    // For making search request
    @GET("/api?tags=")
    Observable<SearchResponse> search(
            @Query("query") String query);

}
