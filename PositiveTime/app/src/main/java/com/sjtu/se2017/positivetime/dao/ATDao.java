package com.sjtu.se2017.positivetime.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sjtu.se2017.positivetime.dao.AppInfoOpenHelper;
import com.sjtu.se2017.positivetime.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class ATDao {
    private ATOpenHelper openHelper;
    SQLiteDatabase db;

    public ATDao(Context context) {
        openHelper = new ATOpenHelper(context, "AT_info", 1);
    }

    /**
     * 查询数据库有没有对应的数据
     */
    public Cursor  query(int time){
        db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from AT where time=?",
                new String[] { ""+time });
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    /*
     * 插入一条数据，如果数据已经存在，就更新
     */
    public void insertOrUpdate(int time, int AT) {
        Cursor cursor = query(time);

        ContentValues cv = new ContentValues(2);
        cv.put("time",time);
        cv.put("AT",AT);
        if(cursor.getCount()==0){
            db.insert("AT",null,cv);
        }
        else{
            db.update("AT",cv,"time=?",new String[]{""+time});
        }
        db.close();
    }

    public int checkAT(int time){
        int AT = 0;
        Cursor cursor = query(time);
        if(cursor.getCount()==0){
            Log.v("checkAT","this time has not AT");
        }
        else{
            AT = cursor.getInt(cursor.getColumnIndex("AT"));
        }
        db.close();
        return AT;
    }
}
