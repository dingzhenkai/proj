package com.sjtu.se2017.positivetime.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sjtu.se2017.positivetime.dao.AppInfoOpenHelper;
import com.sjtu.se2017.positivetime.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppInfoDao {
	private AppInfoOpenHelper openHelper;
	SQLiteDatabase db;

	public AppInfoDao(Context context) {
		openHelper = new AppInfoOpenHelper(context, "app_info", 1);
	}

	public AppInfoDao(){

	}
	/*
	 * 插入一条数据，如果数据已经存在，就更新
	 */
	public void insertOrUpdate(String appName, int weight) {
		Cursor cursor = query(appName);

		ContentValues cv = new ContentValues(2);
		cv.put("label",appName);
		cv.put("weight",weight);
		if(cursor.getCount()==0){
			db.insert("info",null,cv);
		}
		else{
			db.update("info",cv,"label=?",new String[]{appName});
		}
		db.close();
	}

	/**
	 * 查询数据库有没有对应的数据
	 */
	public Cursor  query(String appName){
		db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from info where label=?",
				new String[] { appName });
		if(cursor!=null){
			cursor.moveToFirst();
		}
		return cursor;
	}

	public int checkweight(String appName){
		int weight = 50;
		Cursor cursor = query(appName);
		if(cursor.getCount()==0){
		}
		else{
			weight = cursor.getInt(cursor.getColumnIndex("weight"));
		}
		db.close();
		return weight;
	}

	/*
	 *get all apps installed
	 */
	public List<AppInfo> getAllApps(Context context)throws PackageManager.NameNotFoundException{
		List<AppInfo> appInfos  = new ArrayList<AppInfo>();
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		for (PackageInfo packageInfo : packageInfos) {
			String packageName = packageInfo.packageName;
			//过滤掉系统app
//            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
//                continue;
//            }
			AppInfo appInfo = new AppInfo();
			ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
			appInfo.setAppName((String) packageManager.getApplicationLabel(applicationInfo));

			if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
				continue;
			}
			appInfo.setImage(applicationInfo.loadIcon(packageManager));
			appInfos.add(appInfo);
		}
		return appInfos;
	}
	/**
	 * 删除一条数据，如果数据不存在，就不删除
	 * 
	 * @param packageName
	 * @return 删除成功.失败
	 *//*
	public boolean delete(String packageName) {
		boolean delete = false;
		SQLiteDatabase db = openHelper.getWritableDatabase();

		Cursor cursor = db.rawQuery(
				"select * from appLocktb where packageName=?",
				new String[] { packageName });

		if (cursor != null) {
			delete = cursor.moveToNext();
			cursor.close();
		}
		ContentValues values = new ContentValues();
		values.clear();
		if (delete) {
			// 说明有这条数据，删除
			int data = db.delete(TABLENAME, PACKAGENAME + "=?",
					new String[] { packageName });
			db.close();
			if(data>0){
				return true;
			}else {
				return false;
			}
		} else {
			// 说明没有有这条数据，删除
			return false;
		}
	}*/

}
