package com.pdy.webview;

import java.io.File;

import com.pdy.mobile.BaseActivity;
import com.pdy.mobile.R;
import com.pdy.mobile.R.color;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

public class MyWebChromeClient extends WebChromeClient {

	public static final int FILECHOOSER_RESULTCODE = 1212;
	private String mCameraFilePath;
	private ValueCallback<Uri> mUploadMessage;
	private BaseActivity act;
	WebViewScrollChanged webview;

	private double screenWidth; // 屏幕宽度
	private double screenHeight; // 屏幕高度

	private FrameLayout video;  //传过来的视频
	
	private TextView textView; // 滑动音量显示文本
	private TextView tvLight; // 滑动亮度显示文本

	private double diffY; // move事件下的（Y起始坐标与结束坐标之差）

	// 系统音量功能调取
	private AudioManager mAudioManager;
	//最大声音
	private int maxVoice;
	// 当前声音
	private int mVoice = -1;
	// 隐藏显示参数
	private int hide = 0;
	// 返回图标
	private ImageView showhide;

	public MyWebChromeClient(BaseActivity act) {

		this.act = act;

	}
	
	

	RelativeLayout webviews;
	// 一个回调接口使用的主机应用程序通知当前页面的自定义视图已被撤职
	CustomViewCallback customViewCallback;

	// 进入全屏的时候
	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		
		
		AlbumOrientationEventListener mAlbumOrientationEventListener = new AlbumOrientationEventListener(act, SensorManager.SENSOR_DELAY_NORMAL);  
		if (mAlbumOrientationEventListener.canDetectOrientation()) {  
		    mAlbumOrientationEventListener.enable();  
		    } else {  
		       Log.d("chengcj1", "Can't Detect Orientation");  
		    }  
		
		webviews = (RelativeLayout) act.findViewById(R.id.webviews);
		int child = webviews.getChildCount();
		RelativeLayout a = (RelativeLayout) webviews.getChildAt(child - 1);
		PtrClassicFrameLayout b = (PtrClassicFrameLayout) a.getChildAt(0);
		webview = (WebViewScrollChanged) b.getChildAt(0);
		customViewCallback = callback;
		// 设置webView隐藏
		act.isFull = true;
		// 声明video，把之后的视频放到这里面去
		video = (FrameLayout) act.findViewById(R.id.video_view);
		textView = new TextView(act.getApplicationContext());
		showhide = new ImageView(act.getApplicationContext());
		tvLight = new TextView(act.getApplicationContext());
		showhide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onHideCustomView();
			}
		});
		// 拿到横屏状态下的屏幕宽度
		screenWidth = act.getResources().getDisplayMetrics().widthPixels;
		// 拿到横屏状态下的屏幕高度
		screenHeight = act.getResources().getDisplayMetrics().heightPixels;
		// 开启音量控制服务
		mAudioManager = (AudioManager) act.getSystemService(Context.AUDIO_SERVICE);

		/**
		 * 设置滑动音量显示文本
		 */
		textView.setTextSize(20);
		textView.setTextColor(Color.WHITE);

		/**
		 * 设置返回键
		 */
		showhide.setBackgroundResource(R.drawable.ic_back);
		LayoutParams params = new FrameLayout.LayoutParams(80, 70);
		params.setMargins(50, 50, 80, 80);
		showhide.setLayoutParams(params);
		
		/**
		 * 设置滑动亮度显示文本
		 */
		tvLight.setTextSize(20);
		tvLight.setTextColor(Color.WHITE);
		LayoutParams params2 = new FrameLayout.LayoutParams(20, 20);
		params2.rightMargin = 200;
		
		/**
		 * 添加黑色背景图片遮挡因视频尺寸不够而造成的页面冲突问题（页面返回键与添加的返回键冲突）
		 */
		ImageView imageView = new ImageView(act);
		imageView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		imageView.setImageResource(R.color.black);
		
		
		// 将video放到当前视图中
		video.addView(imageView);
		video.addView(view);
		textView.setVisibility(View.GONE);
		showhide.setVisibility(View.GONE);
		tvLight.setVisibility(View.GONE);
		video.addView(textView);
		video.addView(showhide);
		video.addView(tvLight);
		/**************** 这里开始 **********************/
		
		if (view instanceof FrameLayout) {
			// A video wants to be shown
			FrameLayout frameLayout = (FrameLayout) view;
			/**
			 * 获取到view界面焦点
			 */
			View focusedChild = frameLayout.getFocusedChild();
			
			/**
			 * 界面点击滑动监听
			 */
				focusedChild.setOnTouchListener(new OnTouchListener() {
			
				int lastX; // 记录首次按下的XY值
				int lastY;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					v.setClickable(true); //处理4.x系统下不能进入滑动事件
					// 检测到触摸事件后第一时间得到触摸点坐标 并赋值给x,y
					switch (event.getAction()) {
					// 触摸事件中绕不开的第一步，必然执行，将按下时的触摸点坐标赋值给 lastX 和 last Y
					case MotionEvent.ACTION_DOWN:
						lastX = (int) event.getX(); // 定义 X Y 用于存储按下时的坐标
						lastY = (int) event.getY();
						if (hide == 0) {
							showhide.setVisibility(View.VISIBLE);
							// tvLight.setVisibility(View.VISIBLE);
							hide = 1;
						} else if (hide == 1) {
							showhide.setVisibility(View.GONE);
							// tvLight.setVisibility(View.GONE);
							hide = 0;
						}
						break;
					// 触摸事件的第二步，这时候的x,y已经随着滑动操作产生了变化，用变化后的坐标减去首次触摸时的坐标得到 相对的偏移量
					case MotionEvent.ACTION_MOVE:
						// img.setVisibility(View.VISIBLE);
						int endY = (int) event.getY();
						diffY = Math.abs(lastY - endY);  //首次按下的坐标与离开时坐标的查值
						if (lastX < (screenHeight / 2)) {
							showhide.setVisibility(View.GONE);
							hide = 0;
							textView.setVisibility(View.VISIBLE);
							/*if (diffY > 0) { // 表示音量在增加
								mVoice++;
								textView.setText("音量" + ((diffY / 10) - (diffY / 10 * 0.5)));
							} else if (diffY < 0) { // 表示音量在递减
								//textView.setText("音量" + ((diffY / 10) - (diffY / 10 * 0.5)));
								textView.setText("静音X");
								mVoice = 0;
							}*/
							if (mVoice == -1) {
								mVoice = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
							}
							double percent = (double)(diffY / screenWidth); //滑动距离占屏幕的百分比
							maxVoice = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
						    double num = percent * maxVoice + mVoice;
						    int index = (int)num;
						    Log.e("fffff",""+index);
							if (index > maxVoice){  
						        index = maxVoice;
							} else if (index >= 10) {
						        textView.setText("音量"+100);
						    } else if (index >= 5 && index < 10) {
						    	textView.setText("音量"+60);
						    } else if (index > 0 && index < 5) {
						    	textView.setText("音量"+30);
						    } else {
						    }
							mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
						} else if (lastX > (screenHeight / 2)) { // 进入亮度设置
							showhide.setVisibility(View.GONE);
							hide = 0;
							tvLight.setVisibility(View.VISIBLE);

							if (diffY <= screenWidth / 5) {
								tvLight.setText("亮度10");
								setBrightness(-20);
							} else if (diffY > screenWidth / 5 && diffY <= screenWidth / 4) {
								tvLight.setText("亮度20");
								setBrightness(-10);
							} else if (diffY > screenWidth / 4 && diffY <= screenWidth / 3) {
								tvLight.setText("亮度30");
								setBrightness(0);
							} else if (diffY > screenWidth / 3 && diffY <= screenWidth / 2) {
								tvLight.setText("亮度40");
								setBrightness(10);
							} else if (diffY > screenWidth / 2 && diffY <= screenWidth / 1) {
								tvLight.setText("亮度50");
								setBrightness(20);
							}
						}

						break;
					case MotionEvent.ACTION_UP:

						textView.setVisibility(View.GONE);
						tvLight.setVisibility(View.GONE);
						break;
					}
					return false;
				}
			});			
			/**
			 * 设置屏幕亮度 lp = 0 全暗 ，lp= -1,根据系统设置， lp = 1; 最亮
			 */

		}

		/**************************************/

		video.setVisibility(View.VISIBLE);

		// 设置webView隐藏
		// webview.setVisibility(View.GONE);

		// 横屏显示
		act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// 设置全屏
		setFullScreen();
	}
	
	/**
	 * 用户手机横竖屏监听类
	 * @author MaiBenBen
	 *
	 */
     class AlbumOrientationEventListener extends OrientationEventListener {  
   
	    public AlbumOrientationEventListener(Context context) {  
	        super(context);  
	    }  
	          
	    public AlbumOrientationEventListener(Context context, int rate) {  
	        super(context, rate);  
	    }  	  
	    @Override  
	    public void onOrientationChanged(int orientation) {  
	    	//Log.e("屏幕旋转度数","" +orientation);
	    	int screenOrientation = act.getResources().getConfiguration().orientation;
	        /*if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {  
	            return;  
	        }  */
	  
	       /* if (((orientation >= 0) && (orientation < 45)) || (orientation > 315)) {//设置竖屏
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && orientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                	act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            } else if (orientation > 225 && orientation < 315) { //设置横屏
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                	act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            } else*/ 
	    	if (orientation > 45 && orientation < 135) {// 设置反向横屏
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                	act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                }
            } 
	    }  
	}  

	
     /**
 	 * 设置亮度
 	 * 
 	 * @param brightness
 	 */

	  public void setBrightness(float brightness) {

		// if (lp.screenBrightness <= 0.1) {
		// return;
		// }
		WindowManager.LayoutParams lp = act.getWindow().getAttributes();
		lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
		if (lp.screenBrightness > 1) {
			lp.screenBrightness = 1;
			/*
			 * vibrator = (Vibrator) act.getSystemService("vibrator"); long[]
			 * pattern = { 10, 200 }; // OFF/ON/OFF/ON... 关闭10秒震动200毫秒，不停切换
			 * vibrator.vibrate(pattern, -1);
			 */
		} else if (lp.screenBrightness < 0.2) {
			lp.screenBrightness = (float) 0.2;
			/*
			 * vibrator = (Vibrator) act.getSystemService(act.VIBRATOR_SERVICE);
			 * long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...
			 * vibrator.vibrate(pattern, -1);
			 */
		}
		act.getWindow().setAttributes(lp);
	}

	// 退出全屏的时候
	@Override
	public void onHideCustomView() {
		if (customViewCallback != null) {
			// 隐藏掉
			customViewCallback.onCustomViewHidden();
		}
		// 用户当前的首选方向
		act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
		act.isFull = false;
		// 设置WebView可见
		webview.setVisibility(View.VISIBLE);

		FrameLayout video = (FrameLayout) act.findViewById(R.id.video_view);
		video.setVisibility(View.GONE);
		// 退出全屏
		quitFullScreen();

	}

	/**
	 * 设置全屏
	 */
	private void setFullScreen() {
		// 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
		act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 全屏下的状态码：1098974464
		// 窗口下的状态吗：1098973440

		/************ 全屏状态下 ********************/

	}

	/**
	 * 退出全屏
	 */
	// a&=b相当于 a = a&b
	private void quitFullScreen() {
		// 声明当前屏幕状态的参数并获取
		final WindowManager.LayoutParams attrs = act.getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		act.getWindow().setAttributes(attrs);
		act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		super.onProgressChanged(view, newProgress);
	}

	// For Android 3.0+
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
		myopenFileChooser(uploadMsg, acceptType, null);
	}

	// For Android < 3.0
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		myopenFileChooser(uploadMsg, "", null);
	}

	// For Android > 4.1.1
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
		myopenFileChooser(uploadMsg, acceptType, capture);
	}

	public void myopenFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
		mUploadMessage = uploadMsg;

		act.startActivityForResult(Intent.createChooser(createDefaultOpenableIntent(acceptType, capture), "完成操作需要使用"),
				FILECHOOSER_RESULTCODE);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			// if (null == mUploadMessage) return;
			Uri result = data == null || resultCode != act.RESULT_OK ? null : data.getData();
			if (result == null && data == null && resultCode == Activity.RESULT_OK) {
				File cameraFile = new File(mCameraFilePath);
				if (cameraFile.exists()) {
					result = Uri.fromFile(cameraFile);
					// Broadcast to the media scanner that we have a new photo
					// so it will be added into the gallery for the user.
					act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
				}
			}
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;

		}

	}

	private Intent createDefaultOpenableIntent(String acceptType, String capture) {
		// Create and return a chooser with the default OPENABLE
		// actions including the camera, camcorder and sound
		// recorder where available.
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		if (acceptType == null || acceptType.equals("")) {
			acceptType = "*/*";
		}
		i.setType(acceptType);
		//
		// List<Intent> list=new ArrayList<Intent>();
		// list.add(createCameraIntent());
		//
		// list.add(createCamcorderIntent());

		Intent chooser = null;
		if (acceptType.matches("video.+")) {
			chooser = createChooserIntent(

					createCamcorderIntent()
			// createSoundRecorderIntent()
			);

		} else if (acceptType.matches("image.+")) {
			chooser = createChooserIntent(

					createCameraIntent()
			// createSoundRecorderIntent()
			);

		} else {
			chooser = createChooserIntent();
		}

		chooser.putExtra(Intent.EXTRA_INTENT, i);
		return chooser;
	}

	private Intent createChooserIntent(Intent... list) {
		Intent chooser = new Intent(Intent.ACTION_CHOOSER);
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, list);
		chooser.putExtra(Intent.EXTRA_TITLE, "File Chooser");
		return chooser;
	}

	private Intent createCameraIntent() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File cameraDataDir = new File(externalDataDir.getAbsolutePath() + File.separator + "browser-photos");
		cameraDataDir.mkdirs();
		mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
		return cameraIntent;
	}

	private Intent createCamcorderIntent() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
		File cameraDataDir = new File(externalDataDir.getAbsolutePath() + File.separator + "browser-movies");
		cameraDataDir.mkdirs();
		mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".mp4";
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
		return cameraIntent;

	}

	private Intent createSoundRecorderIntent() {
		return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
	}

}
