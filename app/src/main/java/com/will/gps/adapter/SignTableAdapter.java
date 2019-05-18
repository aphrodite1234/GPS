package com.will.gps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.will.gps.R;
import com.will.gps.bean.SignTableBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaiBenBen on 2019/5/10.
 */

public class SignTableAdapter extends BaseAdapter {

    private List<SignTableBean> signTableBeanList=new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext0;

    public SignTableAdapter (Context context, List<SignTableBean> vector){
        this.signTableBeanList=vector;
        mInflater=LayoutInflater.from(context);
        mContext0 = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView id,originator,time,longitude,latitude;
        ImageView state;
        convertView = mInflater.inflate(R.layout.chat_message_item, null);
        id=(TextView)convertView.findViewById(R.id.sign_table_id);
        originator=(TextView)convertView.findViewById(R.id.sign_table_originator);
        longitude=(TextView)convertView.findViewById(R.id.sign_table_longitude);
        time=(TextView)convertView.findViewById(R.id.sign_table_time);
        latitude=(TextView)convertView.findViewById(R.id.sign_table_latitude);
        state=(ImageView)convertView.findViewById(R.id.sign_table_state);
        SignTableBean signTableBean=signTableBeanList.get(position);
        id.setText(signTableBean.getId());
        originator.setText("发起人："+signTableBean.getOriginator());
        time.setText("发起时间："+signTableBean.getTime());
        longitude.setText("经度："+signTableBean.getLongitude());
        latitude.setText("纬度："+signTableBean.getLatitude());
        if(signTableBean.isState()){//正在进行中
            state.setImageResource(R.mipmap.jinhangzhong);
        }else{
            state.setImageResource(R.mipmap.end);
        }
        return null;
    }
    @Override
    public int getCount() {
        if(signTableBeanList!=null){
            return signTableBeanList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(signTableBeanList!=null){
            return signTableBeanList.get(position);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
