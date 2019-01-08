package com.riotfallen.moviedirectory.core.presenter;

import com.riotfallen.moviedirectory.core.BuildConfig;
import com.riotfallen.moviedirectory.core.api.APIRepository;
import com.riotfallen.moviedirectory.core.api.Client;
import com.riotfallen.moviedirectory.core.model.MovieResponse;
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
        Call<MovieResponse> call = apiRepository.getMovie(BuildConfig.API_KEY, Constant.DEFAULT_MOVIE_LANGUAGE, query, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if(response.isSuccessful()){
                    view.showMovieData(response.body());
                    view.hideMovieLoading();
                } else {
                    view.showMovieError(response.message());
                    view.hideMovieLoading();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                view.showMovieError(t.getMessage());
                view.hideMovieLoading();
            }
        });
    }

    public void getMovie(Integer page){
        view.showMovieLoading();
        APIRepository apiRepository = Client.getClient().create(APIRepository.class);
        Call<MovieResponse> call = apiRepository.getMovie(BuildConfig.API_KEY, Constant.DEFAULT_MOVIE_LANGUAGE, Constant.DEFAULT_SORT_MOVIE, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if(response.isSuccessful()){
                    view.showMovieData(response.body());
                    view.hideMovieLoading();
                } else {
                    view.showMovieError(response.message());
                    view.hideMovieLoading();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                view.showMovieError(t.getMessage());
                view.hideMovieLoading();
            }
        });
    }
}
