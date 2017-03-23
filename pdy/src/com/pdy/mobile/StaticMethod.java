package com.pdy.mobile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.pdy.mobile.R;
import com.pdy.camera.CameraMain2;
import com.pdy.camera.CameraMain2.img;
import com.pdy.common.SignHelper;
import com.pdy.information.VideoInfo;
import com.pdy.textview.utils.ImageUtils;
import com.pdy.textview.view.MyRelativeLayout;
import com.seu.magiccamera.common.utils.Constants;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class StaticMethod {

	public static String filter0 = " -vf colorchannelmixer=.3:.4:.3:0:.3:.4:.3:0:.3:.4:.3 ";
	public static String filter1 = " -vf boxblur=2:1:cr=0:ar=0 ";
	public static Context context;

	/** 最多导入视频长度 **/
	public static int importVideos = 3;
	/** 音频路径 **/
	public static String audioPath = "";
	/** 视频编辑长度限制时间 **/
	public static int maxVideosTime = 15;
	/** 导入视频的最小时间 **/
	public static int minVideoTime = 1;

	/** 通过Intent传递的视频路径 **/
	public static List<String> videoPathsList = new ArrayList<String>();

	public static int[] textColor = { R.color.black, R.color.yellow, R.color.white, R.color.red, R.color.palegreen,
			R.color.thistle };
	public static String[] textType = { "SANS", "MONOSPACE", "SERIF" };
	public static Typeface[] typefaces = { Typeface.SANS_SERIF, Typeface.MONOSPACE, Typeface.SERIF };

	public static Boolean isTouch = false;

	public static List<VideoInfo> videosInfo = new ArrayList();
	public static Handler handler;

	public static BaseActivity lastBaseActivity;

	public static int webViewId = 3000;

	public static class PaoPaoText {
		public String MovieInfoId = "";
		public String UserId = "";
		public String LikeInit = "1";
		public String HateInit = "1";
		public String Title = "";
		public String Content = "";
		public String MovieCircleInfoId = "0";
		public String FoamType = "1";
	}

	public static class PaoPaoAudio {
		public String MovieInfoId = "";
		public String UserId = "";
		public String LikeInit = "1";
		public String HateInit = "1";
		public String Title = "";
		public String Content = "";
		public String MovieCircleInfoId = "0";
		public String FoamType = "3";
		public String VideoTime = "0";
	}

	public static class PaoPaoImage {
		public String MovieInfoId = "";
		public String UserId = "";
		public String LikeInit = "1";
		public String HateInit = "1";
		public String Title = "";
		public String Content = "";
		public String MovieCircleInfoId = "0";
		public String FoamType = "2";
	}

	public static void doGet(final String uriAPI, final String id) {
		handler = new Handler();
		new Thread() {
			public void run() {
				// 锟斤拷锟斤拷HttpGet锟斤拷HttpPost锟斤拷锟襟，斤拷要锟斤拷锟斤拷锟経RL通锟斤拷锟斤拷锟届方锟斤拷锟斤拷锟斤拷HttpGet锟斤拷HttpPost锟斤拷锟斤拷
				HttpGet httpRequst = new HttpGet(uriAPI);
				httpRequst.addHeader("cookie", "UserInfo={\"userId\":" + id + "}");
				String result = "";
				// new DefaultHttpClient().execute(HttpUriRequst requst);
				try {
					// 使锟斤拷DefaultHttpClient锟斤拷锟絜xecute锟斤拷锟斤拷锟斤拷锟斤拷HTTP
					// GET锟斤拷锟襟，诧拷锟斤拷锟斤拷HttpResponse锟斤拷锟斤拷
					HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);// 锟斤拷锟斤拷HttpGet锟斤拷HttpUriRequst锟斤拷锟斤拷锟斤拷
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity httpEntity = httpResponse.getEntity();
						result = EntityUtils.toString(httpEntity);// 取锟斤拷应锟斤拷锟街凤拷锟斤拷
						// 一锟斤拷锟斤拷说锟斤拷要删锟斤拷锟斤拷锟斤拷锟斤拷址锟�
						result.replaceAll("\r", "");// 去锟斤拷锟斤拷锟截斤拷锟斤拷械锟�"\r"锟街凤拷锟斤拷锟斤拷锟斤拷锟斤拷诮锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷锟斤拷示一锟斤拷小锟斤拷锟斤拷
					} else {
						httpRequst.abort();
						Log.e("get error:", "状态码:" + httpResponse.getStatusLine().getStatusCode() + "信息："
								+ httpResponse.getStatusLine());
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = e.getMessage().toString();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = e.getMessage().toString();
				}
				Log.e("js", "get result" + result);
				handler.obtainMessage(0, result).sendToTarget();
			}
		}.start();

	}

	public static void doPost(final String uriAPI, final List<NameValuePair> params, final String id) {

		handler = new Handler();
		new Thread() {
			public void run() {

				String result = "";
				HttpPost httpRequst = new HttpPost(uriAPI);// 锟斤拷锟斤拷HttpPost锟斤拷锟斤拷
				try {
					httpRequst.addHeader("cookie", "UserInfo={\"userId\":" + id + "}");
					httpRequst.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity httpEntity = httpResponse.getEntity();
						result = EntityUtils.toString(httpEntity);// 取锟斤拷应锟斤拷锟街凤拷锟斤拷
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = e.getMessage().toString();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = e.getMessage().toString();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = e.getMessage().toString();
				}
				handler.obtainMessage(0, result).sendToTarget();
			}
		}.start();
	}

	/**
	 * 把两个位图覆盖合成为一个位图，以底层位图的长宽为基准
	 * 
	 * @param backBitmap
	 *            在底部的位图
	 * @param frontBitmap
	 *            盖在上面的位图
	 * @return
	 */
	public static Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap) {

		if (backBitmap == null || backBitmap.isRecycled() || frontBitmap == null || frontBitmap.isRecycled()) {
			Log.e("js", "backBitmap=" + backBitmap + ";frontBitmap=" + frontBitmap);
			return null;
		}
		Bitmap bitmap = backBitmap.copy(Config.RGB_565, true);
		Canvas canvas = new Canvas(bitmap);
		Rect baseRect = new Rect(0, 0, backBitmap.getWidth(), backBitmap.getHeight());
		Rect frontRect = new Rect(0, 0, frontBitmap.getWidth(), frontBitmap.getHeight());
		canvas.drawBitmap(frontBitmap, frontRect, baseRect, null);
		return bitmap;
	}

	/** 保存方法 */
	public static void saveBitmap(String picName, Bitmap bm) {
		File f = new File(picName);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String viewSaveImage(MyRelativeLayout rela) {

		Bitmap bitmap = ImageUtils.createViewBitmap(rela, rela.getWidth(), rela.getHeight());
		String fileName = "CRETIN_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
		String result = ImageUtils.saveBitmapToFile(bitmap, fileName);
		return result;
	}

	public static Bitmap viewSaveBitmap(MyRelativeLayout rela) {
		Bitmap bitmap = ImageUtils.createViewBitmap(rela, rela.getWidth(), rela.getHeight());
		return bitmap;
	}

	/** 缓存路径 **/
	public static String GetFilePath() {
		// 设置拍摄视频缓存路径
		String path;
		File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (dcim.exists()) {
			path = dcim + "/Camera/pdy/";
		} else {
			path = dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/Camera/pdy/";
		}
		isFolderExists(path);
		return path;
	}

	static boolean isFolderExists(String strFolder) {
		File file = new File(strFolder);
		if (!file.exists()) {
			if (file.mkdirs()) {
				return true;
			} else {
				return false;

			}
		}
		return true;

	}

	/**
	 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
	 * 
	 * @param activity
	 * @param imageUri
	 * @author yaoxing
	 * @date 2014-10-12
	 */
	@TargetApi(19)
	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/** 视频路径转缩略图 **/
	public static Bitmap getVideoThumbnail(String filePath) {
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
		return bitmap;
	}

	public static Bitmap getVideoThumbnailAtTimeCache(String filePath, int time, String cacheDir) {
		String cachePath = getThumCachePath(filePath + time, cacheDir);

		File file = new File(cachePath);
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(cachePath);
			return bitmap;
		} else {
			Bitmap bitmap = getVideoThumbnailAtTime(filePath, time);
			StaticMethod.SaveImg(bitmap, filePath + time, cacheDir);
			return bitmap;
		}
	}

	/** 视频路径从指定时间获取缩略图 **/
	public static Bitmap getVideoThumbnailAtTime(String filePath, int time) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			
			/** 不使用关键帧，所以用 OPTION_CLOSEST **/
			bitmap = retriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC );
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
		Bitmap resizeBmp = getThumImg(bitmap, 150);
		bitmap.recycle();
		return resizeBmp;
	}

	public static void getVideoThumbnailArray(Bitmap[] imgArr, String filePath, int[] arrIndex, int[] arrTime) {

		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			for (int i = 0; i < arrTime.length; i++) {
				int time = arrTime[i];
				if (time == -1) {
					// 生成过缩量图的过
					continue;
				}

				int ii = arrIndex[i];
				Bitmap bitmap = null;
				/** 不使用关键帧，所以用 OPTION_CLOSEST **/
				bitmap = retriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
				Bitmap resizeBmp = getThumImg(bitmap, 150);
				bitmap.recycle();
				imgArr[ii] = (resizeBmp);
			}
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

	}

	public static Bitmap getThumImg(Bitmap bitmap, int maxSize) {
		Matrix matrix = new Matrix();
		float scal = 1f;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (width > height) {
			scal = maxSize * 1.0f / height;
		} else {
			scal = maxSize * 1.0f / width;
		}

		matrix.postScale(scal, scal); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return resizeBmp;
	}

	public static String secToTime(String text) {
		int time = Integer.parseInt(text) / 1000;
		return secToTime(time);
	}

	/** 毫秒数转换为00:00:00 **/
	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	/** 获取视频时长 **/
	public static void getVideoInfor(int index) {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		VideoInfo vInfo = videosInfo.get(index);
		mmr.setDataSource(vInfo.videoPath);
		String videoTime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
		String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
		String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
		String rotation = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

		if (rotation.equals("90") || rotation.equals("270")) {
			width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
			height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
		}
		vInfo.rotation = Integer.parseInt(rotation);
		vInfo.videoTime = Integer.parseInt(videoTime);
		vInfo.totalVideoTime = Integer.parseInt(videoTime);
		vInfo.endVideoTime = Integer.parseInt(videoTime);
		
		
		vInfo.width = Integer.parseInt(width);
		vInfo.height = Integer.parseInt(height);
	}

	public static String getVideoTime(String videoPath) {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(videoPath);
		String videoTime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
		return videoTime;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	
	public static void ResetSufaceSize(VideoInfo vInfo, View view,View viewParent,int fromView) {
		int h = Constants.mScreenWidth * 9 / 16;
//		为什么都是
		{
			if(fromView == 0)
			{
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Constants.mScreenWidth, h);

				lp.setMargins(0, 0, 0, 0);
				viewParent.setLayoutParams(lp);
			}else
			{
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Constants.mScreenWidth, h);

				lp.setMargins(0, 0, 0, 0);
				lp.addRule(RelativeLayout.BELOW, R.id.top_menu);
				viewParent.setLayoutParams(lp);
			}
			
		}

		if (h == 0) {
			return;
		}
		int targetHeight = h;
		int left = 0;
		int top = 0;
		int right = 0;
		int bottom = 0;
		double scaleSuface = Constants.mScreenWidth * 1.0 / targetHeight;// TODO:动态获取高度
		double scaleVideo = vInfo.width * 1.0 / vInfo.height;
		int targetWidth = Constants.mScreenWidth;
		if (scaleVideo > scaleSuface) {
			targetHeight = (int) (targetWidth / scaleVideo);
			top = (h - targetHeight) / 2;
		} else if (scaleVideo < scaleSuface) {
			targetWidth = (int) (targetHeight * scaleVideo);
			left = (Constants.mScreenWidth - targetWidth) / 2;

		}
		{
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(targetWidth, targetHeight);
			lp.alignWithParent = true;

			lp.setMargins(left, top, right, bottom);
			view.setLayoutParams(lp);
		}

	}

	public static void SaveImg(Bitmap bitmap, String filePath, String cacheDir) {

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
		{ // 获取SDCard指定目录下
			String sdCardDir = cacheDir;// CameraMain2.this.getCacheDir() +
										// "/thum";
			File dirFile = new File(sdCardDir); // 目录转化成文件夹
			if (!dirFile.exists()) { // 如果不存在，那就建立这个文件夹
				dirFile.mkdirs();
			} // 文件夹有啦，就可以保存图片啦
			String signFileName = SignHelper.GetSha1(filePath);
			File file = new File(sdCardDir, signFileName + ".jpg");

			try {
				FileOutputStream out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static String getThumCachePath(String filePath, String cacheDir) {
		String sign = SignHelper.GetSha1(filePath);
		String cachePath = cacheDir + sign + ".jpg";
		return cachePath;
	}
}
