package com.example.mymovieapp.workers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.domain.model.MovieDto;
import com.example.mymovieapp.R;
import com.example.mymovieapp.di.MyApplication;
import com.example.mymovieapp.ui.MainActivity;
import com.example.mymovieapp.ui.reminder.ReminderListViewModel;

import java.util.Locale;

import javax.inject.Inject;

public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "1001";

    @Inject
    public ReminderListViewModel mReminderListViewModel;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        ((MyApplication) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        int movieId = getInputData().getInt("movieId", -1);
        String movieTitle = getInputData().getString("movieTitle");
        String movieReleaseYear = getInputData().getString("movieReleaseYear");
        double movieVoteAverage = getInputData().getDouble("movieVoteAverage", -1);
        long timestamp = getInputData().getLong("timestamp", -1);
        if (movieId == -1 || timestamp == -1) {
            return Result.failure();
        }
        mReminderListViewModel.deleteReminder(movieId);
        pushNotification(movieId, movieTitle, movieReleaseYear, movieVoteAverage, getApplicationContext());
        return Result.success();
    }

    private void pushNotification(int movieId,
                                  String movieTitle,
                                  String movieReleaseYear,
                                  double movieVoteAverage,
                                  Context context) {

        Intent intent = new Intent(context,  MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Foreground Service channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(movieTitle)
                .setContentText(String.format(Locale.getDefault(), "Year: %s - Rate: %.1f/10\nIt's time to watch this movie!", movieReleaseYear, movieVoteAverage))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Push Notification
        notificationManager.notify(movieId, notification);
    }
}
