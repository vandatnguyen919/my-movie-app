package com.example.mymovieapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domain.model.MovieDto;
import com.example.mymovieapp.R;
import com.example.mymovieapp.databinding.MovieListItemBinding;
import com.example.mymovieapp.listener.OnMovieListItemClickListener;
import com.example.mymovieapp.utils.MovieDiffUtil;

public class FavoriteMovieListAdapter extends ListAdapter<MovieDto, RecyclerView.ViewHolder> {

    private OnMovieListItemClickListener onMovieListItemClickListener;

    public void setOnMovieListItemClickListener(OnMovieListItemClickListener onMovieListItemClickListener) {
        this.onMovieListItemClickListener = onMovieListItemClickListener;
    }

    public FavoriteMovieListAdapter() {
        super(new MovieDiffUtil());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieListItemBinding binding = MovieListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieListViewHolder) {
            MovieDto movieDto = getItem(position);
            MovieListViewHolder movieListViewHolder = ((MovieListViewHolder) holder);
            movieListViewHolder.bind(movieDto);
            movieListViewHolder.binding.starIcon.setImageResource(R.drawable.ic_star_yellow_24);
            movieListViewHolder.binding.setOnFavoriteIconClickListener(v -> {
                onMovieListItemClickListener.onFavoriteIconClick(movieDto);
                movieListViewHolder.binding.starIcon.setImageResource(movieDto.isFavorite() ? R.drawable.ic_star_yellow_24 : R.drawable.ic_star_border_yellow_24);
            });
            movieListViewHolder.binding.setOnMovieListItemClickListener(v -> onMovieListItemClickListener.onMovieListItemClick(movieDto));
        }
    }

    private static class MovieListViewHolder extends RecyclerView.ViewHolder {

        MovieListItemBinding binding;

        public MovieListViewHolder(@NonNull MovieListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MovieDto movieDto) {
            binding.setMovieDto(movieDto);
            binding.executePendingBindings();
        }
    }
}
