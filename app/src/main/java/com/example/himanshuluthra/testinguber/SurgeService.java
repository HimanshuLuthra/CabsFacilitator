package com.example.himanshuluthra.testinguber;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by himanshuluthra on 15/03/17.
 */

public interface SurgeService {

    @GET("test/{name}.php")
    Call<String> getStoryIds(@Path("name") String details, @Query("test") String id);

}
