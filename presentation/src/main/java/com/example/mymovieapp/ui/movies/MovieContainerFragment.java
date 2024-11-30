package com.example.mymovieapp.ui.movies;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mymovieapp.R;
import com.example.mymovieapp.databinding.FragmentMovieContainerBinding;
import com.example.mymovieapp.di.MyApplication;
import com.example.mymovieapp.ui.movies.details.MovieDetailsViewModel;

import javax.inject.Inject;

public class MovieContainerFragment extends Fragment {

    private FragmentMovieContainerBinding mBinding;
    private NavController mNavController;

    @Inject
    public MovieDetailsViewModel mMovieDetailsViewModel;

    public MovieContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) requireActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentMovieContainerBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavHostFragment navHostFragment = (NavHostFragment)
                getChildFragmentManager().findFragmentById(R.id.movie_container_view);
        if (navHostFragment != null) {
            mNavController = navHostFragment.getNavController();
            if (savedInstanceState != null) {
                mNavController.restoreState(savedInstanceState);
            }
            mMovieDetailsViewModel.getMovieDto().observe(getViewLifecycleOwner(), movie -> {
                if (movie != null) {
                    mNavController.navigate(R.id.movie_details_fragment);
                }
            });
        } else {
            Log.e("MovieContainerFragment", "NavHostFragment not found for movie_container_view");
        }
    }
}