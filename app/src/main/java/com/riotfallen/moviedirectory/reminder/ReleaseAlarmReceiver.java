package com.riotfallen.moviedirectory.reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.riotfallen.moviedirectory.R;
import com.riotfallen.moviedirectory.activity.MainActivity;
import com.riotfallen.moviedirectory.core.model.movie.Movie;
import com.riotfallen.moviedirectory.core.model.movie.MovieResponse;
import com.riotfallen.moviedirectory.core.model.movie.Result;
import com.riotfallen.moviedirectory.core.presenter.MoviePresenter;
import com.riotfallen.moviedirectory.core.view.MovieView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReleaseAlarmReceiver extends BroadcastReceiver implements MovieView {

    public static final String TYPE_RELEASE = "ReleaseAlarm";
    public static final String EXTRA_MESSAGE = "message";

    public static String CHANNEL_ID = "channel_02";
    public static CharSequence CHANNEL_NAME = "Movie Notification Release";

    private Context context;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public ReleaseAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        MoviePresenter presenter = new MoviePresenter(this);
        presenter.getUpcomingMovies(1);
    }

    private void showAlarmNotification(Context context, int id, String title) {
        NotificationManager notificationManager;

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification_white_48px)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification_white_48px))
                .setContentTitle("New Movie Release !")
                .setContentText(title + " Now Release! Let's find out")
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(id, builder.build());
        }
    }

    public void setAlarm(Context context, String message) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseAlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent(context, ReleaseAlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmUp)
        {
            Toast.makeText(context, "Release Notification is active", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Release Notification is deactivated", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAlarm(Context context) {
        if (alarmManager!= null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(context, "Release Notification is deactivated", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMovieLoading() {}

    @Override
    public void hideMovieLoading() {}

    @Override
    public void showMovieError(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMovies(MovieResponse data) {
        List<Result> movies = data.getResults();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String now = dateFormat.format(date);
        for (Result movie: movies){
            if(movie.getReleaseDate().equals(now)){
                showAlarmNotification(context, movie.getId(), movie.getTitle());
            }
        }
    }

    @Override
    public void showMovie(Movie data) {

    }
}
