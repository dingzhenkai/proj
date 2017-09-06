package com.sjtu.se2017.positivetime.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ATDao {
    private ATOpenHelper openHelper;
    SQLiteDatabase db;

    public ATDao(Context context) {
        openHelper = new ATOpenHelper(context, "AT_info", 1);
    }

    /**
     * 查询数据库有没有对应的数据
     */
    public Cursor  query(String time){
        db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from AT where time=?",
                new String[] { time });
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    /*
     * 插入一条数据，如果数据已经存在，就更新
     */
    public void insertOrUpdate(String time, long AT) {
        Cursor cursor = query(time);

        ContentValues cv = new ContentValues(2);
        cv.put("time",time);
        cv.put("AT",AT);
        if(cursor.getCount()==0){
            db.insert("AT",null,cv);
        }
        else{
            db.update("AT",cv,"time=?",new String[]{time});
        }
        db.close();
    }

    public long checkAT(String time){
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
