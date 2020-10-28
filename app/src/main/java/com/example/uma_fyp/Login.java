package com.example.uma_fyp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class Login extends Fragment {


    AlertDialog.Builder builder;
    static ProgressDialog progressDialog;

    AutoCompleteTextView email, password;
    TextView forgetpass, register;
    Button signin;
    CheckBox rememberme;
    onClickForgetPass obj;
    onClickSigninButton objSignInbtn;
    onClickRegisterButton objRegisterbtn;

    private String user_email, user_password;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loginscreen, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        progressDialog = new ProgressDialog(getContext());
        builder = new AlertDialog.Builder(getContext());
        email = getActivity().findViewById(R.id.EmailLog);
        rememberme = getActivity().findViewById(R.id.Rememberme);
        password = getActivity().findViewById(R.id.PasswordLog);
        forgetpass = getActivity().findViewById(R.id.ForgotPass);
        register = getActivity().findViewById(R.id.SignIn);
        signin = getActivity().findViewById(R.id.btnSignIn);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            email.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            rememberme.setChecked(true);
        }

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.onclickForgetPass();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (rememberme.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", email.getText().toString());
                    loginPrefsEditor.putString("password", password.getText().toString());
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

                if(validate()){
                    progressDialog.setTitle("Log In");
                    progressDialog.setMessage("Logging . . .");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    objSignInbtn.login(email.getText().toString(), password.getText().toString());
                }
                email.setText("");
                password.setText("");
                return;
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objRegisterbtn.onclickRegisterButton();
            }
        });
    }



    private boolean validate() {
        user_email = email.getText().toString().trim();
        user_password = password.getText().toString().trim();
        if (TextUtils.isEmpty(user_email) || TextUtils.isEmpty(user_password)){
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
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
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
            email.requestFocus();
            return false;
        }
        else if (password.length()<8 )
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
            password.requestFocus();
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        obj = (onClickForgetPass) context;
        objSignInbtn = (onClickSigninButton) context;
        objRegisterbtn = (onClickRegisterButton) context;
    }

    public interface onClickForgetPass{
        public void onclickForgetPass();
    }

    public interface onClickSigninButton{
        public void login(String email, String password);
    }

    public interface onClickRegisterButton{
        public void onclickRegisterButton();
    }
}