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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MaiBenBen on 2019/4/9.
 */

public class PasswordActivity extends Activity implements OnClickListener {
    private Button btn;
    private ImageView imagecode;
    private EditText edit1;
    private EditText edit2;
    private String phoneStr;//手机字符串
    private String codeStr;//验证码字符串
    boolean isChanged = false;
    private CodeUtils codeUtils;
    private String code;
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
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        imagecode.setImageBitmap(bitmap);//生成验证码
        imagecode.setOnClickListener(this);
        btn.setOnClickListener(this);
    }
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PasswordActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
        case R.id.pass_image:
            Bitmap bitmap = codeUtils.createBitmap();
            imagecode.setImageBitmap(bitmap);
            break;
        case R.id.pass_nextstep:
            //phoneStr=edit1.getText().toString().trim();//trim()去除字符串两端空格
            codeStr = edit2.getText().toString().trim();
            /*Log.e("codeStr", codeStr);*/
            phoneStr = edit1.getText().toString().trim().replaceAll("/s","");
            if (!TextUtils.isEmpty(phoneStr)) {//定义需要匹配的正则表达式的规则
                String REGEX_MOBILE_SIMPLE =  "[1][358]\\d{9}";//把正则表达式的规则编译成模板
                Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);//把需要匹配的字符给模板匹配，获得匹配器
                Matcher matcher = pattern.matcher(phoneStr);// 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
                if (matcher.find()) {//匹配手机号是否存在
                    //alterWarning();
                }
                else {
                    toast("手机号格式错误");
                    return;
                }
            } else {
                toast("请先输入手机号");
                return;
            }
            if (null == codeStr || TextUtils.isEmpty(codeStr)) {
                Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            code = codeUtils.getCode();
            Log.e("code", code);
            if (code.equalsIgnoreCase(codeStr)) {
                Toast.makeText(this, "验证码正确", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PasswordActivity.this,Password2Activity.class);
                intent.putExtra("phone",phoneStr);
                startActivity(intent);
                finish();
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
