package com.will.gps.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private Context mcontext;
    private SQLiteDatabase db;

    public DBOpenHelper(Context context) {
        super(context, "sign.db", null, 1);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_user = "CREATE TABLE user(phonenum VARCHAR(11),username VARCHAR(20),password VARCHAR(50),realname VARCHAR(20),birthday VARCHAR(20),sex VARCHAR(2),locate VARCHAR(50),signature VARCHAR(200),photo BLOB)";
        String create_mgroup = "CREATE TABLE mgroup(groupid INTEGER(11),groupname VARCHAR(20),groupowner VARCHAR(11),ownername VARCHAR(20),membernum INTEGER(11))";
        String create_tsmessage = "CREATE TABLE tsmeaage(groupid INTEGER(11),type VARCHAR(20),sender VARCHAR(20),sendername VARCHAR(20),reciver VARCHAR(20),receivername VARCHAR(20),content TEXT,date VARCHAR(20),state INTEGER(1))";
        db.execSQL(create_user);
        db.execSQL(create_mgroup);
        db.execSQL(create_tsmessage);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
