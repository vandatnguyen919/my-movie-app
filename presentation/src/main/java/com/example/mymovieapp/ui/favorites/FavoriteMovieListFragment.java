package com.example.mymovieapp.ui.favorites;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mymovieapp.databinding.FragmentFavoriteMovieListBinding;
import com.example.mymovieapp.di.MyApplication;
import com.example.mymovieapp.ui.adapters.FavoriteMovieListAdapter;
import com.example.mymovieapp.ui.base.BaseMovieListFragment;

public class FavoriteMovieListFragment extends BaseMovieListFragment {

    private FragmentFavoriteMovieListBinding mBinding;
    private FavoriteMovieListAdapter mFavoriteMovieListAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MyApplication) requireActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoriteMovieListAdapter = new FavoriteMovieListAdapter();
        mFavoriteMovieListAdapter.setOnMovieListItemClickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentFavoriteMovieListBinding.inflate(inflater, container, false);
        mBinding.recyclerView.setAdapter(mFavoriteMovieListAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.getFavoriteMovies().observe(getViewLifecycleOwner(), movieResponse -> {
            mFavoriteMovieListAdapter.submitList(movieResponse);
        });
    }
}