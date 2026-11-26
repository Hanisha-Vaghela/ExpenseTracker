//package com.example.expensetracker;
//
//import android.graphics.Color;
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//import com.github.mikephil.charting.utils.ColorTemplate;
//
//import java.util.ArrayList;
//
//public class Stats extends Navbar {  //AppCompatActivity first one
//    PieChart piechart;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_stats);
//
//        setupBottomNav(R.id.stats);
//        piechart = findViewById(R.id.piechart);
//
//        ArrayList<PieEntry> pieEntries = new ArrayList<>();
//            //loop
//        for(int i = 1; i <10 ; i++){
//            //float ma convert karyu
//            float value = (float) (i*10.0);
//            PieEntry pieEntry = new PieEntry(i, value);
//            //add value in array list upper array list ma thi levanu
//            pieEntries.add(pieEntry);// ahiya bracket ma value vadu lidhu (i,value)
//        }
//
//        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Student");
//
//        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        piechart.setData(new PieData(pieDataSet));
//
//        piechart.animateXY(5000,5000);
//
//        piechart.getDescription().setEnabled(false);
//
//
//
//
//    }
//}



package com.example.expensetracker;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Stats extends Navbar {
    PieChart piechartExpense, piechartIncome;
    SQLiteDatabase db;
    SharedPreferences sp;
    TextView tvIncome, tvExpense;
    ArrayList<PieEntry> pieEntriesExpense;
    ArrayList<PieEntry> pieEntriesIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        setupBottomNav(R.id.stats);

        piechartExpense = findViewById(R.id.piechartExpense);
        piechartIncome = findViewById(R.id.piechartIncome);
        tvIncome = findViewById(R.id.tvTotalIncome);
        tvExpense = findViewById(R.id.tvTotalExpense);

        db = openOrCreateDatabase("ExpenseTracker", MODE_PRIVATE, null);
        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);

        pieEntriesExpense = new ArrayList<>();
        pieEntriesIncome = new ArrayList<>();

        getCategoryExpenses();
        getCategoryIncome();
        getTotalAmount();
    }

    // EXPENSE CATEGORY-WISE DATA
    private void getCategoryExpenses() {

        pieEntriesExpense.clear();

        int userid = sp.getInt(ConstantSP.USERID, 0);

        String query = "SELECT category.categoryname, " +
                "SUM(transactiontable.amount) " +
                "FROM transactiontable " +
                "INNER JOIN category " +
                "ON transactiontable.categoryid = category.categoryid " +
                "WHERE transactiontable.userid = ? " +
                "AND transactiontable.transactiontype = ? " +
                "GROUP BY category.categoryid";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{String.valueOf(userid), "Expense"}
        );

        while (cursor.moveToNext()) {
            String categoryName = cursor.getString(0);
            float totalAmount = cursor.getFloat(1);
            pieEntriesExpense.add(
                    new PieEntry(totalAmount, categoryName)
            );
        }
        cursor.close();
        showExpensePieChart();
    }

    // INCOME CATEGORY-WISE DATA
    private void getCategoryIncome() {

        pieEntriesIncome.clear();
        int userid = sp.getInt(ConstantSP.USERID, 0);

        String query = "SELECT category.categoryname, " +
                "SUM(transactiontable.amount) " +
                "FROM transactiontable " +
                "INNER JOIN category " +
                "ON transactiontable.categoryid = category.categoryid " +
                "WHERE transactiontable.userid = ? " +
                "AND transactiontable.transactiontype = ? " +
                "GROUP BY category.categoryid";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{String.valueOf(userid), "Income"}
        );

        while (cursor.moveToNext()) {
            String categoryName = cursor.getString(0);
            float totalAmount = cursor.getFloat(1);
            pieEntriesIncome.add(
                    new PieEntry(totalAmount, categoryName)
            );
        }
        cursor.close();
        showIncomePieChart();
    }

    // DISPLAY EXPENSE PIE CHART
    private void showExpensePieChart() {

        if (pieEntriesExpense.isEmpty()) {
            piechartExpense.clear();
            piechartExpense.setNoDataText("No expense data available");
            piechartExpense.invalidate();
            return;
        }

        PieDataSet pieDataSet = new PieDataSet(
                pieEntriesExpense, "Expenses by Category"
        );

        pieDataSet.setColors(
                0xFFE57373,
                0xFFEF5350,
                0xFFE53935,
                0xFFFF8A65,
                0xFFFF7043,
                0xFFD84315,
                0xFFBF360C
        );
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueTextColor(android.graphics.Color.WHITE);

        PieData pieData = new PieData(pieDataSet);

        piechartExpense.setData(pieData);
        piechartExpense.setUsePercentValues(true);
        piechartExpense.getDescription().setEnabled(false);
        piechartExpense.setDrawHoleEnabled(true);
        piechartExpense.setHoleRadius(45f);
        piechartExpense.setTransparentCircleRadius(50f);
        piechartExpense.getLegend().setEnabled(true);
        piechartExpense.getLegend().setTextColor(ContextCompat.getColor(this, R.color.black));
        piechartExpense.setEntryLabelColor(ContextCompat.getColor(this, R.color.black));
        piechartExpense.animateY(1000);
        piechartExpense.invalidate();
    }

    private void showIncomePieChart() {

        if (pieEntriesIncome.isEmpty()) {
            piechartIncome.clear();
            piechartIncome.setNoDataText("No income data available");
            piechartIncome.invalidate();
            return;
        }

        PieDataSet pieDataSet = new PieDataSet(
                pieEntriesIncome, "Income by Category"
        );

        pieDataSet.setColors(
                0xFF81C784,
                0xFF66BB6A,
                0xFF43A047,
                0xFF2E7D32,
                0xFFA5D6A7,
                0xFF388E3C,
                0xFF1B5E20
        );
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueTextColor(android.graphics.Color.WHITE);

        PieData pieData = new PieData(pieDataSet);

        piechartIncome.setData(pieData);
        piechartIncome.setUsePercentValues(true);
        piechartIncome.getDescription().setEnabled(false);
        piechartIncome.setDrawHoleEnabled(true);
        piechartIncome.setHoleRadius(45f);
        piechartIncome.setTransparentCircleRadius(50f);
        piechartIncome.getLegend().setEnabled(true);
        piechartIncome.getLegend().setTextColor(ContextCompat.getColor(this, R.color.black));
        piechartIncome.setEntryLabelColor(ContextCompat.getColor(this, R.color.black));
        piechartIncome.animateY(1000);
        piechartIncome.invalidate();
    }


    private void getTotalAmount() {

        int userid = sp.getInt(ConstantSP.USERID, 0);

        // Total Income
        Cursor incomeCursor = db.rawQuery(
                "SELECT SUM(amount) FROM transactiontable " +
                        "WHERE userid = ? AND transactiontype = ?",
                new String[]{String.valueOf(userid), "Income"}
        );

        if (incomeCursor.moveToFirst()) {
            double totalIncome = incomeCursor.getDouble(0);
            tvIncome.setText("₹ " + totalIncome);
        }

        incomeCursor.close();


        // Total Expense
        Cursor expenseCursor = db.rawQuery(
                "SELECT SUM(amount) FROM transactiontable " +
                        "WHERE userid = ? AND transactiontype = ?",
                new String[]{String.valueOf(userid), "Expense"}
        );

        if (expenseCursor.moveToFirst()) {
            double totalExpense = expenseCursor.getDouble(0);
            tvExpense.setText("₹ " + totalExpense);
        }

        expenseCursor.close();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (piechartExpense != null && piechartIncome != null) {
            getCategoryExpenses();
            getCategoryIncome();
            getTotalAmount();
        }
    }
}

