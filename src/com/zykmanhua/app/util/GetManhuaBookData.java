package com.zykmanhua.app.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.zykmanhua.app.bean.Manhua;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

public class GetManhuaBookData {
	
	private Context mContext = null;
	private Handler mHandler = null;
	
	public GetManhuaBookData(Context context , Handler handler) {
		mContext = context;
		mHandler = handler;
	}
	
	public void getManhuaBook(final int skipNumber) {
		Parameters parameters = new Parameters();
		parameters.add(Config.KEY, Config.APP_KEY);
		parameters.add(Config.KEY_SKIP, skipNumber);
		
		JuheData.executeWithAPI(
				mContext, 
				Config.APP_ID, 
				Config.URL_MANHUA_BOOK , 
				JuheData.GET, 
				parameters, 
				new DataCallBack() {
					@Override
					public void onSuccess(int statusCode, String responseString) {
						if(statusCode == Config.STATUS_CODE_SUCCESS) {
							//把接收到的JSON数据保存到本地，通过skipNumber来作为key区分
							@SuppressWarnings("static-access")
							SharedPreferences.Editor editor = mContext.getSharedPreferences(Config.Path_offline_ManhuaData, mContext.MODE_PRIVATE).edit();
							editor.putString(skipNumber + "", responseString);
							editor.commit();
							
							ArrayList<Manhua> manhuaList = parseJSON(responseString);
							if(manhuaList != null && mHandler != null) {
								Message msg = Message.obtain(mHandler , Config.RESULT_SUCCESS_CODE , manhuaList);
								msg.sendToTarget();
							}
						}
						else {
							Message msg = Message.obtain(mHandler , Config.RESULT_FAIL_CODE , statusCode);
							msg.sendToTarget();
						}
					}
					
					@Override
					public void onFinish() {
						
					}
					
					@Override
					public void onFailure(int statusCode, String responseString, Throwable throwable) {
						//如果没有联网，就会调用这个onFailure()方法
						//从本地文件中取出数据
						@SuppressWarnings("static-access")
						SharedPreferences preferences = mContext.getSharedPreferences(Config.Path_offline_ManhuaData, mContext.MODE_PRIVATE);
						String response = preferences.getString(skipNumber + "", "0");
						ArrayList<Manhua> manhuaList = parseJSON(response);
						if(manhuaList != null && mHandler != null) {
							Message msg = Message.obtain(mHandler , Config.RESULT_OFFLINE_CODE , manhuaList);
							msg.sendToTarget();
						}
					}
				});
		
	}
	
	
	/**
	 * 对收到的json回应字符串进行解析
	 */
	private ArrayList<Manhua> parseJSON(String responseString) {
		
		ArrayList<Manhua> manhuaList = null;
		
		try {
			JSONObject jsonObject = new JSONObject(responseString);
			int code = jsonObject.getInt(Config.JSON_error_code);
			
			if(code == 200) {
				//如果代码是200，表示服务器返回数据是成功的
				manhuaList = new ArrayList<Manhua>();
				JSONArray jsonArray = jsonObject.getJSONObject(Config.JSON_result).getJSONArray(Config.JSON_bookList);
				int arrLength = jsonArray.length();
				for(int i = 0 ; i < arrLength ; i++) {
					JSONObject dataElement = jsonArray.getJSONObject(i);
					
					Manhua manhua = new Manhua();
					manhua.setmName(dataElement.getString(Config.JSON_name));
					manhua.setmType(dataElement.getString(Config.JSON_type));
					manhua.setmDes(dataElement.getString(Config.JSON_des));
					manhua.setmFinish(dataElement.getBoolean(Config.JSON_finish));
					manhua.setmLastUpdate(dataElement.getInt(Config.JSON_lastUpdate));
					manhua.setmCoverImg(dataElement.getString(Config.JSON_coverImg));
					manhuaList.add(manhua);
				}
			}
			else {
				Message msg = Message.obtain(mHandler, Config.RESULT_FAIL_CODE, code);
				msg.sendToTarget();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return manhuaList;
	}
	
		


}
