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
	private boolean mFirstOffline = true;
	
	
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
				break;
			case Config.RESULT_FAIL_CODE:
				int errorCode = (Integer) msg.obj;
				showToast(errorCode);
				return ;
			case Config.RESULT_OFFLINE_CODE:
				int offlineCode = Config.STATUS_CODE_NO_NETWORK;
				showToast(offlineCode);
				if(mFirstOffline == true) {
					// 得连接网络时，才能拿到图片最新的URL数据
					mManhuas = (ArrayList<Manhua>) msg.obj;
					mFirstOffline = false;
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
								mPhotoWallAdapter.setItemHeight( (int) (columnWidth * 1.4) );
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
		};
		
		
	};
	
	private void showToast(int errorCode) {
		switch (errorCode) {
		case Config.STATUS_CODE_NO_NETWORK:
			Toast.makeText(mContext, "网络不给力,您当前处于离线状态", Toast.LENGTH_SHORT).show();
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
		
		mFirst = true;
		mFirstOffline = true;

		View view = inflater.inflate(R.layout.frag_first, container, false);
		mContext = getActivity().getApplicationContext();
		mPhotoWall = (GridView) view.findViewById(R.id.id_photo_wall);
		mManhuas = new ArrayList<Manhua>();
		//图片的显示大小，值越小，每一行显示的图片越多，但是越小
		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
		//图片之间的空白距离，值越大，空白的边距越大
		mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

		mGetManhuaBookData = new GetManhuaBookData(mContext, mHandler);
		//前面部分漫画封面质量太差，所以就跳过21个漫画之后开始
		mGetManhuaBookData.getManhuaBook(21);

		return view;
	}

}
