package com.will.gps.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.will.gps.bean.Group;

import java.util.ArrayList;
import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper {
    private Context mcontext;
    private Gson gson = new Gson();

    public DBOpenHelper(Context context) {
        super(context, "sign.db", null, 1);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_user = "CREATE TABLE user(phonenum VARCHAR(11),username VARCHAR(20),password VARCHAR(50),realname VARCHAR(20),birthday VARCHAR(20),sex VARCHAR(2),locate VARCHAR(50),signature VARCHAR(200),photo BLOB)";
        String create_mgroup = "CREATE TABLE mgroup(groupid INTEGER(11),groupname VARCHAR(20),groupowner VARCHAR(11),ownername VARCHAR(20),membernum INTEGER(11))";
        String create_tsmessage = "CREATE TABLE tsmessage(groupid INTEGER(11),type VARCHAR(20),sender VARCHAR(20),sendername VARCHAR(20),receiver VARCHAR(20),receivername VARCHAR(20),content TEXT,date VARCHAR(20),state INTEGER(1))";
        String create_groupmember="CREATE TABLE groupmember(groupid INTEGER(11),groupname VARCHAR(20),userphone VARCHAR(11),username VARCHAR(20))";
        db.execSQL(create_user);
        db.execSQL(create_mgroup);
        db.execSQL(create_tsmessage);
        db.execSQL(create_groupmember);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void updateuser(DBOpenHelper dbOpenHelper){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",MySocket.user.getUserName());
        contentValues.put("password",MySocket.user.getPassWord());
        contentValues.put("realname",MySocket.user.getRealName());
        contentValues.put("birthday",MySocket.user.getBirthday());
        contentValues.put("sex",MySocket.user.getSex());
        contentValues.put("signature",MySocket.user.getSignature());
        contentValues.put("locate",MySocket.user.getLocate());
        db.update("user",contentValues,"phonenum = "+MySocket.user.getPhonenum(),null);
    }//更新用户信息

    public void savegroup(DBOpenHelper dbOpenHelper,List<String> groupList){
        if(!groupList.isEmpty()){
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            for (String groups : groupList) {
                Group group = gson.fromJson(groups, Group.class);
                Cursor cursor = db.query("mgroup", null, "groupid="+group.getGroupid(), null, null, null, null);
                if(cursor.getCount()==0){
                    contentValues.put("groupid",group.getGroupid());
                    contentValues.put("groupname",group.getGroupname());
                    contentValues.put("groupowner",group.getGroupowner());
                    contentValues.put("ownername",group.getOwnername());
                    contentValues.put("membernum",group.getMembernum());
                    db.insert("mgroup",null,contentValues);
                }else {
                    contentValues.put("groupname",group.getGroupname());
                    contentValues.put("groupowner",group.getGroupowner());
                    contentValues.put("ownername",group.getOwnername());
                    contentValues.put("membernum",group.getMembernum());
                    db.update("mgroup",contentValues,"groupid="+group.getGroupid(),null);
                }
            }
        }
    }//保存群信息

    public void savemsg(DBOpenHelper dbOpenHelper,RMessage rMessage){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupid",rMessage.getGroupid());
        contentValues.put("type",rMessage.getType());
        contentValues.put("sender",rMessage.getSenderphone());
        contentValues.put("sendername",rMessage.getSendername());
        contentValues.put("receiver",rMessage.getReceiverphone());
        contentValues.put("receivername",rMessage.getReceivername());
        contentValues.put("content",rMessage.getContent());
        contentValues.put("date",rMessage.getDate());
        contentValues.put("state",1);
        db.insert("tsmessage",null,contentValues);
    }//保存群消息

    public List<String> searchgroup(DBOpenHelper dbOpenHelper){
        Group group = new Group();
        List<String> groupList = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("mgroup", null, null, null, null, null, null);
        while(cursor.moveToNext()){
            group.setGroupid(cursor.getInt(cursor.getColumnIndex("groupid")));
            group.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
            group.setGroupowner(cursor.getString(cursor.getColumnIndex("groupowner")));
            group.setOwnername(cursor.getString(cursor.getColumnIndex("ownername")));
            group.setMembernum(cursor.getInt(cursor.getColumnIndex("membernum")));
            groupList.add(gson.toJson(group));
        }
        return groupList;
    }//查询群信息
}
