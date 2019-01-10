package com.riotfallen.moviedirectory.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.riotfallen.moviedirectory.R;
import com.riotfallen.moviedirectory.adapter.recyler.MovieListAdapter;
import com.riotfallen.moviedirectory.core.model.movie.Movie;
import com.riotfallen.moviedirectory.core.model.movie.MovieResponse;
import com.riotfallen.moviedirectory.core.model.movie.Result;
import com.riotfallen.moviedirectory.core.presenter.MoviePresenter;
import com.riotfallen.moviedirectory.core.view.MovieView;

import java.util.ArrayList;
import java.util.List;


public class MovieListFragment extends Fragment implements MovieView {

    public static final String POSITION = "position";

    public static MovieListFragment newInstance(int position){
        MovieListFragment movieListFragment = new MovieListFragment();

        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        movieListFragment.setArguments(args);

        return  movieListFragment;
    }

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private List<Result> movies;

    public MovieListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        recyclerView = view.findViewById(R.id.movieFragmentRecyclerView);
        progressBar = view.findViewById(R.id.movieFragmentProgressBar);
        int position = getArguments() != null ? getArguments().getInt(POSITION, 0) : 0;
        MoviePresenter moviePresenter = new MoviePresenter(this);
        movies = new ArrayList<>();
        if (position == 0) {
            moviePresenter.getNowPlayingMovies(1);
        } else {
            moviePresenter.getUpcomingMovies(1);
        }

        return view;
    }

    @Override
    public void showMovieLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

    }

    @Override
    public void hideMovieLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMovieError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMovies(MovieResponse data) {
        movies.addAll(data.getResults());
        MovieListAdapter movieListAdapter = new MovieListAdapter(getContext(), movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        recyclerView.setAdapter(movieListAdapter);
    }

    @Override
    public void showMovie(Movie data) { }
}
