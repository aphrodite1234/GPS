package com.will.gps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.will.gps.GroupMemberActivity;
import com.will.gps.R;
import com.will.gps.bean.GroupMember;

import java.util.ArrayList;
import java.util.List;

public class GroupmemberAdapter extends BaseAdapter {

    private Context mContext;
    List<String> groupmember = new ArrayList<>();
    public GroupmemberAdapter(List<String> mData, Context mContext) {
        this.groupmember = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return groupmember.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_groupmember,parent,false);
        TextView username = (TextView)convertView.findViewById(R.id.member_user_name);
        TextView userphone = (TextView)convertView.findViewById(R.id.member_user_phone);
        Gson gson = new Gson();
        GroupMember groupMember = gson.fromJson(groupmember.get(position),GroupMember.class);
        username.setText(groupMember.getUsername());
        userphone.setText(groupMember.getUserphone());
        return convertView;
    }
}
