package com.will.gps.layout;

/**
 * Created by MaiBenBen on 2019/4/21.
 */
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.will.gps.R;
import com.will.gps.activity.P2PChatActivity;
import com.will.gps.base.Group;
import com.will.gps.bean.RecentContactBean;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
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
public class RecentMsgFragment extends Fragment {

    private static final String TAG = RecentMsgFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<RecentContactBean> mContactList;
    private RecycleViewAdapter<RecentContactBean> mViewAdapter;
    private Observer<List<RecentContact>> mObserver;
    private SimpleDateFormat mDateFormat;
    private Context context;
    private View view;

    private Group group1=new Group();
    private Group group2=new Group();
    private Group group3=new Group();//测试用
    private RecentContactBean rcb1=new RecentContactBean();
    private RecentContactBean rcb2=new RecentContactBean();
    private RecentContactBean rcb3=new RecentContactBean();

    @SuppressLint("ValidFragment")
    public RecentMsgFragment(Context context){
        this.context = context;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_message,container,false);
        //缓存的rootView需要判断是否已经被加过parent
        //如果有parent需要从parent删除，要不然会发生这个rootView已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        mRecyclerView = view.findViewById(R.id.rcv_message_list);
        mDateFormat = new SimpleDateFormat("HH:mm");
        initRecyclerView();
        //initListener();//更新最近联系人列表
        for (int i=0;i<mContactList.size();i++) {
            //RecentContactBean bean = mContactList.get(i);
            //if (bean.getRecentContact().getContactId().equals(contact.getContactId())){
            //bean.setRecentContact(contact);
            mViewAdapter.notifyItemChanged(i);
        }
        //loadRecentList();
        return view;
    }
    //@Override
   /* public int setLayoutID() {
        return R.layout.activity_fragment_message;
    }*/
/*
    @SuppressLint("SimpleDateFormat")
    //@Override
    public void initView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.rcv_message_list);
        mDateFormat = new SimpleDateFormat("HH:mm");
        initRecyclerView();
        initListener();
        loadRecentList();
    }
*/


    private void initRecyclerView(){//初始化RecyclerView组件
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mContactList = new ArrayList<>(3);

        //测试用数据
        group1.setGroupName("一群");
        group1.setGroupNum("123");
        group2.setGroupName("二群");
        group3.setGroupName("三群");
        rcb1.setGroup(group1);
        rcb2.setGroup(group2);
        rcb3.setGroup(group3);
        mContactList.add(0,rcb1);
        mContactList.add(1,rcb2);
        mContactList.add(2,rcb3);

        mRecyclerView.setLayoutManager(layoutManager);
        mViewAdapter = new RecycleViewAdapter<RecentContactBean>(context,mContactList) {
            @Override
            public int setItemLayoutId(int position) {
                return R.layout.item_recent_msg;
            }

            @Override
            public void bindView(RViewHolder holder, int position) {
                RecentContactBean contactBean= mContactList.get(position);
                //UserInfo userInfo = contactBean.getUserInfo();
                Group group =contactBean.getGroup();
                if (group != null){
                    //mContactList.get(position).setUserInfo(userInfo);
                    mContactList.get(position).setGroup(group);
                    holder.setImageByUrl(context,R.id.iv_head_picture,
                            contactBean.getGroup().getGroupImg(),R.mipmap.bg_img_defalut);
                    holder.setText(R.id.tv_recent_name,contactBean.getGroup().getGroupName());
                }else {
                    holder.setImageResource(R.id.iv_head_picture,R.mipmap.app_logo_main);
                    holder.setText(R.id.tv_recent_name,contactBean.getGroup().getGroupNum());
                }
                holder.setText(R.id.tv_recent_content,contactBean.getGroup().getGroupContent());
                //String time = mDateFormat.format(new Date(contactBean.getRecentContact().getTime()));
                String time=mDateFormat.format(new Date());
                holder.setText(R.id.tv_recent_time,time);
            }
        };

        mViewAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RViewHolder holder, int position) {
                RecentContactBean contactBean = mContactList.get(position);
                Intent intent;
                if (contactBean.getRecentContact().getSessionType() == SessionTypeEnum.P2P){
                    intent = new Intent(context, P2PChatActivity.class);
                    intent.putExtra("NimUserInfo",contactBean.getUserInfo());
                    startActivity(intent);
                }
            }
        });

        mRecyclerView.setAdapter(mViewAdapter);
    }

    private void initListener(){//查看有没有新数据，若有则记录在recentContacts里
        mObserver = new Observer<List<RecentContact>>() {
            @Override
            public void onEvent(List<RecentContact> recentContacts) {
                Log.e(TAG,"Observer RecentContact size = " + recentContacts.size());
                if (mContactList.isEmpty()){
                    List<RecentContactBean> contactBeans = createContactBeans(recentContacts);
                    mContactList.addAll(contactBeans);
                    mViewAdapter.notifyDataSetChanged();
                    return;
                }
                for (RecentContact contact : recentContacts){
                    refreshRecentList(contact);
                }
            }
        };
    }

    private void refreshRecentList(RecentContact contact){//将recentContacts的数据与mContactList的数据对比，如果有新数据则更新
        for (int i=0;i<mContactList.size();i++){
            RecentContactBean bean = mContactList.get(i);
            if (bean.getRecentContact().getContactId().equals(contact.getContactId())){
                bean.setRecentContact(contact);
                mViewAdapter.notifyItemChanged(i);
                break;
            }
            if (i == mContactList.size()-1){
                // 否则为新的最近会话
                RecentContactBean newBean = new RecentContactBean();
                newBean.setRecentContact(contact);
                NimUserInfo userInfo = getUserInfoByAccount(contact.getContactId());
                if (userInfo != null){
                    newBean.setUserInfo(userInfo);
                }else {
                    List<String> a = new ArrayList<>();
                    a.add(contact.getContactId());
                    getUserInfoRemote(a);
                }
                mContactList.add(0,newBean);
                mViewAdapter.notifyItemInserted(0);
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        //NIMClient.getService(MsgServiceObserve.class).observeRecentContact(mObserver,true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG,"onPause");
    }

    private void loadRecentList(){
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, List<RecentContact> result, Throwable exception) {
                        if (exception != null){
                            Log.e(TAG,"loadRecentList exception = " + exception.getMessage());
                            return;
                        }
                        if (code != 200){
                            Log.e(TAG,"loadRecentList error code = " + code);
                            return;
                        }
                        Log.e(TAG,"loadRecentList size = " + result.size());
                        List<RecentContactBean> contactBeans = createContactBeans(result);
                        mContactList.clear();
                        mContactList.addAll(contactBeans);
                        mViewAdapter.notifyDataSetChanged();
                    }
                });
    }

    private List<RecentContactBean> createContactBeans(List<RecentContact> recentContacts){
        List<String> accounts = new ArrayList<>();
        List<RecentContactBean> beanList = new ArrayList<>();
        RecentContactBean bean;
        for (RecentContact contact : recentContacts){
            bean = new RecentContactBean();
            bean.setRecentContact(contact);
            NimUserInfo userInfo = getUserInfoByAccount(contact.getContactId());
            if (userInfo != null){
                bean.setUserInfo(userInfo);
            }else {
                accounts.add(contact.getContactId());
            }
            beanList.add(bean);
        }
        if (!accounts.isEmpty()){
            getUserInfoRemote(accounts);
        }
        return beanList;
    }

    private NimUserInfo getUserInfoByAccount(String account){
        return NIMClient.getService(UserService.class).getUserInfo(account);
    }


    private void getUserInfoRemote(List<String> accounts){
        NIMClient.getService(UserService.class).fetchUserInfo(accounts)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {
                    @Override
                    public void onSuccess(List<NimUserInfo> param) {
                        updateView(param);
                    }

                    @Override
                    public void onFailed(int code) {

                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
    }

    private void updateView(List<NimUserInfo> param){
        boolean isUpdate = false;
        for (NimUserInfo userInfo : param){
            for (RecentContactBean bean : mContactList){
                if (userInfo.getAccount().equals(bean.getRecentContact().getContactId())){
                    bean.setUserInfo(userInfo);
                    isUpdate = true;
                }
            }
        }
        if (isUpdate && mViewAdapter != null){
            mViewAdapter.notifyDataSetChanged();
        }
    }
}

