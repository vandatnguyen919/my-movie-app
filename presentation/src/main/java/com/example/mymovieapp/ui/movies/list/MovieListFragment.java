package com.example.mymovieapp.ui.movies.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.databinding.FragmentMovieListBinding;
import com.example.mymovieapp.di.MyApplication;
import com.example.mymovieapp.ui.adapters.LoadingStateAdapter;
import com.example.mymovieapp.ui.adapters.MovieListAdapter;
import com.example.mymovieapp.ui.base.BaseMovieListFragment;

public class MovieListFragment extends BaseMovieListFragment {

    private FragmentMovieListBinding mBinding;
    private MovieListAdapter mMovieListAdapter;
    private ConcatAdapter mMovieListLoadingStateAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MyApplication) requireActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieListAdapter = new MovieListAdapter();
        mMovieListAdapter.setOnMovieListItemClickListener(this);
        mMovieListLoadingStateAdapter = mMovieListAdapter.withLoadStateFooter(new LoadingStateAdapter());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMovieListBinding.inflate(inflater, container, false);
        mBinding.swipeRefreshLayout.setOnRefreshListener(() -> mViewModel.loadMovies());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.getMovies().observe(getViewLifecycleOwner(), movieResponse -> {
            mMovieListAdapter.submitData(getLifecycle(), movieResponse);
            mBinding.recyclerView.setAdapter(mMovieListLoadingStateAdapter);
            mBinding.swipeRefreshLayout.setRefreshing(false);
            mBinding.loadingProgressBar.setVisibility(View.GONE);
        });
        mViewModel.getToBeUpdatedMovie().observe(getViewLifecycleOwner(), movie -> mMovieListAdapter.notifyMovieChanged(movie));
        mViewModel.getIsGridView().observe(getViewLifecycleOwner(), isGridView -> {
            mMovieListAdapter.setGridView(isGridView);
            mBinding.recyclerView.setAdapter(mMovieListLoadingStateAdapter);
            mBinding.recyclerView.setLayoutManager(getMovieListLayoutManager(isGridView));
        });
    }

    private RecyclerView.LayoutManager getMovieListLayoutManager(boolean isGridView) {
        return isGridView ? new GridLayoutManager(requireContext(), 2) : new LinearLayoutManager(getContext());
    }
}