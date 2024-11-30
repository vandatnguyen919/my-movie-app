package com.example.data.source.remote.paging;

import static com.example.domain.utils.Constants.CATEGORY;
import static com.example.domain.utils.Constants.DEFAULT_CATEGORY;
import static com.example.domain.utils.Constants.DEFAULT_RATE;
import static com.example.domain.utils.Constants.DEFAULT_RELEASE_YEAR;
import static com.example.domain.utils.Constants.DEFAULT_SORT_BY;
import static com.example.domain.utils.Constants.RATE;
import static com.example.domain.utils.Constants.RELEASE_YEAR;
import static com.example.domain.utils.Constants.SORT_BY;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;
import androidx.preference.PreferenceManager;

import com.example.data.source.local.dao.MovieDao;
import com.example.data.source.local.entity.MovieEntity;
import com.example.data.source.remote.model.Movie;
import com.example.data.source.remote.model.MovieResponse;
import com.example.data.source.remote.service.MovieApiService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MoviePagingSource extends RxPagingSource<Integer, Movie> {

    private final MovieApiService movieApiService;
    private final MovieDao movieDao;

    private final SharedPreferences mSharedPreferences;

    @Inject
    public MoviePagingSource(Context context, MovieApiService movieApiService, MovieDao movieDao) {
        this.movieApiService = movieApiService;
        this.movieDao = movieDao;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        String category = mSharedPreferences.getString(CATEGORY, DEFAULT_CATEGORY);
        int rate = Integer.parseInt(mSharedPreferences.getString(RATE, DEFAULT_RATE));
        int releaseYear = Integer.parseInt(mSharedPreferences.getString(RELEASE_YEAR, DEFAULT_RELEASE_YEAR));
        String sortBy = mSharedPreferences.getString(SORT_BY, DEFAULT_SORT_BY);

        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;

        // Fetch movies from API
        Single<MovieResponse> movieSingle = movieApiService.fetchMoviesRxSingle(category, page)
                .subscribeOn(Schedulers.io());

        // Get all favorite movie IDs from Room database
        Single<Set<Integer>> favoriteMovieIdsSingle = movieDao.getAllMovies()
                .subscribeOn(Schedulers.io())
                .map(movies -> movies.stream()
                        .map(MovieEntity::getId)
                        .collect(Collectors.toSet()));

        return Single.zip(movieSingle, favoriteMovieIdsSingle, (movieResponse, favoriteMovieIds) -> {
                    int totalPages = movieResponse.getTotalPages();
                    List<Movie> filteredMovies = filterMovies(movieResponse.getResults(), rate, releaseYear);

                    if ("rating".equals(sortBy)) {
                        filteredMovies.sort(Comparator.comparingDouble(Movie::getVoteAverage).reversed());
                    } else {
                        filteredMovies.sort(Comparator.comparing(Movie::getReleaseDate).reversed());
                    }

                    // Mark movies as favorite if they are in the favorite list
                    for (Movie movie : filteredMovies) {
                        movie.setFavorite(favoriteMovieIds.contains(movie.getId()));
                    }
                    return toLoadResult(filteredMovies, page, totalPages);
                })
                .onErrorReturn(LoadResult.Error::new);
    }

    private LoadResult<Integer, Movie> toLoadResult(List<Movie> movies, Integer page, int totalPages) {
        return new LoadResult.Page<>(
                movies,
                page == 1 ? null : page - 1,
                page < totalPages ? page + 1 : null
        );
    }

    private List<Movie> filterMovies(List<Movie> movies, int rate, int releaseYear) {
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getVoteAverage() >= rate && movie.getReleaseYear() >= releaseYear) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }
}
