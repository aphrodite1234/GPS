package com.will.gps.layout;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private boolean run = true;
    private int[] groupunread=new int[3];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_goup_msg, container, false);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        /*initData();
        initView();*/
        handler.post(task);
        System.out.println("############################################");
        System.out.println(mChildList1);
        return view;
    }
    private final Handler handler = new Handler();
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (run) {
                handler.postDelayed(this, 1000);
                initData();
                /*System.out.println("############################################");
                System.out.println(mChildList1);*/
                initView();
            }
        }
    };
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
            mChildList1.add(gson.toJson(message1));
        }

        mChildList1.addAll(stateList);

        /*RMessage message4=new RMessage();
        RMessage message5=new RMessage();
        RMessage message6=new RMessage();
        RMessage message7=new RMessage();
        RMessage message8=new RMessage();


        message4.setType("群消息");
        message4.setGroupid(15);
        message4.setContent("群消息1！");
        message4.setDate("2019-05-06 23:56");
        message4.setState(1);
        message5.setType("群消息");
        message5.setGroupid(15);
        message5.setContent("群消息2！");
        message5.setDate("2020-05-06 23:56");
        message5.setState(1);
        message6.setType("群消息");
        message6.setGroupid(15);
        message6.setContent("群消息3！");
        message6.setDate("2021-05-06 23:56");
        message6.setState(1);
        message7.setType("群消息");
        message7.setGroupid(24);
        message7.setContent("群消息4！");
        message7.setDate("2009-05-06 23:56");
        message7.setState(1);
        message8.setType("群消息");
        message8.setGroupid(24);
        message8.setContent("群消息5！");
        message7.setDate("2009-05-06 23:56");
        message8.setState(1);

        mChildList1.add(gson.toJson(message4));
        mChildList1.add(gson.toJson(message7));
        mChildList1.add(gson.toJson(message5));
        mChildList1.add(gson.toJson(message8));
        mChildList1.add(gson.toJson(message6));
        System.out.println("############################################");
        System.out.println(mChildList1);*/
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
        for(int i=0;i<3;i++){
            if(groupunread[i]==1)
                expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //int count = new MyExtendableListViewAdapter(context,groups,childs).getGroupCount();
                /*for(int i = 0;i < count;i++){
                    if (i!=groupPosition){
                        expandableListView.collapseGroup(i);
                    }
                }*/
                for(int i=0;i<groupunread.length;i++)
                {
                    if(i==groupPosition)
                        groupunread[i]=1;
                    else
                        groupunread[i]=0;
                }

            }
        });
    }
    /*Fragment依托于Activity，其内部的OnResume和OnPause方法真正归属于其依托的Activity，在Activity可见性变化的时候，
    才会调用这两个方法；如果在Activity中包含一个ViewPager + 多个Fragment的结构，在Fragment的切换过程中，因为Activity
    一直显示，所以Fragment切换是不会调用OnResume和OnPause方法的，当然第一次创建Fragment的时候是会调用的。
    那么问题来了，我们想要在子Fragment对用户可见性变化时处理一些逻辑，该怎么办呢？
    当然是有办法的！*/
    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            //相当于Fragment的onResume,为true时，Fragment可见
            System.out.println("############################################");
            System.out.println(mChildList1);
        }else{
            //相当于Fragment的onPause,为false时，Fragment不可见
            initData();
            System.out.println("############################################");
            System.out.println(mChildList1);
            initView();
        }
    }*/
    @Override
    public void onResume(){
        super.onResume();
        initData();
        /*System.out.println("############################################");
        System.out.println(mChildList1);*/
        initView();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            //pause
            //initData();
            /*System.out.println("############################################");
            System.out.println(mChildList1);*/
            //initView();
            run=false;
        }else{
            //resume
            /*initData();
            *//*System.out.println("############################################");
            System.out.println(mChildList1);*//*
            initView();*/
            run=true;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);//销毁handler，防止内存泄漏
    }
}
