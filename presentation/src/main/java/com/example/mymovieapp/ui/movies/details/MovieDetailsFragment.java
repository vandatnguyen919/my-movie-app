package com.example.mymovieapp.ui.movies.details;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.domain.model.MovieDto;
import com.example.domain.model.ReminderDto;
import com.example.mymovieapp.R;
import com.example.mymovieapp.databinding.FragmentMovieDetailsBinding;
import com.example.mymovieapp.di.MyApplication;
import com.example.mymovieapp.ui.adapters.CastCrewListAdapter;
import com.example.mymovieapp.ui.base.BaseMovieListViewModel;
import com.example.mymovieapp.ui.reminder.ReminderListViewModel;
import com.example.mymovieapp.workers.NotificationWorker;

import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class MovieDetailsFragment extends Fragment {

    private FragmentMovieDetailsBinding mBinding;
    private CastCrewListAdapter mCastCrewListAdapter;

    private MovieDto mMovieDto;

    @Inject
    public MovieDetailsViewModel mMovieDetailsViewModel;

    @Inject
    public BaseMovieListViewModel mBaseMovieListViewModel;

    @Inject
    public ReminderListViewModel mReminderListViewModel;

    private NavController mNavController;

    private OnBackPressedCallback mOnBackPressedCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) requireActivity().getApplication()).getAppComponent().inject(this);
        mCastCrewListAdapter = new CastCrewListAdapter(Collections.emptyList());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false);
        mMovieDto = mMovieDetailsViewModel.getMovieDto().getValue();
        mBinding.setMovieDto(mMovieDto);
        mBinding.executePendingBindings();
        mBinding.setCastCrewListAdapter(mCastCrewListAdapter);
        mBinding.setOnFavoriteIconClickListener(v -> mBaseMovieListViewModel.updateFavoriteMovie(mMovieDto));
        mBinding.setOnReminderButtonClickListener(v -> checkNotificationPermission());
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);

        mBaseMovieListViewModel.getToBeUpdatedMovie().observe(getViewLifecycleOwner(), movie -> {
            if (mMovieDto != null && mMovieDto.getId() == movie.getId()) {
                mBinding.setMovieDto(movie);
            }
        });
        mMovieDetailsViewModel.getCastCrewDto().observe(getViewLifecycleOwner(), castCrewDtos -> {
            if (castCrewDtos != null) {
                mCastCrewListAdapter.setCastCrewDtos(castCrewDtos);
                mBinding.loadingProgressBar.setVisibility(View.GONE);
            }
        });
        mReminderListViewModel.getReminders().observe(getViewLifecycleOwner(), reminderDtos -> {
            if (reminderDtos != null) {
                boolean isReminderSet = false;
                for (ReminderDto reminderDto : reminderDtos) {
                    if (reminderDto.getMovieId() == mMovieDto.getId()) {
                        isReminderSet = true;
                        mBinding.reminderTextView.setText(String.format("*Will be reminded at %s", reminderDto.getFormattedTimestamp()));
                        mBinding.reminderTextView.setVisibility(View.VISIBLE);
                        break;
                    }
                }
                if (!isReminderSet) {
                    mBinding.reminderTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mOnBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mNavController.popBackStack(R.id.movie_list_fragment, false);
                mMovieDetailsViewModel.setMovieDto(null);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), mOnBackPressedCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mOnBackPressedCallback != null) {
            mOnBackPressedCallback.remove();
        }
    }

    private void showDateTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), android.R.style.Theme_Holo_Dialog,
                (view, year, month, dayOfMonth) -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Dialog,
                            (timeView, hourOfDay, minute) -> {

                                calendar.set(year, month, dayOfMonth, hourOfDay, minute, 0);
                                long reminderTimestamp = calendar.getTimeInMillis();


                                long delay = reminderTimestamp - System.currentTimeMillis();
                                if (delay > 0) {
                                    mReminderListViewModel.saveReminder(mMovieDto.getId(), reminderTimestamp);

                                    Data data = new Data.Builder()
                                            .putInt("movieId", mMovieDto.getId())
                                            .putString("movieTitle", mMovieDto.getTitle())
                                            .putString("movieReleaseYear", mMovieDto.getReleaseYear())
                                            .putDouble("movieVoteAverage", mMovieDto.getVoteAverage())
                                            .putLong("timestamp", reminderTimestamp)
                                            .build();

                                    WorkRequest notificationWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                            .setInputData(data)
                                            .addTag(String.valueOf(mMovieDto.getId()))
                                            .build();
                                    WorkManager.getInstance(requireContext()).cancelAllWorkByTag(String.valueOf(mMovieDto.getId()));
                                    WorkManager.getInstance(requireContext()).enqueue(notificationWorkRequest);
                                } else {
                                    Toast.makeText(requireContext(), "Please select a future date and time", Toast.LENGTH_SHORT).show();
                                }

                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    showDateTimeDialog();
                }
            }
    );

    private void checkNotificationPermission() {
        // Check if API level is 33 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                showDateTimeDialog();
            }
        } else {
            // For devices running on API level below 33
            showDateTimeDialog();
        }
    }
}