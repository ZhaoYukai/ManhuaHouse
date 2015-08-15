package com.zykmanhua.app.activity;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.thinkland.sdk.android.JuheSDKInitializer;

public class ZYKManhuaApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		JuheSDKInitializer.initialize(getApplicationContext());
		

		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.build();
		
		ImageLoader.getInstance().init(configuration);
	}
}
