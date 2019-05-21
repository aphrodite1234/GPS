package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntek.commonlibrary.adapter.OnItemClickListener;
import com.suntek.commonlibrary.adapter.RViewHolder;
import com.suntek.commonlibrary.adapter.RecycleViewAdapter;
import com.will.gps.bean.SignTableBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by MaiBenBen on 2019/5/10.
 */

public class SignTableListActivity extends Activity {

    private TextView title,tip;
    private RecyclerView mRecyclerView;
    private List<SignTableBean> signTableBeanList;
    private RecycleViewAdapter<SignTableBean> signtableAdapter;
    private DateFormat dateFormat;
    private List<String> signtables;//从数据库查询到的签到表列表
    private Gson gson=new Gson();
    private ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list);

        title=(TextView)findViewById(R.id.sign_table_title);
        title.setText("签到表");
        tip=(TextView)findViewById(R.id.sign_table_tip);
        mRecyclerView=findViewById(R.id.sign_list);
        dateFormat=new SimpleDateFormat("M月d日 H时m分", Locale.CHINA);

        btn_back=(ImageView)findViewById(R.id.sign_table_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();

        initRecyclerView(signtables);
        for(int i=0;i<signTableBeanList.size();i++)
            signtableAdapter.notifyItemChanged(i);
    }
    private void initData(){
        signtables=new ArrayList<>();
        SignTableBean signTableBean1=new SignTableBean();
        SignTableBean signTableBean2=new SignTableBean();
        signTableBean1.setId(123);
        signTableBean1.setLongitude(114.314499);
        signTableBean1.setLatitude(34.81386);
        signTableBean1.setOriginator("15837811860");
        signTableBean1.setTime(new Date());
        signTableBean1.setState(true);

        signTableBean2.setId(456);
        signTableBean2.setLongitude(114.31192);
        signTableBean2.setLatitude(34.814472);
        signTableBean2.setOriginator("15837811860");
        signTableBean2.setTime(new Date());
        signTableBean2.setState(false);

        signtables.add(gson.toJson(signTableBean1));
        signtables.add(gson.toJson(signTableBean2));
    }

    private void initRecyclerView(List<String> signtables){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        signTableBeanList=new ArrayList<>();

        if (!signtables.isEmpty()) {
            for (String signtable : signtables) {
                SignTableBean signtable1 = gson.fromJson(signtable, SignTableBean.class);
                signTableBeanList.add(signtable1);
            }
        } else {
            tip.setText("点我创建签到活动");
            tip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(SignTableListActivity.this,CreateSignActivity.class);
                    startActivity(i);
                }
            });
        }
        mRecyclerView.setLayoutManager(layoutManager);
        signtableAdapter=new RecycleViewAdapter<SignTableBean>(this,signTableBeanList) {
            @Override
            public int setItemLayoutId(int position) {
                return R.layout.sign_table_item;
            }

            @Override
            public void bindView(RViewHolder holder, int position) {
                SignTableBean signTableBean=signTableBeanList.get(position);
                if(signTableBean!=null){
                    holder.setText(R.id.sign_table_id,String.valueOf(signTableBean.getId()));
                    //holder.setText(R.id.sign_table_originator,signTableBean.getOriginator());
                    holder.setText(R.id.sign_table_time,"时间:"+dateFormat.format(signTableBean.getTime()));
                    holder.setText(R.id.sign_table_longitude,"纬度:"+String.valueOf(signTableBean.getLongitude()));
                    holder.setText(R.id.sign_table_latitude,"纬度:"+String.valueOf(signTableBean.getLatitude()));
                    if(signTableBean.isState())
                        holder.setImageResource(R.id.sign_table_state,R.mipmap.jinhangzhong);
                    else
                        holder.setImageResource(R.id.sign_table_state,R.mipmap.end);
                }
            }
        };
        signtableAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RViewHolder holder, int position) {
                SignTableBean signTableBean=signTableBeanList.get(position);
                Intent i=new Intent(SignTableListActivity.this,ReceiverListAcitivty.class);
                i.putExtra("signtable",signTableBean);
                startActivity(i);
            }
        });
        mRecyclerView.setAdapter(signtableAdapter);
    }
}
