package com.example.domain.usecase;

import com.example.domain.model.MovieDto;
import com.example.domain.repository.MovieRepository;
import com.example.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetFavoriteMoviesUseCase extends SingleUseCase<List<MovieDto>> {

    private final MovieRepository mMovieRepository;

    @Inject
    public GetFavoriteMoviesUseCase(MovieRepository mMovieRepository) {
        this.mMovieRepository = mMovieRepository;
    }

    @Override
    protected Single<List<MovieDto>> buildUseCaseSingle() {
        return mMovieRepository.getAllFavoriteMovies();
    }
}
