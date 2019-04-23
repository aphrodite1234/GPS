package com.will.gps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.will.gps.layout.CircleImageView;

import org.w3c.dom.Text;

/**
 * Created by MaiBenBen on 2019/4/23.
 */

public class GroupInfoActivity extends Activity{
    private ImageView img1;
    private CircleImageView img2;
    private TextView textname;
    private TextView textnum;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void bindView(){
        img1=(ImageView)findViewById(R.id.group_info_img1);
        img2=(CircleImageView) findViewById(R.id.group_info_img2);
        textname=(TextView)findViewById(R.id.group_info_txtname);
        textnum=(TextView)findViewById(R.id.group_info_txtnum);
        btn=(Button)findViewById(R.id.group_info_btn);
    }
}
