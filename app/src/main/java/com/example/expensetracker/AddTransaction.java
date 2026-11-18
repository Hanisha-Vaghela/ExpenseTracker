package com.example.expensetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddTransaction extends AppCompatActivity {

    EditText amount;
    Button btnsave;
    TextView tvother,tvbills,tvfood,tventertainment, tvinvestments, tvshopping;

    String selectedCategory = "Others";
    TextView[] categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        amount = findViewById(R.id.addtransaction_amount);
        btnsave = findViewById(R.id.addtransaction_btnSave);
        tvother = findViewById(R.id.addtransaction_Others);
        tvbills = findViewById(R.id.addtransaction_Bills);
        tvfood = findViewById(R.id.addtransaction_Food);
        tventertainment = findViewById(R.id.addtransaction_Entertainment);
        tvinvestments = findViewById(R.id.addtransaction_Investments);
        tvshopping = findViewById(R.id.addtransaction_Shopping);

        categoryList = new TextView[]{
                tvother,tvbills,tvfood,tventertainment,tvinvestments,tvshopping
        };

        for (TextView box : categoryList){
            box.setOnClickListener(v -> selectCategory((TextView) v));
        }
        btnsave.setOnClickListener(v-> saveTransaction());
    }

    private void saveTransaction() {
        String newamount = amount.getText().toString();

        if(newamount.isEmpty()){
            Toast.makeText(this,"Please Enter Amount",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent result = new Intent();
        result.putExtra("category",selectedCategory);
        result.putExtra("amount",newamount);
        setResult(RESULT_OK,result);
        finish();
    }

    private void selectCategory(TextView clickedBox) {
        for (TextView box : categoryList) {
            box.setBackgroundColor(0xFFF2F2F2);
        }
        clickedBox.setBackgroundColor(0xFFC5CAE9);
        selectedCategory = clickedBox.getText().toString();
    }
}