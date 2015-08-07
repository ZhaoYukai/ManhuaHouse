package com.zykmanhua.app.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.zykmanhua.app.bean.ManhuaContent;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class GetManhuaChapterByName {
	
	private Context mContext = null;
	private Handler mHandler = null;
	
	public GetManhuaChapterByName(Context context , Handler handler) {
		mContext = context;
		mHandler = handler;
	}
	
	public void getChapterByName(final String name , final int skipNumber) {
		Parameters parameters = new Parameters();
		parameters.add(Config.KEY, Config.APP_KEY);
		parameters.add(Config.KEY_COMICNAME, name);
		parameters.add(Config.KEY_SKIP, skipNumber);
		
		JuheData.executeWithAPI(
				mContext, 
				Config.APP_ID, 
				Config.URL_MANHUA_CHAPTER , 
				JuheData.GET, 
				parameters, 
				new DataCallBack() {
					@Override
					public void onSuccess(int statusCode, String responseString) {
						if(statusCode == Config.STATUS_CODE_SUCCESS) {
							ArrayList<ManhuaContent> manhuaContentList = parseJSON(responseString);
							if(manhuaContentList != null && mHandler != null) {
								Message msg = Message.obtain(mHandler , Config.RESULT_SUCCESS_CODE , manhuaContentList);
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
	private ArrayList<ManhuaContent> parseJSON(String responseString) {
		
		ArrayList<ManhuaContent> manhuaContentList = null;
		
		try {
			JSONObject jsonObject = new JSONObject(responseString);
			int code = jsonObject.getInt(Config.JSON_error_code);
			
			if(code == 200) {
				manhuaContentList = new ArrayList<ManhuaContent>();
				JSONArray jsonArray = jsonObject.getJSONObject(Config.JSON_result).getJSONArray(Config.JSON_chapterList);
				int arrLength = jsonArray.length();
				
				for(int i = 0 ; i < arrLength ; i++) {
					JSONObject dataElement = jsonArray.getJSONObject(i);
					ManhuaContent manhuaContent = new ManhuaContent();
					manhuaContent.setmName(jsonObject.getJSONObject(Config.JSON_result).getString(Config.JSON_comicName));
					manhuaContent.setmChapterName(dataElement.getString(Config.JSON_name));
					manhuaContent.setmChapterId(dataElement.getInt(Config.JSON_id));
					manhuaContentList.add(manhuaContent);
				}	
			}
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return manhuaContentList;
	}
	
	
	
	

}
