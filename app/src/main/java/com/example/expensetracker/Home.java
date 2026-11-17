package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends Navbar {
    ImageButton ImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        setupBottomNav(R.id.home);

        ImageButton btnAddTransaction = findViewById(R.id.btnAddTransaction);

        btnAddTransaction.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTransaction.class));
        });
    }
}