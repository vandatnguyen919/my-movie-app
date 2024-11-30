package com.example.domain.usecase;

import androidx.annotation.NonNull;

import com.example.domain.model.MovieDto;
import com.example.domain.repository.MovieRepository;
import com.example.domain.usecase.base.UseCase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteMovieUseCase extends UseCase {

    private final MovieRepository mMovieRepository;

    public FavoriteMovieUseCase(MovieRepository mMovieRepository) {
        this.mMovieRepository = mMovieRepository;
    }

    public void addToFavorite(MovieDto movieDto, @NonNull Action onComplete, @NonNull Consumer<? super Throwable> onError) {
        disposeLast();
        lastDisposable = mMovieRepository.addMovie(movieDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete, onError);
        compositeDisposable.add(lastDisposable);
    }

    public void deleteFromFavorite(int movieId, @NonNull Action onComplete, @NonNull Consumer<? super Throwable> onError) {
        disposeLast();
        lastDisposable = mMovieRepository.deleteMovieById(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete, onError);
        compositeDisposable.add(lastDisposable);
    }

    public void isFavorite(int movieId, Consumer<Boolean> onSuccess, @NonNull Consumer<? super Throwable> onError) {
        disposeLast();
        lastDisposable = mMovieRepository.isFavoriteMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError);
        compositeDisposable.add(lastDisposable);
    }
}
