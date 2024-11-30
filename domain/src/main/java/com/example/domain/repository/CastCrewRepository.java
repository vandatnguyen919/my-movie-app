package com.example.domain.repository;

import com.example.domain.model.CastCrewDto;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface CastCrewRepository {

    Single<List<CastCrewDto>> getCastCrews(int movieId);
}
