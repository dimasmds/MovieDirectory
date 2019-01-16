package com.riotfallen.moviedirectory.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.riotfallen.moviedirectory.BuildConfig;
import com.riotfallen.moviedirectory.R;
import com.riotfallen.moviedirectory.core.db.model.FavoriteMovie;
import com.riotfallen.moviedirectory.core.model.movie.Movie;
import com.riotfallen.moviedirectory.core.model.movie.MovieResponse;
import com.riotfallen.moviedirectory.core.model.video.Video;
import com.riotfallen.moviedirectory.core.presenter.FavoritePresenter;
import com.riotfallen.moviedirectory.core.presenter.MoviePresenter;
import com.riotfallen.moviedirectory.core.presenter.VideoPresenter;
import com.riotfallen.moviedirectory.core.view.FavoriteView;
import com.riotfallen.moviedirectory.core.view.MovieView;
import com.riotfallen.moviedirectory.core.view.VideoView;
import com.riotfallen.moviedirectory.fragment.SimilarFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailMovieActivity extends AppCompatActivity implements MovieView, FavoriteView, VideoView {

    public static final String TITLE_MOVIE = "titleMovie";
    public static final String ID_MOVIE = "idMovie";

    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;
    private ImageView favoriteButton;
    private ImageView playButton;

    private Boolean isFavorite;

    Integer movieId;

    Movie movie;

    private FavoritePresenter favoritePresenter;

    public DetailMovieActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        String movieTitle = getIntent().getStringExtra(TITLE_MOVIE);
        movieId = getIntent().getIntExtra(ID_MOVIE, 0);

        progressBar = findViewById(R.id.detailActivityProgressBar);
        nestedScrollView = findViewById(R.id.detailActivityNestedScrollView);
        ImageView backButton = findViewById(R.id.detailActivityBackButton);
        favoriteButton = findViewById(R.id.detailActivityFavoriteButton);
        TextView textViewToolbar = findViewById(R.id.detailActivityTextViewToolbar);
        playButton = findViewById(R.id.detailActivityPlayVideo);

        textViewToolbar.setText(movieTitle);
        MoviePresenter presenter = new MoviePresenter(this);
        presenter.getMovie(movieId);

        favoriteState();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavorite) {
                    if (favoritePresenter.addToFavorite(movie) && movie != null) {
                        isFavorite = true;
                        setFavorite();
                    } else {
                        Toast.makeText(DetailMovieActivity.this, getString(R.string.message_fail_favorite), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (favoritePresenter.removeFromFavorite(movieId)) {
                        isFavorite = false;
                        setFavorite();
                    } else {
                        Toast.makeText(DetailMovieActivity.this, getString(R.string.message_fail_add_favorite), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        SimilarFragment fragment = SimilarFragment.newInstance(movieId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.detailActivityFrameLayout, fragment);
        transaction.commit();
    }

    private void favoriteState() {
        favoritePresenter = new FavoritePresenter(this, this);
        isFavorite = favoritePresenter.isFavorite(movieId);
        setFavorite();
    }

    private void setFavorite() {
        if (isFavorite) {
            favoriteButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite));
        } else {
            favoriteButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border));
        }
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
    public void showMovies(MovieResponse data) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showMovie(Movie data) {
        movie = data;
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

        final ImageView imageToolbar = findViewById(R.id.detailActivityToolbarBackground);
        Picasso.get().load(BuildConfig.IMAGE_BASE_URL + data.getBackdropPath()).fit().centerCrop().into(imageToolbar);
        Picasso.get().load(BuildConfig.IMAGE_BASE_URL + data.getPosterPath()).fit().centerCrop().into(imageViewThumbnail);

        for (int i = 0; i < data.getSpokenLanguages().size(); i++) {
            if (i == 0)
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

        VideoPresenter videoPresenter = new VideoPresenter(this, this);
        videoPresenter.getVideo(movieId);
    }

    @Override
    public void onAdded(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleted(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFavoriteData(ArrayList<FavoriteMovie> data) {}

    @Override
    public void showVideoData(final Video video) {
        playButton.setVisibility(View.VISIBLE);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = BuildConfig.VIDEO_BASE_URL + video.getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    public void showVideoNotFound(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        playButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showVideoError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        playButton.setVisibility(View.INVISIBLE);
    }
}
