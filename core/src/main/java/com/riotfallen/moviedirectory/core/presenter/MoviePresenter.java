package com.riotfallen.moviedirectory.core.presenter;

import android.support.annotation.NonNull;

import com.riotfallen.moviedirectory.core.BuildConfig;
import com.riotfallen.moviedirectory.core.api.APIRepository;
import com.riotfallen.moviedirectory.core.api.Client;
import com.riotfallen.moviedirectory.core.model.movie.Movie;
import com.riotfallen.moviedirectory.core.model.movie.MovieResponse;
import com.riotfallen.moviedirectory.core.utils.Constant;
import com.riotfallen.moviedirectory.core.view.MovieView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviePresenter {

    private MovieView view;

    public MoviePresenter(MovieView view) {
        this.view = view;
    }

    public void searchMovie(String query, Integer page){
        view.showMovieLoading();
        APIRepository apiRepository = Client.getClient().create(APIRepository.class);
        Call<MovieResponse> call = apiRepository.serachMovie(BuildConfig.API_KEY, Constant.DEFAULT_MOVIE_LANGUAGE, query, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if(response.isSuccessful()){
                    view.showMovies(response.body());
                    view.hideMovieLoading();
                } else {
                    view.showMovieError(response.message());
                    view.hideMovieLoading();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                view.showMovieError(t.getMessage());
                view.hideMovieLoading();
            }
        });
    }

    public void getNowPlayingMovies(Integer page){
        view.showMovieLoading();
        APIRepository apiRepository = Client.getClient().create(APIRepository.class);
        Call<MovieResponse> call = apiRepository.getNowPlayingMovies(BuildConfig.API_KEY, Constant.DEFAULT_MOVIE_LANGUAGE, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if(response.isSuccessful()){
                    view.showMovies(response.body());
                    view.hideMovieLoading();
                } else {
                    view.showMovieError(response.message());
                    view.hideMovieLoading();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                view.showMovieError(t.getMessage());
                view.hideMovieLoading();
            }
        });
    }

    public void getUpcomingMovies(Integer page){
        view.showMovieLoading();
        APIRepository apiRepository = Client.getClient().create(APIRepository.class);
        Call<MovieResponse> call = apiRepository.getUpcomingMovies(BuildConfig.API_KEY, Constant.DEFAULT_MOVIE_LANGUAGE, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if(response.isSuccessful()){
                    view.showMovies(response.body());
                    view.hideMovieLoading();
                } else {
                    view.showMovieError(response.message());
                    view.hideMovieLoading();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                view.showMovieError(t.getMessage());
                view.hideMovieLoading();
            }
        });
    }

    public void getMovie(Integer movieId){
        view.showMovieLoading();
        APIRepository apiRepository = Client.getClient().create(APIRepository.class);
        Call<Movie> call = apiRepository.getMovie(movieId, BuildConfig.API_KEY, Constant.DEFAULT_MOVIE_LANGUAGE);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if(response.isSuccessful()){
                    view.showMovie(response.body());
                    view.hideMovieLoading();
                } else {
                    view.showMovieError(response.message());
                    view.hideMovieLoading();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                view.showMovieError(t.getMessage());
                view.hideMovieLoading();
            }
        });
    }
}
