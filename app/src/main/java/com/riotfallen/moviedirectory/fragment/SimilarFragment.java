package com.riotfallen.moviedirectory.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.riotfallen.moviedirectory.adapter.recyler.SimilarListAdapter;
import com.riotfallen.moviedirectory.core.model.similar.Similar;
import com.riotfallen.moviedirectory.core.presenter.SimilarPresenter;
import com.riotfallen.moviedirectory.core.view.SimilarView;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimilarFragment extends Fragment implements SimilarView {

    public static final String MOVIE_ID = "movieId";

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    public static SimilarFragment newInstance(int movieId){
        SimilarFragment fragment = new SimilarFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID, movieId);
        fragment.setArguments(bundle);

        return fragment;
    }

    public SimilarFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_similar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.similarFragmentProgressBar);
        recyclerView = view.findViewById(R.id.similarFragmentRecyclerView);

        int movieId = Objects.requireNonNull(getArguments()).getInt(MOVIE_ID);
        SimilarPresenter presenter = new SimilarPresenter(this);
        presenter.getSimilar(movieId, 1);
    }

    @Override
    public void showSimilarLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideSimilarLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSimilarError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSimilarNotFound() {
        Toast.makeText(getContext(), getString(R.string.message_similar_not_found), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSimilarData(List<Similar> data) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);
        SimilarListAdapter adapter = new SimilarListAdapter(getContext(), data);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
