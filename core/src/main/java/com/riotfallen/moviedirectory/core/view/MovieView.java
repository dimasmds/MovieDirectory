package com.riotfallen.moviedirectory.core.view;

import com.riotfallen.moviedirectory.core.model.movie.Movie;
import com.riotfallen.moviedirectory.core.model.movie.MovieResponse;

public interface MovieView {
    void showMovieLoading();
    void hideMovieLoading();
    void showMovieError(String message);
    void showMovies(MovieResponse data);
    void showMovie(Movie data);
}
