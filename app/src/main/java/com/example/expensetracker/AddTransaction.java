package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTransaction extends AppCompatActivity {

    EditText amount;
    Spinner spType;
    RadioGroup rgCategory;
    Button btnsave;
    SQLiteDatabase db;
    SharedPreferences sp;
    int[] inArray = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18};
    String[] nameArray = {"Food and Drink","Groceries","Salary","Transport","Fuel","Shopping","Bills and Utilities","Rent","Entertainment","Health","Education","Travel","Investments","Insurance","Subscriptions","Personal Care","Gifts and Donations","Others"};
//    int[] imageArray = {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        db = openOrCreateDatabase("ExpenseTracker",MODE_PRIVATE,null);
        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        String UserTable = "CREATE TABLE IF NOT EXISTS user(userid INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(50),email VARCHAR(30),password VARCHAR(20) )";
        db.execSQL(UserTable);

        String CategoryTable = "CREATE TABLE IF NOT EXISTS category(categoryid INTEGER PRIMARY KEY AUTOINCREMENT,categoryname VARCHAR(50), categoryicon INTEGER )";
        db.execSQL(CategoryTable);

        String TransactionTable = "CREATE TABLE IF NOT EXISTS transactiontable(transactionid  INTEGER PRIMARY KEY AUTOINCREMENT,userid INTEGER , categoryid INTEGER,amount REAL,transactiontype VARCHAR(10) )";
        db.execSQL(TransactionTable);
//        transaction_name VARCHAR(20),

        amount = findViewById(R.id.addtransaction_amount);
        spType = findViewById(R.id.addtransaction_type);
        rgCategory = findViewById(R.id.addtransaction_category);
        btnsave = findViewById(R.id.addtransaction_btnSave);

        btnsave.setOnClickListener(v -> saveTransaction());
        }

    private void saveTransaction() {
        String newAmount = amount.getText().toString().trim();

        if (newAmount.isEmpty()) {
            Toast.makeText(this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
            return;
        }
        double amountnum;
        try {
            amountnum = Double.parseDouble(newAmount);
        } catch (NumberFormatException e) {
            amount.setError("Enter a valid amount");
            return;
        } if (amountnum <= 0){
            amount.setError("Enter Positive amount");
        }

        int checkedId = rgCategory.getCheckedRadioButtonId();
        if (checkedId == -1) {
            Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = spType.getSelectedItem().toString();   // Expense or Income
        String category = ((RadioButton) findViewById(checkedId)).getText().toString();

//        Intent result = new Intent();
//        result.putExtra("type", type);
//        result.putExtra("category", category);
//        result.putExtra("amount", newAmount);
//        setResult(RESULT_OK, result);

        //database ma thi value ne set karva mate cursor no use thay
        Cursor categorycursor = db.rawQuery("SELECT categoryid FROM category WHERE categoryname = ?",new String[]{category});
        int categoryid = 0;
        if (categorycursor.moveToFirst()){
            categoryid = categorycursor.getInt(0);
        }
        categorycursor.close();

        int userid = sp.getInt(ConstantSP.USERID,0);
        db.execSQL("INSERT INTO TransactionTable" + "(userid,categoryid,amount,transactiontype)" +"VALUES(?,?,?,?)",new Object[]{userid,categoryid,amountnum,type});

        finish();
    }
}