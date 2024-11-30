package com.example.domain.usecase;

import androidx.annotation.NonNull;

import com.example.domain.model.ReminderDto;
import com.example.domain.repository.ReminderRepository;
import com.example.domain.usecase.base.UseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReminderUseCase extends UseCase {

    private final ReminderRepository mReminderRepository;

    @Inject
    public ReminderUseCase(ReminderRepository mReminderRepository) {
        this.mReminderRepository = mReminderRepository;
    }

    public void saveReminder(int movieId, long timestamp,
                             @NonNull Action onComplete, @NonNull Consumer<? super Throwable> onError) {
        disposeLast();
        lastDisposable = mReminderRepository.addOrUpdateReminder(movieId, timestamp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete, onError);
        compositeDisposable.add(lastDisposable);
    }

    public void deleteReminder(int movieId,
                               @NonNull Action onComplete, @NonNull Consumer<? super Throwable> onError) {
        disposeLast();
        lastDisposable = mReminderRepository.deleteReminderById(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete, onError);
        compositeDisposable.add(lastDisposable);
    }

    public void getAllReminders(@NonNull Consumer<List<ReminderDto>> onSuccess, @NonNull Consumer<? super Throwable> onError) {
        disposeLast();
        lastDisposable = mReminderRepository.getAllReminders()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError);
        compositeDisposable.add(lastDisposable);
    }
}
