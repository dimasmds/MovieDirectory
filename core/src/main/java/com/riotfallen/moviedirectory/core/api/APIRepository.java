package com.riotfallen.moviedirectory.core.api;

import com.riotfallen.moviedirectory.core.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIRepository {

    @GET("search/movie")
    Call<MovieResponse> serachMovie(@Query("api_key") String apiKey,
                                    @Query("language") String language,
                                    @Query("query") String query,
                                    @Query("page") Integer page);

    @GET("discover/movie")
    Call<MovieResponse> getMovie(@Query("api_key") String apiKey,
                                 @Query("language") String language,
                                 @Query("sort_by") String sortBy,
                                 @Query("page") Integer page);
}
