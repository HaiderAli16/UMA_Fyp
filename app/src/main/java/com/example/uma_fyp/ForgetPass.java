package com.example.uma_fyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPass extends Fragment {

    EditText Email;
    Button resetPass;

    AlertDialog.Builder builder;
    ForgetPassUser obj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.passwordreset, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Email = getActivity().findViewById(R.id.atvEmailRes);
        resetPass = getActivity().findViewById(R.id.btnReset);
        builder = new AlertDialog.Builder(getContext());

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Email.getText().toString().equals("")){
                    builder.setMessage("Field is Empty . . !")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Error");
                    alert.show();
                    return;
                }

                else {
                    obj.forgetpass(Email.getText().toString());
                }
                message();
            }
        });
    }

    private void message() {
        Email.setText("");
        builder.setMessage("A mail is send to you with link of Password Reset . . !")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Reset Password");
        alert.show();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        obj = (ForgetPassUser) context;
    }

    public interface ForgetPassUser{
        public void forgetpass(String email);
    }
}
