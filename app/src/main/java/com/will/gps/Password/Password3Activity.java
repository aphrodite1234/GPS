package com.will.gps.Password;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.will.gps.R;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password3);
        edit1=(EditText)findViewById(R.id.pass3_phone);
        Intent i=getIntent();
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
            case R.id.pass_image:
                break;
            case R.id.pass_nextstep:
                passStr = edit2.getText().toString().trim();
                passCon=edit3.getText().toString().trim();
                Log.e("codeStr", passStr);
                if (null ==passStr || TextUtils.isEmpty(passStr)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (null ==passCon || TextUtils.isEmpty(passCon)) {
                    Toast.makeText(this, "请重新确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passStr!=null&&passCon!=null&&!(passStr.equals(passCon))){
                    Toast.makeText(this,"两次输入密码不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
            default:
                break;
        }
    }
}
