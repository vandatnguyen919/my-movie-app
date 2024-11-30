package com.example.data.mapper;

import androidx.annotation.NonNull;

import com.example.data.source.local.entity.MovieEntity;
import com.example.domain.model.MovieDto;

import retrofit2.Converter;

public class MovieDtoToMovieEntityConverter implements Converter<MovieDto, MovieEntity> {

    @Override
    public MovieEntity convert(@NonNull MovieDto movieDto) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setId(movieDto.getId());
        movieEntity.setTitle(movieDto.getTitle());
        movieEntity.setPosterPath(movieDto.getPosterPath());
        movieEntity.setBackdropPath(movieDto.getBackdropPath());
        movieEntity.setOverview(movieDto.getOverview());
        movieEntity.setReleaseDate(movieDto.getReleaseDate());
        movieEntity.setVoteAverage(movieDto.getVoteAverage());
        movieEntity.setAdult(movieDto.isAdult());
        return movieEntity;
    }
}
