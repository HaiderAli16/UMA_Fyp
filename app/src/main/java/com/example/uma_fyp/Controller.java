package com.example.uma_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Controller extends AppCompatActivity implements ManageProfile.switchPage, Feedback.switchPage{

    BottomNavigationView bottomNavigationView;

    public VoiceCommand objVC = new VoiceCommand();
    public Feedback objFB = new Feedback();
    public ManageProfile objMP = new ManageProfile();

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
                    selectedFragment = objVC;
                    break;
                case R.id.nav_feedback:
                    selectedFragment = objFB;
                    break;
                case R.id.nav_profile:
                    selectedFragment = objMP;
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

    @Override
    public void pageSwitch() {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.controller, new VoiceCommand());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void pageSwitchHome() {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.controller, new VoiceCommand());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}