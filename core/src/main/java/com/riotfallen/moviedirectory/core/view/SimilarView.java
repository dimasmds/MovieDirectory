package com.riotfallen.moviedirectory.core.view;

import com.riotfallen.moviedirectory.core.model.similar.Similar;

import java.util.List;

public interface SimilarView {
    void showSimilarLoading();
    void hideSimilarLoading();
    void showSimilarError(String message);
    void showSimilarNotFound();
    void showSimilarData(List<Similar> data);
}
