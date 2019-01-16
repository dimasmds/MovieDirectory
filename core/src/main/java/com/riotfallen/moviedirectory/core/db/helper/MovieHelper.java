package com.riotfallen.moviedirectory.core.db.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.riotfallen.moviedirectory.core.db.model.FavoriteMovie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.riotfallen.moviedirectory.core.db.helper.DatabaseContract.MovieColumn.COLUMN_MOVIE_BACKDROP;
import static com.riotfallen.moviedirectory.core.db.helper.DatabaseContract.MovieColumn.COLUMN_MOVIE_ID;
import static com.riotfallen.moviedirectory.core.db.helper.DatabaseContract.MovieColumn.COLUMN_MOVIE_OVERVIEW;
import static com.riotfallen.moviedirectory.core.db.helper.DatabaseContract.MovieColumn.COLUMN_MOVIE_RATING;
import static com.riotfallen.moviedirectory.core.db.helper.DatabaseContract.MovieColumn.COLUMN_MOVIE_TITLE;
import static com.riotfallen.moviedirectory.core.db.helper.DatabaseContract.MovieColumn.COLUMN_MOVIE_VOTER;
import static com.riotfallen.moviedirectory.core.db.helper.DatabaseContract.TABLE_FAVORITE;

public class MovieHelper {

    private static String DATABASE_TABLE = TABLE_FAVORITE;
    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public MovieHelper(Context context){
        this.context = context;
    }

    public void open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();
    }

    public ArrayList<FavoriteMovie> query(){
        ArrayList<FavoriteMovie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null,
                null, null, null, _ID + " DESC", null);
        cursor.moveToFirst();
        FavoriteMovie favoriteMovie;
        if(cursor.getCount() > 0){
            do{
                favoriteMovie = new FavoriteMovie();
                favoriteMovie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                favoriteMovie.setMovieId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID)));
                favoriteMovie.setMovieTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_TITLE)));
                favoriteMovie.setMovieOverview(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_OVERVIEW)));
                favoriteMovie.setMovieRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_RATING)));
                favoriteMovie.setMovieVoter(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_VOTER)));
                favoriteMovie.setMovieBackdrop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_BACKDROP)));

                arrayList.add(favoriteMovie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return arrayList;
    }

    public Cursor queryById(int movieId){
        return  database.rawQuery("SELECT * FROM " + TABLE_FAVORITE + " WHERE "
                + COLUMN_MOVIE_ID + " = " + movieId, null);
    }

    public long insert(FavoriteMovie favoriteMovie){
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_MOVIE_ID, favoriteMovie.getMovieId());
        initialValues.put(COLUMN_MOVIE_BACKDROP, favoriteMovie.getMovieBackdrop());
        initialValues.put(COLUMN_MOVIE_OVERVIEW, favoriteMovie.getMovieOverview());
        initialValues.put(COLUMN_MOVIE_RATING, favoriteMovie.getMovieRating());
        initialValues.put(COLUMN_MOVIE_TITLE, favoriteMovie.getMovieTitle());
        initialValues.put(COLUMN_MOVIE_VOTER, favoriteMovie.getMovieVoter());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }


    public int delete(int movieId){
        return database.delete(TABLE_FAVORITE, COLUMN_MOVIE_ID + " = '" + movieId + "'", null);
    }


    Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE, null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    Cursor queryProvider(){
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC");
    }

    long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    int deleteProvider(String id){
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
