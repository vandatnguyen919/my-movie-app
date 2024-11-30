package com.example.data.repository;

import android.content.Context;
import android.provider.Settings;

import com.example.domain.model.UserProfileDto;
import com.example.domain.repository.UserProfileRepository;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class UserProfileRepositoryImpl implements UserProfileRepository {

    private final DatabaseReference databaseReference;
    private final String deviceId;

    @Inject
    public UserProfileRepositoryImpl(Context context, DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    public Completable save(UserProfileDto userProfileDto) {
        return Completable.create(emitter -> {
            userProfileDto.setId(deviceId);
            databaseReference.child(deviceId).setValue(userProfileDto)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onComplete();
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    @Override
    public Single<UserProfileDto> get() {
        return Single.create(emitter -> databaseReference.child(deviceId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UserProfileDto userProfileDto = task.getResult().getValue(UserProfileDto.class);
                        emitter.onSuccess(userProfileDto != null ? userProfileDto : new UserProfileDto());
                    } else {
                        emitter.onError(task.getException());
                    }
                }));
    }
}
