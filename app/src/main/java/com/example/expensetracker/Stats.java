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

public class Stats extends AppCompatActivity {

    BarChart barchart;
    PieChart piechart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        barchart = findViewById(R.id.barchart);
        piechart = findViewById(R.id.piechart);

        // array list
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

//        loop

        for(int i = 1; i <10 ; i++){
            //float ma convert karyu
            float value = (float) (i*10.0);
//            Intialize bar chart entry

            BarEntry barEntry = new BarEntry(i, value);
            PieEntry pieEntry = new PieEntry(i, value);

            //add value in array list upper array list ma thi levanu

            barEntries.add(barEntry); // ahiya bracket ma value vadu lidhu (i,value)
            pieEntries.add(pieEntry);
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Employees");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        barDataSet.setDrawValues(false);
        barchart.setData(new BarData(barDataSet));
        barchart.animateY(5000);

        barchart.getDescription().setText("Employees Chart");
        barchart.getDescription().setTextColor(Color.BLUE);

        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Student");

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        piechart.setData(new PieData(pieDataSet));

        piechart.animateXY(5000,5000);

        piechart.getDescription().setEnabled(false);




    }
}