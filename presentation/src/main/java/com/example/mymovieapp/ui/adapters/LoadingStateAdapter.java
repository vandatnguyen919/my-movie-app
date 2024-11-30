package com.example.mymovieapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.databinding.LoadingItemBinding;

public class LoadingStateAdapter extends LoadStateAdapter<LoadingStateAdapter.LoadingViewHolder> {

    @NonNull
    @Override
    public LoadingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull LoadState loadState) {
        LoadingItemBinding binding = LoadingItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new LoadingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LoadingViewHolder loadingViewHolder, @NonNull LoadState loadState) {
        if (loadState instanceof LoadState.Loading) {
            loadingViewHolder.itemView.setVisibility(View.VISIBLE);
        } else {
            loadingViewHolder.itemView.setVisibility(View.GONE);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(@NonNull LoadingItemBinding loadingItemBinding) {
            super(loadingItemBinding.getRoot());
        }
    }
}
