package com.example.domain.repository;

import com.example.domain.model.UserProfileDto;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface UserProfileRepository {

    Completable save(UserProfileDto userProfileDto);

    Single<UserProfileDto> get();
}
