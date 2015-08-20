package com.zykmanhua.app.bean;

public class ManhuaContent {
	
	private int mTotal;
	private String mName = null;
	private String mChapterName = null;
	private int mChapterId;
	
	
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmChapterName() {
		return mChapterName;
	}
	public int getmTotal() {
		return mTotal;
	}
	public void setmTotal(int mTotal) {
		this.mTotal = mTotal;
	}
	public void setmChapterName(String mChapterName) {
		this.mChapterName = mChapterName;
	}
	public int getmChapterId() {
		return mChapterId;
	}
	public void setmChapterId(int mChapterId) {
		this.mChapterId = mChapterId;
	}
	
	

}
