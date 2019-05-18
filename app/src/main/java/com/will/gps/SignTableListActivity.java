package com.will.gps;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntek.commonlibrary.adapter.RecycleViewAdapter;
import com.will.gps.bean.SignTableBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by MaiBenBen on 2019/5/10.
 */

public class SignTableListActivity extends Activity implements View.OnClickListener {

    private TextView title;
    private RecyclerView mRecyclerView;
    private List<SignTableBean> signTableBeanList;
    private RecycleViewAdapter<SignTableBean> signtableAdapter;
    private DateFormat dateFormat;
    private List<String> signtables;//从数据库查询到的签到表列表
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list);

        mRecyclerView=findViewById(R.id.sign_list);
        dateFormat=new SimpleDateFormat("y年M月d日 H时m分s秒", Locale.CHINA);
        initRecyclerView(signtables);
        for(int i=0;i<signTableBeanList.size();i++)
            signtableAdapter.notifyItemChanged(i);
    }

    private void initRecyclerView(List<String> signtables){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        signTableBeanList=new ArrayList<>();

        if (!signtables.isEmpty()) {
            for (String signtable : signtables) {
                SignTableBean signtable1 = gson.fromJson(signtable, SignTableBean.class);
                /*RecentContactBean recentContactBean = new RecentContactBean();
                recentContactBean.setGroup(group4);*/
                signTableBeanList.add(signtable1);
            }
        } else {

        }
    }
    @Override
    public void onClick(View v) {

    }
}
