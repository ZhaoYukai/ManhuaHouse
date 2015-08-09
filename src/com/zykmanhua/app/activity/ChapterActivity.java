package com.zykmanhua.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.ChapterAdapter;
import com.zykmanhua.app.bean.ManhuaContent;
import com.zykmanhua.app.util.Config;
import com.zykmanhua.app.util.GetManhuaChapterByName;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChapterActivity extends Activity {
	
	private TextView mTV_name = null;
	private TextView mTV_type = null;
	private TextView mTV_lastUpdate = null;
	private TextView mTV_isFinish = null;
	private ListView mListView = null;
	
	private String mManhuaName = null;
	private String mManhuaType = null;
	private int mManhuaLastUpdate;
	private boolean mManhuaIsFinish;
	
	private List<ManhuaContent> mManhuaContents = null;
	private GetManhuaChapterByName getManhuaChapterByName = null;
	private Context mContext = null;
	private ChapterAdapter mAdapter = null;
	
	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.RESULT_SUCCESS_CODE:
				mManhuaContents = (List<ManhuaContent>) msg.obj;
				mAdapter = new ChapterAdapter(mContext, mManhuaContents);
				mListView.setAdapter(mAdapter);
				break;
			case Config.RESULT_FAIL_CODE:
				break;
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chapter);
		
		mManhuaName = getIntent().getStringExtra("ManhuaName");
		mManhuaType = getIntent().getStringExtra("ManhuaType");
		mManhuaLastUpdate = getIntent().getIntExtra("ManhuaLastUpdate", 19910220);
		mManhuaIsFinish = getIntent().getBooleanExtra("ManhuaIsFinish", true);
		
		initViews();
		
		mContext = ChapterActivity.this;
		mManhuaContents = new ArrayList<ManhuaContent>();
		getManhuaChapterByName = new GetManhuaChapterByName(mContext, mHandler);
		getManhuaChapterByName.getChapterByName(mManhuaName, 0);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ManhuaContent manhuaContent = mManhuaContents.get(position);
				Intent intent = new Intent(mContext , ContentActivity.class);
				intent.putExtra(Config.KEY_ManhuaName , manhuaContent.getmName());
				intent.putExtra(Config.KEY_ChapterId , manhuaContent.getmChapterId());
				startActivity(intent);
			}
		});
	}
	
	
	

	private void initViews() {
		mListView = (ListView) findViewById(R.id.id_listView);
		mTV_name = (TextView) findViewById(R.id.id_chapter_name);
		mTV_name.setText("漫画名 : " + mManhuaName);
		
		mTV_type = (TextView) findViewById(R.id.id_chapter_type);
		mTV_type.setText("漫画类型 : " + mManhuaType);
		
		mTV_isFinish = (TextView) findViewById(R.id.id_chapter_isFinish);
		if(mManhuaIsFinish) {
			mTV_isFinish.setText("漫画已完结");
		}
		else {
			mTV_isFinish.setText("漫画还未完结");
		}
		
		int year = mManhuaLastUpdate / 10000;
		int month = mManhuaLastUpdate / 100 % 100;
		int day = mManhuaLastUpdate % 100;
		mTV_lastUpdate = (TextView) findViewById(R.id.id_chapter_lastupdate);
		mTV_lastUpdate.setText("最近更新 : " + year + "年" + month + "月" + day + "日");
	}

}
