package com.zykmanhua.app.bean;

import java.util.HashMap;
import java.util.Map;

import com.zykmanhua.app.util.MD5Tools;

public class GroupCollect {
	
	private Map<String , Manhua> collectHashMap = null;
	
	public GroupCollect() {
		this.collectHashMap = new HashMap<String, Manhua>();
	}
	
	public Map<String, Manhua> getCollectMap() {
		return this.collectHashMap;
	}
	
	public void setCollectMap(Map<String, Manhua> collectHashMap) {
		this.collectHashMap = collectHashMap;
	}
	
	public void addCollect(Manhua collectHashMap) {
		String url = collectHashMap.getmCoverImg();
		String key = MD5Tools.hashKeyForDisk(url);
		this.collectHashMap.put(key , collectHashMap);
	}
	
	public void removeCollect(String url) {
		String key = MD5Tools.hashKeyForDisk(url);
		this.collectHashMap.remove(key);
	}

}
