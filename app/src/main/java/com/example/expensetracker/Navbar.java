package com.example.expensetracker;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navbar extends AppCompatActivity {

    // Call this in every page, after setContentView()
    protected void setupBottomNav(int selectedId) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setSelectedItemId(selectedId);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == selectedId) {
                return true;
            }

            if (id == R.id.home) {
                startActivity(new Intent(this, Home.class));
            } else if (id == R.id.budget) {
                startActivity(new Intent(this, Budget.class));
            } else if (id == R.id.stats) {
                startActivity(new Intent(this, Stats.class));
            } else if (id == R.id.more) {
                startActivity(new Intent(this, More.class));
            } else {
                return false;
            }

            overridePendingTransition(0, 0);
//            finish();
            return true;
        });
    }
}