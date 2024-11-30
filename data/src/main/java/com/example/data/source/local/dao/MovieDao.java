package com.example.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.data.source.local.entity.MovieEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MovieDao {

    @Insert
    Completable insert(MovieEntity movieEntity);

    @Query("SELECT * FROM fav_movie")
    Single<List<MovieEntity>> getAllMovies();

    @Query("DELETE FROM fav_movie WHERE id = :movieId")
    Completable deleteMovieById(int movieId);

    @Query("SELECT EXISTS(SELECT 1 FROM fav_movie WHERE id = :movieId)")
    Single<Boolean> isMovieExists(int movieId);
}
