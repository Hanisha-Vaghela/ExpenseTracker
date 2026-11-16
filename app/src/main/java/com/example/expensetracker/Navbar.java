package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navbar extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);

        bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

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
            return true;
        });
    }
}