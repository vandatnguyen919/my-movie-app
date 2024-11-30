package com.example.domain.usecase;

import com.example.domain.model.CastCrewDto;
import com.example.domain.repository.CastCrewRepository;
import com.example.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetCastCrewsUseCase extends SingleUseCase<List<CastCrewDto>> {

    private int movieId;
    private final CastCrewRepository castCrewRepository;

    @Inject
    public GetCastCrewsUseCase(CastCrewRepository castCrewRepository) {
        this.castCrewRepository = castCrewRepository;
    }

    @Override
    protected Single<List<CastCrewDto>> buildUseCaseSingle() {
        return castCrewRepository.getCastCrews(movieId);
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
