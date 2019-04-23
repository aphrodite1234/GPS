package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by MaiBenBen on 2019/4/22.
 */

public class SearchActivity extends Activity implements View.OnClickListener{
    private ImageView btn_back;
    private ImageView btn_search;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        bindView();
    }

    private void bindView(){
        btn_back=(ImageView)findViewById(R.id.search_back);
        btn_search=(ImageView)findViewById(R.id.search_search);
        editText=(EditText)findViewById(R.id.search_edit);

        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back:
                finish();
                break;
            case R.id.search_search://查找群
                Intent intent=new Intent(SearchActivity.this,GroupInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
