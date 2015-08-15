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
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ForthTab extends Fragment {
	
	private Context mContext = null;
	private GetManhuaDataByType getManhuaDataByType = null;
	private ArrayList<Manhua> mManhuas = null;
	private ManhuaTypeAdapter mManhuaTypeAdapter = null;
	private ListView mListView = null;
	
	private int mlastVisibleItem;
	private int mtotalItemCount;
	private boolean mFirst = true;
	
	
	public ForthTab() {
		// TODO Auto-generated constructor stub
	}
	
	
	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.RESULT_SUCCESS_CODE:
				if(mFirst == true) {
					mManhuas = (ArrayList<Manhua>) msg.obj;
					mFirst = false;
				}
				else {
					ArrayList<Manhua> tmp = new ArrayList<Manhua>();
					tmp = (ArrayList<Manhua>) msg.obj;
					for(Manhua m : tmp) {
						mManhuas.add(m);
					}
					mManhuaTypeAdapter.notifyDataSetChanged();
					return;
				}
				mManhuaTypeAdapter = new ManhuaTypeAdapter(mContext, mManhuas, mListView);
				mListView.setAdapter(mManhuaTypeAdapter);
				
				mListView.setOnScrollListener(new OnScrollListener() {
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						if (mtotalItemCount == mlastVisibleItem && scrollState == SCROLL_STATE_IDLE) {
							loadData();
						}
					}
					
					private void loadData() {
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								int count = mManhuas.size() + 1;
								getManhuaDataByType.getManhuaBook("少女漫画", count);
							}
						}, 500);
					}

					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
						mlastVisibleItem = firstVisibleItem + visibleItemCount;
						mtotalItemCount = totalItemCount;
					}
				});
				
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
			case Config.RESULT_FAIL_CODE:
				int errorCode = (Integer) msg.obj;
				showToast(errorCode);
				break;
			}
		};
		
	};
	
	
	private void showToast(int errorCode) {
		switch (errorCode) {
		case Config.STATUS_CODE_NO_NETWORK:
			Toast.makeText(mContext, errorCode + " : 网络不给力", Toast.LENGTH_SHORT).show();
			break;
		case Config.STATUS_CODE_NO_INIT:
			Toast.makeText(mContext, errorCode + " : 系统错误，没有进行初始化", Toast.LENGTH_SHORT).show();
			break;
		case Config.STATUS_CODE_NO_FIND_INFORMATION:
			Toast.makeText(mContext, errorCode + " : 没有找到对应信息", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.frag_second, container, false);
		mContext = getActivity().getApplicationContext();
		mManhuas = new ArrayList<Manhua>();
		mListView = (ListView) view.findViewById(R.id.id_listView);
		getManhuaDataByType = new GetManhuaDataByType(mContext, mHandler);
		getManhuaDataByType.getManhuaBook("少女漫画", 1);
		
		return view;
	}


}
