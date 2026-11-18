package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends AppCompatActivity {

    EditText name,email,password,confirmpassword;
    Button signup;
    TextView login;
    SharedPreferences sp;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        db = openOrCreateDatabase("ExpenseTracker",MODE_PRIVATE,null);
        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        String UserTable = "CREATE TABLE IF NOT EXISTS user(userid INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(50),email VARCHAR(30),password VARCHAR(20) )";
        db .execSQL(UserTable);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        confirmpassword = findViewById(R.id.signup_confirm);
        signup = findViewById(R.id.signup_btn);
        login = findViewById(R.id.tvLogin);

        signup.setOnClickListener(view ->{
            String name_text = name.getText().toString();
            String email_text = email.getText().toString();
            String pass_text = password.getText().toString();
            String confirmpass_text = confirmpassword.getText().toString();


            if(name_text.isEmpty()){
                name.setError("Enter your name");
                name.requestFocus();
            }
            if (email_text.isEmpty()){
                name.setError("Enter your email");
                email.requestFocus();
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email_text).matches()){
                email.setError("Enter valid email");
                email.requestFocus();
            }
            if(pass_text.isEmpty()){
                password.setError("Enter your password");
                password.requestFocus();
            }
            if (pass_text.length()<6){
                password.setError("Enter atleast 6 digit password");
                password.requestFocus();
            }
            if (confirmpass_text.isEmpty()){
                confirmpassword.setError("your confirm password is empty");
                confirmpassword.requestFocus();
            }
            if (!confirmpass_text.equals(pass_text)) {
                confirmpassword.setError("Password does't match");
                confirmpassword.requestFocus();
            }
            else{
                String CheckUser = "SELECT * FROM user WHERE email = '"+email_text+"' OR password ='"+pass_text+"' ";
                Cursor cursor = db.rawQuery(CheckUser,null);

                if (cursor.getCount()>0){
                    Toast.makeText(this, "user already exists", Toast.LENGTH_SHORT).show();
                }
                else {
                    String insertUser = "INSERT INTO user VALUES(NULL,'"+name_text+"', '"+email_text+"','"+pass_text+"' )  ";
                    db.execSQL(insertUser);
                    sp.edit()
                            .putString(ConstantSP.NAME, name_text)
                            .putString(ConstantSP.EMAIL, email_text)
                            .putString(ConstantSP.PASSWORD,pass_text)
                            .apply();
                    Toast.makeText(this, "user added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,Home.class));
                    finish();
                }
            }
        });


        login.setOnClickListener(view -> {
            startActivity(new Intent(this, Login.class));
            finish();
        });
    }
}