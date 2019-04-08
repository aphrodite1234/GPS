package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.will.gps.Base.ClientThread;
import com.will.gps.R;
import com.will.gps.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.will.gps.Base.ClientThread.getMD5String;

/**
 * Created by MaiBenBen on 2019/4/7.
 */

public class LoadActivity extends Activity {

    private final String DEBUG_TAG="MainActivity";
    private static final String TAG="SCOKET";
    ClientThread clientThread;
    private Handler handler;
    public static final int SHOW=1;
    public static final int DL=2;
    private Button mButton01=null;
    private Button mButton02=null;
    private TextView mTextView=null;
    private EditText mEditText01=null;
    private EditText mEditText02=null;
    JSONObject object=new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        mButton01=(Button)findViewById(R.id.Button01);
        mButton02=(Button)findViewById(R.id.Button02);
        mTextView=(TextView)findViewById(R.id.TextView01);
        mEditText01=(EditText)findViewById(R.id.EditText01);
        mEditText02=(EditText)findViewById(R.id.EditText02);

        final Intent dl = new Intent(LoadActivity.this,RegisterActivity.class);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case SHOW:
                        mTextView.setText("\n"+msg.obj.toString());
                        break;
                    case DL:
                        startActivity(dl);
                    default:break;
                }
                super.handleMessage(msg);
            }
        };

        clientThread=new ClientThread(handler);
        new Thread(clientThread).start();

        mButton01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username=mEditText01.getText().toString();
                String password=getMD5String(mEditText02.getText().toString());
                Message msg=new Message();

                try{
                    object.put("信号","登录");
                    object.put("用户名",username);
                    object.put("密码",password);

                }catch (JSONException e){
                    e.printStackTrace();
                }
                msg.what=ClientThread.SEND;
                msg.obj=object.toString();
                clientThread.pushHandler.sendMessage(msg);
            }

        });

        mButton02.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(com.will.gps.LoadActivity.this,RegisterActivity.class);
                startActivity(intent);
                //startActivity(new Intent("RegisterActivity"));
            }
        });
    }
}
