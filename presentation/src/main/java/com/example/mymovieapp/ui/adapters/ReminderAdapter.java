package com.example.mymovieapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domain.model.ReminderDto;
import com.example.mymovieapp.databinding.ReminderListItemBinding;

public class ReminderAdapter extends ListAdapter<ReminderDto, ReminderAdapter.ReminderViewHolder> {

    private View.OnClickListener onDeleteClickListener;

    private View.OnClickListener onMovieListItemClickListener;

    public void setOnDeleteClickListener(View.OnClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnMovieListItemClickListener(View.OnClickListener onMovieListItemClickListener) {
        this.onMovieListItemClickListener = onMovieListItemClickListener;
    }

    public ReminderAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ReminderDto> DIFF_CALLBACK = new DiffUtil.ItemCallback<ReminderDto>() {
        @Override
        public boolean areItemsTheSame(@NonNull ReminderDto oldItem, @NonNull ReminderDto newItem) {
            return oldItem.getMovieId() == newItem.getMovieId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ReminderDto oldItem, @NonNull ReminderDto newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReminderListItemBinding reminderListItemBinding = ReminderListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReminderViewHolder(reminderListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        ReminderDto reminderDto = getItem(position);
        holder.bind(reminderDto);
        holder.reminderListItemBinding.deleteIcon.setOnClickListener(v -> {
            v.setTag(reminderDto.getMovieId());
            onDeleteClickListener.onClick(v);
        });
        holder.itemView.setOnClickListener(v -> {
            v.setTag(reminderDto.getMovieDto());
            onMovieListItemClickListener.onClick(v);
        });
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {

        ReminderListItemBinding reminderListItemBinding;

        public ReminderViewHolder(@NonNull ReminderListItemBinding reminderListItemBinding) {
            super(reminderListItemBinding.getRoot());
            this.reminderListItemBinding = reminderListItemBinding;
        }

        public void bind(ReminderDto reminderDto) {
            reminderListItemBinding.setReminderDto(reminderDto);
            reminderListItemBinding.executePendingBindings();
        }
    }
}
