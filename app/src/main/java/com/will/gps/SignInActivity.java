package com.will.gps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.will.gps.bean.SignTableBean;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by MaiBenBen on 2019/5/21.
 */

public class SignInActivity extends Activity {
    private TextView deadline,owner,region;
    private ImageView btn_map;
    private SignTableBean signTableBean;
    private SimpleDateFormat dateFormat;
    private Date date;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    private void initView(){
        StrToDate(date);
        //date=signTableBean.getTime();
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);

        /*dateFormat=new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String str=dateFormat.format(date);*/

        System.out.println(date);

        deadline=(TextView)findViewById(R.id.sign_in_deadline);
        owner = (TextView)findViewById(R.id.sign_in_owner);
        region=(TextView)findViewById(R.id.sign_create_region);
        btn_map=(ImageView)findViewById(R.id.btn_map);

        deadline.setText("截至时间:"+NextNMin(cal,10));
        owner.setText("发起人:"+signTableBean.getOriginator());
        region.setText("签到范围:"+signTableBean.getRegion());
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

    private void StrToDate(Date date){
        try {//字符串转化为date
            String string = "2016-10-24 21:59:06";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date=sdf.parse(string);
            System.out.println(date);
        } catch (ParseException e) {
            System.out.println("###################################");
            e.printStackTrace();
        }
    }
}
