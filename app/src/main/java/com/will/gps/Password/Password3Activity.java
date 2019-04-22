package com.will.gps.Password;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MaiBenBen on 2019/4/9.
 */

public class Password3Activity extends Activity implements View.OnClickListener {
    private EditText edit1;
    private EditText edit2;
    private EditText edit3;
    private Button btn;
    private ImageView discover;
    private String passStr=null;
    private String passCon=null;
    private TextView text;
    Intent i;
    RMessage rMessage = new RMessage();
    Gson gson = new Gson();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password3);
        initView();

        final Intent intent=new Intent(Password3Activity.this,Password4Activity.class);
        intent.putExtra("type",i.getStringExtra("type"));

        ((MySocket)getApplication()).setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                rMessage = gson.fromJson(msg.obj.toString(),RMessage.class);
                if(rMessage.getContent().equals("true")){
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Password3Activity.this,"更改失败！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initView(){
        edit1=(EditText)findViewById(R.id.pass3_phone);
        i=getIntent();
        text=(TextView)findViewById(R.id.pass3_txt);
        text.setText(i.getStringExtra("type"));
        edit1.setText(i.getStringExtra("phone"));
        edit2=(EditText)findViewById(R.id.pass3_pass);
        edit3=(EditText)findViewById(R.id.pass3_passconfirm);
        btn=(Button)findViewById(R.id.pass3_nextstep);
        discover=(ImageView)findViewById(R.id.pass3_image);
        discover.setOnClickListener(this);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.pass3_image:
                break;
            case R.id.pass3_nextstep:
                passStr = edit2.getText().toString().trim();
                passCon=edit3.getText().toString().trim();
                Log.e("codeStr", passStr);
                if (null ==passStr || TextUtils.isEmpty(passStr)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    String REGEX_PASSWORD_SIMPLE =  "^[a-zA-Z0-9_]{8,16}$";//把正则表达式的规则编译成模板
                    Pattern pattern = Pattern.compile(REGEX_PASSWORD_SIMPLE);//把需要匹配的字符给模板匹配，获得匹配器
                    Matcher matcher = pattern.matcher(passStr);// 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
                    if(!matcher.matches())
                    {
                        Toast.makeText(this, "密码不符合格式", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (null ==passCon || TextUtils.isEmpty(passCon)) {
                    Toast.makeText(this, "请重新确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passStr!=null&&passCon!=null&&!(passStr.equals(passCon))){
                    Toast.makeText(this,"两次输入密码不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }

                MySocket.user.setPhonenum(edit1.getText().toString());
                MySocket.user.setPassWord(passCon);
                rMessage.setType(text.getText().toString());
                rMessage.setContent(gson.toJson(MySocket.user));
                ((MySocket)getApplication()).send(gson.toJson(rMessage));
                break;
            default:
                break;
        }
    }
}
