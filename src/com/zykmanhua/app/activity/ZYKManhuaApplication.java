package com.zykmanhua.app.activity;

import android.app.Application;

import com.thinkland.sdk.android.JuheSDKInitializer;

public class ZYKManhuaApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		JuheSDKInitializer.initialize(getApplicationContext());
	}
}
