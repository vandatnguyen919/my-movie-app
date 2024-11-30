package com.example.mymovieapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domain.model.ReminderDto;
import com.example.mymovieapp.databinding.NavReminderListItemBinding;
import com.example.mymovieapp.databinding.ReminderListItemBinding;

public class NavReminderAdapter extends ListAdapter<ReminderDto, NavReminderAdapter.ReminderViewHolder> {

    public NavReminderAdapter() {
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
        NavReminderListItemBinding navReminderListItemBinding = NavReminderListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReminderViewHolder(navReminderListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        ReminderDto reminderDto = getItem(position);
        holder.bind(reminderDto);
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {

        NavReminderListItemBinding navReminderListItemBinding;

        public ReminderViewHolder(@NonNull NavReminderListItemBinding navReminderListItemBinding) {
            super(navReminderListItemBinding.getRoot());
            this.navReminderListItemBinding = navReminderListItemBinding;
        }

        public void bind(ReminderDto reminderDto) {
            navReminderListItemBinding.setReminderDto(reminderDto);
            navReminderListItemBinding.executePendingBindings();
        }
    }
}
