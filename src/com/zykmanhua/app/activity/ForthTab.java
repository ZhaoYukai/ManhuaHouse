package com.zykmanhua.app.activity;

import com.zykmanhua.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ForthTab extends Fragment {
	
	public ForthTab() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.frag_forth , container , false);
		TextView tv = (TextView) view.findViewById(R.id.id_tv4);
		tv.setText("ÄÚÈÝ:DFragment");
		return view;
		
	}

}
