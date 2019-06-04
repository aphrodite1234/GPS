package com.will.gps.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.GroupChatActivity;
import com.will.gps.R;
import com.will.gps.SignInActivity;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.RMessage;
import com.will.gps.bean.Group;
import com.will.gps.bean.MessageTabEntity;
import com.will.gps.bean.Signin;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaiBenBen on 2019/5/27.
 */

public class MessageListAdapter extends BaseExpandableListAdapter {
    private Context mcontext;
    private List<String> mGroupList;
    private List<List<RMessage>> mChildList;
    private List<String> mChildList1;
    private Gson gson=new Gson();
    private int[] groupunread=new int[3];//分别存放签到消息，群聊消息，个人消息
    private int [] groupchatunread=new int[100];//[0]位置存放群个数，后面的存放每个群的名称
    private int[] groupchatunread1=new int[100];//[0]位置不存东西，后面的存放上一个数组中群位置对应的消息个数
    private String[] friendchatunread=new String[100];//[0]不存东西，后面的存发送者手机号
    private int[] friendchatunread1=new int[100];//[0]存发送者个数，
    //public static Intent i;
    Group group=new Group();

    public MessageListAdapter(Context context,List<String> mChildList1){
        mcontext=context;
        mGroupList=new ArrayList<>();
        mGroupList.add("签到消息");
        mGroupList.add("群消息");
        mGroupList.add("个人消息");

        mChildList=new ArrayList<>();
        List<RMessage> child1=new ArrayList<>();//签到消息
        List<RMessage> child2=new ArrayList<>();//群消息
        List<RMessage> child3=new ArrayList<>();//个人消息

       if(!mChildList1.isEmpty()){
           for (String Message : mChildList1) {
               RMessage message4 = gson.fromJson(Message, RMessage.class);
               if(message4.getType().equals("签到消息")){
                   child1.add(message4);
                   if(message4.getState()==1)
                       groupunread[0]++;
               }else if(message4.getType().equals("群消息")){
                   child2.add(message4);
                   groupunread[1]++;
                   if(message4.getState()==1){
                       if(groupchatunread[0]>0){
                           for(int i=1;i<=groupchatunread[0];i++){
                               if(message4.getGroupid()==groupchatunread[i]){
                                   groupchatunread1[i]++;//没加第一条，后面再进行处理
                                   groupchatunread[0]--;//重复群为使数量不变-1
                                   groupunread[1]--;//重复群的未读消息算一个（重复的消掉）
                                   child2.remove(child2.size()-1);
                                   child2.remove(i-1);
                                   child2.add(i-1,message4);
                                   break;
                               }
                           }
                       }
                       groupchatunread[0]++;
                       groupchatunread[groupchatunread[0]]=message4.getGroupid();
                   }
               }else if(message4.getType().equals("个人消息")){
                   child3.add(message4);
                   if(message4.getState()==1){
                       groupunread[2]++;
                       if(friendchatunread1[0]>0){
                           for(int i=1;i<=friendchatunread1[0];i++){
                               if(message4.getSendername()==friendchatunread[i]){
                                   friendchatunread1[i]++;//没加第一条，后面再进行处理
                                   friendchatunread1[0]--;
                                   groupunread[2]--;
                                   child3.remove(i-1);
                                   child3.remove(i-1);
                                   child3.add(i-1,message4);
                                   break;
                               }
                           }
                       }
                       friendchatunread1[0]++;
                       friendchatunread[friendchatunread1[0]]=message4.getSendername();
                   }
               }
           }
       }
        mChildList.add(child1);
        mChildList.add(child2);
        mChildList.add(child3);
        /*mChildList=new ArrayList<>();
        List<MessageTabEntity> child1;
        List<MessageTabEntity> child2;*/
    }
    @Override
    // 获取分组的个数
    public int getGroupCount() {
        if(mGroupList==null)
            return 0;
        else
            return mGroupList.size();
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList.get(groupPosition).size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return mChildList.get(groupPosition);
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     */
// 获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        MessageListAdapter.GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_group, parent, false);
            groupViewHolder = new MessageListAdapter.GroupViewHolder();
            groupViewHolder.groupIndicator=(ImageView)convertView.findViewById(R.id.expandable_list_group_indicator);
            groupViewHolder.groupName=(TextView)convertView.findViewById(R.id.expandable_list_group_name);
            groupViewHolder.news=(TextView)convertView.findViewById(R.id.group_unread_message_count);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (MessageListAdapter.GroupViewHolder) convertView.getTag();
        }
        if(isExpanded){
            groupViewHolder.groupIndicator.setImageResource(R.drawable.ic_keyboard_arrow_down_24dp);
        }else{
            groupViewHolder.groupIndicator.setImageResource(R.drawable.ic_keyboard_arrow_right_24dp);
        }
        groupViewHolder.groupName.setText(mGroupList.get(groupPosition));
        if(groupunread[groupPosition]>0){
            groupViewHolder.news.setText(String.valueOf(groupunread[groupPosition]));
        }else{
            groupViewHolder.news.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final MessageListAdapter.ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_message, parent, false);
            childViewHolder = new MessageListAdapter.ChildViewHolder();
            childViewHolder.user_photo = (ImageView) convertView.findViewById(R.id.user_photo);
            childViewHolder.user_nmae=(TextView)convertView.findViewById(R.id.user_name);
            childViewHolder.send_time=(TextView)convertView.findViewById(R.id.send_time);
            childViewHolder.user_message=(TextView)convertView.findViewById(R.id.user_message);
            childViewHolder.unread_message_count=(TextView)convertView.findViewById(R.id.unread_message_count);
            childViewHolder.item=(LinearLayout)convertView.findViewById(R.id.ll_news);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (MessageListAdapter.ChildViewHolder) convertView.getTag();
        }
        childViewHolder.user_photo.setImageResource(R.mipmap.group_chat);
        DBOpenHelper dbOpenHelper=new DBOpenHelper(mcontext);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor=db.query("mgroup",null,"groupid="+mChildList.get(groupPosition).get(childPosition).getGroupid(),null, null, null, null);
        while(cursor.moveToNext()){
            group.setGroupid(cursor.getInt(cursor.getColumnIndex("groupid")));
            group.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
            group.setGroupowner(cursor.getString(cursor.getColumnIndex("groupowner")));
            group.setOwnername(cursor.getString(cursor.getColumnIndex("ownername")));
            group.setMembernum(cursor.getInt(cursor.getColumnIndex("membernum")));
        }
        childViewHolder.user_nmae.setText(group.getGroupname());
        childViewHolder.send_time.setText(mChildList.get(groupPosition).get(childPosition).getDate());
        childViewHolder.user_message.setText(mChildList.get(groupPosition).get(childPosition).getSendername()+":"+mChildList.get(groupPosition).get(childPosition).getContent());
        if(groupPosition==0){
            childViewHolder.unread_message_count.setText("1");
            childViewHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(mcontext, SignInActivity.class);
                    i.putExtra("groupid",mChildList.get(groupPosition).get(childPosition).getGroupid());
                    i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(i);
                    childViewHolder.unread_message_count.setVisibility(View.INVISIBLE);

                }
            });
        }else if(groupPosition==1){
            childViewHolder.unread_message_count.setText(String.valueOf(groupchatunread1[childPosition+1]+1));
            childViewHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(mcontext, GroupChatActivity.class);
                    i.putExtra("groupname",group.getGroupname());
                    i.putExtra("groupid",String.valueOf(group.getGroupid()));
                    i.putExtra("groupowner", group.getGroupowner());
                    i.putExtra("membernum", group.getMembernum());
                    i.putExtra("ismember","true");
                    i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(i);
                    childViewHolder.unread_message_count.setVisibility(View.INVISIBLE);

                }
            });
        }else if(groupPosition==2){
            childViewHolder.unread_message_count.setText(String.valueOf(friendchatunread1[childPosition+1]+1));
            childViewHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mcontext,"好友聊天功能未实现",Toast.LENGTH_SHORT).show();
                }
            });
        }

        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        private ImageView groupIndicator;
        private TextView groupName;
        private TextView news;
    }

    static class ChildViewHolder {
        private ImageView user_photo;
        private TextView user_nmae;
        private TextView send_time;
        private TextView user_message;
        private TextView unread_message_count;
        private LinearLayout item;
    }
}

