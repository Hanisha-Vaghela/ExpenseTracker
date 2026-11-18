package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class Login extends AppCompatActivity {

    EditText email ,password;
    Button loginbtn;
    TextView signup,forgotpass;

    SQLiteDatabase db;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = openOrCreateDatabase("ExpenseTracker",MODE_PRIVATE,null);
        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        String UserTable = "CREATE TABLE IF NOT EXISTS user(userid integer PRIMARY KEY AUTOINCREMENT, name VARCHAR(50),email VARCHAR(30),contact VARCHAR(10),password VARCHAR(20) )";
        db.execSQL(UserTable);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginbtn = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.login_SignUp);
        forgotpass = findViewById(R.id.login_forgot);

        loginbtn.setOnClickListener(view -> {
            String email_text = email.getText().toString();
            String pass_text = password.getText().toString();

            if (email_text.isEmpty()){
                email.setError("Enter your email");
                email.requestFocus();
            } else if (pass_text.isEmpty()) {
                password.setError("Enter your password");
                password.requestFocus();
            }
            else{
                String existUser = "SELECT * FROM user WHERE email = '"+email_text+"' AND password = '"+pass_text+"' ";
                Cursor cursor = db.rawQuery(existUser,null);

                if(cursor.getCount()>0){
                    while (cursor.moveToNext()){
                        sp.edit()
                                .putInt(ConstantSP.USERID,cursor.getInt(0))
                                .putString(ConstantSP.NAME,cursor.getString(1))
                                .putString(ConstantSP.EMAIL,cursor.getString(2))
                                .putString(ConstantSP.PASSWORD,cursor.getString(3))
                                .apply();


                    Toast.makeText(this, "Welcome again", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Home.class));
                    finish();

                    }
                }
                else {
                    Toast.makeText(this,"user not exits",Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgotpass.setOnClickListener(view -> {
            startActivity(new Intent(this, ForgotPass.class));
            finish();
        });

        signup.setOnClickListener(view -> {
            startActivity(new Intent(this, SignUp.class));
            finish();
        });

    }
}