package com.pdy.camera;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.pdy.mobile.R;
import com.pdy.common.SignHelper;
import com.pdy.gridviewControl.DragAdapter;
import com.pdy.mobile.BaseActivity;
import com.pdy.mobile.StaticMethod;
import com.pdy.mobile.VideoPlayer;


public class CameraMain2 extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

	private GridLayout videos;
	int imageId = 1000;
	Map<Integer, String> videosPath = new HashMap<Integer, String>();
	TextView nextView;
	TextView cancleView;
	ImageView numView;
	public static CameraMain2 cameraMain2;
	Thread threadLoadThum = new Thread(new Runnable() {

		@Override
		public void run() {
			while (true) {

				// try {
				//
				// Thread.sleep(1);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				img temp = null;
				try {
					temp = (img) que.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final img tempII = temp;
				final Bitmap bitmap = getVideoThumbnail(tempII.filePath, tempII.width, tempII.height);
				if (bitmap == null) {
					return;
				}

				StaticMethod.SaveImg(bitmap, tempII.filePath, CameraMain2.this.getCacheDir() + "/thum");
				handler.post(new Runnable() {

					@Override
					public void run() {
						ImageView iv = (ImageView) CameraMain2.this.findViewById(tempII.ivId);
						iv.setImageBitmap(bitmap);

					}
				});
			}

		}
	});

	String msg = "正在载入视频列表";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_videos);
		cameraMain2 = this;
		videos = (GridLayout) findViewById(R.id.videos);
		nextView = (TextView) findViewById(R.id.video_choose_next);
		cancleView = (TextView) findViewById(R.id.video_choose_cancel);
		nextView.setOnClickListener(this);
		cancleView.setOnClickListener(this);
		numView = (ImageView) findViewById(R.id.video_choose_num);
		// 最好再检查一下系统版本。
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Boolean isHave = checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
			if (isHave == false)
				this.finish();
		}
		width = this.getWindowManager().getDefaultDisplay().getWidth() / 3;
		bitmapDefault = readBitMap(CameraMain2.this, R.drawable.icon);
		bitmapDefault = StaticMethod.getThumImg(bitmapDefault, width);
		showProgress("", msg);
		ImageView imageView = new ImageView(CameraMain2.this);
		imageView.setTag(imageId);
		imageView.setOnClickListener(CameraMain2.this);
		imageView.setLayoutParams(new LayoutParams(width, width));
		imageView.setImageResource(R.drawable.add_image);
		videos.addView(imageView);

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "");
				getVideoFile(file, 0);//
				threadLoadThum.start();
				hideProgress();
			}
		}, 100);

	}

	int ListStart = 0;
	int ListEnd = 1;
	Handler handler = new Handler() {

	};

	private Bitmap bitmapDefault;

	private String text;
	private int width;

	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片

		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	@SuppressLint("NewApi")
	private boolean checkPermission(String permission, int requsetCode) {
		int hasPermission = this.checkSelfPermission(permission);
		if (hasPermission != PackageManager.PERMISSION_GRANTED) {
			this.requestPermissions(new String[] { permission, Manifest.permission.READ_EXTERNAL_STORAGE },
					requsetCode);
			return false;
		} else {
			return true;
		}
	}

	// @Override
	// public void onRequestPermissionsResult(int requestCode, @NonNull String[]
	// permissions,
	// @NonNull int[] grantResults) {
	// if (requestCode == 1) {
	// // 检查grantResults看看是否用户允许该权限。
	// } else {
	// super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	// }
	// }

	private void getVideoFile(final File file, final int maxDeep) {// �����Ƶ�ļ�
		if (file == null) {
			return;
		}

		// TODO Auto-generated method stub
		file.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File file) {
				// sdCard�ҵ���Ƶ����
				String name = file.getName();

				if (file.isFile()) {
					int i = name.indexOf('.');
					if (i == -1) {
						return false;
					}
					name = name.substring(i);
					if (name.equalsIgnoreCase(".mp4")) {
						final String path = file.getAbsolutePath();

						// bitmap = getVideoThumbnail(path, width,
						// width, Thumbnails.MINI_KIND);
						// bitmap =
						// StaticMethod.getVideoThumbnailAtTime(path,
						// 1);

						text = StaticMethod.getVideoTime(path);

						if (!text.equals("") && text != null) {
							text = StaticMethod.secToTime(text) + " ";
							runOnUiThread(new Runnable() {
								public void run() {

									imageId++;
									int ivId = 200000 + imageId;
									int chkId = 300000 + imageId;
									Bitmap bitmap = getVideoThumbnailCache(path, width, width, ivId);
									View view = LayoutInflater.from(CameraMain2.this)
											.inflate(R.layout.choose_video_item, null);
									LayoutParams lp = new RelativeLayout.LayoutParams(width, width);

									view.setLayoutParams(lp);

									ImageView imageV = (ImageView) view.findViewById(R.id.item_video_image);
									imageV.setImageBitmap(bitmap);
									imageV.setId(ivId);
									imageV.setTag(imageId);

									TextView textV = (TextView) view.findViewById(R.id.item_video_text);
									textV.setText(text);
									textV.setTag(imageId);

									CheckBox buttonV = (CheckBox) view.findViewById(R.id.item_video_button);
									buttonV.setId(chkId);
									buttonV.setTag(imageId);

									buttonV.setOnCheckedChangeListener(CameraMain2.this);
									imageV.setOnClickListener(CameraMain2.this);
									textV.setOnClickListener(CameraMain2.this);

									videos.addView(view);

									videosPath.put(imageId, path);
								}
							});
						}
						return true;
					}
				} else if (file.isDirectory() && maxDeep < 2) {
					String videoName = file.getName();
					if (maxDeep == 0 && videoName.matches("(?i).*(Pictures|DCIM|download).*") || maxDeep > 0) {
						mProgressDialog.setMessage(msg);
						getVideoFile(file, maxDeep + 1);
					}

				}

				return false;
			}
		});

	}

	LinkedBlockingQueue que = new LinkedBlockingQueue();

	/**
	 * * ��ȡ��Ƶ������ͼ
	 * ��ͨ��ThumbnailUtils������һ����Ƶ������ͼ��Ȼ��������ThumbnailUtils������ָ����С������ͼ��
	 * *
	 * �����Ҫ������ͼ�Ŀ�͸߶�С��MICRO_KIND��������Ҫʹ��MICRO_KIND��Ϊkind��ֵ���������ʡ�ڴ档
	 * * @param videoPath ��Ƶ��·�� * @param width ָ�������Ƶ����ͼ�Ŀ�� * @param
	 * height ָ�������Ƶ����ͼ�ĸ߶ȶ� * @param kind
	 * ����MediaStore.Images.Thumbnails���еĳ���MINI_KIND��MICRO_KIND�� *
	 * ���У�MINI_KIND: 512 x 384��MICRO_KIND: 96 x 96 * @return ָ����С����Ƶ����ͼ
	 */
	private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
		Bitmap bitmap = null;
		// ��ȡ��Ƶ������ͼ
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		if (bitmap == null) {
			Log.d("dd", "dd:" + videoPath);
			return null;
		}
		System.out.println("w" + bitmap.getWidth());
		System.out.println("h" + bitmap.getHeight());
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	public class img {
		public img(String filePath, int width, int height, int ivId) {
			this.filePath = filePath;
			this.width = width;
			this.height = height;
			this.ivId = ivId;
		}

		public int width;
		public int height;
		public int ivId;
		public String filePath;
	}



	public Bitmap getVideoThumbnailCache(final String filePath, final int width, final int height, final int ivId) {

		String cachePath =StaticMethod.getThumCachePath(filePath,CameraMain2.this.getCacheDir() + "/thum/");
		File file = new File(cachePath);
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(cachePath);
			return bitmap;
		} else {
			que.add(new img(filePath, width, height, ivId));

			return bitmapDefault;
		}
	}

	public Bitmap getVideoThumbnail(String filePath, int width, int height) {

		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			bitmap = retriever.getFrameAtTime();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

		return bitmap;

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.video_choose_cancel:
			finish();
			return;
		case R.id.video_choose_next:
			if (chooseVideo.size() == 0) {
				Toast.makeText(this, "至少选一个", 1).show();
			} else {
				ArrayList<String> videoPaths = new ArrayList<String>();
				for (Map.Entry entry : chooseVideo.entrySet()) {
					videoPaths.add(entry.getValue().toString());
				}
				Intent i = new Intent(this, HoDragVideo.class);
				i.putStringArrayListExtra("videos", videoPaths);
				startActivity(i);
			}
			return;
		default:
			break;
		}
		// TODO Auto-generated method stub
		if (v.getTag() != null && (int) v.getTag() == 1000) {
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

			Uri fileUri = getOutputMediaFileUri();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, 1);
		} else {
			int chkId = 300000 + (int) v.getTag();
			CheckBox chk = (CheckBox) CameraMain2.this.findViewById(chkId);

			chk.setChecked(!chk.isChecked());
			// String videoPath = getPath((int) v.getTag());
			// Intent i = new Intent(CameraMain2.this, VideoPlayer.class);
			// i.putExtra("videoPath", videoPath);
			// i.putExtra("videoButton", (int) v.getTag() + 1000);
			// CameraMain2.this.startActivityForResult(i, 2);
		}
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri() {
		return Uri.fromFile(getOutputMediaFile());
	}

	/** ���·�� */
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
				// ��SD���ϴ����ļ�����ҪȨ�ޣ�
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

	private String getPath(int viewId) {

		for (int i = 1000; i <= imageId; i++) {
			Log.e("js", "viewId:" + viewId + " imageId:" + imageId);
			if (viewId == i) {
				String vpath = videosPath.get(viewId);
				Log.e("js", "vpath:" + vpath);
				return vpath;
			}
		}
		return "";
	}

	Map<Integer, String> chooseVideo = new HashMap<Integer, String>();

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int id = (int) buttonView.getTag();
		Log.e("js", "ischecked:" + isChecked);
		// TODO Auto-generated method stub
		if (isChecked) {
			if (chooseVideo.size() >= StaticMethod.importVideos) {
				Toast.makeText(this, "最多3个视频", 1).show();
				buttonView.setChecked(false);
			} else {
				Log.e("js", "getPath:" + getPath(id));
				chooseVideo.put(id, getPath(id));
			}
		} else {
			if (chooseVideo.containsKey(id)) {
				chooseVideo.remove(id);
			}
		}
		showNum();
	}

	private void showNum() {
		int num = chooseVideo.size();
		if (num <= StaticMethod.importVideos && num > 0) {
			int imageId = getResources().getIdentifier("num" + num, "drawable", getPackageName());
			numView.setImageDrawable(getResources().getDrawable(imageId));
			numView.setVisibility(View.VISIBLE);
		} else {
			numView.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		// requestCode��ʾ����ı�ʾ resultCode��ʾ������
		Log.d("js", requestCode + "//" + resultCode + "**" + data);
		if (data != null) {
			switch (requestCode) {
			case 1:
				// if (requestCode == 1) {
				Uri uri = data.getData(); // �õ�Uri
				String fPath = StaticMethod.getImageAbsolutePath(this, uri); // ת��Ϊ·��
				String text = StaticMethod.getVideoTime(fPath);
				text = StaticMethod.secToTime(text);
				imageId++;
				Bitmap bitmap = getVideoThumbnail(fPath, width, width, Thumbnails.MINI_KIND);
				View view = LayoutInflater.from(CameraMain2.this).inflate(R.layout.choose_video_item, null);
				LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
				view.setLayoutParams(lp);

				ImageView imageV = (ImageView) view.findViewById(R.id.item_video_image);
				imageV.setImageBitmap(bitmap);

				TextView textV = (TextView) view.findViewById(R.id.item_video_text);
				textV.setText(text);
				CheckBox buttonV = (CheckBox) view.findViewById(R.id.item_video_button);

				int chkId = 300000 + imageId;
				buttonV.setId(chkId);
				buttonV.setTag(imageId);

				buttonV.setOnCheckedChangeListener(CameraMain2.this);
				imageV.setOnClickListener(CameraMain2.this);
				textV.setOnClickListener(CameraMain2.this);

				videos.addView(view, 1);
				imageV.setTag(imageId);
				videosPath.put(imageId, fPath);
				buttonV.setChecked(true);
				break;
			case 2:
				int id = data.getIntExtra("videoButton", 0);
				if (id != 0) {
					CheckBox button = (CheckBox) findViewById(id);
					button.setChecked(true);
				}
				break;
			default:
				break;
			}

		}

	}

}
