package com.example.mymovieapp.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.domain.model.MovieDto;

public class MovieDiffUtil extends DiffUtil.ItemCallback<MovieDto> {

    @Override
    public boolean areItemsTheSame(@NonNull MovieDto oldItem, @NonNull MovieDto newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull MovieDto oldItem, @NonNull MovieDto newItem) {
        return oldItem.equals(newItem);
    }
}
