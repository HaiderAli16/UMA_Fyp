package com.example.uma_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Controller extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav_listner);

        getSupportFragmentManager().beginTransaction().replace(R.id.controller, new VoiceCommand()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav_listner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId())
            {
                case R.id.nav_home:
                    selectedFragment = new VoiceCommand();
                    break;
                case R.id.nav_feedback:
                    selectedFragment = new Feedback();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ManageProfile();
                break;
                case R.id.nav_logout:
                    Intent intent = new Intent(Controller.this, User.class);
                    startActivity(intent);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.controller, selectedFragment).commit();

            return true;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Controller.this, User.class);
        startActivity(intent);
    }
}