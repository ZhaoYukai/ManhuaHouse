package com.zykmanhua.app.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zykmanhua.app.R;
import com.zykmanhua.app.adapter.ManhuaCollectAdapter;
import com.zykmanhua.app.bean.GroupCollect;
import com.zykmanhua.app.bean.Manhua;
import com.zykmanhua.app.util.Config;
import com.zykmanhua.app.util.UpdateCollectListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MyRecordCollect extends Fragment implements OnClickListener , UpdateCollectListener {

	private Context mContext = null;
	private ListView mListView = null;
	private GroupCollect mGroupCollect = null;
	private Button mBtn_GoCollect = null;
	private List<Manhua> mManhuaList = null;
	private ManhuaCollectAdapter mManhuaCollectAdapter = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_myrecord_collect , container, false);
		mListView = (ListView) view.findViewById(R.id.id_collect_listView);
		mGroupCollect = new GroupCollect();
		mContext = getActivity().getApplicationContext();
		
		@SuppressWarnings("static-access")
		SharedPreferences sharedPreferences = mContext.getSharedPreferences("ManhuaCollect", mContext.MODE_PRIVATE);
		String response = sharedPreferences.getString("collect", null);
		if(response != null) {
			//从json数据中解析，还原成HashMap
			JSONObject decodeGroupCollect = JSON.parseObject(response);
			JSONObject jsonObj = decodeGroupCollect.getJSONObject("collectMap");
			if(jsonObj.toString().equals("{}")) {
				view = inflater.inflate(R.layout.frag_myrecord_collect_empty , container, false);
				mBtn_GoCollect = (Button) view.findViewById(R.id.id_btn_myrecord_collect_empty);
				mBtn_GoCollect.setOnClickListener(this);
				return view;
			}
			//取出key
			Set<String> keys = jsonObj.keySet();
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
			mManhuaCollectAdapter = new ManhuaCollectAdapter(mContext , mGroupCollect);
			mListView.setAdapter(mManhuaCollectAdapter);
			
			mManhuaList = mapTransitionList(mGroupCollect.getCollectMap());
			
			mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					mGroupCollect.getCollectMap();
					Manhua manhua = mManhuaList.get(position);
					Intent intent = new Intent(mContext , ChapterActivity.class);
					intent.putExtra(Config.KEY_ManhuaName, manhua.getmName());
					intent.putExtra(Config.KEY_ManhuaType, manhua.getmType());
					intent.putExtra(Config.KEY_ManhuaLastUpdate, manhua.getmLastUpdate());
					intent.putExtra(Config.KEY_ManhuaIsFinish, manhua.ismFinish());
					intent.putExtra(Config.KEY_CoverImg , manhua.getmCoverImg());
					startActivity(intent);
				}
			});
		}
		else {
			view = inflater.inflate(R.layout.frag_myrecord_collect_empty , container, false);
			mBtn_GoCollect = (Button) view.findViewById(R.id.id_btn_myrecord_collect_empty);
			mBtn_GoCollect.setOnClickListener(this);
		}
		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_btn_myrecord_collect_empty:
			Intent intent = new Intent(mContext , MainActivity.class);
			startActivity(intent);
			getActivity().finish();
			break;
		}
	}
	
	public List<Manhua> mapTransitionList(Map<String, Manhua> map) {
		List<Manhua> list = new ArrayList<Manhua>();
		Iterator<Entry<String, Manhua>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Manhua> entry = (Entry<String, Manhua>)iterator.next();
			list.add(entry.getValue());
		}
		return list;
	}

	
	public void onUpdateCollect() {
		//执行刷新
		mGroupCollect = new GroupCollect();
		SharedPreferences sharedPreferences = mContext.getSharedPreferences("ManhuaCollect", Context.MODE_PRIVATE);
		String response = sharedPreferences.getString("collect", null);
		if(response != null) {
			//从json数据中解析，还原成HashMap
			JSONObject decodeGroupCollect = JSON.parseObject(response);
			JSONObject jsonObj = decodeGroupCollect.getJSONObject("collectMap");
			//取出key
			Set<String> keys = jsonObj.keySet();
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
			mManhuaCollectAdapter.notifyDataSetChanged();
		}
	}
	
	

}
