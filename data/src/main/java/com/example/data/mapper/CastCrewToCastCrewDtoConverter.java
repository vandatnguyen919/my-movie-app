package com.example.data.mapper;

import androidx.annotation.NonNull;

import com.example.data.source.remote.model.CastCrew;
import com.example.domain.model.CastCrewDto;

import retrofit2.Converter;

public class CastCrewToCastCrewDtoConverter implements Converter<CastCrew, CastCrewDto> {

    @Override
    public CastCrewDto convert(@NonNull CastCrew castCrew) {
        CastCrewDto castCrewDto = new CastCrewDto();
        castCrewDto.setId(castCrew.getId());
        castCrewDto.setName(castCrew.getName());
        castCrewDto.setProfilePath(castCrew.getProfilePath());
        return castCrewDto;
    }
}
