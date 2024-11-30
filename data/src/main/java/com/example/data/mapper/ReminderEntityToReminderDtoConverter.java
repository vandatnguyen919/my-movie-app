package com.example.data.mapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.data.source.local.entity.ReminderEntity;
import com.example.domain.model.ReminderDto;

import retrofit2.Converter;

public class ReminderEntityToReminderDtoConverter implements Converter<ReminderEntity, ReminderDto> {

    @Nullable
    @Override
    public ReminderDto convert(@NonNull ReminderEntity reminderEntity) {
        ReminderDto reminderDto = new ReminderDto();
        reminderDto.setMovieId(reminderEntity.getMovieId());
        reminderDto.setTimestamp(reminderEntity.getTimestamp());
        return reminderDto;
    }
}
