package com.zykmanhua.app.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.zykmanhua.app.bean.ManhuaPicture;

import android.content.Context;
import android.util.Log;

public class GetChapterContent {
	
	private Context mContext = null;
	
	public GetChapterContent(Context context) {
		mContext = context;
	}
	
	
	public void getChapterContent(final String name , final int id) {
		Parameters parameters = new Parameters();
		parameters.add(Config.KEY, Config.APP_KEY);
		parameters.add(Config.KEY_COMICNAME, name);
		parameters.add(Config.KEY_ID, id);
		
		JuheData.executeWithAPI(
				mContext, 
				Config.APP_ID, 
				Config.URL_MANHUA_CONTENT , 
				JuheData.GET, 
				parameters, 
				new DataCallBack() {
					@Override
					public void onSuccess(int statusCode, String responseString) {
						parseJSON(responseString);
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
	private void parseJSON(String responseString) {
		
		ArrayList<ManhuaPicture> manhuaPictureList = null;
		
		try {
			JSONObject jsonObject = new JSONObject(responseString);
			int code = jsonObject.getInt(Config.JSON_error_code);
			
			if(code == 200) {
				//如果代码是200，表示服务器返回数据是成功的
				manhuaPictureList = new ArrayList<ManhuaPicture>();
				JSONArray jsonArray = jsonObject.getJSONObject(Config.JSON_result).getJSONArray(Config.JSON_imageList);
				int arrLength = jsonArray.length();
				
				for(int i = 0 ; i < arrLength ; i++) {
					JSONObject dataElement = jsonArray.getJSONObject(i);
					ManhuaPicture manhuaPicture = new ManhuaPicture();
					manhuaPicture.setmName(jsonObject.getJSONObject(Config.JSON_result).getString(Config.JSON_comicName));
					manhuaPicture.setmChapterId(jsonObject.getJSONObject(Config.JSON_result).getInt(Config.JSON_chapterId));
					manhuaPicture.setmImageUrl(dataElement.getString(Config.JSON_imageUrl));
					manhuaPicture.setmId(dataElement.getInt(Config.JSON_id));
					manhuaPictureList.add(manhuaPicture);
				}
			}
			
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
	}
	

}
