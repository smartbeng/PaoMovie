package com.pdy.camera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.pdy.mobile.R;
import com.pdy.gridviewControl.DragAdapter;
import com.pdy.gridviewControl.DragGridView;
import com.pdy.information.VideoInfo;
import com.pdy.mobile.BaseActivity;
import com.pdy.mobile.StaticMethod;

public class CameraMain extends BaseActivity implements OnClickListener, OnItemClickListener {

	private DragGridView imageVideoAdd;
	/** GridView的数据 **/
	private DragAdapter adapter;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private Uri fileUri;

	/** re questCode **/
	public static final int REQUSET = 1;

	private List<HashMap<String, Object>> dataSourceList = new ArrayList<HashMap<String, Object>>();
	private TextView importNext;
	
	public static ArrayList<String> videoPaths=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_main);
		/** 关闭按钮事件 **/
		findViewById(R.id.import_movie_close).setOnClickListener(this);
		/** gridview控件 **/
		imageVideoAdd = (DragGridView) findViewById(R.id.import_video_add);

		/** 下一步事件 **/
		importNext = (TextView) findViewById(R.id.import_movie_next);
		importNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CameraMain.this, HoDragVideo.class);
				i.putStringArrayListExtra("videos", videoPaths);
				startActivity(i);
			}
		});

		adapter = new DragAdapter(getApplicationContext(), dataSourceList);
		imageVideoAdd.setAdapter(adapter);
		imageVideoAdd.setOnItemClickListener(this);
		// 设置需要抖动
		imageVideoAdd.setNeedShake(true);
	}

	/** 点击事件处理 **/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.import_movie_close:
			finish();
			break;
		default:
			break;
		}
	}
	
	

	/** gridview点击事件处理 pi和pl数值相同，为点击的第几个子控件0开始 **/
	@Override
	public void onItemClick(AdapterView<?> i, View v, int pi, long pl) {
		AlertDialog.Builder builder = new Builder(CameraMain.this);

		if (pi == dataSourceList.size()) {
			builder.setItems(getResources().getStringArray(R.array.ItemArray), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					/**打开相机录像**/
					if (arg1 == 0) {
						// create new Intent
						Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

						fileUri = getOutputMediaFileUri();
						intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
						intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
						// start the Video Capture Intent
						startActivityForResult(intent, 1);
					}
					/**打开AR相机录像**/
					else if (arg1 == 1) 
					{

					}
					/**打开文件管理器选择视频**/
					else {
						Intent intent = new Intent();
						intent.setType("video/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(intent, 2);
					}
					arg0.dismiss();
				}
			});
			builder.show();
		}
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri() {
		return Uri.fromFile(getOutputMediaFile());
	}

	/** 输出路径*/
	private static File getOutputMediaFile() {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = null;
		try {
			// This location works best if you want the created images to be
			// shared
			// between applications and persist after your app has been
			// uninstalled.
			mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"pdy");

			Log.i("js", "Successfully created mediaStorageDir: " + mediaStorageDir);

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("js", "Error in Creating mediaStorageDir: " + mediaStorageDir);
		}

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				// 在SD卡上创建文件夹需要权限：
				// <uses-permission
				// android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
				Log.d("js", "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// requestCode标示请求的标示 resultCode表示有数据
		Log.d("js", requestCode + "//" + resultCode + "**" + data);
		if (data != null) {
			// if (requestCode == 1) {
			Uri uri = data.getData(); // 得到Uri
			String fPath = StaticMethod.getImageAbsolutePath(this, uri); // 转化为路径
			String text = StaticMethod.getVideoTime(fPath);
			text = StaticMethod.secToTime(text);
			HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
			itemHashMap.put("item_image", StaticMethod.getVideoThumbnail(fPath));
			itemHashMap.put("item_text", text);
			dataSourceList.add(itemHashMap);
			adapter = new DragAdapter(getApplicationContext(), dataSourceList);
			imageVideoAdd.setAdapter(adapter);
			videoPaths.add(fPath);
		}

	}
}
