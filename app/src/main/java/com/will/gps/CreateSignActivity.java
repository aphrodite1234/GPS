package com.will.gps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.bean.Signin;
import com.will.gps.map.AmapActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by MaiBenBen on 2019/5/7.
 */

public class CreateSignActivity extends Activity implements View.OnClickListener{
    private ImageView btn_back;
    private EditText sign_region,sign_content;
    private TextView sign_id;
    private Button btn_create,btn_dingwei1,btn_dingwei2;
    private Button dateButton,timeButton;
    private int id;
    private EditText latitude,longitude;
    public GregorianCalendar QCalendar;
    SimpleDateFormat dateFormat1,dateFormat2;//格式化时间
    private Intent i;
    private double lat,lgt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_create);
        bindView();

        final DBOpenHelper dbOpenHelper = new DBOpenHelper(CreateSignActivity.this);
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

    @SuppressLint("SimpleDateFormat")
    private void bindView(){
        btn_back=(ImageView)findViewById(R.id.sign_create_back);
        sign_id=(TextView)findViewById(R.id.sign_create_id);
        sign_region=(EditText)findViewById(R.id.sign_create_region);
        sign_content=(EditText)findViewById(R.id.create_sign_content);
        latitude=(EditText)findViewById(R.id.sign_create_latitude);
        longitude=(EditText)findViewById(R.id.sign_create_longitude);
        btn_create=(Button)findViewById(R.id.sign_create_btn);
        btn_dingwei1=(Button)findViewById(R.id.sign_create_dingwei1);
        btn_dingwei2=(Button)findViewById(R.id.sign_create_dingwei2);

//        dateButton = (Button)findViewById(R.id.dateButton);
//        timeButton = (Button)findViewById(R.id.timeButton);

        btn_dingwei1.setOnClickListener(this);
        btn_dingwei2.setOnClickListener(this);
        btn_create.setOnClickListener(this);
        btn_back.setOnClickListener(this);

//        dateButton.setOnClickListener(this);
//        timeButton.setOnClickListener(this);

        QCalendar = getCalendarAfter30Mins();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = df.format(QCalendar.getTime());
        //dateButton.setText(dateString);

        df = new SimpleDateFormat("HH:mm:ss");
        String timeString = df.format(QCalendar.getTime());
//        timeButton.setText(timeString);

        dateFormat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//不同的时间格式
        dateFormat2=new SimpleDateFormat("y年M月d日 H时m分s秒", Locale.CHINA);

        i=getIntent();
        try{
            id=Integer.valueOf(i.getStringExtra("groupid"));
            lat=i.getDoubleExtra("lat",0.000000);
            lgt=i.getDoubleExtra("lgt",0.000000);
            latitude.setText(String.valueOf(lat));
            longitude.setText(String.valueOf(lgt));
        }catch(Exception e){
            Toast.makeText(this,"intent为空",Toast.LENGTH_SHORT).show();
        }

        sign_id.setText(id+"");
    }

    @Override@SuppressLint("SimpleDateFormat")
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_create_dingwei1:
                Intent i=new Intent(CreateSignActivity.this, AmapActivity.class);
                i.putExtra("activity","CreateSignActivity");
                i.putExtra("jingdu","high");
                startActivityForResult(i,0);
                break;
            case R.id.sign_create_dingwei2:
                Intent i2=new Intent(CreateSignActivity.this, AmapActivity.class);
                i2.putExtra("activity","CreateSignActivity");
                i2.putExtra("jingdu","low");
                startActivityForResult(i2,0);
            case R.id.sign_create_btn:

                if (TextUtils.isEmpty(sign_id.getText().toString().trim())) {
                    Toast.makeText(CreateSignActivity.this, "请输入签到id", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    id = Integer.valueOf(sign_id.getText().toString().trim());
                }
                if (TextUtils.isEmpty(sign_region.getText().toString().trim())) {
                    Toast.makeText(CreateSignActivity.this, "请输入签到范围", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Integer.valueOf(sign_region.getText().toString().trim())>200){
                    Toast.makeText(CreateSignActivity.this, "签到范围小于200m", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(sign_content.getText().toString().trim())) {
                    Toast.makeText(CreateSignActivity.this, "请输入签到名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                Gson gson = new Gson();
                RMessage rMessage = new RMessage();
                Signin signin = new Signin();
                signin.setGroupid(id);
                signin.setResult(sign_content.getText().toString());
                signin.setOriginator(MySocket.user.getPhonenum());
                signin.setLongitude(longitude.getText().toString());
                signin.setLatitude(latitude.getText().toString());
                signin.setRegion(sign_region.getText().toString());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                signin.setTime(df.format(new Date()));
                signin.setRlongitude(signin.getLongitude());
                signin.setRlatitude(signin.getLatitude());
                rMessage.setGroupid(id);
                rMessage.setType("签到消息");
                rMessage.setSenderphone(MySocket.user.getPhonenum());
                rMessage.setSendername(MySocket.user.getUserName());
                rMessage.setContent(gson.toJson(signin));
                ((MySocket)getApplication()).send(gson.toJson(rMessage));

//                rMessage.setType("登录成功");
//                rMessage.setContent(null);
//                rMessage.setSenderphone(MySocket.user.getPhonenum());
                ((MySocket) getApplication()).update();

                Toast.makeText(CreateSignActivity.this,"创建签到活动成功！",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.sign_create_back:
                //finish();
                System.out.println(dateFormat1.format(QCalendar.getTime()));
                System.out.println(dateFormat2.format(QCalendar.getTime()));
                break;
//            case R.id.dateButton:
//                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        QCalendar.set(year, monthOfYear, dayOfMonth);
//                        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
//                        String dateString = df.format(QCalendar.getTime());
//                        dateButton.setText(dateString);
//                    }
//                }, QCalendar.get(Calendar.YEAR), QCalendar.get(Calendar.MONTH),
//                        QCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                break;
//            case R.id.timeButton:
//                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        QCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                        QCalendar.set(Calendar.MINUTE, minute);
//                        DateFormat df = new SimpleDateFormat("HH:mm");
//                        String timeString = df.format(QCalendar.getTime());
//                        timeButton.setText(timeString);
//                    }
//                }, QCalendar.get(Calendar.HOUR_OF_DAY), QCalendar
//                        .get(Calendar.MINUTE), true).show();
//                break;
        }
    }
    public GregorianCalendar getCalendarAfter30Mins() {
        GregorianCalendar calendar = new GregorianCalendar();
        if (calendar.get(Calendar.MINUTE) >= 30) {
            calendar.add(Calendar.MINUTE, 60 - calendar.get(Calendar.MINUTE));
        } else {
            calendar.add(Calendar.MINUTE, 30 - calendar.get(Calendar.MINUTE));
        }

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            longitude.setText(data.getStringExtra("lgt"));
            latitude.setText(data.getStringExtra("lat"));
        }
    }
}
