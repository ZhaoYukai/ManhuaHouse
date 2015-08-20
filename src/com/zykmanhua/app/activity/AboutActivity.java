package com.zykmanhua.app.activity;

import com.zykmanhua.app.R;
import com.zykmanhua.app.util.Topbar;

import android.app.Activity;
import android.os.Bundle;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_manhuahouse);

		// 顶部的TopBar相关的设置
		Topbar topbar = (Topbar) findViewById(R.id.id_about_topBar);
		topbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
			@Override
			public void rightClick() {
				
			}

			@Override
			public void leftClick() {
				finish();
			}
		});
	}

}
