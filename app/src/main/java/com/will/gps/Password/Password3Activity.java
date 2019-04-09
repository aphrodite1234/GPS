package com.will.gps.Password;

import android.app.Activity;
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
    private Button btn;
    private ImageView discover;
    private String passStr=null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password3);
        edit1=(EditText)findViewById(R.id.pass3_phone);
        edit2=(EditText)findViewById(R.id.pass3_pass);
        btn=(Button)findViewById(R.id.pass3_nextstep);
        discover=(ImageView)findViewById(R.id.pass3_image);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.pass_image:
                break;
            case R.id.pass_nextstep:
                passStr = edit2.getText().toString().trim();
                Log.e("codeStr", passStr);
                if (null ==passStr || TextUtils.isEmpty(passStr)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*String code = codeUtils.getCode();
                Log.e("code", code);
                if (code.equalsIgnoreCase(codeStr)) {
                    Toast.makeText(this, "验证码正确", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                }*/
                break;
            default:
                break;
        }
    }
}
