package com.example.mymovieapp.di;

import com.example.data.mapper.MovieToMovieDtoConverter;
import com.example.data.mapper.ReminderEntityToReminderDtoConverter;
import com.example.data.repository.ReminderRepositoryImpl;
import com.example.data.source.local.dao.ReminderDao;
import com.example.data.source.remote.service.MovieApiService;
import com.example.domain.repository.ReminderRepository;
import com.example.domain.usecase.ReminderUseCase;
import com.example.mymovieapp.ui.reminder.ReminderListViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ReminderModule {

    @Provides
    @Singleton
    public ReminderEntityToReminderDtoConverter provideReminderEntityToReminderDtoConverter() {
        return new ReminderEntityToReminderDtoConverter();
    }

    @Provides
    @Singleton
    public ReminderRepository provideReminderRepository(MovieApiService movieApiService, ReminderDao reminderDao,
                                                        ReminderEntityToReminderDtoConverter reminderEntityToReminderDtoConverter,
                                                        MovieToMovieDtoConverter movieToMovieDtoConverter) {
        return new ReminderRepositoryImpl(movieApiService, reminderDao, reminderEntityToReminderDtoConverter, movieToMovieDtoConverter);
    }

    @Provides
    @Singleton
    public ReminderUseCase provideReminderUseCase(ReminderRepository reminderRepository) {
        return new ReminderUseCase(reminderRepository);
    }

    @Provides
    @Singleton
    public ReminderListViewModel provideReminderViewModel(ReminderUseCase reminderUseCase) {
        return new ReminderListViewModel(reminderUseCase);
    }
}
