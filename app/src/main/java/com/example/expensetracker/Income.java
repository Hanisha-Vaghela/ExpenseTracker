package com.example.expensetracker;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Income extends Navbar {

    RecyclerView recyclerIncome;
    SQLiteDatabase db;
    SharedPreferences sp;
    ArrayList<ExpenseList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_income);
        setupBottomNav(-1);

        db = openOrCreateDatabase("ExpenseTracker",MODE_PRIVATE,null);
        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        String UserTable = "CREATE TABLE IF NOT EXISTS user(userid INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(50),email VARCHAR(30),password VARCHAR(20) )";
        db.execSQL(UserTable);

        String CategoryTable = "CREATE TABLE IF NOT EXISTS category(categoryid INTEGER PRIMARY KEY AUTOINCREMENT,categoryname VARCHAR(50), categoryicon INTEGER )";
        db.execSQL(CategoryTable);

        String TransactionTable = "CREATE TABLE IF NOT EXISTS transactiontable(transactionid  INTEGER PRIMARY KEY AUTOINCREMENT,userid INTEGER , categoryid INTEGER, amount REAL,transactiontype VARCHAR(10) )";
        db.execSQL(TransactionTable);

        arrayList = new ArrayList<>();
        recyclerIncome = findViewById(R.id.recyclerIncome);

        recyclerIncome.setLayoutManager(new LinearLayoutManager(this));
        recyclerIncome.setAdapter(new ExpenseAdapter(Income.this,arrayList,"Income"));

        getExpenses();

    }

    private void getExpenses(){
        {
            String query = "SELECT " +
                    "transactiontable.transactionid, " +
                    "transactiontable.amount, " +
                    "category.categoryname, " +
                    "category.categoryicon " +

                    "FROM transactiontable " +

                    "INNER JOIN category " +
                    "ON transactiontable.categoryid = category.categoryid " +

                    "WHERE transactiontable.userid = ? AND transactiontable.transactiontype = 'Income' "+

                    "ORDER BY transactiontable.transactionid DESC";

            Cursor cursor = db.rawQuery(
                    query,
                    new String[]{String.valueOf(sp.getInt(ConstantSP.USERID, 0))}
            );

            while(cursor.moveToNext()){

                ExpenseList List = new ExpenseList();
                List.amount = cursor.getDouble(1);
                List.categoryName = cursor.getString(2);
                List.categoryIcon = cursor.getInt(3);
                arrayList.add(List);
            }

            cursor.close();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (arrayList != null && recyclerIncome != null) {
            arrayList.clear();
            getExpenses();
            recyclerIncome.getAdapter().notifyDataSetChanged();
        }
    }
}