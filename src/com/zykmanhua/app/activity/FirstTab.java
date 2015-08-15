package com.zykmanhua.app.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

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
	
	private int mlastVisibleItem;
	private int mtotalItemCount;
	private boolean mFirst = true;
	
	
	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.RESULT_SUCCESS_CODE:
				if(mFirst == true) {
					// 得连接网络时，才能拿到图片最新的URL数据
					mManhuas = (ArrayList<Manhua>) msg.obj;
					mFirst = false;
				}
				else {
					ArrayList<Manhua> tmp = new ArrayList<Manhua>();
					tmp = (ArrayList<Manhua>) msg.obj;
					for(Manhua m : tmp) {
						mManhuas.add(m);
					}
					mPhotoWallAdapter.notifyDataSetChanged();
					return ;
				}
				
				mPhotoWallAdapter = new PhotoWallAdapter(mContext, 0, mManhuas, mPhotoWall);
				mPhotoWall.setAdapter(mPhotoWallAdapter);
				mPhotoWall.setOnScrollListener(new OnScrollListener() {
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
								mGetManhuaBookData.getManhuaBook(count);
							}
						}, 500);
					}
					
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
						mlastVisibleItem = firstVisibleItem + visibleItemCount;
						mtotalItemCount = totalItemCount;
					}
				});

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
						Intent intent = new Intent(mContext, ChapterActivity.class);
						intent.putExtra(Config.KEY_ManhuaName, manhua.getmName());
						intent.putExtra(Config.KEY_ManhuaType, manhua.getmType());
						intent.putExtra(Config.KEY_ManhuaLastUpdate, manhua.getmLastUpdate());
						intent.putExtra(Config.KEY_ManhuaIsFinish, manhua.ismFinish());
						intent.putExtra(Config.KEY_CoverImg, manhua.getmCoverImg());
						startActivity(intent);
					}
				});
				break;
			case Config.RESULT_FAIL_CODE:
				// 没有连接网络时，就什么图片也不显示，此时应该有个文本提示
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
		mContext = getActivity().getApplicationContext();
		mPhotoWall = (GridView) view.findViewById(R.id.id_photo_wall);
		mManhuas = new ArrayList<Manhua>();
		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
		mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

		mGetManhuaBookData = new GetManhuaBookData(mContext, mHandler);
		mGetManhuaBookData.getManhuaBook(1);

		return view;
	}

}
