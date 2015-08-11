package com.zykmanhua.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.ContentAdapter;
import com.zykmanhua.app.bean.ManhuaPicture;
import com.zykmanhua.app.util.Config;
import com.zykmanhua.app.util.GetChapterContent;
import com.zykmanhua.app.util.Topbar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class ContentActivity extends Activity {

	private Context mContext = null;
	private ContentAdapter mContentAdapter = null;
	private ViewPager mViewPager = null;
	private GetChapterContent getChapterContent = null;

	private String mManhuaName = null;
	private int mChapterId;
	private String mManhuaChapterName = null;
	private List<ManhuaPicture> mManhuaPictures = null;

	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.RESULT_SUCCESS_CODE:
				mManhuaPictures = (List<ManhuaPicture>) msg.obj;
				mContentAdapter = new ContentAdapter(mContext, mManhuaPictures,mViewPager);
				mViewPager.setAdapter(mContentAdapter);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

		mManhuaName = getIntent().getStringExtra(Config.KEY_ManhuaName);
		mChapterId = getIntent().getIntExtra(Config.KEY_ChapterId, 0);
		mManhuaChapterName = getIntent().getStringExtra(Config.KEY_ManhuaChapterName);

		mContext = ContentActivity.this;
		mViewPager = (ViewPager) findViewById(R.id.id_content_viewpager);
		mManhuaPictures = new ArrayList<ManhuaPicture>();

		getChapterContent = new GetChapterContent(mContext, mHandler);
		getChapterContent.getChapterContent(mManhuaName, mChapterId);

		// ������TopBar��ص�����
		Topbar topbar = (Topbar) findViewById(R.id.id_content_topBar);
		topbar.setTopBarTitle(mManhuaChapterName);
		topbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
			@Override
			public void rightClick() {
				Toast.makeText(mContext, "Right button be clicked.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void leftClick() {
				finish();
			}
		});

	}

}
