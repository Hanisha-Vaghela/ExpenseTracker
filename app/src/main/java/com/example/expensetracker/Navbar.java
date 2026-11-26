
package com.example.expensetracker;
//
//import android.content.Intent;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;

//public class Navbar extends AppCompatActivity {
//
//    // Call after setContentView()
//    protected void setupBottomNav(int selectedId) {
//
//        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
//
//        // Select or clear navbar selection
//        if (selectedId == -1) {
//
//            bottomNav.getMenu().setGroupCheckable(0, true, false);
//            bottomNav.setSelectedItemId(-1);
//            bottomNav.getMenu().setGroupCheckable(0, true, true);
//
//        } else {
//            bottomNav.setSelectedItemId(selectedId);
//        }
//
//        bottomNav.setOnItemSelectedListener(item -> {
//
//            int id = item.getItemId();
//
//            Intent intent;
//
//            if (id == R.id.home) {
//
//                if (this instanceof Home) return true;
//                intent = new Intent(this, Home.class);
//
//            } else if (id == R.id.budget) {
//
//                if (this instanceof Budget) return true;
//                intent = new Intent(this, Budget.class);
//
//            } else if (id == R.id.stats) {
//
//                if (this instanceof Stats) return true;
//                intent = new Intent(this, Stats.class);
//
//            } else if (id == R.id.more) {
//
//                if (this instanceof More) return true;
//                intent = new Intent(this, More.class);
//
//            } else {
//                return false;
//            }
//
//            intent.addFlags(
//                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                            Intent.FLAG_ACTIVITY_SINGLE_TOP
//            );
//
//            startActivity(intent);
//            overridePendingTransition(0, 0);
//
//            return true;
//        });
//    }
//}



//new code

//package com.example.expensetracker;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navbar extends AppCompatActivity {

    // Call after setContentView()
    protected void setupBottomNav(int selectedId) {

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        // Prevents a crash if the layout has no bottom_nav
        if (bottomNav == null) {
            Log.e("Navbar", "bottom_nav not found in " + getClass().getSimpleName());
            return;
        }

        // Select or clear navbar selection
        if (selectedId == -1) {

            Menu menu = bottomNav.getMenu();
            menu.setGroupCheckable(0, true, false);

            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setChecked(false);
            }

            menu.setGroupCheckable(0, true, true);

        } else {
            bottomNav.setSelectedItemId(selectedId);
        }

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            Class<?> target;

            if (id == R.id.home) {
                target = Home.class;
            } else if (id == R.id.budget) {
                target = Budget.class;
            } else if (id == R.id.stats) {
                target = Stats.class;
            } else if (id == R.id.more) {
                target = More.class;
            } else {
                return false;
            }

            // Already on this screen
            if (target.isInstance(this)) return true;

            Intent intent = new Intent(this, target);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);
            overridePendingTransition(0, 0);

            return true;
        });
    }
}