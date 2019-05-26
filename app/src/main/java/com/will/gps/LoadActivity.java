package com.will.gps;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.Password.PasswordActivity;

/**
 * Created by MaiBenBen on 2019/4/7.
 */

public class LoadActivity extends Activity {

    private Button mButton01=null;
    private Button mButton02=null;
    private Button mButton03=null;
    private EditText mEditText01=null;
    private EditText mEditText02=null;
    private ImageView image=null;
    RMessage message = new RMessage();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        mButton01=(Button)findViewById(R.id.Button01);
        mButton02=(Button)findViewById(R.id.Button02);
        mButton03=(Button)findViewById(R.id.Button03);
        mEditText01=(EditText)findViewById(R.id.EditText01);
        mEditText02=(EditText)findViewById(R.id.EditText02);
        image=(ImageView)findViewById(R.id.load_image);
        image.setImageResource(R.drawable.ic_gps);

        DBOpenHelper dbOpenHelper=new DBOpenHelper(LoadActivity.this);
        final SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ((MySocket)getApplication()).read();
        final Intent dl=new Intent(LoadActivity.this,MainActivity.class);
        ((MySocket) getApplication()).setHandler(new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                message = gson.fromJson(msg.obj.toString(),RMessage.class);
                if(message.getContent().equals("true")){
                    Cursor cursor = db.query("user", null, "phonenum="+MySocket.user.getPhonenum(), null, null, null, null);
                    ContentValues contentValues = new ContentValues();
                    if(cursor.getCount()==0){
                        contentValues.put("phonenum",MySocket.user.getPhonenum());
                        db.insert("user",null,contentValues);
                    }
                    startActivity(dl);
                    finish();
                }else {
                    Toast.makeText(LoadActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mButton01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//登录
                MySocket.user.setPhonenum(mEditText01.getText().toString());
                MySocket.user.setPassWord(mEditText02.getText().toString());
                message.setType("登录");
                message.setContent(gson.toJson(MySocket.user));
                ((MySocket)getApplication()).send(gson.toJson(message));
                Toast.makeText(LoadActivity.this,"点击登陆按钮成功",Toast.LENGTH_SHORT).show();
               //startActivity(dl);
            }
        });

        mButton02.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){//注册
                Intent intent=new Intent(com.will.gps.LoadActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
                //startActivity(new Intent("RegisterActivity"));
            }
        });

        mButton03.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){//找回密码
                Intent intent=new Intent(com.will.gps.LoadActivity.this, PasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
