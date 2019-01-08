package com.riotfallen.moviedirectory.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riotfallen.moviedirectory.BuildConfig;
import com.riotfallen.moviedirectory.R;
import com.riotfallen.moviedirectory.core.model.movie.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    private Context context;
    private List<Result> movies;

    public MovieListAdapter(Context context, List<Result> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieListAdapter.MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MovieListViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_movie, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieListViewHolder movieListViewHolder, int i) {
        final int position = i;
        movieListViewHolder.BindItem(movies.get(i));
        movieListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, movies.get(position).getId().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewThumb = itemView.findViewById(R.id.rimImageViewThumbnail);
        private TextView textViewTitle = itemView.findViewById(R.id.rimTextViewTitle);
        private TextView textViewDesc = itemView.findViewById(R.id.rimTextViewDescription);

        MovieListViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void BindItem(Result movie){
            textViewTitle.setText(movie.getTitle());
            textViewDesc.setText(movie.getOverview());
            Picasso.get().load(BuildConfig.IMAGE_BASE_URL + movie.getPosterPath()).fit().centerCrop().into(imageViewThumb);
        }

    }
}
