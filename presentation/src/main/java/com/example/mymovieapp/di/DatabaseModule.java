package com.example.mymovieapp.di;

import android.app.Application;

import androidx.room.Room;

import com.example.data.source.local.AppDatabase;
import com.example.data.source.local.dao.MovieDao;
import com.example.data.source.local.dao.ReminderDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Singleton
    @Provides
    public AppDatabase provideMovieDatabase(Application application) {
        return Room.databaseBuilder(application,
                        AppDatabase.class, "movie_database")
                .allowMainThreadQueries()
                .build();
    }

    @Singleton
    @Provides
    public MovieDao provideMovieDao(AppDatabase appDatabase) {
        return appDatabase.movieDao();
    }

    @Singleton
    @Provides
    public ReminderDao provideReminderDao(AppDatabase appDatabase) {
        return appDatabase.reminderDao();
    }
}
