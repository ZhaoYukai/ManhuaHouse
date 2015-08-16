package com.zykmanhua.app.activity;

import java.util.ArrayList;

import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.ManhuaTypeAdapter;
import com.zykmanhua.app.bean.Manhua;
import com.zykmanhua.app.util.Config;
import com.zykmanhua.app.util.GetManhuaDataByName;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {
	
	private Button mBtn_Cancel = null;
	private EditText mEt_Input = null;
	private ImageView mIv_DeleteText = null;
	
	private Context mContext = null;
	private GetManhuaDataByName getManhuaDataByName = null;
	private ArrayList<Manhua> mManhuas = null;
	private ManhuaTypeAdapter mManhuaTypeAdapter = null;
	private ListView mListView = null;
	
	
	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.RESULT_SUCCESS_CODE:
				break;
			case Config.RESULT_FAIL_CODE:
				int errorCode = (Integer) msg.obj;
				showToast(errorCode);
				return ;
			case Config.RESULT_OFFLINE_CODE:
				int offlineCode = Config.STATUS_CODE_NO_NETWORK;
				showToast(offlineCode);
				break;
			}
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
		setContentView(R.layout.activity_search);
		
		initViews();
		
		
		
		
		mBtn_Cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		
		
		//当单击叉号的时候，清空文字
		mIv_DeleteText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mEt_Input.setText("");
			}
		});
		
		
		
		
		//当文字发生改变的时候，执行的方法
		mEt_Input.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() == 0) {
					mIv_DeleteText.setVisibility(View.GONE);
				}
				else {
					mIv_DeleteText.setVisibility(View.VISIBLE);
				}
			}
		});
		
		
		mEt_Input.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER) {
					String input_words = mEt_Input.getText().toString();
					getManhuaDataByName.getManhuaBook(input_words);
				}
				return false;
			}
		});
		
		
		
	}

	private void initViews() {
		mBtn_Cancel = (Button) findViewById(R.id.id_btn_cancel);
		mEt_Input = (EditText) findViewById(R.id.id_et_search);
		mIv_DeleteText = (ImageView) findViewById(R.id.id_iv_deleteText);
		
		mContext = SearchActivity.this;
		mManhuas = new ArrayList<Manhua>();
		mListView = (ListView) findViewById(R.id.id_listView_search);
		getManhuaDataByName = new GetManhuaDataByName(mContext, mHandler);
	}

}
