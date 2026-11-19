package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends Navbar {
    ImageButton ImageButton;

    SQLiteDatabase db;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        setupBottomNav(R.id.home);

        db = openOrCreateDatabase("ExpenseTracker",MODE_PRIVATE,null);
        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        String UserTable = "CREATE TABLE IF NOT EXISTS user(userid INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(50),email VARCHAR(30),password VARCHAR(20) )";
        db.execSQL(UserTable);

        String CategoryTable = "CREATE TABLE IF NOT EXISTS category(categoryid INTEGER PRIMARY KEY AUTOINCREMENT,categoryname VARCHAR(50), categoryicon INTEGER )";
        db.execSQL(CategoryTable);

        String TransactionTable = "CREATE TABLE IF NOT EXISTS transactiontable(transactionid  INTEGER PRIMARY KEY AUTOINCREMENT,userid INTEGER , categoryid INTEGER,transaction_name VARCHAR(20),amount REAL,transactiontype VARCHAR(10) )";
        db.execSQL(TransactionTable);




        ImageButton = findViewById(R.id.btnAddTransaction);

        ImageButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTransaction.class));
        });
    }
}