package com.zykmanhua.app.bean;

public class Manhua {
	
	private String mType = null;
	private String mName = null;
	private String mDes = null;
	private boolean mFinish = false;
	private int mLastUpdate;
	private String mCoverImg = null;
	
	
	public Manhua() {
		
	}
	
	public Manhua(String mType, String mName, String mDes, boolean mFinish, int mLastUpdate, String mCoverImg) {
		super();
		this.mType = mType;
		this.mName = mName;
		this.mDes = mDes;
		this.mFinish = mFinish;
		this.mLastUpdate = mLastUpdate;
		this.mCoverImg = mCoverImg;
	}
	
	
	

	public String getmType() {
		return mType;
	}

	public void setmType(String mType) {
		this.mType = mType;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmDes() {
		return mDes;
	}

	public void setmDes(String mDes) {
		this.mDes = mDes;
	}

	public boolean ismFinish() {
		return mFinish;
	}

	public void setmFinish(boolean mFinish) {
		this.mFinish = mFinish;
	}

	public int getmLastUpdate() {
		return mLastUpdate;
	}

	public void setmLastUpdate(int mLastUpdate) {
		this.mLastUpdate = mLastUpdate;
	}

	public String getmCoverImg() {
		return mCoverImg;
	}

	public void setmCoverImg(String mCoverImg) {
		this.mCoverImg = mCoverImg;
	}
	
	
	
	

}
