package com.example.mymovieapp.di;

import android.app.Application;

import com.example.mymovieapp.ui.favorites.FavoriteMovieListFragment;
import com.example.mymovieapp.ui.main.MainFragment;
import com.example.mymovieapp.ui.movies.MovieContainerFragment;
import com.example.mymovieapp.ui.movies.details.MovieDetailsFragment;
import com.example.mymovieapp.ui.movies.list.MovieListFragment;
import com.example.mymovieapp.ui.profile.UserProfileFragment;
import com.example.mymovieapp.ui.reminder.ReminderListFragment;
import com.example.mymovieapp.workers.NotificationWorker;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class, NetworkModule.class, MovieModule.class, UserProfileModule.class, ReminderModule.class})
public interface AppComponent {

    // Factory to pass the Application instance
    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application);
    }

    void inject(MainFragment mainFragment);

    void inject(MovieContainerFragment movieContainerFragment);

    void inject(MovieListFragment movieListFragment);

    void inject(MovieDetailsFragment movieDetailsFragment);

    void inject(FavoriteMovieListFragment favouriteMovieListFragment);

    void inject(UserProfileFragment userProfileFragment);

    void inject(ReminderListFragment reminderListFragment);

    void inject(NotificationWorker notificationWorker);
}
