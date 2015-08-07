package com.zykmanhua.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.viewpagerindicator.TabPageIndicator;
import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.TabAdapter;

public class MainActivity extends FragmentActivity {

	//private Context mContext = null;

	private ViewPager mViewPager = null;
	private TabPageIndicator mTabPageIndicator = null;
	private TabAdapter mAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		//mContext = MainActivity.this;
		initView();
		
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewPager);
		mTabPageIndicator = (TabPageIndicator) findViewById(R.id.id_tabPageIndicator);
		mAdapter = new TabAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mTabPageIndicator.setViewPager(mViewPager, 0);
	}
}
