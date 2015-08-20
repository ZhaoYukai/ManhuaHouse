package com.zykmanhua.app.activity;

import com.zykmanhua.app.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MyRecordHistory extends Fragment implements OnClickListener {
	
	private Context mContext = null;
	private Button mBtn_GoHistory = null;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.frag_myrecord_history_empty , container, false);
		
		mContext = getActivity().getApplicationContext();
		mBtn_GoHistory = (Button) view.findViewById(R.id.id_btn_myrecord_history_empty);
		mBtn_GoHistory.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_btn_myrecord_history_empty:
			Intent intent = new Intent(mContext , MainActivity.class);
			startActivity(intent);
			getActivity().finish();
			break;
		}
	}


}
