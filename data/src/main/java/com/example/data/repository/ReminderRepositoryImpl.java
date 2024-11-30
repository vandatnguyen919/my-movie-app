package com.example.data.repository;

import com.example.data.mapper.MovieToMovieDtoConverter;
import com.example.data.mapper.ReminderEntityToReminderDtoConverter;
import com.example.data.source.local.dao.ReminderDao;
import com.example.data.source.local.entity.ReminderEntity;
import com.example.data.source.remote.service.MovieApiService;
import com.example.domain.model.ReminderDto;
import com.example.domain.repository.ReminderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class ReminderRepositoryImpl implements ReminderRepository {

    private final MovieApiService movieApiService;
    private final ReminderDao reminderDao;
    private final ReminderEntityToReminderDtoConverter reminderEntityToReminderDtoConverter;
    private final MovieToMovieDtoConverter movieToMovieDtoConverter;

    @Inject
    public ReminderRepositoryImpl(MovieApiService movieApiService, ReminderDao reminderDao, ReminderEntityToReminderDtoConverter reminderEntityToReminderDtoConverter, MovieToMovieDtoConverter movieToMovieDtoConverter) {
        this.movieApiService = movieApiService;
        this.reminderDao = reminderDao;
        this.reminderEntityToReminderDtoConverter = reminderEntityToReminderDtoConverter;
        this.movieToMovieDtoConverter = movieToMovieDtoConverter;
    }

    @Override
    public Completable addOrUpdateReminder(int movieId, long timestamp) {
        return reminderDao.insertReminder(new ReminderEntity(movieId, timestamp));
    }

    @Override
    public Completable deleteReminderById(int movieId) {
        return reminderDao.deleteReminderById(movieId);
    }

    @Override
    public Single<List<ReminderDto>> getAllReminders() {
        return reminderDao.getAllReminders()
                .flatMap(reminderEntities -> {
                    // Convert each ReminderEntity to ReminderDto with API calls
                    List<Single<ReminderDto>> reminderDtoSingles = reminderEntities.stream()
                            .map(reminderEntity ->
                                    movieApiService.getMovieDetails(reminderEntity.getMovieId())
                                            .map(movie -> {
                                                ReminderDto reminderDto = reminderEntityToReminderDtoConverter.convert(reminderEntity);
                                                reminderDto.setMovieDto(movieToMovieDtoConverter.convert(movie)); // Set the movie details
                                                return reminderDto;
                                            })
                            )
                            .collect(Collectors.toList());

                    // Combine all Single<ReminderDto> into a Single<List<ReminderDto>>
                    return Single.zip(reminderDtoSingles, results -> {
                        List<ReminderDto> reminderDtos = new ArrayList<>();
                        for (Object result : results) {
                            reminderDtos.add((ReminderDto) result);
                        }
                        return reminderDtos;
                    });
                });
    }

}
