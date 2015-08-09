package com.zykmanhua.app.adapter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zykmanhua.app.R;
import com.zykmanhua.app.bean.Manhua;
import com.zykmanhua.app.util.DiskLruCache;
import com.zykmanhua.app.util.DiskLruCache.Snapshot;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ManhuaTypeAdapter extends BaseAdapter {

	private List<Manhua> mManhuaList = null;
	private LayoutInflater mLayoutInflater = null;
	private ListView mListView = null;
	// 记录所有正在下载或等待下载的任务。
	private Set<BitmapWorkerTask> taskCollection = null;

	// 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	private LruCache<String, Bitmap> mMemoryCache = null;

	// 图片硬盘缓存核心类。
	private DiskLruCache mDiskLruCache = null;

	public ManhuaTypeAdapter(Context context, List<Manhua> manhuaList , ListView listView) {
		mManhuaList = manhuaList;
		mLayoutInflater = LayoutInflater.from(context);
		mListView = listView;
		taskCollection = new HashSet<BitmapWorkerTask>();
		// 获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// 设置图片缓存大小为程序最大可用内存的1/8
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};
		try {
			// 获取图片缓存路径
			File cacheDir = getDiskCacheDir(context, "bitmap");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			// 创建DiskLruCache实例，初始化缓存数据
			mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 50 * 1024 * 1024);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
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
		}
		Manhua manhua = getItem(position);
		viewHolder.tv_Manhua_Name.setText(manhua.getmName());
		int manhuaLastUpdate = manhua.getmLastUpdate();
		int year = manhuaLastUpdate / 10000;
		int month = manhuaLastUpdate / 100 % 100;
		int day = manhuaLastUpdate % 100;
		viewHolder.tv_Manhua_lastUpdate.setText("最近更新 : " + year + "年" + month + "月" + day + "日");
		String imageURL = manhua.getmCoverImg();
		viewHolder.iv_Manhua_Cover.setTag(imageURL);
		loadBitmaps(viewHolder.iv_Manhua_Cover, imageURL);
		return convertView;
	}
	
	class ViewHolder {
        public TextView tv_Manhua_Name = null;
        public TextView tv_Manhua_lastUpdate = null;
        public ImageView iv_Manhua_Cover = null;
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
	
	
	/**
	 * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
	 * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
	 */
	public void loadBitmaps(ImageView imageView, String imageUrl) {
		try {
			Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
			if (bitmap == null) { //如果内存中没有那张图片
				BitmapWorkerTask task = new BitmapWorkerTask();
				taskCollection.add(task);
				task.execute(imageUrl);
			} 
			else if (imageView != null && bitmap != null) { //如果内存中有那张图片
				imageView.setImageBitmap(bitmap);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 形参 key:LruCache的键，这里传入图片的URL地址。
	 * 返回值:对应传回键的Bitmap对象，或者返回null。
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
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
	 * 将一张图片存储到LruCache中。
	 * 形参 key:LruCache的键，这里传入图片的URL地址。
	 * 形参 bitmap:LruCache的键，这里传入从网络上下载的Bitmap对象。
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}
	
	
	/**
	 * 异步下载图片的任务。
	 */
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

		//图片的URL地址
		private String imageUrl = null;

		/**
		 * 异步任务先执行这个方法，最终返回得到的图片
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			FileDescriptor fileDescriptor = null;
			FileInputStream fileInputStream = null;
			Snapshot snapShot = null;
			try {
				// 生成图片URL对应的key
				final String key = hashKeyForDisk(imageUrl);
				// 查找key对应的缓存
				snapShot = mDiskLruCache.get(key);
				if (snapShot == null) {
					// 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
					DiskLruCache.Editor editor = mDiskLruCache.edit(key);
					if (editor != null) {
						OutputStream outputStream = editor.newOutputStream(0);
						if (downloadUrlToStream(imageUrl, outputStream)) {
							editor.commit();
						} 
						else {
							editor.abort();
						}
					}
					// 缓存被写入后，再次查找key对应的缓存
					snapShot = mDiskLruCache.get(key);
				}
				
				if (snapShot != null) {
					fileInputStream = (FileInputStream) snapShot.getInputStream(0);
					fileDescriptor = fileInputStream.getFD();
				}
				// 将缓存数据解析成Bitmap对象
				Bitmap bitmap = null;
				if (fileDescriptor != null) {
					bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				}
				if (bitmap != null) {
					// 将Bitmap对象添加到内存缓存当中
					addBitmapToMemoryCache(imageUrl, bitmap);
				}
				return bitmap;
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			finally {
				if (fileDescriptor == null && fileInputStream != null) {
					try {
						fileInputStream.close();
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		
		/**
		 * 上一个方法doInBackground()的返回值，就是该方法的传入参数
		 * 即形参bitmap就是得到的那张图片
		 * 然后在这里把这张图片设置给ImageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			// 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
			ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}
			taskCollection.remove(this);
		}
		

		/**
		 * 建立HTTP请求，并获取Bitmap对象。
		 * 形参 imageUrl:图片的URL地址
		 * 形参 outputStream:下载完成的Bitmap隐藏在OutputStream对象里
		 *    而这个OutputStream对象是与DiskLruCache相关的，所以会自动交由
		 *    DiskLruCache进行处理，最终保存在了硬盘中，需要用get(key)才能取出
		 * 返回值:下载写入Stream成功还是失败
		 */
		private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
			HttpURLConnection urlConnection = null;
			BufferedOutputStream out = null;
			BufferedInputStream in = null;
			try {
				final URL url = new URL(urlString);
				urlConnection = (HttpURLConnection) url.openConnection();
				in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
				out = new BufferedOutputStream(outputStream, 8 * 1024);
				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				return true;
			} 
			catch (final IOException e) {
				e.printStackTrace();
			} 
			finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			return false;
		}

	}
	

}
