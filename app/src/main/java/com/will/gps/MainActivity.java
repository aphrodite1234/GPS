package com.will.gps;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.*;

import com.will.gps.Base.ClientThread;

import static com.will.gps.Base.ClientThread.getMD5String;

public class MainActivity extends AppCompatActivity{

    private final String DEBUG_TAG="MainActivity";
    private static final String TAG="SCOKET";
    ClientThread clientThread;
    private Handler handler;
    public static final int SHOW=1;
    private Button mButton01=null;
    private Button mButton02=null;
    private TextView mTextView=null;
    private EditText mEditText01=null;
    private EditText mEditText02=null;
    JSONObject object=new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
