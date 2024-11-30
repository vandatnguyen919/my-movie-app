package com.example.domain.usecase;

import androidx.annotation.NonNull;

import com.example.domain.model.UserProfileDto;
import com.example.domain.repository.UserProfileRepository;
import com.example.domain.usecase.base.UseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserProfileUseCase extends UseCase {

    private final UserProfileRepository mUserProfileRepository;

    @Inject
    public UserProfileUseCase(UserProfileRepository mUserProfileRepository) {
        this.mUserProfileRepository = mUserProfileRepository;
    }

    public void saveUserProfile(UserProfileDto userProfileDto, @NonNull Action onComplete, @NonNull Consumer<? super Throwable> onError) {
        disposeLast();
        lastDisposable = mUserProfileRepository.save(userProfileDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete, onError);
        compositeDisposable.add(lastDisposable);
    }

    public void getUserProfile(@NonNull Consumer<UserProfileDto> onSuccess, @NonNull Consumer<? super Throwable> onError) {
        disposeLast();
        lastDisposable =  mUserProfileRepository.get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError);
        compositeDisposable.add(lastDisposable);
    }
}
