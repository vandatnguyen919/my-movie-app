package com.example.mymovieapp.ui.settings;

import static com.example.domain.utils.Constants.CATEGORY;
import static com.example.domain.utils.Constants.DEFAULT_CATEGORY;
import static com.example.domain.utils.Constants.DEFAULT_RATE;
import static com.example.domain.utils.Constants.RATE;
import static com.example.domain.utils.Constants.RELEASE_YEAR;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.mymovieapp.R;
public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences.OnSharedPreferenceChangeListener mSharedPreferenceChangeListener;

    private ListPreference mCategoryPreference;
    private Preference mRatePreference;
    private EditTextPreference mReleaseYearPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        mEditor = mSharedPreferences.edit();
        mSharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (CATEGORY.equals(key)) {
                String category = sharedPreferences.getString(CATEGORY, DEFAULT_CATEGORY);
                setCategoryPreferenceValue(category);
            }
        };
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mSharedPreferenceChangeListener);

        setUpRatePreference();
        setUpReleaseYearPreference();
    }

    // Set the value of the category preference with the actual value to sync to Settings Fragment UI
    private void setCategoryPreferenceValue(String category) {
        mCategoryPreference = findPreference(CATEGORY);
        if (mCategoryPreference != null) {
            mCategoryPreference.setValue(category);
        }
    }

    private void setUpRatePreference() {
        mRatePreference = findPreference(RATE);
        if (mRatePreference != null) {
            int rate = Integer.parseInt(mSharedPreferences.getString(RATE, DEFAULT_RATE));
            mRatePreference.setSummary(rate == 0 ? "" : String.valueOf(rate));
            mRatePreference.setOnPreferenceClickListener(preference -> {
                showRateDialog();
                return true;
            });
        }
    }

    private void setUpReleaseYearPreference() {
        mReleaseYearPreference = findPreference(RELEASE_YEAR);
        if (mReleaseYearPreference != null) {
            mReleaseYearPreference.setOnBindEditTextListener(editText -> {
                // Set input type to number
                editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mSharedPreferenceChangeListener);
    }

    private void showRateDialog() {
        final int currentRate = Integer.parseInt(mSharedPreferences.getString(RATE, DEFAULT_RATE));

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Movie with rate from");

        // Inflate custom layout for seekbar and yes/no buttons
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_rate_seekbar, null);
        SeekBar seekBar = dialogView.findViewById(R.id.seekBar);
        TextView seekValue = dialogView.findViewById(R.id.seek_value);

        seekBar.setProgress(currentRate);
        seekValue.setText(String.valueOf(currentRate));

        // Update text when seekbar changes
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekValue.setText(String.valueOf(progress)); // Show the selected rate value
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        builder.setView(dialogView);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Handle the "Yes" confirmation here
            int selectedRate = seekBar.getProgress();
            if (currentRate != selectedRate) {
                mRatePreference.setSummary(selectedRate == 0 ? "" : String.valueOf(selectedRate));
                mEditor.putString(RATE, String.valueOf(selectedRate));
                mEditor.apply();
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}