package com.example.uma_fyp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class VoiceCommand extends Fragment {

    private static final int REQUEST_MICROPHONE = 1000;
    private static final int REQUEST_CODE = 765;
    LottieAnimationView lottieAnimationView;

    Intent vab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homerecord, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lottieAnimationView = getActivity().findViewById(R.id.microphone);
        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageMicrophone();
                getVoiceCommand();
            }
        });
    }

    public void getVoiceCommand() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ur_PK");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (ActivityNotFoundException ignored) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE)
        {
            if (resultCode==RESULT_OK && data != null)
            {
                vab = data;
                showText();
            }
            else
            {
                Toast.makeText(getContext(), "Unable to Convert", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getContext(), "Unable to Convert", Toast.LENGTH_SHORT).show();
        }
    }

    private void showText() {
        ArrayList<String> toText = vab.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        Toast.makeText(getContext(), toText.get(0), Toast.LENGTH_SHORT).show();
    }

    public void manageMicrophone() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);
        }
    }
}
