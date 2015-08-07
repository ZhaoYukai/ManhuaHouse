package com.zykmanhua.app.activity;

import com.zykmanhua.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SecondTab extends Fragment {
	
	public SecondTab() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.frag_second , container , false);
		TextView tv = (TextView) view.findViewById(R.id.id_tv2);
		tv.setText("ÄÚÈÝ:BFragment");
		return view;
		
	}

}
