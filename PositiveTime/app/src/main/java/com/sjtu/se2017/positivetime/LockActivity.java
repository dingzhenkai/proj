package com.sjtu.se2017.positivetime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class LockActivity extends Activity {

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.lock_ui);
	}

	@Override
	public void onBackPressed() {
		/*
		 * <activity android:name="com.android.launcher2.Launcher"
		 * android:launchMode="singleTask" android:clearTaskOnLaunch="true"
		 * android:stateNotNeeded="true" android:theme="@style/Theme"
		 * android:screenOrientation="nosensor"
		 * android:windowSoftInputMode="stateUnspecified|adjustPan">
		 * <intent-filter> <action android:name="android.intent.action.MAIN" />
		 * <category android:name="android.intent.category.HOME" /> <category
		 * android:name="android.intent.category.DEFAULT" /> <category
		 * android:name="android.intent.category.MONKEY"/> </intent-filter>
		 * </activity>
		 */
		// 打开桌面
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		startActivity(intent);
		// super.onBackPressed();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
