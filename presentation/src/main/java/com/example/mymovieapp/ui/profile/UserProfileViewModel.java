package com.example.mymovieapp.ui.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.domain.model.UserProfileDto;
import com.example.domain.usecase.UserProfileUseCase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

public class UserProfileViewModel extends ViewModel {

    private final DatabaseReference mDatabaseReference;
    private final UserProfileUseCase mUserProfileUseCase;

    private final MutableLiveData<UserProfileDto> mUserProfile = new MutableLiveData<>();

    @Inject
    public UserProfileViewModel(DatabaseReference mDatabaseReference, UserProfileUseCase mUserProfileUseCase) {
        this.mUserProfileUseCase = mUserProfileUseCase;
        loadUserProfile();
        this.mDatabaseReference = mDatabaseReference;
        this.mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadUserProfile();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("UserProfileViewModel", "onCancelled: " + error.getMessage());
            }
        });
    }

    public LiveData<UserProfileDto> getUserProfile() {
        return mUserProfile;
    }

    public void saveUserProfile(UserProfileDto userProfileDto) {
        mUserProfileUseCase.saveUserProfile(userProfileDto, this::loadUserProfile, throwable -> {
            Log.d("UserProfileViewModel", "saveUserProfile: " + throwable.getMessage());
        });
    }

    private void loadUserProfile() {
        mUserProfileUseCase.getUserProfile(mUserProfile::setValue, throwable -> {
            Log.d("UserProfileViewModel", "loadUserProfile: " + throwable.getMessage());
        });
    }
}
