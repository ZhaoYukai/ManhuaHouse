package com.zykmanhua.app.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zykmanhua.app.R;
import com.zykmanhua.app.bean.GroupCollect;
import com.zykmanhua.app.bean.Manhua;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ManhuaCollectAdapter extends BaseAdapter {
	
	private Context mContext = null;
	private LayoutInflater mLayoutInflater = null;
	private ImageLoader mImageLoader = null;
	private DisplayImageOptions mOptions = null;
	private GroupCollect mGroupCollect = null;
	private Map<String, Manhua> mManhuaMap = null;
	private List<Manhua> mManhuaList = null;

	public ManhuaCollectAdapter(Context context, GroupCollect groupCollect) {
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mImageLoader = ImageLoader.getInstance();
		mGroupCollect = groupCollect;
		mManhuaMap = mGroupCollect.getCollectMap();
		mManhuaList = mapTransitionList(mManhuaMap);
		mOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.empty_photo)
		.showImageOnFail(R.drawable.empty_photo)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Config.RGB_565)
		.build();
	}

	@Override
	public int getCount() {
		return mManhuaList.size();
	}

	@Override
	public Manhua getItem(int position) {
		return mManhuaList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.frag_item_listview , null);
			viewHolder.tv_Manhua_Name = (TextView) convertView.findViewById(R.id.id_manhua_name);
			viewHolder.tv_Manhua_lastUpdate = (TextView) convertView.findViewById(R.id.id_manhua_lastUpdate);
			viewHolder.iv_Manhua_Cover = (ImageView) convertView.findViewById(R.id.id_manhua_Cover);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.iv_Manhua_Cover.setImageResource(R.drawable.empty_photo);
		}
		Manhua manhua = getItem(position);
		viewHolder.tv_Manhua_Name.setText(manhua.getmName());
		int manhuaLastUpdate = manhua.getmLastUpdate();
		int year = manhuaLastUpdate / 10000;
		int month = manhuaLastUpdate / 100 % 100;
		int day = manhuaLastUpdate % 100;
		viewHolder.tv_Manhua_lastUpdate.setText("最近更新 : " + year + "年" + month + "月" + day + "日");
		String imageURL = manhua.getmCoverImg();
		mImageLoader.displayImage(imageURL , viewHolder.iv_Manhua_Cover , mOptions , new SimpleImageLoadingListener() , new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				
			}
		});
		return convertView;
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
	
	
	
	class ViewHolder {
        public TextView tv_Manhua_Name = null;
        public TextView tv_Manhua_lastUpdate = null;
        public ImageView iv_Manhua_Cover = null;
    }

}
