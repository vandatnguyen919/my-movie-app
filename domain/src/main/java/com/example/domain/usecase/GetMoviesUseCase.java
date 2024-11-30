package com.example.domain.usecase;

import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.domain.model.MovieDto;
import com.example.domain.repository.MovieRepository;
import com.example.domain.usecase.base.FlowableUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class GetMoviesUseCase extends FlowableUseCase<PagingData<MovieDto>> {

    private final MovieRepository mMovieRepository;

    private CoroutineScope mCoroutineScope;

    @Inject
    public GetMoviesUseCase(MovieRepository mMovieRepository) {
        this.mMovieRepository = mMovieRepository;
    }

    @Override
    protected Flowable<PagingData<MovieDto>> buildUseCaseFlowable() {
        return PagingRx.cachedIn(mMovieRepository.getMovies(), mCoroutineScope);
    }

    public void setCoroutineScope(CoroutineScope mCoroutineScope) {
        this.mCoroutineScope = mCoroutineScope;
    }
}
