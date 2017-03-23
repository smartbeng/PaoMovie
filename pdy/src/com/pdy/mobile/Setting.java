package com.pdy.mobile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import com.pdy.mobile.R;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.WebHistoryItem;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Setting extends BaseActivity {

	@Bind(R.id.auto_play_video_settings_text)
	TextView autoPlayVideoSettingsText;
	@Bind(R.id.clean_cache_text)
	TextView cleanCacheText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		ButterKnife.bind(this);
		try {
			String cache = getTotalCacheSize(this);
			cleanCacheText.setText(cache);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@OnClick(R.id.back)
	void Back() {
		finish();
	}
	
	@OnClick(R.id.about)
	void About()
	{
		webfun.openNewWindow(Constants.urlAbout,false,false,2);
//		Intent i = new Intent(Setting.this, UIWebview.class);
//		i.putExtra("url", Constants.urlAbout);
//		i.putExtra("pull", false);
//		startActivityForResult(i, 1);
	}

	@OnClick(R.id.auto_play_video_settings)
	void AutoPlayVideoSettings() {
		Intent i = new Intent(Setting.this, AutoPlayVideoSettings.class);
		startActivityForResult(i, 1);
		overridePendingTransition(R.anim.push_bottom_in, R.anim.none);
	}

	@OnClick(R.id.clean_cache)
	void CleanCache() {
		clearAllCache(this);
		cleanCacheText.setText("0M");
	}
	
	@OnClick(R.id.modify_password)
	void ModifyPassword(){
		webfun.openNewWindow(Constants.urlModifyPassword, false, false, 2);
	}
	
	@OnClick(R.id.feedback)
	void FeedBack(){
		startActivity(new Intent(this,FeedBack.class));
		overridePendingTransition(R.anim.slide_right_in, R.anim.none);
	}
	
	@OnClick(R.id.application_score)
	void ApplicationScore(){
		Uri uri = Uri.parse("market://details?id="+getPackageName());  
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		startActivity(intent);  
	}
	
	@OnClick(R.id.exit)
	void exit() {
		new AlertDialog.Builder(Setting.this).setTitle("确认要退出吗？").setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// 点击“确认”后的操作
						CookieManager cookieManager = CookieManager.getInstance();
						String CookieStr = cookieManager.getCookie(Constants.urlHost);
						if (CookieStr != null && !CookieStr.equals("")) {
							final String UserId = GetCookieParamInt(CookieStr, "userId");
							// 移除Alias
							try {
								mPushAgent.addExclusiveAlias(UserId, "LXMessageAliasTypePraise", null);
								mPushAgent.addExclusiveAlias(UserId, "LXMessageAliasTypeComment", null);
								mPushAgent.addExclusiveAlias(UserId, "LXMessageAliasTypeSystem", null);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						cookieManager.removeAllCookie();
						HelperSP.deleteToSP(Setting.this, "UserId");
						Back();
						RefreshWebview("LoadUserInfo", "LoadUserInfo()");
					}
				}).setNegativeButton("返回", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“返回”后的操作,这里不设置没有任何操作
					}
				}).show();
	}

	public String GetCookieParamInt(String CookieStr, String key) {
		String urlDeCode = null;
		try {
			urlDeCode = URLDecoder.decode(CookieStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return "";
		}

		Pattern p = Pattern.compile(key + "\"\\:(\\d+)", Pattern.MULTILINE);
		Matcher m = p.matcher(urlDeCode);
		if (m.find()) {

			String UserName = m.group(1);
			UserName = UserName.replaceAll("%40", "@");
			return UserName;
		} else {

			return "";
		}

	}

	/**
	 * 获取缓存大小
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getTotalCacheSize(Context context) throws Exception {
		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cacheSize += getFolderSize(context.getExternalCacheDir());
		}
		File file = new File(StaticMethod.GetFilePath());
		if (file.exists()) {
			cacheSize += getFolderSize(file);
		}
		return getFormatSize(cacheSize);
	}

	// 获取文件大小
	// Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
	// 目录，一般放一些长时间保存的数据
	// Context.getExternalCacheDir() -->
	// SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			// return size + "Byte";
			return "0K";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "K";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
	}

	/**
	 * 清除缓存
	 * 
	 * @param context
	 */
	public static void clearAllCache(Context context) {
		deleteDir(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			deleteDir(context.getExternalCacheDir());
		}
		File file = new File(StaticMethod.GetFilePath());
		if (file.exists()) {
			deleteDir(file);
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			int netState = data.getIntExtra("netState", 0);
			switch (netState) {
			case 0:
				autoPlayVideoSettingsText.setText("仅wifi");
				break;
			case 1:
				autoPlayVideoSettingsText.setText("移动蜂窝和wifi");
				break;
			case 2:
				autoPlayVideoSettingsText.setText("从不");
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}

}
