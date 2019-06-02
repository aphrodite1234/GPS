package com.will.gps.layout;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.GroupChatActivity;
import com.will.gps.R;
import com.will.gps.adapter.MessageListAdapter;
import com.will.gps.adapter.MyExtendableListViewAdapter;
import com.will.gps.base.RMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaiBenBen on 2019/4/21.
 */

public class MessageFragment extends Fragment {
    private ExpandableListView expandableListView;
    private List<String> mChildList1;
    private Context context;
    private View view;
    private Gson gson=new Gson();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_goup_msg, container, false);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        initData();
        initView();
        return view;
    }
    private void initData(){
        mChildList1=new ArrayList<>();
        RMessage message1=new RMessage();
        RMessage message2=new RMessage();
        RMessage message3=new RMessage();
        RMessage message4=new RMessage();
        RMessage message5=new RMessage();
        RMessage message6=new RMessage();
        RMessage message7=new RMessage();

        try {//字符串转化为date
            String str1 = "2016-10-24 21:59:06";
            String str2="2019-05-06 23:56:21";
            String str3="2020-05-06 23:56:34";
            String str4="2009-05-06 23:56:09";
            String str5="2021-05-06 23:56:45";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //date=sdf.parse(string);
            //System.out.println(date);
            message1.setDate(sdf.parse(str1));
            message2.setDate(sdf.parse(str2));
            message3.setDate(sdf.parse(str3));
            message4.setDate(sdf.parse(str2));
            message5.setDate(sdf.parse(str3));
            message6.setDate(sdf.parse(str4));
            message7.setDate(sdf.parse(str5));
        } catch (ParseException e) {
            System.out.println("###################################");
            e.printStackTrace();
        }
        message1.setType("签到消息");
        message1.setGroupid(15);
        message1.setContent("新的签到活动1！");
        //message1.setDate("2019-05-06 23:56");
        message1.setState(1);
        message2.setType("签到活动");
        message2.setGroupid(21);
        message2.setContent("新的签到活动2！");
        //message2.setDate("2020-05-06 23:56");
        message2.setState(1);
        message3.setType("签到活动");
        message3.setGroupid(24);
        message3.setContent("新的签到活动3！");
        //message3.setDate("2021-05-06 23:56");
        message3.setState(1);

        message4.setType("群消息");
        message4.setGroupid(15);
        message4.setContent("群消息1！");
        //message4.setDate("2019-05-06 23:56");
        message4.setState(1);
        message5.setType("群消息");
        message5.setGroupid(15);
        message5.setContent("群消息2！");
        //message5.setDate("2020-05-06 23:56");
        message5.setState(1);
        message6.setType("群消息");
        message6.setGroupid(15);
        message6.setContent("群消息3！");
        //message6.setDate("2021-05-06 23:56");
        message6.setState(1);
        message7.setType("群消息");
        message7.setGroupid(21);
        message7.setContent("群消息4！");
        //message7.setDate("2009-05-06 23:56");
        message7.setState(1);

        mChildList1.add(gson.toJson(message1));
        mChildList1.add(gson.toJson(message2));
        mChildList1.add(gson.toJson(message3));
        mChildList1.add(gson.toJson(message4));
        mChildList1.add(gson.toJson(message5));
        mChildList1.add(gson.toJson(message6));
        mChildList1.add(gson.toJson(message7));
    }
    private void initView(){
        expandableListView = (ExpandableListView)view.findViewById(R.id.expend_list);
        expandableListView.setAdapter(new MessageListAdapter(context,mChildList1));
        //设置分组的监听
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        //设置子项布局监听
        /*expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(context,childs.get(groupPosition).get(childPosition).getGroupname(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, GroupChatActivity.class);
                intent.putExtra("groupname",childs.get(groupPosition).get(childPosition).getGroupname());
                intent.putExtra("groupid", String.valueOf(childs.get(groupPosition).get(childPosition).getGroupid()));
                intent.putExtra("groupowner", childs.get(groupPosition).get(childPosition).getGroupowner());
                intent.putExtra("membernum", childs.get(groupPosition).get(childPosition).getMembernum());
                intent.putExtra("ismember","true");
                startActivity(intent);
                return true;
            }
        });*/
    }
}
