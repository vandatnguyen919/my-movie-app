package com.example.mymovieapp.ui.reminder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.work.WorkManager;

import com.example.domain.model.MovieDto;
import com.example.mymovieapp.R;
import com.example.mymovieapp.databinding.FragmentReminderListBinding;
import com.example.mymovieapp.di.MyApplication;
import com.example.mymovieapp.ui.adapters.ReminderAdapter;
import com.example.mymovieapp.ui.movies.details.MovieDetailsViewModel;

import javax.inject.Inject;

public class ReminderListFragment extends Fragment {

    private FragmentReminderListBinding mBinding;
    private ReminderAdapter mReminderAdapter;

    @Inject
    public MovieDetailsViewModel mMovieDetailsViewModel;

    @Inject
    public ReminderListViewModel mReminderListViewModel;

    private NavController mNavController;

    private OnBackPressedCallback mOnBackPressedCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MyApplication) requireActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReminderAdapter = new ReminderAdapter();
        mReminderAdapter.setOnDeleteClickListener(this::onDeleteReminderClick);
        mReminderAdapter.setOnMovieListItemClickListener(this::onMovieListItemClick);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentReminderListBinding.inflate(inflater, container, false);
        mBinding.reminderRecyclerView.setAdapter(mReminderAdapter);
        mBinding.backArrowIcon.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        mReminderListViewModel.getReminders().observe(getViewLifecycleOwner(), reminders -> mReminderAdapter.submitList(reminders));
    }

    @Override
    public void onResume() {
        super.onResume();
        mOnBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mNavController.popBackStack(R.id.main_fragment, false);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mOnBackPressedCallback != null) {
            mOnBackPressedCallback.remove();
        }
    }

    public void onDeleteReminderClick(View view) {
        int movieId = (int) view.getTag();
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Reminder")
                .setMessage("Are you sure you want to delete this reminder?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    mReminderListViewModel.deleteReminder(movieId);
                    WorkManager.getInstance(requireContext()).cancelAllWorkByTag(String.valueOf(movieId));
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void onMovieListItemClick(View view) {
        MovieDto movieDto = (MovieDto) view.getTag();
        mMovieDetailsViewModel.setMovieDto(movieDto);
        mNavController.popBackStack(R.id.main_fragment, false);
    }
}