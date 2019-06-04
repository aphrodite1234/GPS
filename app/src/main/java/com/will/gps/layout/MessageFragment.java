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
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.RMessage;
import com.will.gps.bean.Signin;

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
        DBOpenHelper dbOpenHelper=new DBOpenHelper(getActivity());
        List<String> signList = dbOpenHelper.searchsignin(dbOpenHelper);
        List<String> stateList = dbOpenHelper.searchstate(dbOpenHelper);
        mChildList1=new ArrayList<>();

        for(String signin:signList){
            Signin signin1 = gson.fromJson(signin,Signin.class);
            RMessage message1=new RMessage();
            message1.setType("签到消息");
            message1.setGroupid(signin1.getGroupid());
            message1.setContent(signin1.getResult());
            message1.setDate(signin1.getTime());
            message1.setState(1);
            mChildList1.add(gson.toJson(message1));
        }

        for (String tsmessage:stateList){
            mChildList1.add(tsmessage);
        }

//        RMessage message4=new RMessage();
//        RMessage message5=new RMessage();
//        RMessage message6=new RMessage();
//        RMessage message7=new RMessage();
//
//        message4.setType("群消息");
//        message4.setGroupid(15);
//        message4.setContent("群消息1！");
//        //message4.setDate("2019-05-06 23:56");
//        message4.setState(1);
//        message5.setType("群消息");
//        message5.setGroupid(15);
//        message5.setContent("群消息2！");
//        //message5.setDate("2020-05-06 23:56");
//        message5.setState(1);
//        message6.setType("群消息");
//        message6.setGroupid(15);
//        message6.setContent("群消息3！");
//        //message6.setDate("2021-05-06 23:56");
//        message6.setState(1);
//        message7.setType("群消息");
//        message7.setGroupid(21);
//        message7.setContent("群消息4！");
//        //message7.setDate("2009-05-06 23:56");
//        message7.setState(1);
//
//        mChildList1.add(gson.toJson(message4));
//        mChildList1.add(gson.toJson(message5));
//        mChildList1.add(gson.toJson(message6));
//        mChildList1.add(gson.toJson(message7));
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
