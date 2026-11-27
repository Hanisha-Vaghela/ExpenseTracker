package com.example.expensetracker;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTransaction extends AppCompatActivity {

    EditText amount;
    Spinner spType, spCategory, spPayment;
    Button btnsave;
    SQLiteDatabase db;
    SharedPreferences sp;

    String[] types = {"Expense", "Income"};

    String[] expenseCategories = {"Select Category", "Food and Drinks", "Groceries",
            "Accommodation", "Transport", "Fuel", "Health", "Shopping",
            "Bills and Utilities", "Entertainment", "Education", "Travel",
            "Insurance", "Subscriptions", "Personal Care", "Gifts and Donations",
            "Others"};

    String[] incomeCategories = {"Select Category", "Salary", "Bonus", "Gift",
            "Freelance", "Business", "Investments", "Interest", "Rental Income",
            "Refund", "Others"};

    String[] paymentModes = {"Select Payment Mode", "Cash", "UPI", "Debit Card",
            "Credit Card", "Net Banking", "Wallet"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        db = openOrCreateDatabase("ExpenseTracker", MODE_PRIVATE, null);
        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);

        String UserTable = "CREATE TABLE IF NOT EXISTS user(userid INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(50),email VARCHAR(30),password VARCHAR(20) )";
        db.execSQL(UserTable);

        String CategoryTable = "CREATE TABLE IF NOT EXISTS category(categoryid INTEGER PRIMARY KEY AUTOINCREMENT,categoryname VARCHAR(50), categoryicon INTEGER )";
        db.execSQL(CategoryTable);

        String TransactionTable = "CREATE TABLE IF NOT EXISTS transactiontable(transactionid  INTEGER PRIMARY KEY AUTOINCREMENT,userid INTEGER , categoryid INTEGER,amount REAL,transactiontype VARCHAR(10) )";
        db.execSQL(TransactionTable);

        // Adds the new column to your table without deleting old data
        addPaymentColumn();

        amount = findViewById(R.id.addtransaction_amount);
        spType = findViewById(R.id.addtransaction_type);
        spCategory = findViewById(R.id.addtransaction_category);
        spPayment = findViewById(R.id.addtransaction_payment);
        btnsave = findViewById(R.id.addtransaction_btnSave);

        setDropdown(spType, types);
        setDropdown(spPayment, paymentModes);

        // Change categories when Income / Expense is selected
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (types[position].equals("Income")) {
                    setDropdown(spCategory, incomeCategories);
                } else {
                    setDropdown(spCategory, expenseCategories);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnsave.setOnClickListener(v -> saveTransaction());
    }

    private void setDropdown(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                items
        );
        spinner.setAdapter(adapter);
    }

    private void addPaymentColumn() {
        boolean exists = false;
        Cursor cursor = db.rawQuery("PRAGMA table_info(transactiontable)", null);
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals("paymentmode")) {
                exists = true;
            }
        }
        cursor.close();

        if (!exists) {
            db.execSQL("ALTER TABLE transactiontable ADD COLUMN paymentmode VARCHAR(20)");
        }
    }

    // Returns the category id. Adds the category first if it is new.
    private int getCategoryId(String category) {
        Cursor cursor = db.rawQuery(
                "SELECT categoryid FROM category WHERE categoryname = ?",
                new String[]{category});

        int categoryid;
        if (cursor.moveToFirst()) {
            categoryid = cursor.getInt(0);
        } else {
            db.execSQL("INSERT INTO category(categoryname, categoryicon) VALUES(?, ?)",
                    new Object[]{category, R.drawable.ic_bills});
            categoryid = getCategoryId(category);
        }
        cursor.close();
        return categoryid;
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
        }

        if (amountnum <= 0) {
            amount.setError("Enter Positive amount");
            return;
        }

        // Position 0 is the "Select..." text
        if (spCategory.getSelectedItemPosition() <= 0) {
            Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spPayment.getSelectedItemPosition() <= 0) {
            Toast.makeText(this, "Please Select Payment Mode", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = spType.getSelectedItem().toString();
        String category = spCategory.getSelectedItem().toString();
        String paymentMode = spPayment.getSelectedItem().toString();

        int categoryid = getCategoryId(category);
        int userid = sp.getInt(ConstantSP.USERID, 0);

        db.execSQL("INSERT INTO transactiontable(userid,categoryid,amount,transactiontype,paymentmode) VALUES(?,?,?,?,?)",
                new Object[]{userid, categoryid, amountnum, type, paymentMode});

        finish();
    }
}
