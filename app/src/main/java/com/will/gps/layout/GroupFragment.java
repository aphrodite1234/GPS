package com.will.gps.layout;

import android.annotation.SuppressLint;
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
import com.will.gps.adapter.MyExtendableListViewAdapter;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.MySocket;
import com.will.gps.bean.Group;
import com.will.gps.bean.RecentContactBean;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import com.will.gps.base.Encoding;



/**
 * Created by MaiBenBen on 2019/5/20.
 */
@SuppressLint("ValidFragment")
public class GroupFragment extends Fragment {
    private ExpandableListView expandableListView;
    private List<String> groups,groups1;
    private List<List<Group>>childs;
    private Context context;
    private View view;
    private Gson gson=new Gson();

    @SuppressLint("SimpleDateFormat")
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
        groups1=dbOpenHelper.searchgroup(dbOpenHelper);
        groups=new ArrayList<>();
        groups.add("我发起的签到群");
        groups.add("我加入的签到群");

        childs=new ArrayList<>();
        List<Group> child1=new ArrayList<>();
        /*Group child1_1=new Group();
        child1_1.setGroupname("child1-1");
        child1.add(child1_1);*/

        List<Group> child2=new ArrayList<>();
        /*child2.add("child2-1");
        child2.add("dhild2-2");
        child2.add("child2-3");*/
        String phone="15837811860";
        if (!groups1.isEmpty()) {
            for (String group : groups1) {
                Group group4 = gson.fromJson(group, Group.class);
                if(MySocket.user.getPhonenum().equals(group4.getGroupowner())){
                    child1.add(group4);
                }else{
                    child2.add(group4);
                }
            }
        }

        childs.add(child1);
        childs.add(child2);
    }

    private void initView(){
        expandableListView = (ExpandableListView)view.findViewById(R.id.expend_list);
        expandableListView.setAdapter(new MyExtendableListViewAdapter(context,groups,childs));
        //设置分组的监听
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Toast.makeText(context,groups.get(groupPosition) , Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //设置子项布局监听
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
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
        });
        //控制他只能打开一个组
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = new MyExtendableListViewAdapter(context,groups,childs).getGroupCount();
                for(int i = 0;i < count;i++){
                    if (i!=groupPosition){
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });
    }
}
