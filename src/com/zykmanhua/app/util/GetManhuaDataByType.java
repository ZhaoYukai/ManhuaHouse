package com.zykmanhua.app.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.zykmanhua.app.bean.Manhua;

public class GetManhuaDataByType {
	
	private Context mContext = null;
	private Handler mHandler = null;
	
	public GetManhuaDataByType(Context context , Handler handler) {
		mContext = context;
		mHandler = handler;
	}
	
	public void getManhuaBook(final String manhuaType , final int skipNumber) {
		Parameters parameters = new Parameters();
		parameters.add(Config.KEY, Config.APP_KEY);
		parameters.add("type", manhuaType);
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
						Message msg = Message.obtain(mHandler, Config.RESULT_FAIL_CODE, statusCode + ":" + throwable.getMessage());
						msg.sendToTarget();
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
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return manhuaList;
	}
	
		


}
