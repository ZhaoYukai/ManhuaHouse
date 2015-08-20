package com.zykmanhua.app.adapter;

import java.util.List;

import com.zykmanhua.app.R;
import com.zykmanhua.app.bean.ManhuaContent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChapterAdapter extends BaseAdapter {
	
	private Context mContext = null;
	private List<ManhuaContent> mManhuaContents = null;
	private LayoutInflater mInflater = null;
	
	
	
	public ChapterAdapter(Context context , List<ManhuaContent> manhuaContents) {
		mContext = context;
		mManhuaContents = manhuaContents;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mManhuaContents.size();
	}

	@Override
	public ManhuaContent getItem(int position) {
		return mManhuaContents.get(position);
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
			convertView = mInflater.inflate(R.layout.item_chapter_layout , null);
			viewHolder.tv_chapter = (TextView) convertView.findViewById(R.id.id_chapter_textview);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		String chapterName = getItem(position).getmChapterName();
		viewHolder.tv_chapter.setText(chapterName);
		
		return convertView;
	}
	
	
	class ViewHolder {
		public TextView tv_chapter = null;
	}

}
