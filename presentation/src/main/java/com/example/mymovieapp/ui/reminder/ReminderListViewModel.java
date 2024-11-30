package com.example.mymovieapp.ui.reminder;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.domain.model.ReminderDto;
import com.example.domain.usecase.ReminderUseCase;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ReminderListViewModel extends ViewModel {

    private final ReminderUseCase mReminderUseCase;

    private final MutableLiveData<List<ReminderDto>> mRemindersLiveData = new MutableLiveData<>();

    @Inject
    public ReminderListViewModel(ReminderUseCase mReminderUseCase) {
        this.mReminderUseCase = mReminderUseCase;

        loadReminders();
    }

    public LiveData<List<ReminderDto>> getReminders() {
        return mRemindersLiveData;
    }

    public void saveReminder(int movieId, long timestamp) {
        mReminderUseCase.saveReminder(movieId, timestamp, this::loadReminders,
                throwable -> Log.e("ReminderViewModel", "saveReminder: ", throwable)
        );
    }

    public void deleteReminder(int movieId) {
        mReminderUseCase.deleteReminder(movieId, this::loadReminders,
                throwable -> Log.e("ReminderViewModel", "deleteReminder: ", throwable)
        );
    }

    public void loadReminders() {
        mReminderUseCase.getAllReminders(mRemindersLiveData::setValue, throwable -> {
            mRemindersLiveData.setValue(Collections.emptyList());
            Log.e("ReminderViewModel", "loadReminders: ", throwable);
        });
    }
}
