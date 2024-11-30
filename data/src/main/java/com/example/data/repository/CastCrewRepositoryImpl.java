package com.example.data.repository;

import com.example.data.mapper.CastCrewToCastCrewDtoConverter;
import com.example.data.source.remote.model.CastCrew;
import com.example.data.source.remote.service.MovieApiService;
import com.example.domain.model.CastCrewDto;
import com.example.domain.repository.CastCrewRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class CastCrewRepositoryImpl implements CastCrewRepository {

    private final MovieApiService movieApiService;

    private final CastCrewToCastCrewDtoConverter castCrewToCastCrewDtoConverter;

    @Inject
    public CastCrewRepositoryImpl(MovieApiService movieApiService, CastCrewToCastCrewDtoConverter castCrewToCastCrewDtoConverter) {
        this.movieApiService = movieApiService;
        this.castCrewToCastCrewDtoConverter = castCrewToCastCrewDtoConverter;
    }

    @Override
    public Single<List<CastCrewDto>> getCastCrews(int movieId) {
        return movieApiService.getMovieCastCrew(movieId)
                .map(castCrewResponse -> {
                    List<CastCrew> casts = castCrewResponse.getCasts();
                    List<CastCrew> crews = castCrewResponse.getCrews();
                    casts.addAll(crews);
                    return casts.stream().map(castCrewToCastCrewDtoConverter::convert).collect(Collectors.toList());
                });
    }
}
