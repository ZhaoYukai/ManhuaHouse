package com.zykmanhua.app.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.zykmanhua.app.bean.ManhuaType;

public class GetManhuaTypeData {
	
	private Context mContext = null;
	
	public GetManhuaTypeData(Context context) {
		mContext = context;
	}
	
	public void getManhuaType() {
		
		Parameters parameters = new Parameters();
		parameters.add("key", Config.APP_KEY);
		
		JuheData.executeWithAPI(
				mContext, 
				Config.APP_ID, 
				Config.URL_MANHUA_TYPE , 
				JuheData.GET, 
				parameters, 
				new DataCallBack() {
					@Override
					public void onSuccess(int statusCode, String responseString) {
						ArrayList<ManhuaType> manhuaTypeList = parseJSON(responseString);
					}
					
					@Override
					public void onFinish() {
						
					}
					
					@Override
					public void onFailure(int statusCode, String responseString, Throwable throwable) {
						
					}
				});
		
	}
	
	
	
	/**
	 * 对收到的json回应字符串进行解析
	 */
	private ArrayList<ManhuaType> parseJSON(String responseString) {
		
		ArrayList<ManhuaType> manhuaTypeList = null;
		
		try {
			JSONObject jsonObject = new JSONObject(responseString);
			int code = jsonObject.getInt(Config.JSON_error_code);
			
			if(code == 0) {
				//如果代码是0，表示服务器返回数据是成功的
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
