package com.example.uma_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();


        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.parentLayout, new Anima());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        new CountDownTimer(SPLASH_SCREEN, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //Toast.makeText(MainActivity.this, ""+"seconds remaining: " + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
            }

            public void onFinish() {
                //Toast.makeText(MainActivity.this, "Finised method ", Toast.LENGTH_SHORT).show();

                PaperOnboardingPage scr1 = new PaperOnboardingPage("Manage Info",
                        "Manage your profile info and give feedback",
                        Color.parseColor("#044fab"), R.drawable.person_icon, R.drawable.ic_person_black_24dp);

                PaperOnboardingPage scr2 = new PaperOnboardingPage("Give Command",
                        "Click on Mic Button to give commands",
                        Color.parseColor("#21d6d3"), R.drawable.mic_icon, R.drawable.ic_mic_black_24dp);

                ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
                elements.add(scr1);
                elements.add(scr2);


                PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(elements);

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.parentLayout, onBoardingFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
                    @Override
                    public void onRightOut() {
                       Intent intent = new Intent(MainActivity.this, User.class);
                       startActivity(intent);
                       finish();
                    }
                });
            }
        }.start();
    }
}