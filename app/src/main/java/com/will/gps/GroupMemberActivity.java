package com.will.gps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.will.gps.adapter.GroupmemberAdapter;
import com.will.gps.base.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberActivity extends AppCompatActivity implements View.OnClickListener {

    int groupid;
    List<String> groupmember = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        groupid=getIntent().getIntExtra("groupid",0);
        DBOpenHelper dbOpenHelper = new DBOpenHelper(GroupMemberActivity.this);
        groupmember = dbOpenHelper.searchgroupmember(dbOpenHelper,groupid);

        GroupmemberAdapter groupmemberAdapter = new GroupmemberAdapter(groupmember,GroupMemberActivity.this);
        ListView listView = (ListView)findViewById(R.id.groupmember_Listview);
        listView.setAdapter(groupmemberAdapter);
        ImageView back = (ImageView)findViewById(R.id.groupmember_info_back);
        back.setOnClickListener(this);
        ImageView more = (ImageView)findViewById(R.id.groupmember_info_more);
        more.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.groupmember_info_back:
                finish();
                break;
            case R.id.groupmember_info_more:
                Toast.makeText(GroupMemberActivity.this,"暂无功能",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
