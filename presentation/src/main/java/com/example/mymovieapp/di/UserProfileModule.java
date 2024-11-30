package com.example.mymovieapp.di;

import android.content.Context;

import com.example.data.repository.UserProfileRepositoryImpl;
import com.example.domain.repository.UserProfileRepository;
import com.example.domain.usecase.UserProfileUseCase;
import com.example.mymovieapp.ui.profile.UserProfileViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserProfileModule {

    @Provides
    @Singleton
    public DatabaseReference provideDatabaseReference() {
        return FirebaseDatabase
                .getInstance("https://my-movie-app-eb5cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");
    }

    @Provides
    @Singleton
    public UserProfileRepository provideUserProfileRepository(Context context, DatabaseReference databaseReference) {
        return new UserProfileRepositoryImpl(context, databaseReference);
    }

    @Provides
    @Singleton
    public UserProfileUseCase provideUserProfileUseCase(UserProfileRepository userProfileRepository) {
        return new UserProfileUseCase(userProfileRepository);
    }

    @Provides
    @Singleton
    public UserProfileViewModel provideUserProfileViewModel(DatabaseReference databaseReference, UserProfileUseCase userProfileUseCase) {
        return new UserProfileViewModel(databaseReference, userProfileUseCase);
    }
}
