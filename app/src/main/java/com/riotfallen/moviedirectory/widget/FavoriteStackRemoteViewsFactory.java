package com.riotfallen.moviedirectory.widget;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riotfallen.moviedirectory.BuildConfig;
import com.riotfallen.moviedirectory.R;
import com.riotfallen.moviedirectory.core.db.model.FavoriteMovie;

import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;
import static com.riotfallen.moviedirectory.core.db.helper.DatabaseContract.CONTENT_URI;

public class FavoriteStackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {


    private Context context;
    private Cursor cursor;

    FavoriteStackRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                cursor == null || !cursor.moveToPosition(position)) {
            return null;
        }

        FavoriteMovie movie = getItem(position);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item_movie);

        try {
            Bitmap preview = Glide.with(context)
                    .asBitmap()
                    .load(BuildConfig.IMAGE_BASE_URL + movie.getMovieBackdrop())
                    .apply(new RequestOptions().centerCrop())
                    .submit()
                    .get();
            remoteViews.setImageViewBitmap(R.id.wimImageViewPoster , preview);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        remoteViews.setTextViewText(R.id.wimTextViewTitle, movie.getMovieTitle());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return cursor.moveToPosition(position) ? cursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    private FavoriteMovie getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid!");
        }

        return new FavoriteMovie(cursor);
    }
}
