package com.example.mymovieapp.ui.profile;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.domain.model.UserProfileDto;
import com.example.mymovieapp.R;
import com.example.mymovieapp.databinding.FragmentProfileBinding;
import com.example.mymovieapp.di.MyApplication;
import com.example.mymovieapp.utils.BitmapUtils;
import com.example.mymovieapp.utils.Validator;

import java.io.IOException;
import java.util.Calendar;

import javax.inject.Inject;

public class UserProfileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    private FragmentProfileBinding mBinding;

    @Inject
    public UserProfileViewModel mUserProfileViewModel;

    private NavController mNavController;

    private OnBackPressedCallback mOnBackPressedCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MyApplication) requireActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentProfileBinding.inflate(inflater, container, false);

        mBinding.backArrowIcon.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        mBinding.profileImageView.setOnClickListener(this::showPopupMenu);
        mBinding.cancelProfileEditButton.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
        mBinding.doneProfileEditButton.setOnClickListener(v -> {
            // Validate email
            String email = mBinding.profileEmailEditText.getText().toString();
            if (!Validator.isValidEmail(email)) {
                Toast.makeText(requireContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            UserProfileDto userProfileDto = new UserProfileDto();
            userProfileDto.setAvatar(BitmapUtils.encodeToBase64(BitmapUtils.getBitmapFromImageView(mBinding.profileImageView)));
            userProfileDto.setName(mBinding.profileNameEditText.getText().toString());
            userProfileDto.setEmail(mBinding.profileEmailEditText.getText().toString());
            userProfileDto.setBirthDate(mBinding.profileBirthDateText.getText().toString());
            String gender = "";
            if (mBinding.radioMale.isChecked()) {
                gender = "Male";
            } else if (mBinding.radioFemale.isChecked()) {
                gender = "Female";
            }
            userProfileDto.setGender(gender);
            mUserProfileViewModel.saveUserProfile(userProfileDto);
            mNavController.popBackStack(R.id.main_fragment, false);
        });
        mBinding.profileBirthDateText.setOnClickListener(v -> showDatePickerDialog());

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        mUserProfileViewModel.getUserProfile().observe(getViewLifecycleOwner(), userProfileDto -> {
            if (userProfileDto.getAvatar() != null) {
                mBinding.profileImageView.setImageBitmap(BitmapUtils.decodeFromBase64(userProfileDto.getAvatar()));
            }
            mBinding.profileNameEditText.setText(userProfileDto.getName());
            mBinding.profileEmailEditText.setText(userProfileDto.getEmail());
            mBinding.profileBirthDateText.setText(userProfileDto.getBirthDate());
            if ("Male".equals(userProfileDto.getGender())) {
                mBinding.radioMale.setChecked(true);
            } else if ("Female".equals(userProfileDto.getGender())) {
                mBinding.radioFemale.setChecked(true);
            }
        });
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

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.popup_camera) {
            checkCameraPermission();
            return true;
        }  else if (item.getItemId() == R.id.popup_gallery) {
            checkGalleryPermission();
            return true;
        }
        return false;
    }

    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int GALLERY_PERMISSION_CODE = 102;
    private int mCurrentPermissionRequest;

    // Register for Activity Result to capture an image from the camera
    private final ActivityResultLauncher<Intent> CAPTURE_IMAGE_LAUNCHER = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        mBinding.profileImageView.setImageBitmap(imageBitmap);
                    }
                }
            }
    );

    private final ActivityResultLauncher<String> GALLERY_LAUNCHER = registerForActivityResult(
            new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        Bitmap imageBitmap;
                        try {
                            imageBitmap = BitmapUtils.getBitmapFromUri(requireContext(), uri);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        mBinding.profileImageView.setImageBitmap(imageBitmap);
                    }
                }
            }
    );

    private final ActivityResultLauncher<String> REQUEST_PERMISSION_LAUNCHER  = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    if (mCurrentPermissionRequest == CAMERA_PERMISSION_CODE) {
                        openCamera();
                    } else if (mCurrentPermissionRequest == GALLERY_PERMISSION_CODE) {
                        openGallery();
                    }
                }
            }
    );

    private void checkCameraPermission() {
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }

    private void checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission(Manifest.permission.READ_MEDIA_IMAGES, GALLERY_PERMISSION_CODE);
        } else {
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, GALLERY_PERMISSION_CODE);
        }
    }

    private void checkPermission(String permission, int requestCode) {
        mCurrentPermissionRequest = requestCode;
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_DENIED) {
            REQUEST_PERMISSION_LAUNCHER.launch(permission);
        } else {
            if (requestCode == CAMERA_PERMISSION_CODE) {
                openCamera();
            } else if (requestCode == GALLERY_PERMISSION_CODE) {
                openGallery();
            }
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        CAPTURE_IMAGE_LAUNCHER.launch(takePictureIntent);
    }

    private void openGallery() {
        GALLERY_LAUNCHER.launch("image/*");
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (!mBinding.profileBirthDateText.getText().toString().isEmpty()) {
            String[] dateParts = mBinding.profileBirthDateText.getText().toString().split("/");
            year = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]) - 1;
            day = Integer.parseInt(dateParts[2]);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(), android.R.style.Theme_Holo_Dialog,
                (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                    mBinding.profileBirthDateText.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
}