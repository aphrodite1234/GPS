package com.will.gps;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.will.gps.layout.CircleImageView;

import org.w3c.dom.Text;

/**
 * Created by MaiBenBen on 2019/4/23.
 */

public class GroupInfoActivity extends Activity implements View.OnClickListener{
    private ImageView img1,img_back,img_more;
    private CircleImageView img2;
    private TextView textname;
    private TextView textnum;
    private Button btn;
    private boolean isMember=false;
    private RelativeLayout mynameview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        bindView();
        /*btn.setOnClickListener(new View.OnClickListener() {//申请加群，先判断是否为本群成员
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    private void bindView(){
        img1=(ImageView)findViewById(R.id.group_info_img1);
        img2=(CircleImageView) findViewById(R.id.group_info_img2);
        textname=(TextView)findViewById(R.id.group_info_txtname);
        textnum=(TextView)findViewById(R.id.group_info_txtnum);
        btn=(Button)findViewById(R.id.group_info_btn);
        mynameview=(RelativeLayout)findViewById(R.id.group_info_layout_myname);
        img_back=(ImageView)findViewById(R.id.group_chat_back);
        img_more=(ImageView)findViewById(R.id.group_chat_more);
        img_back.setOnClickListener(this);
        img_more.setOnClickListener(this);
        btn.setOnClickListener(this);

        if(isMember==false) {
            btn.setText("申请加群");
            btn.setTextColor(Color.BLUE);
            mynameview.setVisibility(View.GONE);
        }

        else {
            btn.setText("退群");
            btn.setTextColor(Color.RED);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_info_btn:
                if (isMember == false)//不是群成员
                {
                    Toast.makeText(GroupInfoActivity.this, "申请发送成功！", Toast.LENGTH_SHORT).show();
                } else//已经是群成员
                {

                }
                break;
            case R.id.group_info_back:
                finish();
                break;
            case R.id.group_info_more:
                Toast.makeText(GroupInfoActivity.this,"功能未实现",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
