package com.zykmanhua.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.viewpagerindicator.TabPageIndicator;
import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.TabAdapter;

public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager = null;
	private TabPageIndicator mTabPageIndicator = null;
	private TabAdapter mAdapter = null;
	private Button mBtn_Search = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
		
		mBtn_Search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext() , SearchActivity.class);
				startActivity(intent);
			}
		});
		
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewPager);
		mTabPageIndicator = (TabPageIndicator) findViewById(R.id.id_tabPageIndicator);
		mAdapter = new TabAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mTabPageIndicator.setViewPager(mViewPager, 0);
		mBtn_Search = (Button) findViewById(R.id.id_btn_top_search);
	}
	
	
}
