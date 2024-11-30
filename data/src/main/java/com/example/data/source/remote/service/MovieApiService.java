package com.example.data.source.remote.service;

import com.example.data.source.remote.model.CastCrewResponse;
import com.example.data.source.remote.model.Movie;
import com.example.data.source.remote.model.MovieResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {

    @GET("movie/{category}?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Single<MovieResponse> fetchMoviesRxSingle(@Path("category") String category, @Query("page") int page);

    @GET("movie/{movieId}?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Single<Movie> getMovieDetails(@Path("movieId") int movieId);

    @GET("movie/{movieId}/credits?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Single<CastCrewResponse> getMovieCastCrew(@Path("movieId") int movieId);
}
