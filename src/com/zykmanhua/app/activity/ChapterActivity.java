package com.zykmanhua.app.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.ChapterAdapter;
import com.zykmanhua.app.bean.ManhuaContent;
import com.zykmanhua.app.util.Config;
import com.zykmanhua.app.util.DiskLruCache;
import com.zykmanhua.app.util.GetManhuaChapterByName;
import com.zykmanhua.app.util.Topbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChapterActivity extends Activity {
	
	private TextView mTV_name = null;
	private TextView mTV_type = null;
	private TextView mTV_lastUpdate = null;
	private TextView mTV_isFinish = null;
	private ListView mListView = null;
	private ImageView mIV_cover = null;
	
	private String mManhuaName = null;
	private String mManhuaType = null;
	private String mManhuaCover = null;
	private int mManhuaLastUpdate;
	private boolean mManhuaIsFinish;
	
	private List<ManhuaContent> mManhuaContents = null;
	private GetManhuaChapterByName getManhuaChapterByName = null;
	private Context mContext = null;
	private ChapterAdapter mAdapter = null;
	
	// 图片硬盘缓存核心类。
	private DiskLruCache mDiskLruCache = null;
	
	
	
	
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
		
		mManhuaName = getIntent().getStringExtra(Config.KEY_ManhuaName);
		mManhuaType = getIntent().getStringExtra(Config.KEY_ManhuaType);
		mManhuaLastUpdate = getIntent().getIntExtra(Config.KEY_ManhuaLastUpdate, 19910220);
		mManhuaIsFinish = getIntent().getBooleanExtra(Config.KEY_ManhuaIsFinish, true);
		mManhuaCover = getIntent().getStringExtra(Config.KEY_CoverImg);
		
		initViews();
		
		mContext = ChapterActivity.this;
		mManhuaContents = new ArrayList<ManhuaContent>();
		getManhuaChapterByName = new GetManhuaChapterByName(mContext, mHandler);
		getManhuaChapterByName.getChapterByName(mManhuaName, 0);
		
		try {
			// 获取图片缓存路径
			File cacheDir = getDiskCacheDir(mContext, Config.Disk_Route_Chapter);
			if (cacheDir.exists()) {
				// 创建DiskLruCache实例，初始化缓存数据
				mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(mContext), 1, 50 * 1024 * 1024);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		loadBitmaps(mIV_cover, mManhuaCover);
		
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ManhuaContent manhuaContent = mManhuaContents.get(position);
				Intent intent = new Intent(mContext , ContentActivity.class);
				intent.putExtra(Config.KEY_ManhuaName , manhuaContent.getmName());
				intent.putExtra(Config.KEY_ChapterId , manhuaContent.getmChapterId());
				intent.putExtra(Config.KEY_ManhuaChapterName, manhuaContent.getmChapterName());
				startActivity(intent);
			}
		});
		
		//顶部的TopBar相关的设置
		Topbar topbar = (Topbar) findViewById(R.id.id_topBar);
		topbar.setTopBarTitle(mManhuaName);
        topbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
			@Override
			public void rightClick() {
				Toast.makeText(mContext , "Right button be clicked." , Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void leftClick() {
				finish();
			}
		});
	}
	
	
	

	private void initViews() {
		mListView = (ListView) findViewById(R.id.id_listView);
		mTV_name = (TextView) findViewById(R.id.id_chapter_name);
		mTV_name.setText(mManhuaName);
		
		mTV_type = (TextView) findViewById(R.id.id_chapter_type);
		mTV_type.setText(mManhuaType);
		
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
		
		mIV_cover = (ImageView) findViewById(R.id.id_manhua_chapter_Cover);
	}
	
	
	
	public void loadBitmaps(ImageView imageView, String imageUrl) {
		try {
			if(mDiskLruCache != null) {
				//就从硬盘中去加载图片
				String key = hashKeyForDisk(imageUrl);
				DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
				if(snapshot != null) { //如果硬盘中有这个缓存，就直接加载
					InputStream is = snapshot.getInputStream(0);
					Bitmap tmpBitmap = BitmapFactory.decodeStream(is);
					imageView.setImageBitmap(tmpBitmap);
				}
				else {
					return;
				}
			}
			else {
				return ;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 使用MD5算法对传入的key进行加密并返回。
	 */
	public String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}
	
	
	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	
	
	/**
	 * 根据传入的uniqueName获取硬盘缓存的路径地址。
	 */
	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} 
		else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}
	
	
	/**
	 * 获取当前应用程序的版本号。
	 */
	public int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} 
		catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	

}
