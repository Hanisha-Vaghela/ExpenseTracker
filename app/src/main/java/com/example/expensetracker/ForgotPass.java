package com.example.expensetracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPass extends AppCompatActivity {

    EditText email,newpassword,confirmnewpassword;
    Button resetpassword;
    TextView loginback;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        db = openOrCreateDatabase("ExpenseTracker",MODE_PRIVATE,null);
        String UserTable = "CREATE TABLE IF NOT EXISTS user(userid integer PRIMARY KEY AUTOINCREMENT, name VARCHAR(50),email VARCHAR(30), contact VARCHAR(10),password VARCHAR(20) )";
        db.execSQL(UserTable);

        email = findViewById(R.id.forgot_email);
        newpassword = findViewById(R.id.forgot_newpassword);
        confirmnewpassword = findViewById(R.id.forgot_confirmnewpassword);
        resetpassword = findViewById(R.id.forgot_resetbtn);
        loginback = findViewById(R.id.forgot_loginback);

        resetpassword.setOnClickListener(view -> {

            String email_text = email.getText().toString().trim();
            String newpassword_text = newpassword.getText().toString().trim();
            String confirmnewpassword_text = confirmnewpassword.getText().toString().trim();

            if (email_text.isEmpty()){
                email.setError("Enter your Email!");
                email.requestFocus();
            }
            else if(newpassword_text.isEmpty()){
                newpassword.setError("Enter your password");
                newpassword.requestFocus();
            } else if (newpassword_text.length()<6) {
                newpassword.setError("Enter Valid Password");
                newpassword.requestFocus();
            }
            else if(confirmnewpassword_text.isEmpty()){
                confirmnewpassword.setError("Enter Your Password");
                confirmnewpassword.requestFocus();
            }
            else if (!newpassword_text.equals(confirmnewpassword_text)) {
            confirmnewpassword.setError("password do not match");
            confirmnewpassword.requestFocus();
            }
            else{
                String CheckUser = "SELECT * FROM user WHERE email = '"+email_text+"' ";
                Cursor cursor = db.rawQuery(CheckUser,null);
                if (cursor.getCount() > 0){
                    String updatePassword = "UPDATE user SET password = '"+newpassword_text+"' WHERE email ='"+email_text+"' ";
                    db.execSQL(updatePassword);
                    Toast.makeText(this, "Password Changed Successfully...!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Login.class));
                    finish();
                }else{
                    email.setError("User does not exist");
                    email.requestFocus();
                }
            }

        });

        loginback.setOnClickListener(view -> {
            startActivity(new Intent(this, Login.class));
            finish();
        });

    }
}