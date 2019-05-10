package com.will.gps.layout;

/**
 * Created by MaiBenBen on 2019/4/21.
 */

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.GroupChatActivity;
import com.will.gps.GroupChatActivity;
import com.will.gps.MainActivity;
import com.will.gps.R;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.bean.Group;
import com.will.gps.base.RMessage;
import com.will.gps.bean.RecentContactBean;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.suntek.commonlibrary.adapter.OnItemClickListener;
import com.suntek.commonlibrary.adapter.RViewHolder;
import com.suntek.commonlibrary.adapter.RecycleViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by wudeng on 2017/8/28.
 */
@SuppressLint("ValidFragment")
public class GroupMsgFragment extends Fragment {

    private static final String TAG = GroupMsgFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<RecentContactBean> mContactList;
    private List<String> groupList = new ArrayList<>();
    private RecycleViewAdapter<RecentContactBean> mViewAdapter;
    private Observer<List<RecentContact>> mObserver;
    private SimpleDateFormat mDateFormat;
    private Context context;
    private RMessage rmessage;
    private View view;
    private Gson gson = new Gson();

    private Group group1 = new Group();
    private Group group2 = new Group();
    private Group group3 = new Group();//测试用
    private RecentContactBean rcb1 = new RecentContactBean();
    private RecentContactBean rcb2 = new RecentContactBean();
    private RecentContactBean rcb3 = new RecentContactBean();

    @SuppressLint("ValidFragment")
    public GroupMsgFragment(List<RecentContactBean> List) {
        mContactList = List;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_fragment_group, container, false);
        //缓存的rootView需要判断是否已经被加过parent
        //如果有parent需要从parent删除，要不然会发生这个rootView已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        mRecyclerView = view.findViewById(R.id.rcv_group_list);
        mDateFormat = new SimpleDateFormat("HH:mm");

        DBOpenHelper dbOpenHelper=new DBOpenHelper(getActivity());
        initRecyclerView(dbOpenHelper.searchgroup(dbOpenHelper));
        //initListener();//更新最近联系人列表
        for (int i = 0; i < mContactList.size(); i++) {
            //RecentContactBean bean = mContactList.get(i);
            //if (bean.getRecentContact().getContactId().equals(contact.getContactId())){
            //bean.setRecentContact(contact);
            mViewAdapter.notifyItemChanged(i);
        }
        //loadRecentList();
        return view;
    }

    public void initRecyclerView(List<String> groups) {//初始化RecyclerView组件
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mContactList = new ArrayList<>(3);

        if (!groups.isEmpty()) {
            for (String group : groups) {
                Group group4 = gson.fromJson(group, Group.class);
                RecentContactBean recentContactBean = new RecentContactBean();
                recentContactBean.setGroup(group4);
                mContactList.add(recentContactBean);
            }
        } else {
            group1.setGroupname("暂未加入任何群");
            rcb1.setGroup(group1);
            mContactList.add(rcb1);
        }

//        group1.setGroupname("暂未加入任何群");
//        rcb1.setGroup(group1);
//        mContactList.add(rcb1);

        //测试用数据
//        group1.setGroupname("一群");
//        group1.setGroupid(123);
//        group2.setGroupname("二群");
//        group3.setGroupname("三群");
//        rcb1.setGroup(group1);
//        rcb2.setGroup(group2);
//        rcb3.setGroup(group3);
//        mContactList.add(0,rcb1);
//        mContactList.add(1,rcb2);
//        mContactList.add(2,rcb3);

        mRecyclerView.setLayoutManager(layoutManager);
        mViewAdapter = new RecycleViewAdapter<RecentContactBean>(context, mContactList) {
            @Override
            public int setItemLayoutId(int position) {
                return R.layout.item_recent_msg;
            }

            @Override
            public void bindView(RViewHolder holder, int position) {
                RecentContactBean contactBean = mContactList.get(position);
                //UserInfo userInfo = contactBean.getUserInfo();
                Group group = contactBean.getGroup();
                if (group != null) {
                    //mContactList.get(position).setUserInfo(userInfo);
                    mContactList.get(position).setGroup(group);
                    holder.setImageByUrl(context, R.id.iv_head_picture,
                            contactBean.getGroup().getGroupimg(), R.mipmap.group_chat);
                    holder.setText(R.id.tv_recent_name, contactBean.getGroup().getGroupname());
                } else {
                    holder.setImageResource(R.id.iv_head_picture, R.mipmap.app_logo_main);
                    holder.setText(R.id.tv_recent_name, Integer.toString(contactBean.getGroup().getGroupid()));
                }
                //String time = mDateFormat.format(new Date(contactBean.getRecentContact().getTime()));
                String time = mDateFormat.format(new Date());
                holder.setText(R.id.tv_recent_time, time);
            }
        };

        mViewAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RViewHolder holder, int position) {
                RecentContactBean contactBean = mContactList.get(position);
                Intent intent;
                //if (contactBean.getRecentContact().getSessionType() == SessionTypeEnum.P2P){
                intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("groupname", contactBean.getGroup().getGroupname());
                intent.putExtra("groupid", String.valueOf(contactBean.getGroup().getGroupid()));
                intent.putExtra("groupowner", contactBean.getGroup().getGroupowner());
                intent.putExtra("membernum", contactBean.getGroup().getMembernum());
                intent.putExtra("ismember","true");
                startActivity(intent);
                //}
            }
        });

        mRecyclerView.setAdapter(mViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        //NIMClient.getService(MsgServiceObserve.class).observeRecentContact(mObserver,true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }
}

