package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class SplashScreen extends AppCompatActivity {

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if(sp.getString(ConstantSP.NAME,"").isEmpty()){
                startActivity(new Intent(this, SignUp.class));
                finish();
            }
            else{
                startActivity(new Intent(SplashScreen.this, Home.class));
                finish(); // so back button doesn't return to splash
            }
        }, 3000);
    }
}