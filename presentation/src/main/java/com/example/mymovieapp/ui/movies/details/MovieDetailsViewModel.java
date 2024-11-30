package com.example.mymovieapp.ui.movies.details;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.domain.model.CastCrewDto;
import com.example.domain.model.MovieDto;
import com.example.domain.usecase.GetCastCrewsUseCase;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MovieDetailsViewModel extends ViewModel {

    private final GetCastCrewsUseCase mGetCastCrewsUseCase;

    private final MutableLiveData<MovieDto> mMovieDtoMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<CastCrewDto>> mCastCrewDtosMutableLiveData = new MutableLiveData<>();

    @Inject
    public MovieDetailsViewModel(GetCastCrewsUseCase mGetCastCrewsUseCase) {
        this.mGetCastCrewsUseCase = mGetCastCrewsUseCase;
    }

    public void setMovieDto(MovieDto movieDto) {
        mMovieDtoMutableLiveData.setValue(movieDto);
        if (movieDto != null) {
            loadCastCrews(movieDto.getId());
        }
    }

    public MutableLiveData<MovieDto> getMovieDto() {
        return mMovieDtoMutableLiveData;
    }

    public MutableLiveData<List<CastCrewDto>> getCastCrewDto() {
        return mCastCrewDtosMutableLiveData;
    }

    public void loadCastCrews(int movieId) {
        mGetCastCrewsUseCase.setMovieId(movieId);
        mGetCastCrewsUseCase.execute(mCastCrewDtosMutableLiveData::setValue,
                throwable -> mCastCrewDtosMutableLiveData.setValue(Collections.emptyList()),
                () -> Log.d("MovieDetailsViewModel", "getCastCrewsUseCase: onComplete"));
    }
}