package com.example.uma_fyp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Registration extends Fragment {

    AlertDialog.Builder builder;
    ProgressDialog progressDialog;

    public onClickSignIn obj;

    public String email, password, confirmPassword;

    public AutoCompleteTextView Email, Password, ConfrimPassword;
    public TextView switchtologin;
    public Button signIn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registration, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        builder = new AlertDialog.Builder(getContext());
        progressDialog = new ProgressDialog(getContext());

        Email = getActivity().findViewById(R.id.userEmail);
        Password = getActivity().findViewById(R.id.userPassword);
        ConfrimPassword = getActivity().findViewById(R.id.userConfirmpassword);
        signIn = getActivity().findViewById(R.id.btnSignUp);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    progressDialog.setTitle("Register Account");
                    progressDialog.setMessage("Creating Your Account . . .");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    obj.signup(Email.getText().toString(), Password.getText().toString());
                }
                Email.setText("");
                Password.setText("");
                ConfrimPassword.setText("");
                return;

            }
        });
    }

    private boolean validate() {
        email = Email.getText().toString();
        password = Password.getText().toString();
        confirmPassword = ConfrimPassword.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
            builder.setMessage("Please Fill Every Field . . !")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Error");
            alert.show();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches())
        {
            builder.setMessage("Please Enter Valid Email . . !")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Error");
            alert.show();
            Email.requestFocus();
            return false;
        }
        else if (password.length()<8 || confirmPassword.length()<8)
        {
            builder.setMessage("Min Password Length is 8 . . !")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Error");
            alert.show();
            Password.requestFocus();
            return false;
        }
        else if (!(password.equals(confirmPassword))){
            builder.setMessage("Passwords are not matching . . !")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Error");
            alert.show();
            Password.requestFocus();
            return false;
        }
        else {
            return true;
        }

    }

    public interface onClickSignIn{
        public void signup(String email, String password);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        obj = (onClickSignIn) context;
    }
}
