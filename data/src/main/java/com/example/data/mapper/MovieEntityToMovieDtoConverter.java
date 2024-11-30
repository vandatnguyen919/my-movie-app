package com.example.data.mapper;

import androidx.annotation.NonNull;

import com.example.data.source.local.entity.MovieEntity;
import com.example.domain.model.MovieDto;

import retrofit2.Converter;

public class MovieEntityToMovieDtoConverter implements Converter<MovieEntity, MovieDto> {

    @Override
    public MovieDto convert(@NonNull MovieEntity movieEntity) {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movieEntity.getId());
        movieDto.setTitle(movieEntity.getTitle());
        movieDto.setOverview(movieEntity.getOverview());
        movieDto.setReleaseDate(movieEntity.getReleaseDate());
        movieDto.setVoteAverage(movieEntity.getVoteAverage());
        movieDto.setBackdropPath(movieEntity.getBackdropPath());
        movieDto.setPosterPath(movieEntity.getPosterPath());
        movieDto.setAdult(movieEntity.isAdult());
        return movieDto;
    }
}
