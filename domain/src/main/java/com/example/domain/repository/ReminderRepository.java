package com.example.domain.repository;

import com.example.domain.model.ReminderDto;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface ReminderRepository {

    Completable addOrUpdateReminder(int movieId, long timestamp);

    Completable deleteReminderById(int movieId);

    Single<List<ReminderDto>> getAllReminders();
}
