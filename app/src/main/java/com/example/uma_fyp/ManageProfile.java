package com.example.uma_fyp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManageProfile extends Fragment {

    private String firstname, secoundname, phone, email;

    AutoCompleteTextView Firstname, Secoundname, Phone, Email;
    Button managedata;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manageprofile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Firstname = getActivity().findViewById(R.id.firstname);
        Secoundname = getActivity().findViewById(R.id.secoundname);
        Phone = getActivity().findViewById(R.id.phonenumber);
        Email = getActivity().findViewById(R.id.EmailManage);
        managedata = getActivity().findViewById(R.id.UpdateProfile);

        builder = new AlertDialog.Builder(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        managedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChanges();
            }
        });

    }

    public void getChanges() {
        firstname = Firstname.getText().toString().trim();
        secoundname = Secoundname.getText().toString().trim();
        phone = Phone.getText().toString().trim();
        email = Email.getText().toString().trim();
        if (validate()){
            String getUserID = user.getUid();
            Data userData = new Data(firstname, secoundname, phone, email);

            FirebaseDatabase.getInstance().getReference("User")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "Registration Complete", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Registraion Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Registraion Passed", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(firstname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)){
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
        else if (firstname.length()<3 || secoundname.length()<3)
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
            Firstname.requestFocus();
            Secoundname.requestFocus();
            return false;
        }
        else if (phone.length() <= 10){
            builder.setMessage("Please Enter Valid Phone Number . . !")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Error");
            alert.show();
            Phone.requestFocus();
            return false;
        }
        else {
            return true;
        }

    }
}
