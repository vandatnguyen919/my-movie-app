package com.example.mymovieapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domain.model.CastCrewDto;
import com.example.mymovieapp.databinding.CastCrewListItemBinding;

import java.util.List;

public class CastCrewListAdapter extends RecyclerView.Adapter<CastCrewListAdapter.ViewHolder> {

    private List<CastCrewDto> castCrewDtos;

    public CastCrewListAdapter(List<CastCrewDto> castCrewDtos) {
        this.castCrewDtos = castCrewDtos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CastCrewListItemBinding binding = CastCrewListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CastCrewDto castCrewDto = castCrewDtos.get(position);
        holder.bind(castCrewDto);
    }

    @Override
    public int getItemCount() {
        return castCrewDtos.size();
    }

    public void setCastCrewDtos(List<CastCrewDto> castCrewDtos) {
        this.castCrewDtos = castCrewDtos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CastCrewListItemBinding binding;

        public ViewHolder(@NonNull CastCrewListItemBinding castCrewListItemBinding) {
            super(castCrewListItemBinding.getRoot());
            binding = castCrewListItemBinding;
        }

        public void bind(CastCrewDto castCrewDto) {
            binding.setCastCrewDto(castCrewDto);
            binding.executePendingBindings();
        }
    }
}
