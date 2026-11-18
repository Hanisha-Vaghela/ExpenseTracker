package com.example.expensetracker;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Stats extends Navbar {  //AppCompatActivity first one
    PieChart piechart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        setupBottomNav(R.id.stats);
        piechart = findViewById(R.id.piechart);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
            //loop
        for(int i = 1; i <10 ; i++){
            //float ma convert karyu
            float value = (float) (i*10.0);
            PieEntry pieEntry = new PieEntry(i, value);
            //add value in array list upper array list ma thi levanu
            pieEntries.add(pieEntry);// ahiya bracket ma value vadu lidhu (i,value)
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Student");

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        piechart.setData(new PieData(pieDataSet));

        piechart.animateXY(5000,5000);

        piechart.getDescription().setEnabled(false);




    }
}