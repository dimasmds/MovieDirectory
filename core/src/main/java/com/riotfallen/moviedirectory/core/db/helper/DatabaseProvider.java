package com.riotfallen.moviedirectory.core.db.helper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.Objects;

import static com.riotfallen.moviedirectory.core.db.helper.DatabaseContract.AUTHORITY;
import static com.riotfallen.moviedirectory.core.db.helper.DatabaseContract.CONTENT_URI;

public class DatabaseProvider extends ContentProvider {

    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;

    private static final UriMatcher uriMacther = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMacther.addURI(AUTHORITY, DatabaseContract.TABLE_FAVORITE, MOVIE);

        uriMacther.addURI(AUTHORITY, DatabaseContract.TABLE_FAVORITE + "/#", MOVIE_ID);
    }

    private MovieHelper movieHelper;

    @Override
    public boolean onCreate() {
        movieHelper = new MovieHelper(getContext());
        movieHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;
        switch (uriMacther.match(uri)) {
            case MOVIE:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        }

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        long added;

        switch (uriMacther.match(uri)) {
            case MOVIE:
                added = movieHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        int delete;
        switch (uriMacther.match(uri)) {
            case MOVIE_ID:
                delete = movieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                delete = 0;
                break;
        }

        if(delete > 0){
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return delete;
    }
}
