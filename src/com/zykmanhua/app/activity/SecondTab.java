package com.zykmanhua.app.activity;

import java.util.ArrayList;

import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.ManhuaTypeAdapter;
import com.zykmanhua.app.bean.Manhua;
import com.zykmanhua.app.util.Config;
import com.zykmanhua.app.util.GetManhuaDataByType;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SecondTab extends Fragment {
	
	private Context mContext = null;
	private GetManhuaDataByType getManhuaDataByType = null;
	private ArrayList<Manhua> mManhuas = null;
	private ManhuaTypeAdapter mManhuaTypeAdapter = null;
	private ListView mListView = null;
	
	public SecondTab() {
		// TODO Auto-generated constructor stub
	}
	
	
	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.RESULT_SUCCESS_CODE:
				mManhuas = (ArrayList<Manhua>) msg.obj;
				mManhuaTypeAdapter = new ManhuaTypeAdapter(mContext, mManhuas, mListView);
				mListView.setAdapter(mManhuaTypeAdapter);
				mListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Manhua manhua = mManhuas.get(position);
						Intent intent = new Intent(mContext , ChapterActivity.class);
						intent.putExtra(Config.KEY_ManhuaName, manhua.getmName());
						intent.putExtra(Config.KEY_ManhuaType, manhua.getmType());
						intent.putExtra(Config.KEY_ManhuaLastUpdate, manhua.getmLastUpdate());
						intent.putExtra(Config.KEY_ManhuaIsFinish, manhua.ismFinish());
						intent.putExtra(Config.KEY_CoverImg , manhua.getmCoverImg());
						startActivity(intent);
					}
				});
				break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.frag_second, container, false);
		mContext = getActivity().getApplicationContext();
		mManhuas = new ArrayList<Manhua>();
		mListView = (ListView) view.findViewById(R.id.id_listView);
		getManhuaDataByType = new GetManhuaDataByType(mContext, mHandler);
		getManhuaDataByType.getManhuaBook("ÇàÄêÂþ»­", 1);
		return view;
	}

}
