package com.will.gps;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.bean.SignTableBean;
import com.will.gps.bean.Signin;
import com.will.gps.map.AmapActivity;
import com.will.gps.map.ReceiverMapActivity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.zip.Inflater;

/**
 * Created by MaiBenBen on 2019/5/21.
 */

public class SignInActivity extends Activity {
    private TextView mTvCountdowntimer,owner,region,longitude,latitude,groupid;
    private ImageView btn_map;
    private Button btn_sign,gao,di;
    private SignTableBean signTableBean;
    private SimpleDateFormat dateFormat;
    private Cursor cursor;
    private long timeStemp;
    private CountDownTimer timer;
    private EditText rlongitude,rlatitude;
    private int groupId;
    RMessage rMessage=new RMessage();
    Gson gson=new Gson();
    Signin signin=new Signin();
    int done,state;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        groupId=getIntent().getIntExtra("groupid",0);
        initData();
        initView();
        final DBOpenHelper dbOpenHelper = new DBOpenHelper(SignInActivity.this);
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
                        System.out.println(signin.getDone());
                        System.out.println(signin.getState());
                        if(signin.getState()==1){
                            dbOpenHelper.endsign(dbOpenHelper,gson.fromJson(rMessage.getContent(),Signin.class));
                            initData();
                        }else {
                            if(signin.getDone()==1){
                                dbOpenHelper.updatesignin(dbOpenHelper,signin.getId());
                                btn_sign.setText("签到成功");
                                btn_sign.setBackgroundColor(Color.parseColor("#23d249"));
                            }else {
                                Toast.makeText(SignInActivity.this,"签到失败，请稍后重试",Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case "签到截止":
                        dbOpenHelper.endsign(dbOpenHelper,gson.fromJson(rMessage.getContent(),Signin.class));
                        initData();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initData(){
        signTableBean=new SignTableBean();
        DBOpenHelper dbOpenHelper=new DBOpenHelper(this);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        cursor=db.query("signin",null,"receiver="+MySocket.user.getPhonenum()+" AND groupid="+groupId+" AND state=0",null, null, null, null);
        while(cursor.moveToNext()){
            state=cursor.getInt(cursor.getColumnIndex("state"));
            done=cursor.getInt(cursor.getColumnIndex("done"));
            signTableBean.setContent(cursor.getString(cursor.getColumnIndex("result")));
            signTableBean.setOriginator(cursor.getString(cursor.getColumnIndex("originator")));
            signTableBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            signTableBean.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
            signTableBean.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
            signTableBean.setRegion(Integer.valueOf(cursor.getString(cursor.getColumnIndex("region"))));
            signTableBean.setTime(cursor.getString(cursor.getColumnIndex("time")));
        }
    }

    private void initView(){
        /*date=new Date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);*///将Date类转化为Calender类
        /*dateFormat=new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String str=dateFormat.format(date);*/

//        mTvCountdowntimer=(TextView)findViewById(R.id.sign_in_deadline);
        owner = (TextView)findViewById(R.id.sign_in_owner);
        region=(TextView)findViewById(R.id.sign_in_region);
        //btn_map=(ImageView)findViewById(R.id.sign_in_map);
        btn_sign=(Button)findViewById(R.id.sign_in_sign);
        longitude=(TextView)findViewById(R.id.sign_inor_longitude);
        rlongitude=(EditText)findViewById(R.id.sign_in_longitude);
        latitude=(TextView)findViewById(R.id.sign_inor_latitude);
        rlatitude=(EditText)findViewById(R.id.sign_in_latitude);
        gao=(Button)findViewById(R.id.sign_in_dingwei1);
        di=(Button)findViewById(R.id.sign_in_dingwei2);
        groupid=(TextView)findViewById(R.id.sign_in_groupid);

//        if(cursor.getCount()==0){
//            mTvCountdowntimer.setText("签到剩余时间：00:00:00");
//        }else{
//            Date d=new Date();
//            timeStemp=StrToDate(signTableBean.getTime()).getTime() - d.getTime();
//            if(timeStemp<=0) timeStemp=0;
//            getCountDownTime();
//        }
        groupid.setText("活动主题："+signTableBean.getContent());
        owner.setText("发起人："+signTableBean.getOriginator());
        region.setText("签到范围："+signTableBean.getRegion()+"米");
        longitude.setText(signTableBean.getLongitude());
        latitude.setText(signTableBean.getLatitude());
//        mTvCountdowntimer.setText(signTableBean.getTime());
//        btn_map.setImageResource(R.mipmap.position);
//        btn_map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i2=new Intent(SignInActivity.this, ReceiverMapActivity.class);
//
//                startActivity(i2);
//            }
//        });

        if(state==1){
            btn_sign.setText("签到结束");
        }else {
            if (done==1){
                btn_sign.setText("签到成功");
                btn_sign.setBackgroundColor(Color.parseColor("#23d249"));
            }else {
                btn_sign.setText("签到");
            }
        }

        btn_sign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(btn_sign.getText().equals("签到")){
                    signin.setGroupid(groupId);
                    signin.setId(signTableBean.getId());
                    signin.setRlongitude(rlongitude.getText().toString());
                    signin.setRlatitude(rlatitude.getText().toString());
                    signin.setRegion(String.valueOf(signTableBean.getRegion()));
                    rMessage.setType("用户签到");
                    rMessage.setContent(gson.toJson(signin));
                    ((MySocket)getApplication()).send(gson.toJson(rMessage));
                }
            }
        });
        gao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SignInActivity.this, AmapActivity.class);
                i.putExtra("activity","SignInActivity");
                i.putExtra("jingdu","high");
                i.putExtra("longitude",signTableBean.getLongitude());
                i.putExtra("latitude",signTableBean.getLatitude());
                startActivityForResult(i,0);
            }
        });
        di.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SignInActivity.this, AmapActivity.class);
                i.putExtra("activity","SignInActivity");
                i.putExtra("jingdu","low");
                i.putExtra("longitude",signTableBean.getLongitude());
                i.putExtra("latitude",signTableBean.getLatitude());
                startActivityForResult(i,0);
            }
        });
    }
    private void getCountDownTime() {
        timer = new CountDownTimer(timeStemp, 1000) {
            @Override
            public void onTick(long l) {
                long day = l / (1000 * 24 * 60 * 60); //单位天
                long hour = (l - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60); //单位时
                long minute = (l - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60); //单位分
                long second = (l - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;//单位秒
//                mTvCountdowntimer.setText("签到剩余时间："+hour + "小时" + minute + "分钟" + second + "秒");
            }
            @Override
            public void onFinish() {                 //倒计时为0时执行此方法
//                mTvCountdowntimer.setText("签到剩余时间：00:00:00");
//                btn_sign.setText("签到结束 查看此次签到情况");
            }
        };
        timer.start();
    }

    private String NextNMin(Calendar cal,int n){//n为分钟数，n小于24*60，即签到时间不长于一天
        String datetime = null;
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        int hour=cal.get(Calendar.HOUR);//小时
        int minute=cal.get(Calendar.MINUTE);//分
        int second=cal.get(Calendar.SECOND);//秒
        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天
        System.out.println("现在的时间是：公元"+year+"年"+month+"月"+day+"日   "+hour+"时"+minute+"分"+second+"秒    星期"+WeekOfYear);

        if(minute<60-n%60){
            datetime=year+"年"+month+"月"+day+"日  "+hour+":"+minute;
        }else{
            if(hour<24-n/60-1)
                datetime=year+"年"+month+"月"+day+"日  "+(hour+1+n/60)+":"+minute;
            else {
                if(!(month==1||month==3||month==5||month==7||month==8||month==10||month==12)) {
                    if(month==2) {
                        if((year%4==0&&year%100!=0)||year%400==0) {
                            if(year>29)
                                datetime="日期发生错误";
                            else if(day<=29) {
                                if(day==29)
                                    datetime=year+"年3月1日  "+(hour+1+n/60)%24+":"+minute;
                                else
                                    datetime=year+"年"+month+"月"+(day+1)+"日  "+(hour+1+n/60)%24+":"+minute;
                            }
                        }
                        else if(day>28)
                            datetime="日期发生错误";
                        else if(day<=28) {
                            if(day==28)
                                datetime=year+"年3月1日  "+(hour+1+n/60)%24+":"+minute;
                            else
                                datetime=year+"年2月"+(day+1)+"日  "+(hour+1+n/60)%24+":"+minute;
                        }
                    } else if(day>30) {
                        datetime="日期发生错误";
                    } else if(day<=30) {
                        if(day==30)
                            datetime=year+"年"+(month+1)+"月1日  "+(hour+1+n/60)%24+":"+minute;
                        else
                            datetime=year+"年"+month+"月"+(day+1)+"日  "+(hour+1+n/60)%24+":"+minute;
                    }

                } else if(day<=31) {
                    if(day==31) {
                        if(month==12)
                            datetime=(year+1)+"年1月1日  "+(hour+1+n/60)%24+":"+minute;
                        else
                            datetime=year+"年"+(month+1)+"月1日  "+(hour+1+n/60)%24+":"+minute;
                    }else
                        datetime=year+"年"+month+"月"+(day+1)+"日  "+(hour+1+n/60)%24+":"+minute;
                }
            }
        }
        return datetime;
    }

    private Date StrToDate(String datestr){
        Date date=new Date();
        try {//字符串转化为date
            //String string = "2016-10-24 21:59:06";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date=sdf.parse(datestr);
            System.out.println(date);
        } catch (ParseException e) {
            System.out.println("###################################");
            e.printStackTrace();
        }
        return date;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            rlongitude.setText(data.getStringExtra("lgt"));
            rlatitude.setText(data.getStringExtra("lat"));
        }
    }
}
