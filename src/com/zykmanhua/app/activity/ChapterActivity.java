package com.zykmanhua.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.ChapterAdapter;
import com.zykmanhua.app.bean.GroupCollect;
import com.zykmanhua.app.bean.Manhua;
import com.zykmanhua.app.bean.ManhuaContent;
import com.zykmanhua.app.util.Config;
import com.zykmanhua.app.util.GetManhuaChapterByName;
import com.zykmanhua.app.util.MD5Tools;
import com.zykmanhua.app.util.Topbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChapterActivity extends Activity {
	
	private View mFooter = null;
	private TextView mTV_name = null;
	private TextView mTV_type = null;
	private TextView mTV_lastUpdate = null;
	private TextView mTV_isFinish = null;
	private ListView mListView = null;
	private ImageView mIV_cover = null;
	private Topbar mTopbar = null;
	
	private String mManhuaName = null;
	private String mManhuaType = null;
	private String mManhuaCover = null;
	private int mManhuaLastUpdate;
	private boolean mManhuaIsFinish;
	private int mlastVisibleItem;
	private int mtotalItemCount;
	private boolean mFirst = true;
	private boolean mFirstOffline = true;
	private int mtotalCount;
	private boolean hasCollected = false;
	private GroupCollect mGroupCollect = null;
	
	private List<ManhuaContent> mManhuaContents = null;
	private GetManhuaChapterByName getManhuaChapterByName = null;
	private Context mContext = null;
	private ChapterAdapter mAdapter = null;
	
	private ImageLoader mImageLoader = null;
	private DisplayImageOptions mOptions = null;
	
	
	
	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.RESULT_SUCCESS_CODE:
				if(mFirst == true) {
					mManhuaContents = (List<ManhuaContent>) msg.obj;
					mtotalCount = mManhuaContents.get(0).getmTotal();
					mFirst = false;
				}
				else {
					List<ManhuaContent> tmp = new ArrayList<ManhuaContent>();
					tmp = (List<ManhuaContent>) msg.obj;
					for(ManhuaContent m : tmp) {
						mManhuaContents.add(m);
					}
					mAdapter.notifyDataSetChanged();
					return;
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
					mManhuaContents = (List<ManhuaContent>) msg.obj;
					mtotalCount = mManhuaContents.get(0).getmTotal();
					mFirstOffline = false;
				}
				else {
					List<ManhuaContent> tmp = new ArrayList<ManhuaContent>();
					tmp = (List<ManhuaContent>) msg.obj;
					for(ManhuaContent m : tmp) {
						mManhuaContents.add(m);
					}
					mAdapter.notifyDataSetChanged();
					return;
				}
				break;
			}
			mAdapter = new ChapterAdapter(mContext, mManhuaContents);
			mListView.setAdapter(mAdapter);
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chapter);
		
		mFirst = true;
		mFirstOffline = true;
		
		mContext = ChapterActivity.this;
		mManhuaName = getIntent().getStringExtra(Config.KEY_ManhuaName);
		mManhuaType = getIntent().getStringExtra(Config.KEY_ManhuaType);
		mManhuaLastUpdate = getIntent().getIntExtra(Config.KEY_ManhuaLastUpdate, 19910220);
		mManhuaIsFinish = getIntent().getBooleanExtra(Config.KEY_ManhuaIsFinish, true);
		mManhuaCover = getIntent().getStringExtra(Config.KEY_CoverImg);
		
		initViews();
		
		mManhuaContents = new ArrayList<ManhuaContent>();
		getManhuaChapterByName = new GetManhuaChapterByName(mContext, mHandler);
		getManhuaChapterByName.getChapterByName(mManhuaName, 0);
		
		
		mImageLoader.displayImage(mManhuaCover , mIV_cover , mOptions , new SimpleImageLoadingListener() , new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				
			}
		});
		
		
		
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
		
		
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (mtotalItemCount == mlastVisibleItem && scrollState == SCROLL_STATE_IDLE) {
					mFooter.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
					loadData();
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				mlastVisibleItem = firstVisibleItem + visibleItemCount;
				mtotalItemCount = totalItemCount;
			}
		});
		
		//先从本地文件中还原出字符串
		mGroupCollect = new GroupCollect();
		SharedPreferences sharedPreferences = getSharedPreferences("ManhuaCollect", MODE_PRIVATE);
		String response = sharedPreferences.getString("collect", null);
		if(response != null) {
			//从json数据中解析，还原成HashMap
			JSONObject decodeGroupCollect = JSON.parseObject(response);
			JSONObject jsonObj = decodeGroupCollect.getJSONObject("collectMap");
			//取出key
			Set<String> keys = jsonObj.keySet();
			String tmpKey = MD5Tools.hashKeyForDisk(mManhuaCover);
			hasCollected = keys.contains(tmpKey);
			if(hasCollected == true) {
				mTopbar.setTopBarRightTitle("已收藏");
				mTopbar.setTopBarRightColor(Color.YELLOW);
			}
			else {
				mTopbar.setTopBarRightTitle("收藏");
				mTopbar.setTopBarRightColor(Color.WHITE);
			}
			//继续取出JSON中的数据，继续构造HashMap
			String[] st = new String[keys.size()];
			Iterator<String> iterator = keys.iterator();
			int i = 0;
			while(iterator.hasNext()) {
				st[i] = iterator.next();
				i++;
			}
			for(int j = 0 ; j < st.length ; j++) {
				JSONObject obj = jsonObj.getJSONObject(st[j]);
				String name = obj.getString("mName");
				int updateTime = obj.getIntValue("mLastUpdate");
				String url = obj.getString("mCoverImg");
				String type = obj.getString("mType");
				String des = obj.getString("mDes");
				boolean isfinish = obj.getBooleanValue("mFinish");
				Manhua manhuaCollect = new Manhua(type, name, des, isfinish, updateTime, url);
				mGroupCollect.addCollect(manhuaCollect);
			}
			//上面这个for循环结束之后，mGroupCollect里面就存储了当前所有的已收藏的漫画数据
		}
		
		
		//updateCollectListener = ;
		
		//顶部的TopBar相关的设置
		mTopbar.setTopBarTitle(mManhuaName);
        mTopbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
			@Override
			public void rightClick() {
				if(hasCollected == false) { //如果为false，表示还没被收藏过，既然单击了就收藏
					mTopbar.setTopBarRightTitle("已收藏");
					mTopbar.setTopBarRightColor(Color.YELLOW);
					//构造HashMap
					Manhua manhuaCollect = new Manhua(mManhuaType, mManhuaName, "无", mManhuaIsFinish, mManhuaLastUpdate, mManhuaCover);
					String urlKey = MD5Tools.hashKeyForDisk(mManhuaCover);
					HashMap<String , Manhua> hashMap = new HashMap<String, Manhua>();
					hashMap.put(urlKey, manhuaCollect);
					//HashMap转换为JSON数据
					mGroupCollect.addCollect(manhuaCollect);
				}
				else { //如果为true，表示已被收藏过，单击了就取消收藏
					mTopbar.setTopBarRightTitle("收藏");
					mTopbar.setTopBarRightColor(Color.WHITE);
					mGroupCollect.removeCollect(mManhuaCover);
				}
				String jsonString = JSON.toJSONString(mGroupCollect);
				//把json字符串保存进文件中
				SharedPreferences.Editor editor = getSharedPreferences("ManhuaCollect" , MODE_PRIVATE).edit();
				editor.putString("collect", jsonString);
				editor.commit();
			}
			
			@Override
			public void leftClick() {
				finish();
			}
		});
	}
	
	private void loadData() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				int count = mManhuaContents.size();
				if(count != mtotalCount) {
					getManhuaChapterByName.getChapterByName(mManhuaName, count);
				}
				else {
					Toast.makeText(mContext, "没有数据可以供加载了", Toast.LENGTH_SHORT).show();
				}
				mFooter.findViewById(R.id.load_layout).setVisibility(View.GONE);
			}
		}, 1000);
	}

	private void initViews() {
		mFooter = LayoutInflater.from(mContext).inflate(R.layout.footer_layout , null);
		mFooter.findViewById(R.id.load_layout).setVisibility(View.GONE);
		mListView = (ListView) findViewById(R.id.id_listView);
		mListView.addFooterView(mFooter);
		mTV_name = (TextView) findViewById(R.id.id_chapter_name);
		mTV_name.setText(mManhuaName);
		mTopbar = (Topbar) findViewById(R.id.id_topBar);
		
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
		
		mImageLoader = ImageLoader.getInstance();
		mOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.empty_photo)
		.showImageOnFail(R.drawable.empty_photo)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
		.build();
	}

}
