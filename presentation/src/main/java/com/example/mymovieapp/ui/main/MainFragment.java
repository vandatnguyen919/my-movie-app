package com.example.mymovieapp.ui.main;

import static com.example.domain.utils.Constants.CATEGORY;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.domain.model.MovieDto;
import com.example.mymovieapp.R;
import com.example.mymovieapp.databinding.FragmentMainBinding;
import com.example.mymovieapp.di.MyApplication;
import com.example.mymovieapp.ui.about.AboutFragment;
import com.example.mymovieapp.ui.adapters.NavReminderAdapter;
import com.example.mymovieapp.ui.adapters.ViewPagerStateAdapter;
import com.example.mymovieapp.ui.base.BaseMovieListViewModel;
import com.example.mymovieapp.ui.favorites.FavoriteMovieListFragment;
import com.example.mymovieapp.ui.movies.MovieContainerFragment;
import com.example.mymovieapp.ui.movies.details.MovieDetailsViewModel;
import com.example.mymovieapp.ui.profile.UserProfileViewModel;
import com.example.mymovieapp.ui.reminder.ReminderListViewModel;
import com.example.mymovieapp.ui.settings.SettingsFragment;
import com.example.mymovieapp.utils.BitmapUtils;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class MainFragment extends Fragment implements MenuProvider {

    private static final List<String> TAB_TITLE_LIST = Arrays.asList(
            "Movies",
            "Favourite",
            "Settings",
            "About");

    private static final List<Integer> TAB_ICON_LIST = Arrays.asList(
            R.drawable.ic_home_24,
            R.drawable.ic_favorite_24,
            R.drawable.ic_settings_24,
            R.drawable.ic_info_24);

    private FragmentMainBinding mBinding;
    private NavReminderAdapter mNavReminderAdapter;

    private MenuItem mSearchItem;
    private MenuItem mLayoutChangeItem;
    private MenuItem mPopularMoviesItem;
    private MenuItem mTopRatedMoviesItem;
    private MenuItem mUpcomingMoviesItem;
    private MenuItem mNowPlayingMoviesItem;

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private View mHeaderView;
    private NavController mNavController;

    @Inject
    public MovieDetailsViewModel mMovieDetailsViewModel;

    @Inject
    public BaseMovieListViewModel mBaseMovieListViewModel;

    @Inject
    public UserProfileViewModel mUserProfileViewModel;

    @Inject
    public ReminderListViewModel mReminderListViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MyApplication) requireActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavReminderAdapter = new NavReminderAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMainBinding.inflate(inflater, container, false);
        mTabLayout = mBinding.tabLayout;
        mViewPager2 = mBinding.viewPager2;
        mToolbar = mBinding.toolbar;
        mHeaderView = mBinding.navView.getHeaderView(0);
        requireActivity().runOnUiThread(() -> new Handler().post(() -> {
            mNavController = Navigation.findNavController(requireActivity(), R.id.main_container_view);
        }));
        requireActivity().addMenuProvider(this, getViewLifecycleOwner());
        ((AppCompatActivity) requireActivity()).setSupportActionBar(mToolbar);

        RecyclerView navReminderRecyclerView = mHeaderView.findViewById(R.id.nav_reminder_recycler_view);
        navReminderRecyclerView.setAdapter(mNavReminderAdapter);

        setUpEditProfileButton();
        setUpShowAllRemindersButton();

        createTabLayoutViewPager2();
        createDrawerLayout();

        return mBinding.getRoot();
    }

    private void setUpShowAllRemindersButton() {
        Button showAllReminderButton = mHeaderView.findViewById(R.id.show_all_reminder_button);
        showAllReminderButton.setOnClickListener(v -> mNavController.navigate(R.id.reminder_fragment));
    }

    private void setUpEditProfileButton() {
        Button profileEditButton = mHeaderView.findViewById(R.id.profile_edit_button);
        profileEditButton.setOnClickListener(v -> mNavController.navigate(R.id.profile_fragment));
    }

    private void createDrawerLayout() {
        // Set up the drawer layout
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                requireActivity(), mBinding.drawerLayout, mToolbar, R.string.nav_open, R.string.nav_close);
        mBinding.drawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
    }

    private void createTabLayoutViewPager2() {
        List<Fragment> fragmentList = getFragments();

        ViewPagerStateAdapter viewPagerStateAdapter = new ViewPagerStateAdapter(requireActivity(), fragmentList);
        mViewPager2.setAdapter(viewPagerStateAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) ->
                tab.setText(TAB_TITLE_LIST.get(position)).setIcon(TAB_ICON_LIST.get(position))
        ).attach();
        mViewPager2.setOffscreenPageLimit(3);
    }

    private static @NonNull List<Fragment> getFragments() {
        MovieContainerFragment movieContainerFragment = new MovieContainerFragment();
        FavoriteMovieListFragment favoriteMovieListFragment = new FavoriteMovieListFragment();
        SettingsFragment settingsFragment = new SettingsFragment();
        AboutFragment aboutFragment = new AboutFragment();

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(movieContainerFragment);
        fragmentList.add(favoriteMovieListFragment);
        fragmentList.add(settingsFragment);
        fragmentList.add(aboutFragment);
        return fragmentList;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.option_menu, menu);
        mSearchItem = menu.findItem(R.id.search_menu_item);
        mLayoutChangeItem = menu.findItem(R.id.layout_change_menu_item);
        mPopularMoviesItem = menu.findItem(R.id.popular_movies_menu_item);
        mTopRatedMoviesItem = menu.findItem(R.id.top_rated_movies_menu_item);
        mUpcomingMoviesItem = menu.findItem(R.id.upcoming_movies_menu_item);
        mNowPlayingMoviesItem = menu.findItem(R.id.nowplaying_movies_menu_item);

        setUpActionBar(mViewPager2.getCurrentItem());

        SearchView searchView = (SearchView) mSearchItem.getActionView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mBaseMovieListViewModel.queryFavoriteMovies(newText);
                    return false;
                }
            });
            searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
                mViewPager2.setUserInputEnabled(!hasFocus);
            });
        }

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setUpActionBar(position);
            }
        });

        mUserProfileViewModel.getUserProfile().observe(getViewLifecycleOwner(), userProfileDto -> {
            if (userProfileDto != null) {
                ImageView profileImageView = mHeaderView.findViewById(R.id.profile_image);
                TextView profileNameTextView = mHeaderView.findViewById(R.id.profile_name_text);
                TextView profileEmailTextView = mHeaderView.findViewById(R.id.profile_email_text);
                TextView profileBirthDateTextView = mHeaderView.findViewById(R.id.profile_birth_date_text);
                TextView profileGenderTextView = mHeaderView.findViewById(R.id.profile_gender_text);

                if (userProfileDto.getAvatar() != null) {
                    profileImageView.setImageBitmap(BitmapUtils.decodeFromBase64(userProfileDto.getAvatar()));
                }
                profileBirthDateTextView.setText(userProfileDto.getBirthDate());
                profileGenderTextView.setText(userProfileDto.getGender());
                profileNameTextView.setText(userProfileDto.getName());
                profileEmailTextView.setText(userProfileDto.getEmail());
            }
        });

        mBaseMovieListViewModel.getCachedFavoriteMovies().observe(getViewLifecycleOwner(), movieResponse -> {
            updateBadge(movieResponse.size());
        });

        mMovieDetailsViewModel.getMovieDto().observe(getViewLifecycleOwner(), movieDto -> {
            if (movieDto != null) {
//                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                setUpActionBar(0);
            } else {
                setUpActionBar(mViewPager2.getCurrentItem());
            }
            mViewPager2.setCurrentItem(0);
        });

        mReminderListViewModel.getReminders().observe(getViewLifecycleOwner(), reminders -> {
            mNavReminderAdapter.submitList(reminders);
        });
    }

    private void setUpActionBar(int position) {

        final boolean isMoviesTab = position == 0;
        final boolean isFavoriteTab = position == 1;
        final MovieDto movieDto = mMovieDetailsViewModel.getMovieDto().getValue();
        final boolean isMovieDetails = movieDto != null;

        String title;
        if (isMoviesTab && isMovieDetails) {
            mActionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24);
            mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            mToolbar.setNavigationOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

            title = movieDto.getTitle();
            mSearchItem.setVisible(false);
            mLayoutChangeItem.setVisible(false);
            mPopularMoviesItem.setVisible(false);
            mTopRatedMoviesItem.setVisible(false);
            mUpcomingMoviesItem.setVisible(false);
            mNowPlayingMoviesItem.setVisible(false);
        } else {
            mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            mToolbar.setNavigationOnClickListener(v -> mBinding.drawerLayout.openDrawer(GravityCompat.START));

            title = TAB_TITLE_LIST.get(position);
            mSearchItem.setVisible(isFavoriteTab);
            mLayoutChangeItem.setVisible(isMoviesTab);
            mPopularMoviesItem.setVisible(isMoviesTab);
            mTopRatedMoviesItem.setVisible(isMoviesTab);
            mUpcomingMoviesItem.setVisible(isMoviesTab);
            mNowPlayingMoviesItem.setVisible(isMoviesTab);
        }
        mToolbar.setTitle(title);
    }

    public void updateBadge(int count) {
        TabLayout.Tab tab = mTabLayout.getTabAt(1);
        if (tab != null) {
            BadgeDrawable badge = tab.getOrCreateBadge();
            badge.setVisible(true);
            badge.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
            badge.setBadgeTextColor(ResourcesCompat.getColor(getResources(), android.R.color.white, null));
            String countString = count > 99 ? "99+" : String.valueOf(count);
            badge.setText(countString);
            if (count == 0) {
                badge.setVisible(false);
            }
        }
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int itemId = menuItem.getItemId();
        if (itemId == R.id.layout_change_menu_item) {
            Drawable gridDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_grid_view_24, null);
            Drawable listDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_list_view_24, null);

            boolean isGridView = Boolean.FALSE.equals(mBaseMovieListViewModel.getIsGridView().getValue());
            mBaseMovieListViewModel.setIsGridView(isGridView);
            menuItem.setIcon(isGridView ? listDrawable : gridDrawable);
            return true;
        } else if (itemId == R.id.popular_movies_menu_item) {
            editor.putString(CATEGORY, "popular");
            editor.apply();
            return true;
        } else if (itemId == R.id.top_rated_movies_menu_item) {
            editor.putString(CATEGORY, "top_rated");
            editor.apply();
            return true;
        } else if (itemId == R.id.upcoming_movies_menu_item) {
            editor.putString(CATEGORY, "upcoming");
            editor.apply();
            return true;
        } else if (itemId == R.id.nowplaying_movies_menu_item) {
            editor.putString(CATEGORY, "now_playing");
            editor.apply();
            return true;
        }
        return false;
    }
}