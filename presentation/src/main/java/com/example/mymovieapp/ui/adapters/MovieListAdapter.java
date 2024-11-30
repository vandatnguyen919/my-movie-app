package com.example.mymovieapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domain.model.MovieDto;
import com.example.mymovieapp.R;
import com.example.mymovieapp.databinding.MovieGridItemBinding;
import com.example.mymovieapp.databinding.MovieListItemBinding;
import com.example.mymovieapp.listener.OnMovieListItemClickListener;
import com.example.mymovieapp.utils.MovieDiffUtil;

import java.util.List;

public class MovieListAdapter extends PagingDataAdapter<MovieDto, RecyclerView.ViewHolder> {

    private OnMovieListItemClickListener onMovieListItemClickListener;

    public void setOnMovieListItemClickListener(OnMovieListItemClickListener onMovieListItemClickListener) {
        this.onMovieListItemClickListener = onMovieListItemClickListener;
    }

    public MovieListAdapter() {
        super(new MovieDiffUtil());
    }

    private boolean isGridView = false;

    public boolean isGridView() {
        return isGridView;
    }

    public void setGridView(boolean isGridView) {
        this.isGridView = isGridView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isGridView) {
            MovieGridItemBinding binding = MovieGridItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MovieGridViewHolder(binding);
        } else {
            MovieListItemBinding binding = MovieListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MovieListViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieListViewHolder) {
            MovieDto movieDto = getItem(position);
            MovieListViewHolder movieListViewHolder = ((MovieListViewHolder) holder);
            movieListViewHolder.bind(movieDto);
            movieListViewHolder.binding.starIcon.setImageResource(movieDto.isFavorite() ? R.drawable.ic_star_yellow_24 : R.drawable.ic_star_border_yellow_24);
            movieListViewHolder.binding.setOnFavoriteIconClickListener(v -> {
                onMovieListItemClickListener.onFavoriteIconClick(movieDto);
                movieListViewHolder.binding.starIcon.setImageResource(movieDto.isFavorite() ? R.drawable.ic_star_yellow_24 : R.drawable.ic_star_border_yellow_24);
            });
            movieListViewHolder.binding.setOnMovieListItemClickListener(v -> onMovieListItemClickListener.onMovieListItemClick(movieDto));
        } else if (holder instanceof MovieGridViewHolder) {
            MovieDto movieDto = getItem(position);
            MovieGridViewHolder movieGridViewHolder = ((MovieGridViewHolder) holder);
            movieGridViewHolder.bind(movieDto);
            movieGridViewHolder.binding.setOnMovieListItemClickListener(v -> onMovieListItemClickListener.onMovieListItemClick(movieDto));
        }
    }

    public void notifyMovieChanged(MovieDto movieDto) {
        List<MovieDto> currentList = snapshot().getItems();
        for (int i = 0; i < currentList.size(); i++) {
            if (currentList.get(i).getId() == movieDto.getId()) {
                MovieDto m = currentList.get(i);
                m.setFavorite(movieDto.isFavorite());
                notifyItemChanged(i);
                break;
            }
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

    private static class MovieGridViewHolder extends RecyclerView.ViewHolder {

        MovieGridItemBinding binding;

        public MovieGridViewHolder(@NonNull MovieGridItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MovieDto movieDto) {
            binding.setMovieDto(movieDto);
            binding.executePendingBindings();
        }
    }
}
