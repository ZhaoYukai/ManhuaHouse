package com.zykmanhua.app.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zykmanhua.app.R;
import com.zykmanhua.app.bean.Manhua;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * GridView���������������첽������������ͼƬչʾ����Ƭǽ�ϡ�
 */
public class PhotoWallAdapter extends ArrayAdapter<Manhua> {

	//GridView��ʵ��
	private GridView mPhotoWall = null;
	//��¼ÿ������ĸ߶ȡ�
	private int mItemHeight = 0;
	private LayoutInflater mLayoutInflater = null;
	private ImageLoader mImageLoader = null;
	private DisplayImageOptions mOptions = null;

	
	/**
	 * �ܶ��Ա�����ĳ�ʼ������
	 */
	public PhotoWallAdapter(Context context, int textViewResourceId, List<Manhua> manhuas, GridView photoWall) {
		super(context, textViewResourceId, manhuas);
		
		mLayoutInflater = LayoutInflater.from(context);
		mPhotoWall = photoWall;
		
		mImageLoader = ImageLoader.getInstance();
		mOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.empty_photo)
		.showImageOnFail(R.drawable.empty_photo)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
		.build();
		
		
		mPhotoWall.setOnScrollListener(new PauseOnScrollListener(mImageLoader, true, true));
	}
	
	

	/**
	 * ÿ������Ҫ��ʾ����������������getView()����
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Manhua manhua = getItem(position);
		String imageUrl = manhua.getmCoverImg();
		String manhuaName = manhua.getmName();
		ViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.photo_layout, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.id_photo);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.id_firsttab_manhua_name);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.imageView.setImageResource(R.drawable.empty_photo);
		}
		if(viewHolder.imageView.getLayoutParams().height != mItemHeight) {
			viewHolder.imageView.getLayoutParams().height = mItemHeight;
		}
		viewHolder.textView.setText(manhuaName);
		mImageLoader.displayImage(imageUrl , viewHolder.imageView , mOptions , new SimpleImageLoadingListener() , new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				
			}
		});
		return convertView;
		
	}
	
	class ViewHolder {
		ImageView imageView = null;
		TextView textView = null;
	}
	
	
	/**
	 * ����item����ĸ߶ȡ�
	 */
	public void setItemHeight(int height) {
		if (height == mItemHeight) {
			return;
		}
		mItemHeight = height;
		notifyDataSetChanged();
	}

	

}