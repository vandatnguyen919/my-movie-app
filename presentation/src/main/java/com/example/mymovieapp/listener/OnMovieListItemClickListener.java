package com.example.mymovieapp.listener;

import com.example.domain.model.MovieDto;

public interface OnMovieListItemClickListener {

    void onFavoriteIconClick(MovieDto movieDto);

    void onMovieListItemClick(MovieDto movieDto);
}
