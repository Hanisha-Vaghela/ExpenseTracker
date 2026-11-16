
package com.example.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class DarkMode {

    private static final String PREF_NAME = "ExpenseTrackerSettings";
    private static final String KEY_DARK_MODE = "darkMode";

    public static void applyMode(Context context) {

        SharedPreferences preferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        boolean isDark = preferences.getBoolean(KEY_DARK_MODE, false);

        AppCompatDelegate.setDefaultNightMode(
                isDark
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
