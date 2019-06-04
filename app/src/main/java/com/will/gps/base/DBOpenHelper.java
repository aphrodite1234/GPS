package com.will.gps.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.will.gps.bean.ChatEntity;
import com.will.gps.bean.Group;
import com.will.gps.bean.GroupMember;
import com.will.gps.bean.Signin;

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
        String create_user = "CREATE TABLE user(phonenum VARCHAR(11),username VARCHAR(20),password VARCHAR(50)," +
                "realname VARCHAR(20),birthday VARCHAR(20),sex VARCHAR(2),locate VARCHAR(50)," +
                "signature VARCHAR(200),photo BLOB)";
        String create_mgroup = "CREATE TABLE mgroup(groupid INTEGER(11),groupname VARCHAR(20)," +
                "groupowner VARCHAR(11),ownername VARCHAR(20),membernum INTEGER(11))";
        String create_tsmessage = "CREATE TABLE tsmessage(user VARCHAR(11),groupid INTEGER(11),type VARCHAR(20)," +
                "sender VARCHAR(20),sendername VARCHAR(20),receiver VARCHAR(20),receivername VARCHAR(20)," +
                "content TEXT,date VARCHAR(20),state INTEGER(1))";
        String create_groupmember="CREATE TABLE groupmember(gmid INTEGER(11),groupid INTEGER(11),groupname VARCHAR(20)," +
                "userphone VARCHAR(11),username VARCHAR(20))";
        String create_signin="CREATE TABLE signin(id INTEGER(11),groupid INTEGER(11),originator VARCHAR(11),time VARCHAR(20)," +
                "longitude VARCHAR(20),latitude VARCHAR(20),region VARCHAR(20),receiver VARCHAR(11)," +
                "rlongitude VARCHAR(20),rlatitude VARCHAR(20),state VARCHAR(20),done VARCHAR(20),result VARCHAR(20))";
        db.execSQL(create_user);
        db.execSQL(create_mgroup);
        db.execSQL(create_tsmessage);
        db.execSQL(create_groupmember);
        db.execSQL(create_signin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //更新用户信息
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
    }

    //保存群
    public void savegroup(DBOpenHelper dbOpenHelper,List<String> groupList){
        if(!groupList.isEmpty()){
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            db.delete("mgroup",null,null);
            ContentValues contentValues = new ContentValues();
            for (String groups : groupList) {
                Group group = gson.fromJson(groups, Group.class);
                contentValues.put("groupid",group.getGroupid());
                contentValues.put("groupname",group.getGroupname());
                contentValues.put("groupowner",group.getGroupowner());
                contentValues.put("ownername",group.getOwnername());
                contentValues.put("membernum",group.getMembernum());
                db.insert("mgroup",null,contentValues);
            }
        }
    }

    //查询群信息
    public List<String> searchgroup(DBOpenHelper dbOpenHelper){
        Group group = new Group();
        List<String> groupList = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor1=db.query("groupmember", new String[]{"groupid"},"userphone="+MySocket.user.getPhonenum(),null,null,null,null);
        while (cursor1.moveToNext()){
            Cursor cursor = db.query("mgroup", null, "groupid="+cursor1.getInt(cursor1.getColumnIndex("groupid")), null, null, null, null);
            while(cursor.moveToNext()){
                group.setGroupid(cursor.getInt(cursor.getColumnIndex("groupid")));
                group.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
                group.setGroupowner(cursor.getString(cursor.getColumnIndex("groupowner")));
                group.setOwnername(cursor.getString(cursor.getColumnIndex("ownername")));
                group.setMembernum(cursor.getInt(cursor.getColumnIndex("membernum")));
                groupList.add(gson.toJson(group));
            }
        }
        return groupList;
    }

    //保存群成员
    public void savemember(DBOpenHelper dbOpenHelper, List<String> list){
        if(!list.isEmpty()){
            SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
            db.delete("groupmember",null,null);
            ContentValues contentValues=new ContentValues();
            for(String groupmem:list){
                GroupMember groupMember = gson.fromJson(groupmem,GroupMember.class);
                contentValues.put("gmid",groupMember.getGmid());
                contentValues.put("groupid",groupMember.getGroupid());
                contentValues.put("groupname",groupMember.getGroupname());
                contentValues.put("username",groupMember.getUsername());
                contentValues.put("userphone",groupMember.getUserphone());
                db.insert("groupmember",null,contentValues);
            }
        }
    }

    //保存群消息
    public void savemsg(DBOpenHelper dbOpenHelper,RMessage rMessage){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user",MySocket.user.getPhonenum());
        contentValues.put("groupid",rMessage.getGroupid());
        contentValues.put("type",rMessage.getType());
        contentValues.put("sender",rMessage.getSenderphone());
        contentValues.put("sendername",rMessage.getSendername());
        contentValues.put("receiver",rMessage.getReceiverphone());
        contentValues.put("receivername",rMessage.getReceivername());
        contentValues.put("content",rMessage.getContent());
        contentValues.put("date",rMessage.getDate());
        contentValues.put("state",0);
        db.insert("tsmessage",null,contentValues);
    }

    //群消息已读
    public void setMessage(DBOpenHelper dbOpenHelper,int groupId){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("state",1);
        db.update("tsmessage",contentValues,"state = 0 AND groupid = "+groupId+" AND user='"+MySocket.user.getPhonenum()+"'",null);
    }

    //保存签到信息
    public void savesign(DBOpenHelper dbOpenHelper, Signin signin){
        SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("signin",null,"id="+signin.getId(),null,null,null,null);
        if(cursor.getCount()==0){
            ContentValues contentValues=new ContentValues();
            contentValues.put("id",signin.getId());
            contentValues.put("groupid",signin.getGroupid());
            contentValues.put("originator",signin.getOriginator());
            contentValues.put("time",signin.getTime());
            contentValues.put("longitude",signin.getLongitude());
            contentValues.put("latitude",signin.getLatitude());
            contentValues.put("region",signin.getRegion());
            contentValues.put("receiver",signin.getReceiver());
            contentValues.put("rlongitude",signin.getRlongitude());
            contentValues.put("rlatitude",signin.getRlatitude());
            contentValues.put("state",signin.getState());
            contentValues.put("done",signin.getDone());
            contentValues.put("result",signin.getResult());
            db.insert("signin",null,contentValues);
        }else {
            ContentValues contentValues=new ContentValues();
            contentValues.put("groupid",signin.getGroupid());
            contentValues.put("originator",signin.getOriginator());
            contentValues.put("time",signin.getTime());
            contentValues.put("longitude",signin.getLongitude());
            contentValues.put("latitude",signin.getLatitude());
            contentValues.put("region",signin.getRegion());
            contentValues.put("receiver",signin.getReceiver());
            contentValues.put("rlongitude",signin.getRlongitude());
            contentValues.put("rlatitude",signin.getRlatitude());
            contentValues.put("state",signin.getState());
            contentValues.put("done",signin.getDone());
            contentValues.put("result",signin.getResult());
            db.update("signin",contentValues,"id="+signin.getId(),null);
        }
    }

    //查询个人未签到信息
    public List<String> searchsignin(DBOpenHelper dbOpenHelper){
        List<String> signList = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("signin", null, "receiver='"+MySocket.user.getPhonenum()+"' AND done=0 AND state=0", null, null, null, null);
        while(cursor.moveToNext()){
            Signin signin = new Signin();
            signin.setGroupid(cursor.getInt(cursor.getColumnIndex("groupid")));
            signin.setOriginator(cursor.getString(cursor.getColumnIndex("originator")));
            signin.setTime(cursor.getString(cursor.getColumnIndex("time")));
            signin.setResult(cursor.getString(cursor.getColumnIndex("result")));
            signList.add(gson.toJson(signin));
        }
        return signList;
    }

    //查询个人未读消息
    public List<String> searchstate(DBOpenHelper dbOpenHelper){
        List<String> stateList = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("tsmessage", null, "user='"+MySocket.user.getPhonenum()+"' AND state=0", null, null, null, null);
        while(cursor.moveToNext()){
            RMessage rMessage = new RMessage();
            rMessage.setType("群消息");
            rMessage.setGroupid(cursor.getInt(cursor.getColumnIndex("groupid")));
            rMessage.setContent(cursor.getString(cursor.getColumnIndex("content")));
            rMessage.setDate(cursor.getString(cursor.getColumnIndex("date")));
            rMessage.setSendername(cursor.getString(cursor.getColumnIndex("sendername")));
            stateList.add(gson.toJson(rMessage));
        }
        return stateList;
    }
}
