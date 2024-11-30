package com.example.domain.repository;

import androidx.paging.PagingData;

import com.example.domain.model.MovieDto;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MovieRepository {

    Flowable<PagingData<MovieDto>> getMovies();

    Completable addMovie(MovieDto movieDto);

    Single<List<MovieDto>> getAllFavoriteMovies();

    Completable deleteMovieById(int movieId);

    Single<Boolean> isFavoriteMovie(int movieId);
}
