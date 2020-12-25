package com.example.uma_fyp;

import org.tensorflow.lite.Interpreter;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.FileInputStream;
import java.lang.reflect.MalformedParametersException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

public class TextProcess {

    public String TextData;
    Python py;
    Context context;
    Interpreter tflite;

    public TextProcess()
    {

    }

    public TextProcess(Context context){
        py = Python.getInstance();
        tflite = new Interpreter(loadModel());
        this.context = context;
    }

    public float doInference(String TextData)
    {
        this.TextData = TextData;

        //output_txt.setText(Float.toString( prediction));

        final PyObject pyobj = py.getModule("textprocess");

        float inputValArr[][][]= new float[1][20][300];
        inputValArr=pyobj.callAttr("testTxt", TextData).toJava(float[][][].class);
        float outputValArr[][]=new float [1][1];
        tflite.run(inputValArr,outputValArr);
        return outputValArr[0][0];
    }

    private MappedByteBuffer loadModel(){
        //InputStream tffiles=this.getAssets().open("model.tflite");
        //BitmapFactory.decodeStream(tffiles);
        AssetFileDescriptor fileDescriptor=null;
        try{
            fileDescriptor = context.getAssets().openFd("model.tflite");
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

}
