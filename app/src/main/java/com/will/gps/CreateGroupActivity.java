package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by MaiBenBen on 2019/4/23.
 */

public class CreateGroupActivity extends Activity {

    private EditText groupname,groupID;
    private Button button;
    private String nameStr,idStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        bindView();
    }

    private void bindView(){
        groupname=(EditText)findViewById(R.id.group_create_name);
        groupID=(EditText)findViewById(R.id.group_create_id);
        button=(Button)findViewById(R.id.group_create_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//创建群
                nameStr= groupname.getText().toString().trim();
                idStr=groupID.getText().toString().trim();
                if (null ==nameStr || TextUtils.isEmpty(nameStr)) {
                    Toast.makeText(CreateGroupActivity.this, "请输入群名称", Toast.LENGTH_SHORT).show();
                    return;
                }else if(nameStr.length()<3||nameStr.length()>16){
                    Toast.makeText(CreateGroupActivity.this, "群名称不合法", Toast.LENGTH_SHORT).show();
                    return;
                }else if(null ==idStr || TextUtils.isEmpty(idStr)) {
                    Toast.makeText(CreateGroupActivity.this, "请输入群ID", Toast.LENGTH_SHORT).show();
                    return;
                }else if(idStr.length()<3||idStr.length()>10){
                    Toast.makeText(CreateGroupActivity.this, "群ID不合法", Toast.LENGTH_SHORT).show();
                    return;
                }
                //创建群操作
                Intent intent=new Intent(CreateGroupActivity.this,GroupInfoActivity.class);
                intent.putExtra("groupname",nameStr);
                intent.putExtra("groupid",idStr);
                intent.putExtra("type","create");
                startActivity(intent);
            }
        });
    }
}
