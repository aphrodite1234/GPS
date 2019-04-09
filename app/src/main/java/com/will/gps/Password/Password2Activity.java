package com.will.gps.Password;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.will.gps.R;
import com.will.gps.layout.VerifyCodeView;

/**
 * Created by MaiBenBen on 2019/4/9.
 */

public class Password2Activity extends Activity{
    VerifyCodeView verifyCodeView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password2);
        verifyCodeView = (VerifyCodeView) findViewById(R.id.verify_code_view);
        verifyCodeView.setInputCompleteListener(new VerifyCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                Toast.makeText(Password2Activity.this, "inputComplete: " + verifyCodeView.getEditContent(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void invalidContent() {
            }
        });
    }
}
