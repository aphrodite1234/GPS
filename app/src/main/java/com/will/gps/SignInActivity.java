package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.will.gps.base.DBOpenHelper;
import com.will.gps.bean.SignTableBean;
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
    private TextView mTvCountdowntimer,owner,region;
    private ImageView btn_map;
    private Button btn_sign;
    private SignTableBean signTableBean;
    private SimpleDateFormat dateFormat;
    private Cursor cursor;
    private long timeStemp;
    private CountDownTimer timer;
    private Intent i;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initData();
        initView();
    }

    private void initData(){
        signTableBean=new SignTableBean();
        DBOpenHelper dbOpenHelper=new DBOpenHelper(this);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        cursor=db.query("signin",null,"groupid="+getIntent().getIntExtra("groupid",0)+" AND state=1",null, null, null, null);
        if(cursor.getCount()==0){
            signTableBean.setOriginator("15837811860");
            signTableBean.setTime("2019-6-1 20:05:00");
            signTableBean.setRegion(0);
            signTableBean.setLongitude("0.0");
            signTableBean.setLatitude("0.0");
            signTableBean.setId(0);
            i=new Intent(SignInActivity.this,SignTableListActivity.class);
            i.putExtra("groupid",String.valueOf(getIntent().getIntExtra("groupid",0)));
            i.putExtra("groupowner",signTableBean.getOriginator());
        }else{
            while(cursor.moveToNext()){
                signTableBean.setOriginator(cursor.getString(cursor.getColumnIndex("originator")));
                signTableBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                signTableBean.setLongitude(cursor.getString(cursor.getColumnIndex("longtitude")));
                signTableBean.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                signTableBean.setRegion(Integer.valueOf(cursor.getString(cursor.getColumnIndex("region"))));
                signTableBean.setTime(cursor.getString(cursor.getColumnIndex("time")));
            }
            i=new Intent(SignInActivity.this,ReceiverListAcitivty.class);
            i.putExtra("signid",String.valueOf(signTableBean.getId()));
            i.putExtra("signtable",signTableBean);
        }

    }

    private void initView(){
        /*date=new Date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);*///将Date类转化为Calender类
        /*dateFormat=new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String str=dateFormat.format(date);*/

        mTvCountdowntimer=(TextView)findViewById(R.id.sign_in_deadline);
        owner = (TextView)findViewById(R.id.sign_in_owner);
        region=(TextView)findViewById(R.id.sign_in_region);
        btn_map=(ImageView)findViewById(R.id.sign_in_map);
        btn_sign=(Button)findViewById(R.id.sign_in_sign);

        if(cursor.getCount()==0){
            mTvCountdowntimer.setText("签到剩余时间：00:00:00");
        }else{
            Date d=new Date();
            timeStemp=StrToDate(signTableBean.getTime()).getTime() - d.getTime();
            if(timeStemp<=0) timeStemp=0;
            getCountDownTime();
        }
        owner.setText("发起人："+signTableBean.getOriginator());
        region.setText("签到范围："+signTableBean.getRegion());
        btn_map.setImageResource(R.mipmap.position);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2=new Intent(SignInActivity.this, ReceiverMapActivity.class);

                startActivity(i2);
            }
        });
        btn_sign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(i);
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
                mTvCountdowntimer.setText("签到剩余时间："+hour + "小时" + minute + "分钟" + second + "秒");
            }
            @Override
            public void onFinish() {                 //倒计时为0时执行此方法
                mTvCountdowntimer.setText("签到剩余时间：00:00:00");
                btn_sign.setText("签到结束 查看此次签到情况");
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
}
