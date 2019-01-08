package com.riotfallen.moviedirectory.core.view;

import com.riotfallen.moviedirectory.core.model.movie.MovieResponse;

public interface MovieView {
    void showMovieLoading();
    void hideMovieLoading();
    void showMovieError(String message);
    void showMovieData(MovieResponse data);
}
