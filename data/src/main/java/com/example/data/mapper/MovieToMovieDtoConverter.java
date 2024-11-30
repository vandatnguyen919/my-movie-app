package com.example.data.mapper;

import androidx.annotation.NonNull;

import com.example.data.source.remote.model.Movie;
import com.example.domain.model.MovieDto;

import retrofit2.Converter;

public class MovieToMovieDtoConverter implements Converter<Movie, MovieDto> {

    @Override
    public MovieDto convert(@NonNull Movie movie) {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setOverview(movie.getOverview());
        movieDto.setPosterPath(movie.getPosterPath());
        movieDto.setBackdropPath(movie.getBackdropPath());
        movieDto.setReleaseDate(movie.getReleaseDate());
        movieDto.setVoteAverage(movie.getVoteAverage());
        movieDto.setFavorite(movie.isFavorite());
        return movieDto;
    }
}
