
package com.example.expensetracker;

import android.content.SharedPreferences;
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

import com.google.android.material.switchmaterial.SwitchMaterial;
//change here AppCompatActivity to navbar
public class More extends Navbar {

    TextView tvName, tvEmail, tvWeekStart;
    Button btnEditProfile;
    SwitchMaterial switchDark;
    View rowWeekStart;
    RadioGroup rgTimeFormat;
    RadioButton rb12, rb24;

    SharedPreferences preferences;

    private static final String PREF_NAME = "ExpenseTrackerSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
// for bottom navbar
        setupBottomNav(R.id.more);

        // Initialize SharedPreferences
        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Initialize views
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvWeekStart = findViewById(R.id.tvWeekStart);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        switchDark = findViewById(R.id.switchDark);

        rowWeekStart = findViewById(R.id.rowWeekStart);

        rgTimeFormat = findViewById(R.id.rgTimeFormat);
        rb12 = findViewById(R.id.rb12);
        rb24 = findViewById(R.id.rb24);

        // Load saved settings
        loadProfile();
        loadWeekStart();
        loadTimeFormat();
        loadDarkMode();

        // Edit Profile
        btnEditProfile.setOnClickListener(v -> editProfile());

        // Dark Mode
        switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {

            preferences.edit()
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

            preferences.edit()
                    .putString("timeFormat", format)
                    .apply();

            Toast.makeText(this,
                    format + " format selected",
                    Toast.LENGTH_SHORT).show();
        });
    }

    // Load Profile
    private void loadProfile() {

        String name = preferences.getString("userName", "Your Name");
        String email = preferences.getString("userEmail", "your@email.com");

        tvName.setText(name);
        tvEmail.setText(email);
    }

    // Edit Profile
    private void editProfile() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 10);

        EditText etName = new EditText(this);
        etName.setHint("Enter your name");
        etName.setSingleLine(true);
        etName.setText(tvName.getText().toString());

        EditText etEmail = new EditText(this);
        etEmail.setHint("Enter your email");
        etEmail.setSingleLine(true);
        etEmail.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etEmail.setText(tvEmail.getText().toString());

        layout.addView(etName);
        layout.addView(etEmail);

        new AlertDialog.Builder(this)
                .setTitle("Edit Profile")
                .setView(layout)
                .setPositiveButton("Save", (dialog, which) -> {

                    String name = etName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();

                    if (name.isEmpty() || email.isEmpty()
                            || !android.util.Patterns.EMAIL_ADDRESS
                            .matcher(email).matches()) {

                        Toast.makeText(this,
                                "Enter a valid name and email",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    preferences.edit()
                            .putString("userName", name)
                            .putString("userEmail", email)
                            .apply();

                    loadProfile();

                    Toast.makeText(this,
                            "Profile updated successfully",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Load Week Start
    private void loadWeekStart() {

        String week = preferences.getString("weekStart", "Monday");
        tvWeekStart.setText(week);
    }

    // Select Week Start
    private void selectWeekStart() {

        String[] options = {"Monday", "Sunday"};

        String current = preferences.getString("weekStart", "Monday");

        int selected = current.equals("Sunday") ? 1 : 0;

        new AlertDialog.Builder(this)
                .setTitle("Week starts on")
                .setSingleChoiceItems(options, selected,
                        (dialog, which) -> {

                            String week = options[which];

                            preferences.edit()
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

        String format = preferences.getString("timeFormat", "12-hour");

        if (format.equals("24-hour")) {
            rb24.setChecked(true);
        } else {
            rb12.setChecked(true);
        }
    }

    // Load Dark Mode
    private void loadDarkMode() {

        boolean isDark = preferences.getBoolean("darkMode", false);

        switchDark.setChecked(isDark);
    }
}
