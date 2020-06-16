package com.example.uma_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User extends AppCompatActivity implements Login.onClickForgetPass, Login.onClickSigninButton, Login.onClickRegisterButton, 
                                                        Registration.onClickSignIn, ForgetPass.ForgetPassUser{

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private FirebaseAuth firebaseAuth;

    public String userEmail, password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        firebaseAuth = FirebaseAuth.getInstance();

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.userLayout, new Login());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    @Override
    public void onclickForgetPass() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.userLayout, new ForgetPass());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void getFragmentStackClear(){
        Fragment fragment = fragmentManager.findFragmentById(R.id.userLayout);
        while (fragment != null){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(User.this, "Login Passed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(User.this, Controller.class);
                            startActivity(intent);
                            finish();
                        } 
                        else {
                            Toast.makeText(User.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onclickRegisterButton() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.userLayout, new Registration());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void signup(String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(User.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(User.this, "Login Passed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(User.this, Controller.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(User.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void forgetpass(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.userLayout, new Login());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else {
                    String error = task.getException().toString();
                    Toast.makeText(User.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String exception = e.getMessage().toString();
                Toast.makeText(User.this, "Exception : " + exception, Toast.LENGTH_SHORT).show();
            }
        });
    }


}