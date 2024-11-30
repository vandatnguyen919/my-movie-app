package com.example.mymovieapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Handle the splash screen for API 31+
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // Navigate to MainActivity after the splash screen
        startActivity(new Intent(this, MainActivity.class));
        finish(); // Close SplashActivity
    }
}

