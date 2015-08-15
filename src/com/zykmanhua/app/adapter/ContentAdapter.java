package com.zykmanhua.app.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zykmanhua.app.R;
import com.zykmanhua.app.bean.ManhuaPicture;
import com.zykmanhua.app.util.ZoomImageView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ContentAdapter extends PagerAdapter {

	private Context mContext = null;
	private List<ManhuaPicture> mManhuaPictures = null;
	private ImageView[] mImageViews = null;
	private ImageLoader mImageLoader = null;
	private DisplayImageOptions mOptions = null;
	

	

	public ContentAdapter(Context context, List<ManhuaPicture> manhuaPictures , ViewPager viewPager) {
		mContext = context;
		mManhuaPictures = manhuaPictures;
		mImageViews = new ImageView[getCount()];
		mImageLoader = ImageLoader.getInstance();
		mOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.empty_photo)
		.showImageOnFail(R.drawable.empty_photo)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
		.build();
	}
	
	
	
	

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ZoomImageView zoomImageView = new ZoomImageView(mContext);
		zoomImageView.setImageResource(R.drawable.empty_photo);
		container.addView(zoomImageView);
		mImageViews[position] = zoomImageView;
		String imageURL = mManhuaPictures.get(position).getmImageUrl();
		
		mImageLoader.displayImage(imageURL , zoomImageView , mOptions , new SimpleImageLoadingListener() , new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				
			}
		});
		return zoomImageView;
	}
	
	
	
	
	

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mImageViews[position]);
	}
	
	
	
	

	@Override
	public int getCount() {
		return mManhuaPictures.size();
	}
	
	
	
	

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	

}
