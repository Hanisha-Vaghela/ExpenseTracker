package com.example.expensetracker;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Budget extends Navbar {

    SQLiteDatabase db;
    SharedPreferences sp;
    TextView tvTotal, tvSpent, tvLeft, tvNoBudget;
    ProgressBar progressTotal;
    Button btnAdd;
    RecyclerView rvBudget;
    ArrayList<BudgetList> arrayList;
    BudgetAdapter adapter;

    // Same names as the expense categories in AddTransaction
    String[] categories = {"Food and Drinks", "Groceries", "Accommodation", "Transport",
            "Fuel", "Health", "Shopping", "Bills and Utilities", "Entertainment",
            "Education", "Travel", "Insurance", "Subscriptions", "Personal Care",
            "Gifts and Donations", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget);
        setupBottomNav(R.id.budget);

        // Keeps content below the status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, 0);
            return insets;
        });

        db = openOrCreateDatabase("ExpenseTracker", MODE_PRIVATE, null);
        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);

        db.execSQL("CREATE TABLE IF NOT EXISTS category(categoryid INTEGER PRIMARY KEY AUTOINCREMENT,categoryname VARCHAR(50), categoryicon INTEGER )");
        db.execSQL("CREATE TABLE IF NOT EXISTS transactiontable(transactionid INTEGER PRIMARY KEY AUTOINCREMENT,userid INTEGER, categoryid INTEGER, amount REAL, transactiontype VARCHAR(10) )");
        db.execSQL("CREATE TABLE IF NOT EXISTS budget(budgetid INTEGER PRIMARY KEY AUTOINCREMENT, userid INTEGER, categoryname VARCHAR(50), amount REAL, UNIQUE(userid, categoryname))");

        tvTotal = findViewById(R.id.tvBudgetTotal);
        tvSpent = findViewById(R.id.tvBudgetSpent);
        tvLeft = findViewById(R.id.tvBudgetLeft);
        tvNoBudget = findViewById(R.id.tvNoBudget);
        progressTotal = findViewById(R.id.progressBudgetTotal);
        btnAdd = findViewById(R.id.btnAddBudget);
        rvBudget = findViewById(R.id.rvBudget);

        arrayList = new ArrayList<>();
        adapter = new BudgetAdapter(arrayList, new BudgetAdapter.OnBudgetClick() {
            @Override
            public void onEdit(BudgetList item) {
                showBudgetDialog(item);
            }

            @Override
            public void onDelete(BudgetList item) {
                confirmDelete(item);
            }
        });
        rvBudget.setLayoutManager(new LinearLayoutManager(this));
        rvBudget.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> showBudgetDialog(null));
        // Data is loaded in onResume()
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBudgets();
    }

    private int getUserId() {
        return sp.getInt(ConstantSP.USERID, 0);
    }

    private void loadBudgets() {
        arrayList.clear();

        String query = "SELECT budget.categoryname, budget.amount, " +
                "IFNULL((SELECT SUM(transactiontable.amount) " +
                "FROM transactiontable " +
                "INNER JOIN category " +
                "ON transactiontable.categoryid = category.categoryid " +
                "WHERE transactiontable.userid = budget.userid " +
                "AND transactiontable.transactiontype = 'Expense' " +
                "AND category.categoryname = budget.categoryname), 0) " +
                "FROM budget " +
                "WHERE budget.userid = ? " +
                "ORDER BY budget.budgetid";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(getUserId())});

        double totalLimit = 0;
        double totalSpent = 0;

        while (cursor.moveToNext()) {
            BudgetList item = new BudgetList(
                    cursor.getString(0),
                    cursor.getDouble(1),
                    cursor.getDouble(2));
            totalLimit += item.limit;
            totalSpent += item.spent;
            arrayList.add(item);
        }
        cursor.close();

        adapter.notifyDataSetChanged();
        tvNoBudget.setVisibility(arrayList.isEmpty() ? View.VISIBLE : View.GONE);

        int color = BudgetAdapter.statusColor(totalSpent, totalLimit);
        int used = totalLimit > 0 ? (int) Math.floor(totalSpent * 100 / totalLimit) : 0;

        tvTotal.setText(BudgetAdapter.money(totalLimit));
        tvSpent.setText("Spent: " + BudgetAdapter.money(totalSpent));
        progressTotal.setProgress(Math.min(used, 100));
        progressTotal.setProgressTintList(ColorStateList.valueOf(color));

        if (totalSpent > totalLimit) {
            tvLeft.setText("Over budget by " + BudgetAdapter.money(totalSpent - totalLimit));
        } else {
            tvLeft.setText("Remaining: " + BudgetAdapter.money(totalLimit - totalSpent));
        }
        tvLeft.setTextColor(color);
    }

    // item is null for a new budget, otherwise we are editing it
    private void showBudgetDialog(BudgetList item) {
        View view = getLayoutInflater().inflate(R.layout.dialog_budget, null);
        Spinner spCategory = view.findViewById(R.id.spBudgetCategory);
        EditText etAmount = view.findViewById(R.id.etBudgetAmount);

        String[] options = item == null ? categories : new String[]{item.categoryName};
        spCategory.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options));

        if (item != null) {
            etAmount.setText(java.math.BigDecimal.valueOf(item.limit).toPlainString());
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(item == null ? "Set Budget" : "Edit Budget")
                .setView(view)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .show();

        // Set here so the dialog stays open if the amount is wrong
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            double value;
            try {
                value = Double.parseDouble(etAmount.getText().toString().trim());
            } catch (NumberFormatException e) {
                etAmount.setError("Enter a valid amount");
                return;
            }

            if (value <= 0) {
                etAmount.setError("Enter Positive amount");
                return;
            }

            // Updates the old budget if this category already has one
            String category = spCategory.getSelectedItem().toString();
            db.execSQL("INSERT OR REPLACE INTO budget(budgetid, userid, categoryname, amount) " +
                            "VALUES((SELECT budgetid FROM budget WHERE userid = ? AND categoryname = ?), ?, ?, ?)",
                    new Object[]{getUserId(), category, getUserId(), category, value});

            Toast.makeText(this, "Budget saved", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            loadBudgets();
        });
    }

    private void confirmDelete(BudgetList item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete budget?")
                .setMessage("Remove the budget for " + item.categoryName + "?")
                .setPositiveButton("Delete", (d, which) -> {
                    db.execSQL("DELETE FROM budget WHERE userid = ? AND categoryname = ?",
                            new Object[]{getUserId(), item.categoryName});
                    loadBudgets();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
