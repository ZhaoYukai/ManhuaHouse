package com.zykmanhua.app.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.zykmanhua.app.bean.ManhuaType;

public class GetManhuaTypeData {
	
	private Context mContext = null;
	private Handler mHandler = null;
	
	public GetManhuaTypeData(Context context , Handler handler) {
		mContext = context;
		mHandler = handler;
	}
	
	public void getManhuaType() {
		
		Parameters parameters = new Parameters();
		parameters.add(Config.KEY, Config.APP_KEY);
		
		JuheData.executeWithAPI(
				mContext, 
				Config.APP_ID, 
				Config.URL_MANHUA_TYPE , 
				JuheData.GET, 
				parameters, 
				new DataCallBack() {
					@Override
					public void onSuccess(int statusCode, String responseString) {
						if(statusCode == Config.STATUS_CODE_SUCCESS) {
							ArrayList<ManhuaType> manhuaTypeList = parseJSON(responseString);
							if(manhuaTypeList != null && mHandler != null) {
								Message msg = Message.obtain(mHandler , Config.RESULT_SUCCESS_CODE , manhuaTypeList);
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
	 */
	private ArrayList<ManhuaType> parseJSON(String responseString) {
		
		ArrayList<ManhuaType> manhuaTypeList = null;
		
		try {
			JSONObject jsonObject = new JSONObject(responseString);
			int code = jsonObject.getInt(Config.JSON_error_code);
			
			if(code == 0) {
				manhuaTypeList = new ArrayList<ManhuaType>();
				JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_result);
				int arrLength = jsonArray.length();
				for(int i = 0 ; i < arrLength ; i++) {
					String dataElement = jsonArray.getString(i);
					ManhuaType manhuaType = new ManhuaType();
					manhuaType.setmType(dataElement);
					manhuaTypeList.add(manhuaType);
				}
				
			}
			
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return manhuaTypeList;
	}
	
	
	
	
	
	
	
	

}
