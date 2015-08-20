package com.zykmanhua.app.activity;

import com.viewpagerindicator.TabPageIndicator;
import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.TabAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LibraryFragment extends Fragment {
	
	private ViewPager mViewPager = null;
	private TabPageIndicator mTabPageIndicator = null;
	private TabAdapter mAdapter = null;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.tab01	, container , false);
		
		mViewPager = (ViewPager) view.findViewById(R.id.id_viewPager);
		mTabPageIndicator = (TabPageIndicator) view.findViewById(R.id.id_tabPageIndicator);
		mAdapter = new TabAdapter(getFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mTabPageIndicator.setViewPager(mViewPager, 0);
		
		return view;
		
	}

}
