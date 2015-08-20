package com.zykmanhua.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.pgyersdk.update.PgyUpdateManager;
import com.zykmanhua.app.R;

public class MainActivity extends FragmentActivity implements OnClickListener {
	
	private LinearLayout mTabLibrary = null;
	private LinearLayout mTabMyRecord = null;
	private LinearLayout mTabSetting = null;
	
	private ImageButton mImgLibrary = null;
	private ImageButton mImgMyRecord = null;
	private ImageButton mImgSetting = null;
	
	private Fragment mTab01 = null;
	private Fragment mTab02 = null;
	private Fragment mTab03 = null;
	
	private Button mBtn_Search = null;
	
	private FragmentManager mFragmentManager = null;
	private FragmentTransaction mTransaction = null;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//检查当前是否是最新版本
		PgyUpdateManager.register(MainActivity.this);

		initView();
		initEvents();
		
		setSelect(0);
	}

	
	private void initView() {
		mTabLibrary = (LinearLayout) findViewById(R.id.id_tab_library);
		mTabMyRecord = (LinearLayout) findViewById(R.id.id_tab_myrecord);
		mTabSetting = (LinearLayout) findViewById(R.id.id_tab_settings);
		
		mImgLibrary = (ImageButton) findViewById(R.id.id_tab_library_img);
		mImgMyRecord = (ImageButton) findViewById(R.id.id_tab_myrecord_img);
		mImgSetting = (ImageButton) findViewById(R.id.id_tab_settings_img);
		
		mBtn_Search = (Button) findViewById(R.id.id_btn_top_search);
	}
	
	
	private void initEvents() {
		mTabLibrary.setOnClickListener(this);
		mTabMyRecord.setOnClickListener(this);
		mTabSetting.setOnClickListener(this);
		mBtn_Search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext() , SearchActivity.class);
				startActivity(intent);
			}
		});
	}
	

	@Override
	public void onClick(View v) {
		resetImgs();
		switch (v.getId()) {
		case R.id.id_tab_library:
			setSelect(0);
			break;
		case R.id.id_tab_myrecord:
			setSelect(1);
			break;
		case R.id.id_tab_settings:
			setSelect(2);
			break;
		}
	}


	private void resetImgs() {
		mImgLibrary.setImageResource(R.drawable.tab_library_normal);
		mImgMyRecord.setImageResource(R.drawable.tab_myrecord_normal);
		mImgSetting.setImageResource(R.drawable.tab_settings_normal);
	}
	
	
	private void setSelect(int i) {
		mFragmentManager = getSupportFragmentManager();
		mTransaction = mFragmentManager.beginTransaction();
		//先把所有的Fragment都隐藏掉，然后再显示我们想要的那个
		hideFragment(mTransaction);
		
		//把图片设置为亮的
		//设置内容区域
		switch (i) {
		case 0:
			if(mTab01 == null) {
				mTab01 = new LibraryFragment();
				mTransaction.add(R.id.id_content , mTab01);
			}
			else {
				mTransaction.show(mTab01);
			}
			mImgLibrary.setImageResource(R.drawable.tab_library_pressed);
			break;
		case 1:
			if(mTab02 == null) {
				mTab02 = new MyRecordFragment();
				mTransaction.add(R.id.id_content , mTab02);
			}
			else {
				mTransaction.show(mTab02);
			}
			mImgMyRecord.setImageResource(R.drawable.tab_myrecord_pressed);
			break;
		case 2:
			if(mTab03 == null) {
				mTab03 = new SettingFragment();
				mTransaction.add(R.id.id_content , mTab03);
			}
			else {
				mTransaction.show(mTab03);
			}
			mImgSetting.setImageResource(R.drawable.tab_settings_pressed);
			break;
		}
		
		mTransaction.commit();
	}

	private void hideFragment(FragmentTransaction transaction) {
		if(mTab01 != null) {
			transaction.hide(mTab01);
		}
		if(mTab02 != null) {
			transaction.hide(mTab02);
		}
		if(mTab03 != null) {
			transaction.hide(mTab03);
		}
	}
	
}
