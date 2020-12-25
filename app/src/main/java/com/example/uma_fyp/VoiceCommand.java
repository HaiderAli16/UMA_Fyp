package com.example.uma_fyp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
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
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class VoiceCommand extends Fragment {

    private static final int REQUEST_MICROPHONE = 1000;
    private static final int REQUEST_CODE = 765;
    LottieAnimationView lottieAnimationView;

    Python py;
    Interpreter tflite;

    TextProcess textProcess;
    Float PredictedResult;
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

    private MappedByteBuffer loadModel(){
        //InputStream tffiles=this.getAssets().open("model.tflite");
        //BitmapFactory.decodeStream(tffiles);
        AssetFileDescriptor fileDescriptor=null;
        try{
            fileDescriptor = getContext().getAssets().openFd("model.tflite");
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }

        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        MappedByteBuffer mappedByteBuffer=null;
        try{
            mappedByteBuffer=fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
        }
        catch(Exception expected)
        {
            expected.printStackTrace();
        }

        return mappedByteBuffer;
    }

    private void showText() {
        ArrayList<String> toText = vab.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

        //textProcess = new TextProcess(this.getContext());
        //PredictedResult =  textProcess.doInference(toText.get(0));
        //Toast.makeText(getActivity(), ""+ PredictedResult, Toast.LENGTH_SHORT).show();

        py = Python.getInstance();
        tflite = new Interpreter(loadModel());


        final PyObject pyobj = py.getModule("textprocess");

        float inputValArr[][][]= new float[1][20][300];
        inputValArr=pyobj.callAttr("testTxt", toText.get(0)).toJava(float[][][].class);
        float outputValArr[][]=new float [1][1];
        tflite.run(inputValArr,outputValArr);
        PredictedResult = outputValArr[0][0];
        Toast.makeText(getContext(), "" + PredictedResult.toString(), Toast.LENGTH_SHORT).show();






        if (toText.get(0).equals("گوگل اوپن کرو") || toText.get(0).equals("گوگل اوپن کرو"))
        {
            Uri uri = Uri.parse("http://www.google.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        else if (toText.get(0).equals("واٹس اپ اوپن کر دو") || toText.get(0).equals("واٹس ایپ کھول دو"))
        {
            Intent i = new Intent(Intent.ACTION_MAIN);
            PackageManager managerclock = getActivity().getPackageManager();
            i = managerclock.getLaunchIntentForPackage("com.whatsapp");
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
        }

        else if (toText.get(0).equals("میسیج ایپ کھول دو") || toText.get(0).equals("اوپن میسجز"))
        {
            String textnum = "12345";
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.setData(Uri.parse("sms:"));
            startActivity(smsIntent);
        }

        else if (toText.get(0).equals("کیمرہ کھولو دو") || toText.get(0).equals("اوپن کیمرہ"))
        {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
        }

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
