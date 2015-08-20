package com.zykmanhua.app.activity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.zykmanhua.app.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SettingFragment extends Fragment implements OnClickListener {
	
	private Button mBtn_ClearMemory = null;
	private Button mBtn_CheckUpdate = null;
	private Button mBtn_FeedBack = null;
	private Button mBtn_About = null;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.tab03	, container , false);
		mBtn_ClearMemory = (Button) view.findViewById(R.id.id_general_setting_clearMemory);
		mBtn_CheckUpdate = (Button) view.findViewById(R.id.id_general_setting_checkUpdate);
		mBtn_FeedBack = (Button) view.findViewById(R.id.id_about_feedback);
		mBtn_About = (Button) view.findViewById(R.id.id_about_manhuahouse);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mBtn_ClearMemory.setOnClickListener(this);
		mBtn_CheckUpdate.setOnClickListener(this);
		mBtn_FeedBack.setOnClickListener(this);
		mBtn_About.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_general_setting_clearMemory:
			new AlertDialog.Builder(getActivity())
			.setTitle("清理图片缓存")
			.setMessage("确认清理所有图片缓存吗?")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ImageLoader.getInstance().clearDiskCache();
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.show();
			break;
		case R.id.id_general_setting_checkUpdate:
			PgyUpdateManager.register(getActivity() , new UpdateManagerListener() {
				@Override
				public void onUpdateAvailable(final String result) {
					final AppBean appBean = getAppBeanFromString(result);
					new AlertDialog.Builder(getActivity())
					.setTitle("更新")
					.setMessage(appBean.getReleaseNote())
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startDownloadTask(getActivity(), appBean.getDownloadURL());
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.show();
				}
				
				@Override
				public void onNoUpdateAvailable() {
					Toast.makeText(getActivity(), "已经是最新版本", Toast.LENGTH_SHORT).show();
				}
			});
			break;
		case R.id.id_about_feedback:
			PgyFeedback.getInstance().showDialog(getActivity());
			break;
		case R.id.id_about_manhuahouse:
			Intent intent = new Intent(getActivity() , AboutActivity.class);
			startActivity(intent);
			break;
		}
	}

}
