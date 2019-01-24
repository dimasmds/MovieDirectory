package com.riotfallen.moviedirectory.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.riotfallen.moviedirectory.BuildConfig;
import com.riotfallen.moviedirectory.R;
import com.riotfallen.moviedirectory.adapter.recyler.SimilarListAdapter;
import com.riotfallen.moviedirectory.core.db.model.FavoriteMovie;
import com.riotfallen.moviedirectory.core.model.movie.Movie;
import com.riotfallen.moviedirectory.core.model.movie.MovieResponse;
import com.riotfallen.moviedirectory.core.model.similar.Similar;
import com.riotfallen.moviedirectory.core.model.video.Video;
import com.riotfallen.moviedirectory.core.presenter.FavoritePresenter;
import com.riotfallen.moviedirectory.core.presenter.MoviePresenter;
import com.riotfallen.moviedirectory.core.presenter.SimilarPresenter;
import com.riotfallen.moviedirectory.core.presenter.VideoPresenter;
import com.riotfallen.moviedirectory.core.utils.DateUtils;
import com.riotfallen.moviedirectory.core.view.FavoriteView;
import com.riotfallen.moviedirectory.core.view.MovieView;
import com.riotfallen.moviedirectory.core.view.SimilarView;
import com.riotfallen.moviedirectory.core.view.VideoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailMovieActivity extends AppCompatActivity implements
        MovieView, FavoriteView, VideoView, SimilarView {

    public static final String TITLE_MOVIE = "titleMovie";
    public static final String ID_MOVIE = "idMovie";
    public static final String MOVIE_STATE = "movieState";
    public static final String VIDEO_STATE = "videoState";
    public static final String SIMILAR_STATE = "similarState";

    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;
    private ImageView favoriteButton;
    private ImageView playButton;
    private ProgressBar progressBarFooter;
    private RecyclerView recyclerView;

    private Boolean isFavorite;

    private Integer movieId;

    private Movie movie;
    private Video video;
    private List<Similar> similars;


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
        progressBarFooter = findViewById(R.id.detailMovieActivityProgressDialogFooter);
        recyclerView = findViewById(R.id.detailMovieActivityRecyclerViewFooter);

        textViewToolbar.setText(movieTitle);

        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(MOVIE_STATE);
            assert movie != null;
            loadMovieData(movie);
            if(savedInstanceState.getParcelable(VIDEO_STATE) != null){
                video = savedInstanceState.getParcelable(VIDEO_STATE);
                loadVideoData(video);
            }

            if(savedInstanceState.getParcelableArrayList(SIMILAR_STATE) != null){
                similars = savedInstanceState.getParcelableArrayList(SIMILAR_STATE);
                loadSimilarData(similars);
                progressBarFooter.setVisibility(View.INVISIBLE);
            }

        } else {
            MoviePresenter presenter = new MoviePresenter(this);
            presenter.getMovie(movieId);
            VideoPresenter videoPresenter = new VideoPresenter(this, this);
            videoPresenter.getVideo(movieId);
            SimilarPresenter similarPresenter = new SimilarPresenter(this);
            similarPresenter.getSimilar(movieId, 1);
        }

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
        loadMovieData(movie);
    }

    private void loadMovieData(Movie data) {
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
        textViewYearMovie.setText(new DateUtils().dateToString(data.getReleaseDate(),
                "yyyy-mm-dd", "dd-mm-yyyy"));
        if (data.getProductionCompanies().size() != 0) {
            textViewProduction.setText(data.getProductionCompanies().get(0).getName());
        } else {
            textViewProduction.setText(getString(R.string.unknown));
        }
        textViewRating.setText(String.format(Locale.getDefault(), "%.1f", data.getVoteAverage()));
        textViewDescription.setText(data.getOverview());
        textViewDuration.setText(String.format(Locale.getDefault(), getString(R.string.d_minutes), data.getRuntime()));

        final ImageView imageToolbar = findViewById(R.id.detailActivityToolbarBackground);
        Picasso.get().load(BuildConfig.IMAGE_BASE_URL + data.getBackdropPath()).fit().centerCrop().into(imageToolbar);
        Picasso.get().load(BuildConfig.IMAGE_BASE_URL + data.getPosterPath()).fit().centerCrop().into(imageViewThumbnail);

        for (int i = 0; i < data.getSpokenLanguages().size(); i++) {
            if (i == 0)
                textViewLanguage.setText(data.getSpokenLanguages().get(i).getName());
            else
                textViewLanguage.setText(String.format("%s, %s", textViewLanguage.getText(), data.getSpokenLanguages().get(i).getName()));
        }

        for (int i = 0; i < data.getGenres().size(); i++) {
            TextView category = new TextView(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            params.setMarginEnd(10);

            category.setLayoutParams(params);
            category.setTextColor(getResources().getColor(android.R.color.white));
            category.setLines(1);
            category.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            category.setPadding(10, 0, 10, 0);
            category.setText(data.getGenres().get(i).getName());
            linearLayoutCategory.addView(category);
        }
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
    public void showFavoriteData(ArrayList<FavoriteMovie> data) { }

    @Override
    public void showVideoData(Video video) {
        this.video = video;
        loadVideoData(this.video);
    }

    private void loadVideoData(final Video video){
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

    @Override
    public void showSimilarLoading() {
        progressBarFooter.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideSimilarLoading() {
        progressBarFooter.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSimilarError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSimilarNotFound() {
        Toast.makeText(this, getString(R.string.message_similar_not_found), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSimilarData(List<Similar> data) {
        similars = data;
        loadSimilarData(similars);

    }

    private void loadSimilarData(List<Similar> data){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        SimilarListAdapter adapter = new SimilarListAdapter(this, data);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_STATE, movie);
        outState.putParcelable(VIDEO_STATE, video);
        outState.putParcelableArrayList(SIMILAR_STATE, (ArrayList<? extends Parcelable>) similars);
    }
}
