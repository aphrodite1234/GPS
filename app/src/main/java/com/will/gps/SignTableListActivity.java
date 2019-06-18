package com.will.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suntek.commonlibrary.adapter.OnItemClickListener;
import com.suntek.commonlibrary.adapter.RViewHolder;
import com.suntek.commonlibrary.adapter.RecycleViewAdapter;
import com.will.gps.base.CreateExcel;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.MySocket;
import com.will.gps.base.PermissionActivity;
import com.will.gps.base.RMessage;
import com.will.gps.bean.SignTableBean;
import com.will.gps.bean.Signin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by MaiBenBen on 2019/5/10.
 */

public class SignTableListActivity extends AppCompatActivity{

    private TextView title,tip,content;
    private RecyclerView mRecyclerView;
    private List<SignTableBean> signTableBeanList;
    private List<Signin> signins;
    private RecycleViewAdapter<SignTableBean> signtableAdapter;
    private DateFormat dateFormat;
    private List<String> signtables;//从数据库查询到的签到表列表
    private Gson gson=new Gson();
    private ImageView btn_back,btn_print;
    private int groupId;
    private String groupowner;
    private PopupMenu popupMenu;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list);

        title=(TextView)findViewById(R.id.sign_table_title);
        title.setText("签到表");
        tip=(TextView)findViewById(R.id.sign_table_tip);
        content=(TextView)findViewById(R.id.sign_table_content);
        mRecyclerView=findViewById(R.id.sign_list);
        dateFormat=new SimpleDateFormat("M-d日 H:m", Locale.CHINA);

        btn_back=(ImageView)findViewById(R.id.sign_table_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Intent intent = getIntent();
        groupId=Integer.valueOf(intent.getStringExtra("groupid"));
        groupowner=intent.getStringExtra("groupowner");
        final DBOpenHelper dbOpenHelper=new DBOpenHelper(SignTableListActivity.this);
        initData(dbOpenHelper);

        initRecyclerView(signtables);
        for(int i=0;i<signTableBeanList.size();i++)
            signtableAdapter.notifyItemChanged(i);

        btn_print=(ImageView)findViewById(R.id.btn_map);
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
        popupMenu=new PopupMenu(this,findViewById(R.id.btn_map));
        menu=popupMenu.getMenu();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_group_info, menu);
        popupMenu.getMenu().findItem(R.id.menu_groupinfo_startsign).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_groupinfo_endsign).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_groupinfo_signlist).setVisible(false);
        menu.add(Menu.NONE, Menu.FIRST + 0, 0, "导入到本地");
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "发送到我的电脑");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 1:
                        //Toast.makeText(SignTableListActivity.this,"点击1",Toast.LENGTH_SHORT).show();
                        startActivityForResult(new Intent(SignTableListActivity.this, PermissionActivity.class).putExtra(PermissionActivity.KEY_PERMISSIONS_ARRAY,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}), PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE);
                        CreateExcel createExcel=new CreateExcel(signins,signTableBeanList);
                        break;
                    case 2:
                        //Toast.makeText(SignTableListActivity.this,"点击2",Toast.LENGTH_SHORT).show();
                        startActivityForResult(new Intent(SignTableListActivity.this, PermissionActivity.class).putExtra(PermissionActivity.KEY_PERMISSIONS_ARRAY,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}), PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE);
                        CreateExcel createExcel1=new CreateExcel(signins,signTableBeanList);
                        Intent shareIntent=new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + "/documents/"+signins.get(0).getGroupid()+"签到情况.xls"));
                        //shareIntent.putExtra(Intent.EXTRA_STREAM,createExcel1.file.toURI());
                        shareIntent.setType("application/vnd.ms-excel");
                        startActivity(Intent.createChooser(shareIntent,"分享到"));
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        //btn_print.setImageResource(R.drawable.ic_print_white_24dp);
       /* btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SignTableListActivity.this, PermissionActivity.class).putExtra(PermissionActivity.KEY_PERMISSIONS_ARRAY,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}), PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE);
                CreateExcel createExcel=new CreateExcel(signins,signTableBeanList);
            }
        });*/
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
   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case
        }
    }*/
    private void initData(DBOpenHelper dbOpenHelper){
        signtables=new ArrayList<>();
        signins=new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        SQLiteDatabase db1 = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("signin", null, "groupid="+groupId, null, null, null, null);
        //Toast.makeText(SignTableListActivity.this,groupId+"",Toast.LENGTH_LONG).show();
        while(cursor.moveToNext()){
            Signin signin=new Signin();
            signin.setId(cursor.getInt(cursor.getColumnIndex("id")));
            signin.setGroupid(cursor.getInt(cursor.getColumnIndex("groupid")));
            signin.setOriginator(cursor.getString(cursor.getColumnIndex("originator")));
            signin.setTime(cursor.getString(cursor.getColumnIndex("time")));
            signin.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
            signin.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
            signin.setRegion(cursor.getString(cursor.getColumnIndex("region")));
            signin.setReceiver(cursor.getString(cursor.getColumnIndex("receiver")));
            signin.setRlongitude(cursor.getString(cursor.getColumnIndex("rlongitude")));
            signin.setRlatitude(cursor.getString(cursor.getColumnIndex("rlatitude")));
            signin.setState(cursor.getInt(cursor.getColumnIndex("state")));
            signin.setDone(cursor.getInt(cursor.getColumnIndex("done")));
            signin.setResult(cursor.getString(cursor.getColumnIndex("result")));
            signins.add(signin);
            Cursor cursor1 = db1.query("mgroup", null, "groupid="+groupId, null, null, null, null);
            String originator = cursor.getString(cursor.getColumnIndex("originator"));
            String receiver = cursor.getString(cursor.getColumnIndex("receiver"));
            if(originator.equals(receiver)){
                cursor1.moveToNext();
                SignTableBean signTableBean1=new SignTableBean();
                signTableBean1.setId(cursor.getInt(cursor.getColumnIndex("id")));
                signTableBean1.setLongitude(cursor.getString(cursor.getColumnIndex("rlongitude")));
                signTableBean1.setLatitude(cursor.getString(cursor.getColumnIndex("rlatitude")));
                signTableBean1.setOriginator(cursor1.getString(cursor1.getColumnIndex("ownername")));
                signTableBean1.setTime(cursor.getString(cursor.getColumnIndex("time")));
                signTableBean1.setState(cursor.getInt(cursor.getColumnIndex("state")));
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE) {
            switch (resultCode) {
                case PermissionActivity.CALL_BACK_RESULT_CODE_SUCCESS:
                    //Toast.makeText(this, "权限申请成功！", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionActivity.CALL_BACK_RESULE_CODE_FAILURE:
                    Toast.makeText(this, "导出失败，查看应用存储权限！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    //Toast.makeText(this, "GPS打开失败!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
