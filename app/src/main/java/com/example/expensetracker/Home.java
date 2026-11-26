package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Home extends Navbar {
    ImageButton ImageButton;
    Button income, expense;

    SQLiteDatabase db;
    SharedPreferences sp;
    RecyclerView transactionRV;

    String[] nameArray = {"Food and Drinks","Groceries","Salary","Transport","Fuel","Shopping","Bills and Utilities","Rent","Entertainment","Health","Education","Travel","Investments","Insurance","Subscriptions","Personal Care","Gifts and Donations","Others"};
    int[] imageArray = {R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills,
            R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills,
            R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills,R.drawable.ic_bills};
    ArrayList<TransactionList> arrayList;

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

        String TransactionTable = "CREATE TABLE IF NOT EXISTS transactiontable(transactionid  INTEGER PRIMARY KEY AUTOINCREMENT,userid INTEGER , categoryid INTEGER, amount REAL,transactiontype VARCHAR(10) )";
        db.execSQL(TransactionTable);

        transactionRV = findViewById(R.id.recyclerTransactions);
        ImageButton = findViewById(R.id.btnAddTransaction);
        income = findViewById(R.id.btnIncome);
        expense = findViewById(R.id.btnExpense);

        income.setOnClickListener(v -> {
            startActivity(new Intent(this, Income.class));
        });

        expense.setOnClickListener(v -> {
            startActivity(new Intent(this, Expense.class));
        });

        ImageButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTransaction.class));
        });

        for(int i=0; i< nameArray.length;i++){
            String checkCategory = "SELECT * FROM category WHERE categoryname = '"+nameArray[i]+"' ";
            Cursor cursor = db.rawQuery(checkCategory,null);

            if(cursor.getCount()==0){
                String insertCategory = "INSERT INTO category VALUES(NULL, '"+nameArray[i]+"', '"+imageArray[i]+"')";
                db.execSQL(insertCategory);
            }
        }

        arrayList = new ArrayList<>();
        transactionRV.setLayoutManager(new LinearLayoutManager(this));
        getTransactions();

        transactionRV.setAdapter(new TransactionAdapter(Home.this,arrayList));
    }

    private void getTransactions(){
        {

            String query = "SELECT " +
                    "transactiontable.transactionid, " +
                    "transactiontable.amount, " +
                    "transactiontable.transactiontype, " +
                    "category.categoryname, " +
                    "category.categoryicon " +

                    "FROM transactiontable " +

                    "INNER JOIN category " +
                    "ON transactiontable.categoryid = category.categoryid " +

                    "WHERE transactiontable.userid = ? " +

                    "ORDER BY transactiontable.transactionid DESC";

            Cursor cursor = db.rawQuery(
                    query,
                    new String[]{String.valueOf(sp.getInt(ConstantSP.USERID, 0))}
            );

            while(cursor.moveToNext()){

                TransactionList List = new TransactionList();
                List.amount = cursor.getDouble(1);
                List.type = cursor.getString(2);
                List.categoryName = cursor.getString(3);
                List.categoryIcon = cursor.getInt(4);
                arrayList.add(List);
            }

            cursor.close();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (arrayList != null && transactionRV != null) {
            arrayList.clear();
            getTransactions();
            transactionRV.getAdapter().notifyDataSetChanged();
        }
    }
}