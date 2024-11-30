package com.example.mymovieapp.ui.base;


import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.PagingData;
import androidx.preference.PreferenceManager;

import com.example.domain.model.MovieDto;
import com.example.domain.usecase.FavoriteMovieUseCase;
import com.example.domain.usecase.GetFavoriteMoviesUseCase;
import com.example.domain.usecase.GetMoviesUseCase;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class BaseMovieListViewModel extends AndroidViewModel {

    private final GetMoviesUseCase mGetMoviesUseCase;
    private final GetFavoriteMoviesUseCase mGetFavoriteMoviesUseCase;
    private final FavoriteMovieUseCase mFavoriteMovieUseCase;

    private final MutableLiveData<PagingData<MovieDto>> mPagingDataMutableLiveData = new MutableLiveData<>();

    // To update the favorite icon in the movie list
    private final MutableLiveData<MovieDto> mToBeUpdatedMovie = new MutableLiveData<>();

    private final MutableLiveData<List<MovieDto>> mFavoriteMoviesMutableLiveData = new MutableLiveData<>();

    // Fav movies will be searched in cached fav movies instead of constantly querying the database to enhance performance
    private final MutableLiveData<List<MovieDto>> mCachedFavoriteMoviesMutableLiveData = new MutableLiveData<>();
    private String mQuery = "";

    // To switch between grid and list view
    private final MutableLiveData<Boolean> mIsGridView = new MutableLiveData<>(false);

    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener;

    @Inject
    public BaseMovieListViewModel(@NotNull Application application, GetMoviesUseCase mGetMoviesUseCase, GetFavoriteMoviesUseCase mGetFavoriteMoviesUseCase, FavoriteMovieUseCase mFavoriteMovieUseCase) {
        super(application);
        this.mGetMoviesUseCase = mGetMoviesUseCase;
        this.mGetFavoriteMoviesUseCase = mGetFavoriteMoviesUseCase;
        this.mFavoriteMovieUseCase = mFavoriteMovieUseCase;

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        mPreferenceChangeListener = (sharedPreferences, key) -> loadMovies();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);

        loadMovies();
        loadFavoriteMovies();
    }

    public LiveData<Boolean> getIsGridView() {
        return mIsGridView;
    }

    public void setIsGridView(boolean isGridView) {
        mIsGridView.setValue(isGridView);
    }

    public LiveData<MovieDto> getToBeUpdatedMovie() {
        return mToBeUpdatedMovie;
    }

    public LiveData<PagingData<MovieDto>> getMovies() {
        return mPagingDataMutableLiveData;
    }

    public LiveData<List<MovieDto>> getCachedFavoriteMovies() {
        return mCachedFavoriteMoviesMutableLiveData;
    }

    public LiveData<List<MovieDto>> getFavoriteMovies() {
        return mFavoriteMoviesMutableLiveData;
    }

    public void updateFavoriteMovie(MovieDto movieDto) {
        mFavoriteMovieUseCase.isFavorite(movieDto.getId(),
                isFavorite -> {
                    if (isFavorite) {
                        deleteMovieFromFavorite(movieDto);
                    } else {
                        addMovieToFavorite(movieDto);
                    }
                },
                throwable -> Log.d("MovieListViewModel", "updateFavoriteMovie: " + throwable.getMessage())
        );
    }

    private void addMovieToFavorite(MovieDto movieDto) {
        mFavoriteMovieUseCase.addToFavorite(movieDto,
                () -> {
                    movieDto.setFavorite(true);
                    mToBeUpdatedMovie.setValue(movieDto);
                    loadFavoriteMovies();
                },
                throwable -> Log.d("MovieListViewModel", "addMovieToFavorite: " + throwable.getMessage())
        );
    }

    private void deleteMovieFromFavorite(MovieDto movieDto) {
        mFavoriteMovieUseCase.deleteFromFavorite(movieDto.getId(),
                () -> {
                    movieDto.setFavorite(false);
                    mToBeUpdatedMovie.setValue(movieDto);
                    loadFavoriteMovies();
                },
                throwable -> Log.d("MovieListViewModel", "deleteMovieFromFavorite: " + throwable.getMessage()));
    }

    public void loadMovies() {
        mGetMoviesUseCase.setCoroutineScope(ViewModelKt.getViewModelScope(this));
        mGetMoviesUseCase.execute(
                mPagingDataMutableLiveData::setValue,
                throwable -> Log.d("MovieListViewModel", "updateMovies: " + throwable.getMessage()),
                () -> Log.d("MovieListViewModel", "updateMovies: ")
        );
    }

    public void queryFavoriteMovies(String query) {
        mQuery = query;
        List<MovieDto> result = mCachedFavoriteMoviesMutableLiveData.getValue().stream()
                .filter(movieDto -> movieDto.getTitle().toLowerCase().contains(mQuery.toLowerCase()))
                .collect(Collectors.toList());
        mFavoriteMoviesMutableLiveData.setValue(result);
    }

    private void loadFavoriteMovies() {
        mGetFavoriteMoviesUseCase.execute(
                movieDtos -> {
                    mCachedFavoriteMoviesMutableLiveData.setValue(movieDtos);
                    movieDtos = movieDtos.stream()
                            .filter(movieDto -> movieDto.getTitle().toLowerCase().contains(mQuery.toLowerCase()))
                            .collect(Collectors.toList());
                    mFavoriteMoviesMutableLiveData.setValue(movieDtos);
                },
                throwable -> Log.d("MovieListViewModel", "updateFavoriteMovies: " + throwable.getMessage()),
                () -> Log.d("MovieListViewModel", "updateFavoriteMovies: ")
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mGetMoviesUseCase.dispose();
        mGetFavoriteMoviesUseCase.dispose();
        mFavoriteMovieUseCase.dispose();

        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mPreferenceChangeListener);
    }
}