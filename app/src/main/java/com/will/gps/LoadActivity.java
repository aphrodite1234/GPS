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

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case SHOW:
                        mTextView.setText("\n"+msg.obj.toString());
                        break;
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
                /*Socket socket=null;
                String message = mEditText01.getText().toString() + "/r/n";
               try
               {

                    //创建Socket
                    socket = new Socket("188.131.189.2",12345);
                    //socket = new Socket("10.14.114.127",54321); //IP：10.14.114.127，端口54321
                    PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
                    out.println(message+"wmy");
                    //接收来自服务器的消息
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msg = br.readLine();
                    if ( msg != null )
                    {
                    mTextView.setText(msg);
                    }
                    else
                    {
                    mTextView.setText("数据错误!");
                    }
                    //关闭流
                    out.close();
                    br.close();
                    //关闭Socket
                    socket.close();
               } catch (Exception e)
                {
                // TODO: handle exception
                Log.e(DEBUG_TAG, e.toString());
                }*/
                String username=mEditText01.getText().toString();
                String password=getMD5String(mEditText02.getText().toString());
                Message msg=new Message();

                try{
                    object.put("信号","注册");
                    object.put("用户名",username);
                    object.put("密码",password);

                }catch (JSONException e){
                    e.printStackTrace();
                }
                //clientThread.send(content);
                msg.what=ClientThread.SEND;
                msg.obj=object.toString();
                clientThread.pushHandler.sendMessage(msg);

                /*mEditText01.setTag("");
*/
                /*Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);*/

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
