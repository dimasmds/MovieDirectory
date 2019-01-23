package com.riotfallen.moviedirectory.activity;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class SearchActivity extends AppCompatActivity implements MovieView {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private MoviePresenter moviePresenter;

    private List<Result> movies;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressBar = findViewById(R.id.searchActivityProgressBar);
        recyclerView = findViewById(R.id.searchActivityRecyclerView);

        editText = findViewById(R.id.searchActivityEditText);

        movies = new ArrayList<>();

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList("movies");
            loadMovies(movies);
            editText.setText(savedInstanceState.getString("query"));
        } else {
            moviePresenter = new MoviePresenter(this);
        }

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = editText.getText().toString();
                    moviePresenter.searchMovie(query, 1);
                    return true;
                }
                return false;

            }
        });
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMovies(MovieResponse data) {
        movies = data.getResults();
        loadMovies(movies);
    }

    private void loadMovies(List<Result> movies) {
        MovieListAdapter movieListAdapter = new MovieListAdapter(this, movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        recyclerView.setAdapter(movieListAdapter);
    }

    @Override
    public void showMovie(Movie data) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies", (ArrayList<? extends Parcelable>) movies);
        outState.putString("query", editText.getText().toString());
    }
}
