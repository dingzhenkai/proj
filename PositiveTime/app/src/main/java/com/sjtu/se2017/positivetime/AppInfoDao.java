package com.sjtu.se2017.positivetime;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import com.sjtu.se2017.positivetime.model.AppInfo;

public class AppInfoDao {

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
}
