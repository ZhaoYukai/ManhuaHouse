package com.zykmanhua.app.activity;

import com.viewpagerindicator.TabPageIndicator;
import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.MyRecordAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyRecordFragment extends Fragment {
	
	private ViewPager mMyRecordViewPager = null;
	private TabPageIndicator mMyRecordTabPageIndicator = null;
	private MyRecordAdapter mMyRecordAdapter = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.tab02	, container , false);
		
		mMyRecordViewPager = (ViewPager) view.findViewById(R.id.id_myrecord_viewPager);
		mMyRecordTabPageIndicator = (TabPageIndicator) view.findViewById(R.id.id_myrecord_tabPageIndicator);
		mMyRecordAdapter = new MyRecordAdapter(getFragmentManager());
		mMyRecordViewPager.setAdapter(mMyRecordAdapter);
		mMyRecordTabPageIndicator.setViewPager(mMyRecordViewPager, 0);
		
		return view;
	}
	
}
