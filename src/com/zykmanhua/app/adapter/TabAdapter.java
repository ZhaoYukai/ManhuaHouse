package com.zykmanhua.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.zykmanhua.app.activity.FifthTab;
import com.zykmanhua.app.activity.FirstTab;
import com.zykmanhua.app.activity.ForthTab;
import com.zykmanhua.app.activity.SecondTab;
import com.zykmanhua.app.activity.ThirdTab;
import com.zykmanhua.app.util.Config;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
	
	private List<Fragment> mFragments = null;
	
	public TabAdapter(FragmentManager fm) {
		super(fm);
		
		mFragments = new ArrayList<Fragment>();
		
		FirstTab firstTab = new FirstTab();
		mFragments.add(firstTab);
		
		SecondTab secondTab = new SecondTab();
		mFragments.add(secondTab);
		
		ThirdTab thirdTab = new ThirdTab();
		mFragments.add(thirdTab);
		
		ForthTab forthTab = new ForthTab();
		mFragments.add(forthTab);
		
		FifthTab fifthTab = new FifthTab();
		mFragments.add(fifthTab);
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return Config.TITLES.length;
	}
	
	
	@Override
	public CharSequence getPageTitle(int position) {
		return Config.TITLES[position];
	}

}
