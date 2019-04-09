package com.will.gps.Password;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.will.gps.Base.CodeUtils;
import com.will.gps.R;

/**
 * Created by MaiBenBen on 2019/4/9.
 */

public class PasswordActivity extends Activity implements OnClickListener {
    private Button btn;
    private ImageView imagecode;
    private EditText edit1;
    private EditText edit2;
    private String codeStr;
    boolean isChanged = false;
    private CodeUtils codeUtils;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        btn=(Button)findViewById(R.id.pass_nextstep);
        initView();
    }

    private void initView(){
        imagecode=(ImageView)findViewById(R.id.pass_image);
        btn=(Button)findViewById(R.id.pass_nextstep);
        edit1=(EditText)findViewById(R.id.pass_phone);
        edit2=(EditText)findViewById(R.id.pass_check);
        imagecode.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
        case R.id.pass_image:
            codeUtils = CodeUtils.getInstance();
            Bitmap bitmap = codeUtils.createBitmap();
            imagecode.setImageBitmap(bitmap);
            break;
        case R.id.pass_nextstep:
            codeStr = edit2.getText().toString().trim();
            Log.e("codeStr", codeStr);
            if (null == codeStr || TextUtils.isEmpty(codeStr)) {
                Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            String code = codeUtils.getCode();
            Log.e("code", code);
            if (code.equalsIgnoreCase(codeStr)) {
                Toast.makeText(this, "验证码正确", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PasswordActivity.this,Password2Activity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
            }
            break;
        default:
            break;
        }
    }
}
