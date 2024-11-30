package com.example.mymovieapp.ui.base;

import androidx.fragment.app.Fragment;

import com.example.domain.model.MovieDto;
import com.example.mymovieapp.listener.OnMovieListItemClickListener;
import com.example.mymovieapp.ui.movies.details.MovieDetailsViewModel;

import javax.inject.Inject;

public class BaseMovieListFragment extends Fragment implements OnMovieListItemClickListener {

    @Inject
    public BaseMovieListViewModel mViewModel;

    @Inject
    public MovieDetailsViewModel mMovieDetailsViewModel;

    @Override
    public void onFavoriteIconClick(MovieDto movieDto) {
        mViewModel.updateFavoriteMovie(movieDto);
    }

    @Override
    public void onMovieListItemClick(MovieDto movieDto) {
        mMovieDetailsViewModel.setMovieDto(movieDto);
    }
}
