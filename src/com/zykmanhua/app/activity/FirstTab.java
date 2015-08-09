package com.zykmanhua.app.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.PhotoWallAdapter;
import com.zykmanhua.app.bean.Manhua;
import com.zykmanhua.app.util.Config;
import com.zykmanhua.app.util.GetManhuaBookData;

public class FirstTab extends Fragment {
	
	private Context mContext = null;
	
	private GetManhuaBookData mGetManhuaBookData = null;
	private ArrayList<Manhua> mManhuas = null;

	private GridView mPhotoWall = null;
	private PhotoWallAdapter mPhotoWallAdapter = null;
	private int mImageThumbSize;
	private int mImageThumbSpacing;
	
	private boolean mOnce = true;
	
	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.RESULT_SUCCESS_CODE:
				//得连接网络时，才能拿到图片的URL数据
				mManhuas = (ArrayList<Manhua>) msg.obj;
				mPhotoWallAdapter = new PhotoWallAdapter(mContext, 0, mManhuas , mPhotoWall);
				mPhotoWall.setAdapter(mPhotoWallAdapter);
				
				mPhotoWall.getViewTreeObserver().addOnGlobalLayoutListener(
						new ViewTreeObserver.OnGlobalLayoutListener() {
							@SuppressWarnings("deprecation")
							@Override
							public void onGlobalLayout() {
								final int numColumns = (int) Math.floor(mPhotoWall.getWidth() / (mImageThumbSize + mImageThumbSpacing));
								if (numColumns > 0) {
									int columnWidth = (mPhotoWall.getWidth() / numColumns) - mImageThumbSpacing;
									mPhotoWallAdapter.setItemHeight(columnWidth);
									mPhotoWall.getViewTreeObserver().removeGlobalOnLayoutListener(this);
								}
							}
						});
				
				mPhotoWall.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Manhua manhua = mManhuas.get(position);
						Intent intent = new Intent(mContext , ChapterActivity.class);
						intent.putExtra(Config.KEY_ManhuaName, manhua.getmName());
						intent.putExtra(Config.KEY_ManhuaType, manhua.getmType());
						intent.putExtra(Config.KEY_ManhuaLastUpdate, manhua.getmLastUpdate());
						intent.putExtra(Config.KEY_ManhuaIsFinish, manhua.ismFinish());
						startActivity(intent);
					}
				});
				break;
			case Config.RESULT_FAIL_CODE:
				//没有连接网络时，就什么图片也不显示，此时应该有个文本提示
				
				break;
			}
		};
	};
	
	
	/**
	 * 构造方法只会调用一次
	 */
	public FirstTab() {
		
	}
	
	
	/**
	 * 每当Fragment切换回来的时候，都会再执行这个onCreateView()方法！！
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.frag_first, container, false);
		mPhotoWall = (GridView) view.findViewById(R.id.id_photo_wall);
		
		if(mOnce == true) {
			mContext = getActivity().getApplicationContext();
			mManhuas = new ArrayList<Manhua>();
			
			mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
			mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
			
			mGetManhuaBookData = new GetManhuaBookData(mContext , mHandler);
			mGetManhuaBookData.getManhuaBook(1);
			mOnce = false;
		}
		else {
			mGetManhuaBookData.getManhuaBook(1);
		}
		
		return view;

	}
	
	
	
	
	@Override
	public void onPause() {
		super.onPause();
		mPhotoWallAdapter.fluchCache();
	}
	
	
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mPhotoWallAdapter.cancelAllTasks();
	}
	
	
	
	
	

}
