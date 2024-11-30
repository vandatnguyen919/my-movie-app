package com.example.data.repository;

import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingDataTransforms;
import androidx.paging.rxjava3.PagingRx;

import com.example.data.mapper.MovieDtoToMovieEntityConverter;
import com.example.data.mapper.MovieEntityToMovieDtoConverter;
import com.example.data.mapper.MovieToMovieDtoConverter;
import com.example.data.source.local.dao.MovieDao;
import com.example.data.source.local.entity.MovieEntity;
import com.example.data.source.remote.model.Movie;
import com.example.data.source.remote.paging.MoviePagingSource;
import com.example.domain.model.MovieDto;
import com.example.domain.repository.MovieRepository;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Singleton
public class MovieRepositoryImpl implements MovieRepository {

    private final MoviePagingSource moviePagingSource;
    private final MovieDao movieDao;

    private final MovieToMovieDtoConverter movieToMovieDtoConverter;
    private final MovieDtoToMovieEntityConverter movieDtoToMovieEntityConverter;
    private final MovieEntityToMovieDtoConverter movieEntityToMovieDtoConverter;

    @Inject
    public MovieRepositoryImpl(MoviePagingSource moviePagingSource, MovieDao movieDao, MovieToMovieDtoConverter movieToMovieDtoConverter, MovieDtoToMovieEntityConverter movieDtoToMovieEntityConverter, MovieEntityToMovieDtoConverter movieEntityToMovieDtoConverter) {
        this.moviePagingSource = moviePagingSource;
        this.movieDao = movieDao;
        this.movieToMovieDtoConverter = movieToMovieDtoConverter;
        this.movieDtoToMovieEntityConverter = movieDtoToMovieEntityConverter;
        this.movieEntityToMovieDtoConverter = movieEntityToMovieDtoConverter;
    }

    private Pager<Integer, Movie> getMoviePager() {
        // Number of items per page
        final int PAGE_SIZE = 20;
        // Number of items to prefetch
        final int PREFETCH_DISTANCE = 1;
        // Whether to display placeholders when actual data is not yet loaded
        final boolean ENABLE_PLACEHOLDERS = false;
        // Number of items to load for the first page
        final int INITIAL_LOAD_SIZE_HINT = 5;
        // Maximum number of items that Paging will keep in memory
        // Standard equation: maxSize = pageSize + (2 * prefetchDistance)
        final int MAX_SIZE = PAGE_SIZE + (2 * PREFETCH_DISTANCE);

        return new Pager<>(
                new PagingConfig(PAGE_SIZE, PREFETCH_DISTANCE, ENABLE_PLACEHOLDERS, INITIAL_LOAD_SIZE_HINT, MAX_SIZE),
                () -> moviePagingSource);
    }

    @Override
    public Flowable<PagingData<MovieDto>> getMovies() {

        Pager<Integer, Movie> pager = getMoviePager();
        Flowable<PagingData<Movie>> moviePagingFlowable = PagingRx.getFlowable(pager);

        // Convert Movie to MovieDto in paging data
        return moviePagingFlowable.map(pagingData ->
                PagingDataTransforms.map(pagingData, Executors.newSingleThreadExecutor(), movieToMovieDtoConverter::convert)
        );
    }

    @Override
    public Completable addMovie(MovieDto movieDto) {
        MovieEntity movieEntity = movieDtoToMovieEntityConverter.convert(movieDto);
        return movieDao.insert(movieEntity);
    }

    @Override
    public Single<List<MovieDto>> getAllFavoriteMovies() {
        return movieDao.getAllMovies()
                .map(movieEntities -> movieEntities.stream()
                        .map(movieEntity -> {
                            MovieDto movieDto = movieEntityToMovieDtoConverter.convert(movieEntity);
                            movieDto.setFavorite(true);
                            return movieDto;
                        })
                        .collect(Collectors.toList()));
    }

    @Override
    public Completable deleteMovieById(int movieId) {
        return movieDao.deleteMovieById(movieId);
    }

    @Override
    public Single<Boolean> isFavoriteMovie(int movieId) {
        return movieDao.isMovieExists(movieId);
    }
}
