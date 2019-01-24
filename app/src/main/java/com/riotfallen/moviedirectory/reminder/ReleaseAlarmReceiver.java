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
    public static final String EXTRA_TYPE = "type";

    public static String CHANNEL_ID = "channel_02";
    public static CharSequence CHANNEL_NAME = "Movie Notification Release";

    private Context context;

    private static int NOTIF_ID_RELEASE = 100;

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

        PendingIntent pendingIntent = getPendingIntent(context);

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

    public void setAlarm(Context context, String type, String time, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReleaseAlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_RELEASE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, ReleaseAlarmReceiver.class);
        return PendingIntent.getBroadcast(context, NOTIF_ID_RELEASE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
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
