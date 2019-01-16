package com.riotfallen.moviedirectory.core.db.helper;

import android.net.Uri;
import android.provider.BaseColumns;

class DatabaseContract {

    static String TABLE_FAVORITE = "table_favorite";
    static String AUTHORITY = "com.riotfallen.moviedirectory.core";


    static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVORITE)
            .build();

    static final class MovieColumn implements BaseColumns {

        static String COLUMN_MOVIE_ID = "movieId";
        static String COLUMN_MOVIE_TITLE = "movieTitle";
        static String COLUMN_MOVIE_OVERVIEW = "movieOverview";
        static String COLUMN_MOVIE_RATING = "movieRating";
        static String COLUMN_MOVIE_VOTER = "movieVoter";
        static String COLUMN_MOVIE_BACKDROP = "movieBackdrop";
    }

}
