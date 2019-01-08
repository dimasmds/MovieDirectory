package com.riotfallen.moviedirectory.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.riotfallen.moviedirectory.R;
import com.riotfallen.moviedirectory.adapter.MovieListAdapter;
import com.riotfallen.moviedirectory.core.model.movie.MovieResponse;
import com.riotfallen.moviedirectory.core.model.movie.Result;
import com.riotfallen.moviedirectory.core.presenter.MoviePresenter;
import com.riotfallen.moviedirectory.core.view.MovieView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieView {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private MoviePresenter moviePresenter;

    private List<Result> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.mainActivityRecyclerView);
        progressBar = findViewById(R.id.mainActivityProgressBar);
        final EditText editTextSearch = findViewById(R.id.mainActivityEditTextSearch);

        movies = new ArrayList<>();

        moviePresenter = new MoviePresenter(this);
        moviePresenter.getMovie(1);

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    movies.clear();
                    String query = editTextSearch.getText().toString();
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
    public void showMovieData(MovieResponse data) {
        movies.addAll(data.getResults());
        MovieListAdapter movieListAdapter = new MovieListAdapter(this, movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        recyclerView.setAdapter(movieListAdapter);
    }
}
