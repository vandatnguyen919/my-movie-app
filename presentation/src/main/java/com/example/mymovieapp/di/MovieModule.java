package com.example.mymovieapp.di;

import android.app.Application;
import android.content.Context;

import com.example.data.mapper.CastCrewToCastCrewDtoConverter;
import com.example.data.mapper.MovieDtoToMovieEntityConverter;
import com.example.data.mapper.MovieEntityToMovieDtoConverter;
import com.example.data.mapper.MovieToMovieDtoConverter;
import com.example.data.repository.CastCrewRepositoryImpl;
import com.example.data.repository.MovieRepositoryImpl;
import com.example.data.source.local.dao.MovieDao;
import com.example.data.source.remote.paging.MoviePagingSource;
import com.example.data.source.remote.service.MovieApiService;
import com.example.domain.repository.CastCrewRepository;
import com.example.domain.repository.MovieRepository;
import com.example.domain.usecase.FavoriteMovieUseCase;
import com.example.domain.usecase.GetCastCrewsUseCase;
import com.example.domain.usecase.GetFavoriteMoviesUseCase;
import com.example.domain.usecase.GetMoviesUseCase;
import com.example.mymovieapp.ui.base.BaseMovieListViewModel;
import com.example.mymovieapp.ui.movies.details.MovieDetailsViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieModule {

    // Mapper
    @Singleton
    @Provides
    MovieDtoToMovieEntityConverter provideMovieDtoToMovieEntityConverter() {
        return new MovieDtoToMovieEntityConverter();
    }

    @Singleton
    @Provides
    MovieEntityToMovieDtoConverter provideMovieEntityToMovieDtoConverter() {
        return new MovieEntityToMovieDtoConverter();
    }

    @Singleton
    @Provides
    MovieToMovieDtoConverter provideMovieToMovieDtoConverter() {
        return new MovieToMovieDtoConverter();
    }

    @Singleton
    @Provides
    CastCrewToCastCrewDtoConverter provideCastCrewToCastCrewDtoConverter() {
        return new CastCrewToCastCrewDtoConverter();
    }

    // Repositories
    @Singleton
    @Provides
    MoviePagingSource provideMoviePagingSource(Context context, MovieApiService movieApiService, MovieDao movieDao) {
        return new MoviePagingSource(context, movieApiService, movieDao);
    }

    @Singleton
    @Provides
    MovieRepository provideMovieRepository(MoviePagingSource moviePagingSource,
                                           MovieDao movieDao,
                                           MovieToMovieDtoConverter movieToMovieDtoConverter,
                                           MovieDtoToMovieEntityConverter movieDtoToMovieEntityConverter,
                                           MovieEntityToMovieDtoConverter movieEntityToMovieDtoConverter) {
        return new MovieRepositoryImpl(moviePagingSource, movieDao, movieToMovieDtoConverter, movieDtoToMovieEntityConverter, movieEntityToMovieDtoConverter);
    }

    @Singleton
    @Provides
    CastCrewRepository provideCastCrewRepository(MovieApiService movieApiService, CastCrewToCastCrewDtoConverter castCrewToCastCrewDtoConverter) {
        return new CastCrewRepositoryImpl(movieApiService, castCrewToCastCrewDtoConverter);
    }

    // Use cases
    @Singleton
    @Provides
    GetMoviesUseCase provideGetMoviesUseCase(MovieRepository movieRepository) {
        return new GetMoviesUseCase(movieRepository);
    }

    @Singleton
    @Provides
    GetFavoriteMoviesUseCase provideGetFavoriteMoviesUseCase(MovieRepository movieRepository) {
        return new GetFavoriteMoviesUseCase(movieRepository);
    }

    @Singleton
    @Provides
    FavoriteMovieUseCase provideFavoriteMovieUseCase(MovieRepository movieRepository) {
        return new FavoriteMovieUseCase(movieRepository);
    }

    @Singleton
    @Provides
    GetCastCrewsUseCase provideGetCastCrewsUseCase(CastCrewRepository castCrewRepository) {
        return new GetCastCrewsUseCase(castCrewRepository);
    }

    // View models
    @Singleton
    @Provides
    BaseMovieListViewModel provideMovieListViewModel(Application application,
                                                     GetMoviesUseCase getMoviesUseCase,
                                                     GetFavoriteMoviesUseCase getFavoriteMoviesUseCase,
                                                     FavoriteMovieUseCase favoriteMovieUseCase) {
        return new BaseMovieListViewModel(application, getMoviesUseCase, getFavoriteMoviesUseCase, favoriteMovieUseCase);
    }

    @Singleton
    @Provides
    MovieDetailsViewModel provideMovieDetailsViewModel(GetCastCrewsUseCase castCrewsUseCase) {
        return new MovieDetailsViewModel(castCrewsUseCase);
    }
}
