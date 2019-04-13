package com.will.gps.Password;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyuncs.exceptions.ClientException;
import com.will.gps.R;
import com.will.gps.layout.VerifyCodeView;

import org.w3c.dom.Text;

import static com.will.gps.Base.MainALiSms.SendSms;
import static com.will.gps.Base.SmsDemo.sendCode;

/**
 * Created by MaiBenBen on 2019/4/9.
 */

public class Password2Activity extends Activity{
    VerifyCodeView verifyCodeView;
    private Button button;
    private Intent i;
    private String phone;
    private TextView text1;
    private String code;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password2);
        button=(Button)findViewById(R.id.pass2_nextstep);
        text1=(TextView)findViewById(R.id.pass2_txt1);
        i=getIntent();
        phone=i.getStringExtra("phone");//得到用户输入的电话号码

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {//不放在线程里出错android.os.NetworkOnMainThreadException不允许在主线程中进行网络访问
                    code = Integer.toString((int)((Math.random()*9+1)*100000));  //每次调用生成一位四位数的随机数
                    SendSms(phone,code);//发送验证码
                    System.out.println("code="+code);
                    System.out.println("#######################");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //phone=phone.substring(0,3)+"******"+phone.substring(9);//135******23
        text1.setText(text1.getText()+phone.substring(0,3)+"******"+phone.substring(9));
        verifyCodeView = (VerifyCodeView) findViewById(R.id.verify_code_view);
        verifyCodeView.setInputCompleteListener(new VerifyCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {//EditText中text长度达到六位
                Toast.makeText(Password2Activity.this, "inputComplete: " + verifyCodeView.getEditContent(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void invalidContent() {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyCodeView.getEditContent().equals(code)){
                    Intent intent=new Intent(Password2Activity.this,Password3Activity.class);
                    intent.putExtra("type","重置密码");
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }
                else{
                    //toast后面没加show()方法调试半天不显示
                    Toast.makeText(Password2Activity.this,"验证码输入错误，请重新输入！"+verifyCodeView.getEditContent(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
