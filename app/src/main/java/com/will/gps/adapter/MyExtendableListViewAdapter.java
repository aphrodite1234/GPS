package com.will.gps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.will.gps.R;
import com.will.gps.bean.Group;

import org.w3c.dom.Text;

import java.util.List;

public class MyExtendableListViewAdapter extends BaseExpandableListAdapter {
    private Context mcontext;
    private List<String> mGroupList;
    private List<List<Group>> mChildList;

    public MyExtendableListViewAdapter(Context context,List<String> groups,List<List<Group>> childs){
        mcontext=context;
        this.mGroupList=groups;
        this.mChildList=childs;
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
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.groupIndicator=(ImageView)convertView.findViewById(R.id.expandable_list_group_indicator);
            groupViewHolder.groupName=(TextView)convertView.findViewById(R.id.expandable_list_group_name);
            groupViewHolder.newtip=(TextView)convertView.findViewById(R.id.group_unread_message_count);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if(isExpanded){
            groupViewHolder.groupIndicator.setImageResource(R.drawable.ic_keyboard_arrow_down_24dp);
        }else{
            groupViewHolder.groupIndicator.setImageResource(R.drawable.ic_keyboard_arrow_right_24dp);
        }
        groupViewHolder.groupName.setText(mGroupList.get(groupPosition));
        groupViewHolder.newtip.setVisibility(View.GONE);
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.childName = (TextView) convertView.findViewById(R.id.expandable_list_child_name);
            convertView.setTag(childViewHolder);

        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.childName.setText(mChildList.get(groupPosition).get(childPosition).getGroupname());
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
        private TextView newtip;
    }

    static class ChildViewHolder {
        private TextView childName;
    }
}