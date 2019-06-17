package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suntek.commonlibrary.adapter.OnItemClickListener;
import com.suntek.commonlibrary.adapter.RViewHolder;
import com.suntek.commonlibrary.adapter.RecycleViewAdapter;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.bean.SignTableBean;
import com.will.gps.bean.Signin;
import com.will.gps.map.AmapActivity;

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
    private int groupId;
    private String groupowner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list);

        title=(TextView)findViewById(R.id.sign_table_title);
        title.setText("签到表");
        tip=(TextView)findViewById(R.id.sign_table_tip);
        mRecyclerView=findViewById(R.id.sign_list);
        dateFormat=new SimpleDateFormat("M-d日 H:m", Locale.CHINA);

        btn_back=(ImageView)findViewById(R.id.sign_table_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        groupId=Integer.valueOf(intent.getStringExtra("groupid"));
        groupowner=intent.getStringExtra("groupowner");
        final DBOpenHelper dbOpenHelper=new DBOpenHelper(SignTableListActivity.this);
        initData(dbOpenHelper);

        initRecyclerView(signtables);
        for(int i=0;i<signTableBeanList.size();i++)
            signtableAdapter.notifyItemChanged(i);

        ((MySocket)getApplication()).setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Gson gson = new Gson();
                Signin signin = new Signin();
                RMessage rMessage = gson.fromJson(msg.obj.toString(), RMessage.class);
                String type = rMessage.getType();
                switch (type){
                    case "群消息":
                        dbOpenHelper.savemsg(dbOpenHelper, rMessage);
                        break;
                    case "签到消息":
                        signin = gson.fromJson(rMessage.getContent(),Signin.class);
                        dbOpenHelper.savesign(dbOpenHelper,signin);
                        break;
                    case "解散群":
                        dbOpenHelper.deletegroup(dbOpenHelper,rMessage.getGroupid());
                        break;
                    case "用户签到":
                        signin=gson.fromJson(rMessage.getContent(),Signin.class);
                        if(signin.getDone()==1){
                            dbOpenHelper.updatesignin(dbOpenHelper,signin.getId());
                        }
                        break;
                    case "签到截止":
                        dbOpenHelper.endsign(dbOpenHelper,gson.fromJson(rMessage.getContent(),Signin.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void initData(DBOpenHelper dbOpenHelper){
        signtables=new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        SQLiteDatabase db1 = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("signin", null, "groupid="+groupId, null, null, null, null);
        //Toast.makeText(SignTableListActivity.this,groupId+"",Toast.LENGTH_LONG).show();
        while(cursor.moveToNext()){
            Cursor cursor1 = db1.query("mgroup", null, "groupid="+groupId, null, null, null, null);
            String originator = cursor.getString(cursor.getColumnIndex("originator"));
            String receiver = cursor.getString(cursor.getColumnIndex("receiver"));
            if(originator.equals(receiver)){
                cursor1.moveToNext();
                SignTableBean signTableBean1=new SignTableBean();
                signTableBean1.setLongitude(cursor.getString(cursor.getColumnIndex("rlongitude")));
                signTableBean1.setLatitude(cursor.getString(cursor.getColumnIndex("rlatitude")));
                signTableBean1.setOriginator(cursor1.getString(cursor1.getColumnIndex("ownername")));
                signTableBean1.setTime(cursor.getString(cursor.getColumnIndex("time")));
                signTableBean1.setState(cursor.getInt(cursor.getColumnIndex("state")));
                signTableBean1.setContent(cursor.getString(cursor.getColumnIndex("result")));
                signtables.add(gson.toJson(signTableBean1));
            }
        }
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
                    //holder.setText(R.id.sign_table_id,String.valueOf(signTableBean.getContetn()));
                    holder.setText(R.id.sign_table_content,signTableBean.getContent());
                    holder.setText(R.id.sign_table_originator,signTableBean.getOriginator());
                    holder.setText(R.id.sign_table_time,"时间:"+signTableBean.getTime());
//                    holder.setText(R.id.sign_table_longitude,"经度:"+String.valueOf(signTableBean.getLongitude()));
//                    holder.setText(R.id.sign_table_latitude,"纬度:"+String.valueOf(signTableBean.getLatitude()));
                    if(signTableBean.getState()==0)
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
                i.putExtra("groupid",groupId);
                i.putExtra("time",signTableBean.getTime());
                startActivity(i);
            }
        });
        mRecyclerView.setAdapter(signtableAdapter);
    }
}
