
package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
//change here AppCompatActivity to navbar
public class More extends Navbar {

    TextView Name, Email, tvWeekStart;
    Button Delete,logout;
    SwitchMaterial switchDark;
    View rowWeekStart;
    RadioGroup rgTimeFormat;
    RadioButton rb12, rb24;
    SharedPreferences sp;
    SQLiteDatabase db;

    private static final String PREF_NAME = "ExpenseTrackerSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        // for bottom navbar
        setupBottomNav(R.id.more);

        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);
        db = openOrCreateDatabase(ConstantSP.PREF,MODE_PRIVATE,null);

        String UserTable = "CREATE TABLE IF NOT EXISTS user (userid integer PRIMARY KEY AUTOINCREMENT,name VARCHAR(50),email VARCHAR(30),password VARCHAR(20))";
        db.execSQL(UserTable);



        // Initialize views
        Delete = findViewById(R.id.more_delete);
        logout = findViewById(R.id.more_logoutbtn);
        Name = findViewById(R.id.more_name);
        Email = findViewById(R.id.tvEmail);
        tvWeekStart = findViewById(R.id.tvWeekStart);
        switchDark = findViewById(R.id.switchDark);
        rowWeekStart = findViewById(R.id.rowWeekStart);
        rgTimeFormat = findViewById(R.id.rgTimeFormat);
        rb12 = findViewById(R.id.rb12);
        rb24 = findViewById(R.id.rb24);

        Name.setText("Welcome, " + sp.getString(ConstantSP.NAME, ""));
        Email.setText(sp.getString(ConstantSP.EMAIL, ""));

        logout.setOnClickListener(view -> {
            sp.edit().clear().apply();
            startActivity(new Intent(this, Login.class));
            finish();
        });
        Delete.setOnClickListener(view -> {
            db.execSQL("DELETE FROM user WHERE userid = "+sp.getInt(ConstantSP.USERID,0));
            sp.edit().clear().apply();
            Snackbar.make(view,"Sure to delete your account",Snackbar.LENGTH_SHORT).setAction("yes", v -> {
                startActivity(new Intent(this,Login.class));
                finish();
            }).show();
        });


        // Load saved settings
        loadProfile();
        loadWeekStart();
        loadTimeFormat();
        loadDarkMode();

        // Edit Profile
//        btnEditProfile.setOnClickListener(v -> editProfile());

        // Dark Mode
        switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {

            sp.edit()
                    .putBoolean("darkMode", isChecked)
                    .apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Week Start
        rowWeekStart.setOnClickListener(v -> selectWeekStart());

        // Time Format
        rgTimeFormat.setOnCheckedChangeListener((group, checkedId) -> {

            String format;

            if (checkedId == R.id.rb12) {
                format = "12-hour";
            } else {
                format = "24-hour";
            }

            sp.edit()
                    .putString("timeFormat", format)
                    .apply();

            Toast.makeText(this,
                    format + " format selected",
                    Toast.LENGTH_SHORT).show();
        });
    }

    // Load Profile
    private void loadProfile() {


//        String name = sp.getString("userName", "Your Name");
//        Name.setText("Welcome" +sp.getString(ConstantSP.NAME,""));
//        tvEmail.setText("Email"+sp.getString(ConstantSP.EMAIL,""));
//        String email = sp.getString("userEmail", "your@email.com");

//        tvName.setText(name);
//        tvEmail.setText(email);
    }



    // Load Week Start
    private void loadWeekStart() {

        String week = sp.getString("weekStart", "Monday");
        tvWeekStart.setText(week);
    }

    // Select Week Start
    private void selectWeekStart() {

        String[] options = {"Monday", "Sunday"};

        String current = sp.getString("weekStart", "Monday");

        int selected = current.equals("Sunday") ? 1 : 0;

        new AlertDialog.Builder(this)
                .setTitle("Week starts on")
                .setSingleChoiceItems(options, selected,
                        (dialog, which) -> {

                            String week = options[which];

                            sp.edit()
                                    .putString("weekStart", week)
                                    .apply();

                            tvWeekStart.setText(week);

                            dialog.dismiss();

                            Toast.makeText(this,
                                    "Week starts on " + week,
                                    Toast.LENGTH_SHORT).show();
                        })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Load Time Format
    private void loadTimeFormat() {

        String format = sp.getString("timeFormat", "12-hour");

        if (format.equals("24-hour")) {
            rb24.setChecked(true);
        } else {
            rb12.setChecked(true);
        }
    }

    // Load Dark Mode
    private void loadDarkMode() {

        boolean isDark = sp.getBoolean("darkMode", false);

        switchDark.setChecked(isDark);
    }
}
