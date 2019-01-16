package com.riotfallen.moviedirectory.core.view;

import com.riotfallen.moviedirectory.core.db.model.FavoriteMovie;

import java.util.ArrayList;

public interface FavoriteView {
    void onAdded(String message);
    void onDeleted(String message);
    void showFavoriteData(ArrayList<FavoriteMovie> data);
}
