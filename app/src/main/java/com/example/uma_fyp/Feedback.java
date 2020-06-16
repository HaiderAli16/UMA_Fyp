package com.example.uma_fyp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.Edits;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback extends Fragment {

    switchPage obj;
    EditText getfeedback;
    Button submitfeedback;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;

    public String getQuery;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feedback, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getfeedback = getActivity().findViewById(R.id.feedbackEdtTxt);
        submitfeedback = getActivity().findViewById(R.id.btnFeedback);
        progressDialog = new ProgressDialog(getContext());
        builder = new AlertDialog.Builder(getContext());


        submitfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
                obj.pageSwitchHome();
                return;
            }
        });
    }

    private void submitFeedback() {
        getQuery = getfeedback.getText().toString();
        if (TextUtils.isEmpty(getQuery)){
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
            progressDialog.setTitle("Feedback");
            progressDialog.setMessage("Thank you for giving your Responce . . .");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            FirebaseDatabase.getInstance().getReference("User")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Feedback").setValue(getQuery).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();

                        builder.setMessage("Data Updated . . !")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle("Success");
                        alert.show();
                        getfeedback.setText("");
                    }
                }
            });
        }
    }
    public interface switchPage{
        public void pageSwitchHome();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        obj = (switchPage) context;
    }
}