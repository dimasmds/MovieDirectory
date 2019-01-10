package com.riotfallen.moviedirectory.core.api;

import com.riotfallen.moviedirectory.core.model.movie.Movie;
import com.riotfallen.moviedirectory.core.model.movie.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIRepository {

    @GET("search/movie")
    Call<MovieResponse> serachMovie(@Query("api_key") String apiKey,
                                    @Query("language") String language,
                                    @Query("query") String query,
                                    @Query("page") Integer page);

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey,
                                            @Query("language") String language,
                                            @Query("page") Integer page);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(@Query("api_key") String apiKey,
                                            @Query("language") String language,
                                            @Query("page") Integer page);

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(@Path("movie_id") Integer movieId,
                         @Query("api_key") String apiKey,
                         @Query("language") String language);
}
