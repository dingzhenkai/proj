package com.sjtu.se2017.positivetime.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.SystemClock;

import com.sjtu.se2017.positivetime.LockActivity;
import com.sjtu.se2017.positivetime.dao.AppInfoDao;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class WatchDogService extends IntentService {
	private static final boolean DEBUG = false;
	private static final String APPINFO_UNCHECKED = "appinfo.UNCHECKED";
	private Context context;
	private AppInfoDao appInfoDao;
	private String unCheckedPackageName;
	private UnCheckedReceiver receiver;

	public WatchDogService() {
		super("abcde");
		context = this;
		appInfoDao = new AppInfoDao(context);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new UnCheckedReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(APPINFO_UNCHECKED);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		while (true) {
			String packageName = "packagenamenull";
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
				long time = System.currentTimeMillis();
				List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
				if (appList != null && appList.size() > 0) {
					SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
					for (UsageStats usageStats : appList) {
						mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
					}
					if (mySortedMap != null && !mySortedMap.isEmpty()) {
						packageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
					}
				}
			} else {
				ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
				List<RunningTaskInfo> runningTasks = am.getRunningTasks(99);
				packageName = runningTasks.get(0).topActivity
						.getPackageName();
			}
			// 获取最近打开的App label
			PackageManager packageManager = context.getPackageManager();
			ApplicationInfo applicationInfo = new ApplicationInfo();
			try{
				applicationInfo = packageManager.getApplicationInfo(packageName, 0);
			}catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
			String appName = (String) packageManager.getApplicationLabel(applicationInfo);
			Cursor b = appInfoDao.query(appName);

			if (appInfoDao.checkweight(appName) < 50) {
				// 说明是娱乐类程序
				if (packageName.equals(unCheckedPackageName)) {
				} else {
					Intent intent2 = new Intent(context, LockActivity.class);
					intent2.putExtra("packageName", packageName);// TODO：这一行不加，就没有办法去临时取消保护了！！！
					intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent2);
				}
			} else {
			}
			if (DEBUG) {
				System.out.println("packageName 0:" + packageName);
			}
			SystemClock.sleep(300);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
	}

	class UnCheckedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				if (intent.getAction().equals(APPINFO_UNCHECKED)) {
					unCheckedPackageName = intent.getStringExtra("packageName");
					System.out.println("unCheckedPackageName: "
							+ unCheckedPackageName);
				} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
					unCheckedPackageName = null;
				}
			}
		}

	}
}
