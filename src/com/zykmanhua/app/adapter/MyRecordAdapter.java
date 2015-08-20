package com.zykmanhua.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.zykmanhua.app.activity.MyRecordCollect;
import com.zykmanhua.app.activity.MyRecordHistory;
import com.zykmanhua.app.util.Config;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyRecordAdapter extends FragmentPagerAdapter {

	private List<Fragment> mMyRecordFragments = null;
	
	public MyRecordAdapter(FragmentManager fm) {
		super(fm);
		
		mMyRecordFragments = new ArrayList<Fragment>();
		
		MyRecordCollect myRecordCollect = new MyRecordCollect();
		mMyRecordFragments.add(myRecordCollect);
		
		MyRecordHistory myRecordHistory = new MyRecordHistory();
		mMyRecordFragments.add(myRecordHistory);
	}

	@Override
	public Fragment getItem(int position) {
		return mMyRecordFragments.get(position);
	}

	@Override
	public int getCount() {
		return Config.MYRECORD_TITLES.length;
	}
	
	
	@Override
	public CharSequence getPageTitle(int position) {
		return Config.MYRECORD_TITLES[position];
	}

}
