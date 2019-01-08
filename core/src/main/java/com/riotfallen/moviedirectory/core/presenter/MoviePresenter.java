package com.riotfallen.moviedirectory.core.presenter;

import android.support.annotation.NonNull;

import com.riotfallen.moviedirectory.core.BuildConfig;
import com.riotfallen.moviedirectory.core.api.APIRepository;
import com.riotfallen.moviedirectory.core.api.Client;
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
                    view.showMovieData(response.body());
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

    public void getMovie(Integer page){
        view.showMovieLoading();
        APIRepository apiRepository = Client.getClient().create(APIRepository.class);
        Call<MovieResponse> call = apiRepository.getMovies(BuildConfig.API_KEY, Constant.DEFAULT_MOVIE_LANGUAGE, Constant.DEFAULT_SORT_MOVIE, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if(response.isSuccessful()){
                    view.showMovieData(response.body());
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
}
