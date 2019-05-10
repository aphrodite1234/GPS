package com.will.gps;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by MaiBenBen on 2019/5/7.
 */

public class CreateSignActivity extends Activity {
    private ImageView btn_back;
    private EditText sign_name,sign_location;
    private Button btn_create;
    private String name,location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        bindView();
    }

    private void bindView(){
        btn_back=(ImageView)findViewById(R.id.sign_create_back);
        sign_name=(EditText)findViewById(R.id.sign_create_name);
        sign_location=(EditText)findViewById(R.id.sign_create_location);
        btn_create=(Button)findViewById(R.id.sign_create_btn);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = sign_name.getText().toString().trim();
                location=sign_location.getText().toString().trim();
                if (null ==name || TextUtils.isEmpty(name)) {
                    Toast.makeText(CreateSignActivity.this, "请输入签到名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(null ==location || TextUtils.isEmpty(location)) {
                    Toast.makeText(CreateSignActivity.this, "请输入签到地点", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(CreateSignActivity.this,"创建签到活动成功！",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
