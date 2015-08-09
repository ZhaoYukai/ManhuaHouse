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
	// ��¼�����������ػ�ȴ����ص�����
	private Set<BitmapWorkerTask> taskCollection = null;

	// ͼƬ���漼���ĺ����࣬���ڻ����������غõ�ͼƬ���ڳ����ڴ�ﵽ�趨ֵʱ�Ὣ�������ʹ�õ�ͼƬ�Ƴ�����
	private LruCache<String, Bitmap> mMemoryCache = null;

	// ͼƬӲ�̻�������ࡣ
	private DiskLruCache mDiskLruCache = null;

	public ManhuaTypeAdapter(Context context, List<Manhua> manhuaList , ListView listView) {
		mManhuaList = manhuaList;
		mLayoutInflater = LayoutInflater.from(context);
		mListView = listView;
		taskCollection = new HashSet<BitmapWorkerTask>();
		// ��ȡӦ�ó����������ڴ�
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// ����ͼƬ�����СΪ�����������ڴ��1/8
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};
		try {
			// ��ȡͼƬ����·��
			File cacheDir = getDiskCacheDir(context, "bitmap");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			// ����DiskLruCacheʵ������ʼ����������
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
		viewHolder.tv_Manhua_lastUpdate.setText("������� : " + year + "��" + month + "��" + day + "��");
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
	 * ���ݴ����uniqueName��ȡӲ�̻����·����ַ��
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
	 * ��ȡ��ǰӦ�ó���İ汾�š�
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
	 * ����Bitmap���󡣴˷�������LruCache�м��������Ļ�пɼ���ImageView��Bitmap����
	 * ��������κ�һ��ImageView��Bitmap�����ڻ����У��ͻῪ���첽�߳�ȥ����ͼƬ��
	 */
	public void loadBitmaps(ImageView imageView, String imageUrl) {
		try {
			Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
			if (bitmap == null) { //����ڴ���û������ͼƬ
				BitmapWorkerTask task = new BitmapWorkerTask();
				taskCollection.add(task);
				task.execute(imageUrl);
			} 
			else if (imageView != null && bitmap != null) { //����ڴ���������ͼƬ
				imageView.setImageBitmap(bitmap);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * ��LruCache�л�ȡһ��ͼƬ����������ھͷ���null��
	 * �β� key:LruCache�ļ������ﴫ��ͼƬ��URL��ַ��
	 * ����ֵ:��Ӧ���ؼ���Bitmap���󣬻��߷���null��
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}
	
	
	
	/**
	 * ʹ��MD5�㷨�Դ����key���м��ܲ����ء�
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
	 * ��һ��ͼƬ�洢��LruCache�С�
	 * �β� key:LruCache�ļ������ﴫ��ͼƬ��URL��ַ��
	 * �β� bitmap:LruCache�ļ������ﴫ������������ص�Bitmap����
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}
	
	
	/**
	 * �첽����ͼƬ������
	 */
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

		//ͼƬ��URL��ַ
		private String imageUrl = null;

		/**
		 * �첽������ִ��������������շ��صõ���ͼƬ
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			FileDescriptor fileDescriptor = null;
			FileInputStream fileInputStream = null;
			Snapshot snapShot = null;
			try {
				// ����ͼƬURL��Ӧ��key
				final String key = hashKeyForDisk(imageUrl);
				// ����key��Ӧ�Ļ���
				snapShot = mDiskLruCache.get(key);
				if (snapShot == null) {
					// ���û���ҵ���Ӧ�Ļ��棬��׼�����������������ݣ���д�뻺��
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
					// ���汻д����ٴβ���key��Ӧ�Ļ���
					snapShot = mDiskLruCache.get(key);
				}
				
				if (snapShot != null) {
					fileInputStream = (FileInputStream) snapShot.getInputStream(0);
					fileDescriptor = fileInputStream.getFD();
				}
				// ���������ݽ�����Bitmap����
				Bitmap bitmap = null;
				if (fileDescriptor != null) {
					bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				}
				if (bitmap != null) {
					// ��Bitmap������ӵ��ڴ滺�浱��
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
		 * ��һ������doInBackground()�ķ���ֵ�����Ǹ÷����Ĵ������
		 * ���β�bitmap���ǵõ�������ͼƬ
		 * Ȼ�������������ͼƬ���ø�ImageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			// ����Tag�ҵ���Ӧ��ImageView�ؼ��������غõ�ͼƬ��ʾ������
			ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}
			taskCollection.remove(this);
		}
		

		/**
		 * ����HTTP���󣬲���ȡBitmap����
		 * �β� imageUrl:ͼƬ��URL��ַ
		 * �β� outputStream:������ɵ�Bitmap������OutputStream������
		 *    �����OutputStream��������DiskLruCache��صģ����Ի��Զ�����
		 *    DiskLruCache���д������ձ�������Ӳ���У���Ҫ��get(key)����ȡ��
		 * ����ֵ:����д��Stream�ɹ�����ʧ��
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
