package com.example.mymovieapp.ui.adapters;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.mymovieapp.R;

public class BindingAdapters {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
            .load(imageUrl)
            .placeholder(R.drawable.default_image_gray_24dp)
            .error(R.drawable.error_image_gray_24)
            .into(view);
    }
}
