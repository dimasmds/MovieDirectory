package com.riotfallen.moviedirectory.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.riotfallen.moviedirectory.BuildConfig;
import com.riotfallen.moviedirectory.R;
import com.riotfallen.moviedirectory.core.model.movie.Movie;
import com.riotfallen.moviedirectory.core.model.movie.MovieResponse;
import com.riotfallen.moviedirectory.core.presenter.MoviePresenter;
import com.riotfallen.moviedirectory.core.view.MovieView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailMovieActivity extends AppCompatActivity implements MovieView {

    public static final String TITLE_MOVIE = "titleMovie";
    public static final String ID_MOVIE = "idMovie";

    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        String movieTitle = getIntent().getStringExtra(TITLE_MOVIE);
        Integer movieId = getIntent().getIntExtra(ID_MOVIE, 0);

        progressBar = findViewById(R.id.detailActivityProgressBar);
        nestedScrollView = findViewById(R.id.detailActivityNestedScrollView);

        Toolbar toolbar = findViewById(R.id.detailActivityToolbar);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(movieTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MoviePresenter presenter = new MoviePresenter(this);

        presenter.getMovie(movieId);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMovieLoading() {
        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideMovieLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMovieError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMovies(MovieResponse data) {}

    @SuppressLint("SetTextI18n")
    @Override
    public void showMovie(Movie data) {
        TextView textViewTitle = findViewById(R.id.detailActivityTextViewTitle);
        TextView textViewProduction = findViewById(R.id.detailActivityTextViewProduction);
        TextView textViewYearMovie = findViewById(R.id.detailActivityTextViewYear);
        TextView textViewRating = findViewById(R.id.detailActivityTextViewRating);
        TextView textViewDuration = findViewById(R.id.detailActivityTextViewDuration);
        TextView textViewDescription = findViewById(R.id.detailActivityTextViewOverview);
        TextView textViewLanguage = findViewById(R.id.detailActivityTextViewLanguage);
        ImageView imageViewThumbnail = findViewById(R.id.detailActivityThumbnail);

        LinearLayout linearLayoutCategory = findViewById(R.id.detailActivityLinearLayoutCategory);

        textViewTitle.setText(data.getTitle());
        textViewYearMovie.setText(data.getReleaseDate());
        if (data.getProductionCompanies().size() != 0) {
            textViewProduction.setText(data.getProductionCompanies().get(0).getName());
        } else {
            textViewProduction.setText("Unknown");
        }
        textViewRating.setText(data.getVoteAverage().toString());
        textViewDescription.setText(data.getOverview());
        textViewDuration.setText(data.getRuntime() + " Minutes");


        Picasso.get().load(BuildConfig.IMAGE_BASE_URL + data.getPosterPath()).fit().centerCrop().into(imageViewThumbnail);

        for(int i = 0; i < data.getSpokenLanguages().size(); i++){
            if(i == 0)
                textViewLanguage.setText(data.getSpokenLanguages().get(i).getName());
            else
                textViewLanguage.setText(textViewLanguage.getText() + ", " + data.getSpokenLanguages().get(i).getName());
        }

        for (int i = 0; i < data.getGenres().size(); i++) {
            TextView category = new TextView(this);

            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            ((LinearLayout.LayoutParams) params).setMarginEnd(10);

            category.setLayoutParams(params);
            category.setTextColor(getResources().getColor(android.R.color.white));
            category.setLines(1);
            category.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            category.setPadding(10, 0, 10, 0);
            category.setText(data.getGenres().get(i).getName());
            linearLayoutCategory.addView(category);

            if (i == 2) {
                break;
            }
        }

    }


}
