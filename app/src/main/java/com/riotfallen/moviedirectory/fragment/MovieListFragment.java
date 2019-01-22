package com.riotfallen.moviedirectory.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.riotfallen.moviedirectory.R;
import com.riotfallen.moviedirectory.adapter.recyler.FavoriteListAdapter;
import com.riotfallen.moviedirectory.adapter.recyler.MovieListAdapter;
import com.riotfallen.moviedirectory.core.db.model.FavoriteMovie;
import com.riotfallen.moviedirectory.core.model.movie.Movie;
import com.riotfallen.moviedirectory.core.model.movie.MovieResponse;
import com.riotfallen.moviedirectory.core.model.movie.Result;
import com.riotfallen.moviedirectory.core.presenter.FavoritePresenter;
import com.riotfallen.moviedirectory.core.presenter.MoviePresenter;
import com.riotfallen.moviedirectory.core.view.FavoriteView;
import com.riotfallen.moviedirectory.core.view.MovieView;

import java.util.ArrayList;
import java.util.List;


public class MovieListFragment extends Fragment implements MovieView, FavoriteView {

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
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Result> movies;
    private ArrayList<FavoriteMovie> favoriteMovies;

    int position;
    MoviePresenter moviePresenter;
    FavoritePresenter favoritePresenter;

    public MovieListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        recyclerView = view.findViewById(R.id.movieFragmentRecyclerView);
        progressBar = view.findViewById(R.id.movieFragmentProgressBar);
        swipeRefreshLayout = view.findViewById(R.id.movieFragmentSwipeRefreshLayout);
        position = getArguments() != null ? getArguments().getInt(POSITION, 0) : 0;
        moviePresenter = new MoviePresenter(this);
        favoritePresenter = new FavoritePresenter(getContext(),this);
        movies = new ArrayList<>();
        favoriteMovies = new ArrayList<>();
        if (position == 0) {
            moviePresenter.getNowPlayingMovies(1);
        } else if(position == 1) {
            moviePresenter.getUpcomingMovies(1);
        } else {
            favoritePresenter.showFavoriteData();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (position == 0) {
                    moviePresenter.getNowPlayingMovies(1);
                } else if(position == 1) {
                    moviePresenter.getUpcomingMovies(1);
                } else {
                    favoritePresenter.showFavoriteData();
                }
            }
        });

        return view;
    }

    @Override
    public void showMovieLoading() {
        if(!swipeRefreshLayout.isRefreshing()){
            progressBar.setVisibility(View.VISIBLE);
        }
        recyclerView.setVisibility(View.INVISIBLE);

    }

    @Override
    public void hideMovieLoading() {
        if(!swipeRefreshLayout.isRefreshing()){
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
        recyclerView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMovieError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showMovies(MovieResponse data) {
        movies.clear();
        movies.addAll(data.getResults());
        MovieListAdapter movieListAdapter = new MovieListAdapter(getContext(), movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        recyclerView.setAdapter(movieListAdapter);
    }

    @Override
    public void showMovie(Movie data) {}
    @Override
    public void onAdded(String message) {}
    @Override
    public void onDeleted(String message) {}

    @Override
    public void showFavoriteData(ArrayList<FavoriteMovie> data) {
        favoriteMovies.clear();
        favoriteMovies.addAll(data);
        FavoriteListAdapter favoriteAdapter = new FavoriteListAdapter(getContext(), favoriteMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        recyclerView.setAdapter(favoriteAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}
