package com.pdy.camera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;
import com.pdy.mobile.R;
import com.pdy.information.VideoInfo;
import com.pdy.mobile.BaseActivity;
import com.pdy.mobile.HelperSP;
import com.pdy.mobile.PostResult;
import com.pdy.mobile.StaticMethod;
import com.pdy.mobile.UploadProgress;
import com.pdy.mobile.UploadThread;
import com.seu.magiccamera.common.utils.Constants;
import com.seu.magicfilter.display.MagicMovieDisplay;
import com.seu.magicfilter.display.MagicVideoDisplay;
import com.seu.magicfilter.utils.SaveTaskMovie.onMovieSaveListener;
import com.yixia.videoeditor.adapter.UtilityAdapter;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class HoDragVideo extends BaseActivity {
	/** 开始转码 */
	private static final int HANDLER_ENCODING_START = 100;
	/** 转码进度 */
	private static final int HANDLER_ENCODING_PROGRESS = 101;
	/** 转码结束 */
	private static final int HANDLER_ENCODING_END = 102;

	/** 视频编辑中滑动视频列表 **/
	private LinearLayout videosScroll;
	/** 手势 **/
	private GestureDetector mGestureDetector;
	/** 滑动视频列表的父节点 **/
	private View videosHorizontal;
	/** 视频预览 */
	private MyVideoView mVideoView;
	/** 视频播放的数量 **/
	int videoi = 0;
	/** 播放按键控件 **/
	private ImageView videoPlayImage;
	/** 进度条开始时间 **/
	private TextView videoTimeStart;
	/** 进度条结束时间 **/
	private TextView videoTimeEnd;
	/** 视频总长度 **/
	private int videoTimes = 0;
	/** 点击添加视频按钮下标 **/
	private int addVideoIndex = 0;
	private RelativeLayout recordLayout;

	/** 背景音乐列表 **/
	private LinearLayout musicLinear;

	private TextView video;

	private TextView music;
	private MagicVideoDisplay mMagicVideoDisplay;
	private ImageView exprot;
	private ImageView musicImage;
	private GLSurfaceView aGl;
	String cmd = "";
	private String path;
	private LinearLayout musicScroll;
	private int tWidth;
	private int tHeight;
	private ImageView subImage;
	public static HoDragVideo hoDragVideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		// TODO 自动生成的构造函数存根
		setContentView(R.layout.editor_video_main);

		hoDragVideo = this;
		if (getIntent().getStringArrayListExtra("videos") != null)
			StaticMethod.videoPathsList = getIntent().getStringArrayListExtra("videos");
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StaticMethod.videosInfo.clear();
				// TODO 自动生成的方法存根
				finish();
			}
		});

		aGl = new GLSurfaceView(getApplicationContext());
		LinearLayout alin = (LinearLayout) findViewById(R.id.alin);
		alin.addView(aGl);
		alin.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		recordLayout = (RelativeLayout) findViewById(R.id.record_layout);

		subImage = (ImageView) recordLayout.findViewById(R.id.sub_image);
		/** 绑定控件 **/
		mVideoView = (MyVideoView) findViewById(R.id.record_preview);

		videosScroll = (LinearLayout) findViewById(R.id.videos_scroll);
		videoProgressSeekBar = (SeekBar) findViewById(R.id.video_progress);

		/** 手势 **/
		mGestureDetector = new GestureDetector(this, new DrapGestureListener());

		musicScroll = (LinearLayout) findViewById(R.id.music_scroll);

		/** 视频列表 ,点击空白处取消视频编辑 **/
		videosHorizontal = findViewById(R.id.videos_horizontal);
		videosHorizontal.setOnTouchListener(mOnTouchListener);
		musicLinear = (LinearLayout) findViewById(R.id.music_linear);
		video = (TextView) findViewById(R.id.video);
		music = (TextView) findViewById(R.id.music);
		mMagicMovieDisplay = new MagicMovieDisplay(this, aGl, null);

		/** 视频编辑界面 **/
		video.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				musicLinear.setVisibility(View.GONE);
				videosHorizontal.setVisibility(View.VISIBLE);
				// MediaRecorder mediaRecorder = new MediaRecorder();
				// mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
			}
		});

		/** 音频编辑界面 **/
		music.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currVIvideo = 0;
				currVITime = 1;
				videosHorizontal.setVisibility(View.GONE);
				musicLinear.setVisibility(View.VISIBLE);

				/** 加载视频缩略图 **/
				final Handler handler = new Handler();
				if (musicScroll.getChildCount() == 0) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Log.e("js", currVIvideo + " == " + StaticMethod.videosInfo.size());
							if (currVIvideo == StaticMethod.videosInfo.size()) {
								handler.removeCallbacks(this);
								return;
							}
							VideoInfo vInfo = StaticMethod.videosInfo.get(currVIvideo);
							int time = vInfo.endVideoTime - vInfo.startVideoTime;
							if (currVITime * 1000 < time) {
								currVIBitmap = StaticMethod.getVideoThumbnailAtTimeCache(vInfo.videoPath,
										currVITime * 1000, HoDragVideo.this.getCacheDir() + "/thum");
							} else {
								currVIvideo++;
								currVITime = 1;
							}
							runOnUiThread(new Runnable() {
								public void run() {
									ImageView imageView = new ImageView(HoDragVideo.this);
									imageView.setImageBitmap(currVIBitmap);
									musicScroll.addView(imageView);
								}
							});
							currVITime++;
							handler.post(this);
						}
					});
				}
			}
		});

		/** 添加音频按钮 **/
		musicImage = (ImageView) findViewById(R.id.music_Image);
		if (StaticMethod.audioPath.equals("")) {
			musicImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setType("audio/mp3");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intent, 1);
				}
			});
		} else {
			musicImage.setImageResource(R.drawable.audio);
		}

		boolean isEditorVideo = getIntent().getBooleanExtra("isEditorVideo", false);
		// 初始化
		initConstants();
		InitializationVideoList(isEditorVideo);
		initMagicPreview();
		Log.e("js", "isEditorVideo:" + isEditorVideo);
		if (StaticMethod.videosInfo != null && StaticMethod.videosInfo.size() > 0) {
			final VideoInfo vInfo = StaticMethod.videosInfo.get(0);
			setSubTextView(vInfo);
			Handler filterHandler = new Handler();
			filterHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mMagicVideoDisplay.setFilter(vInfo.videoFilter);

					ResetSufaceSize(vInfo);

					mMagicVideoDisplay.setVideoPath(vInfo.videoPath, vInfo.startVideoTime, false);
				}
			}, 100);
		} else {
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if (StaticMethod.videosInfo != null && StaticMethod.videosInfo.size() > 0) {
						final VideoInfo vInfo = StaticMethod.videosInfo.get(0);
						ResetSufaceSize(vInfo);
						mMagicVideoDisplay.setVideoPath(vInfo.videoPath, 1, false);

					}

				}
			}, 500);

		}

		exprot = (ImageView) findViewById(R.id.exprot);
		path = StaticMethod.GetFilePath();

		/** 导出按钮 **/
		exprot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mMagicVideoDisplay.pause();
				VideoInfo vInfo = null;

				tWidth = recordLayout.getWidth();
				tHeight = recordLayout.getHeight();
				tWidth = 480;
				tHeight = 480;
				yuvTotalLength = 0;
				readLength = 0;
				readFile = 0;
				totalFile = 0;
				totalProgress = 0;
				output = new ByteArrayOutputStream();
				Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case HANDLER_ENCODING_START:
							if (!isFinishing()) {
								showProgress("", getString(R.string.record_preview_encoding));

								// releaseVideo();

								sendEmptyMessage(HANDLER_ENCODING_PROGRESS);
							}
							break;
						case HANDLER_ENCODING_PROGRESS:// 读取进度
							// int progress =
							// UtilityAdapter.FilterParserInfo(UtilityAdapter.FILTERINFO_PROGRESS);
							if (mProgressDialog != null) {
								mProgressDialog
										.setMessage(getString(R.string.record_preview_encoding_format, totalProgress));
							}
							if (totalProgress < 100)
								sendEmptyMessageDelayed(HANDLER_ENCODING_PROGRESS, 200);
							else {
								sendEmptyMessage(HANDLER_ENCODING_END);
							}
							break;
						case HANDLER_ENCODING_END:
							int mDuration = UtilityAdapter.FilterParserInfo(UtilityAdapter.FILTERINFO_TOTALMS);
							// mThemeSufaceView.release();
							hideProgress();
							break;
						case UtilityAdapter.NOTIFYVALUE_BUFFEREMPTY:
							// showLoading();
							break;
						case UtilityAdapter.NOTIFYVALUE_BUFFERFULL:
							hideLoading();
							break;
						case UtilityAdapter.NOTIFYVALUE_PLAYFINISH:
							/** 播放完成时报告 */
							if (!isFinishing()) {// && !mStopPlayer) {
								// showLoading();
								// mThemeSufaceView.release();
								// mThemeSufaceView.initFilter();
								// mPlayStatus.setVisibility(View.GONE);
							}
							break;
						case UtilityAdapter.NOTIFYVALUE_HAVEERROR:
							/** 无法播放时报告 */
							if (!isFinishing()) {
								// Toast.makeText(MediaPreviewActivity.this,
								// R.string.record_preview_theme_load_faild,
								// Toast.LENGTH_SHORT).show();
							}
							break;
						}
						super.handleMessage(msg);
					}
				};
				handler.sendEmptyMessage(HANDLER_ENCODING_START);

				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {

						// TODO Auto-generated method stub
						VideoInfo vInfo = null;
						totalFile = StaticMethod.videosInfo.size();
						for (int i = 0; i < StaticMethod.videosInfo.size(); i++) {
							vInfo = StaticMethod.videosInfo.get(i);
							trimmVideo = new TrimmVideo(vInfo.videoPath,
									(vInfo.startVideoTime + vInfo.videosHorizontalXStart) / 1000,
									(vInfo.endVideoTime - vInfo.startVideoTime) / 1000 + 1, "video" + i + ".mp4");
							readFile++;
							totalProgress += readFile * 100 / totalFile * 2 / 10;
						}
						for (int i = 0; i < StaticMethod.videosInfo.size(); i++) {
							vInfo = StaticMethod.videosInfo.get(i);

							/** 解码 **/
							int padX = 0;
							int padY = 0;
							String scaleWidth = tWidth + "";
							String scaleHeight = tHeight + "";
							String rotation = "";
							int vInfoWidth = vInfo.width;
							int vInfoHeight = vInfo.height;
							if (vInfo.rotation == 90 || vInfo.rotation == 270) {
								rotation = ",rotate=PI/2*" + vInfo.rotation / 90;
								vInfoWidth = vInfo.height;
								vInfoHeight = vInfo.width;
							}
							double scale;

							if (vInfoWidth > vInfoHeight) {
								scale = vInfoWidth * 1.0 / tWidth;
								padY = (int) ((vInfoWidth - vInfoHeight) / 2 / scale);
								scaleHeight = "-1";
							} else if (vInfoWidth < vInfoHeight) {
								scale = vInfoHeight * 1.0 / tHeight;
								padX = (int) ((vInfoHeight - vInfoWidth) / 2 / scale);
								scaleWidth = "-1";
							}

							cmd = "ffmpeg -d stdout -loglevel error -y -i " + path + "video" + i + ".mp4"
							// + " -s "+ tWidth + "x" + tHeight
									+ " -vf scale=" + scaleWidth + ":" + scaleHeight + ",pad=" + tWidth + ":" + tHeight
									+ ":" + padX + ":" + padY + rotation + " -r 25 -pix_fmt nv21 -an " + path + "video1"
									+ i + ".yuv";
							UtilityAdapter.FFmpegRun("", cmd);
							File yuvFile = new File(path + "video1" + i + ".yuv");
							yuvTotalLength += yuvFile.length();

						}
						try {
							input = new FileInputStream(path + "video1" + edcodeNum + ".yuv");
							fsOutput = new FileOutputStream(path + "output.yuv");
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						setSubTextView(vInfo);
						if (vInfo.videoFilter != 0) {
							mMagicMovieDisplay.setFilter(vInfo.videoFilter);
						}
						sWidth = vInfo.width;
						sHeight = vInfo.height;
						getbitmap();
					}
				});
				t.start();
			}
		});
	}

	int currVIvideo = 0;
	Bitmap currVIBitmap = null;
	int currVITime = 1;
	int edcodeNum = 0;
	FileInputStream input = null;
	TrimmVideo trimmVideo;
	private Bitmap bmp;
	private ByteArrayOutputStream output;
	private int sWidth = 0;
	private int sHeight = 0;
	private FileOutputStream fsOutput;
	private MagicMovieDisplay mMagicMovieDisplay;
	long readLength = 0;
	long yuvTotalLength = 0;
	long totalProgress = 0;
	private int readFile;
	private int totalFile;

	/** 获取yuv数据流并添加滤镜处理 **/
	void getbitmap() {
		int ch = -1;
		try {
			int length = (int) (tWidth * tHeight * 1.5);
			// int length = (int) (sWidth * sHeight * 1.5);
			readLength += length;
			byte[] movie = new byte[length];
			/** 读取每一帧 **/
			if ((ch = input.read(movie)) != -1) {
				totalProgress = readLength * 100 / yuvTotalLength * 8 / 10 + 20;
				/** 每一帧转换为yuvimage **/
				YuvImage a = new YuvImage(movie, ImageFormat.NV21, tWidth, tHeight, null);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				a.compressToJpeg(new Rect(0, 0, tWidth, tHeight), 100, stream);
				/** 转化为bitmap **/
				bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
				stream.close();

				output = new ByteArrayOutputStream();
				output.reset();
				/** 设置gupimage **/
				mMagicMovieDisplay.setImageBitmap(bmp);

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/** 开始处理图片 **/
				output = new ByteArrayOutputStream();
				mMagicMovieDisplay.savaImage(output, listener);

				// bmp.recycle();

			} else {
				edcodeNum++;
				input.close();
				/** 判断是否是最后一个视频 **/
				if (edcodeNum == StaticMethod.videosInfo.size()) {
					fsOutput.close();

					/** 滤镜添加完毕后，开始编码 **/
					cmd = "ffmpeg -d stdout -loglevel verbose -y -pix_fmt nv21 -ss 0 -video_size " + tWidth + "x"
							+ tHeight + " -i " + path + "output.yuv ";
					/** 添加音频 **/
					if (StaticMethod.audioPath != "") {
						cmd += "-i '" + StaticMethod.audioPath + "' -t " + videoTimes+" -strict -2";
					}

					SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
					String date = sDateFormat.format(new java.util.Date());
					cmd += " -vcodec h264 " + path + "video" + date + ".mp4";
					// TODO Auto-generated method stub
					UtilityAdapter.FFmpegRun("", cmd);
					String videoPath = path + "video" + date + ".mp4";
					Bitmap bm = StaticMethod.getVideoThumbnail(videoPath);
					final String videoTime = StaticMethod.getVideoTime(videoPath);
					StaticMethod.saveBitmap(path + "video" + date + ".png", bm);
					String uriVideoAPI = "http://paody.lansum.cn/api/api/Foam/UploadVideo";
					String uriImageAPI = "http://paody.lansum.cn/api/api/Foam/UploadPoster";
					String cookie = HelperSP.getFromSP(this, "UserId", "UserId");
					UploadThread thread = new UploadThread(uriImageAPI, null,
							new String[] { path + "video" + date + ".png" }, new PostResult() {

								@Override
								public void PostCallbackResult(String result) {
									// TODO Auto-generated method stub
									OpenVideoFoam(result, 1, videoTime);
								}
							});
					thread.cookie = cookie;
					thread.start();
					thread.setUploadProgress(new UploadProgress() {

						@Override
						public void UploadCallbackResult(float progress) {
							// TODO Auto-generated method stub

						}
					});
					UploadThread thread1 = new UploadThread(uriVideoAPI, null, new String[] { videoPath },
							new PostResult() {

								@Override
								public void PostCallbackResult(String result) {
									// TODO Auto-generated method stub
									OpenVideoFoam(result, 2);
								}
							});
					thread1.cookie = cookie;
					thread1.start();
					thread1.setUploadProgress(new UploadProgress() {

						@Override
						public void UploadCallbackResult(float progress) {
							// TODO Auto-generated method stub

						}
					});
				} else {
					Log.i("js", "下一个" + edcodeNum);
					/** 设置下一个读取文件，继续读取 **/
					try {
						input = new FileInputStream(StaticMethod.GetFilePath() + "video1" + edcodeNum + ".yuv");
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					VideoInfo vInfo = StaticMethod.videosInfo.get(edcodeNum);

					if (vInfo.videoFilter != 0) {
						mMagicMovieDisplay.setFilter(vInfo.videoFilter);
					}
					setSubTextView(vInfo);
					sWidth = vInfo.width;
					sHeight = vInfo.height;
					getbitmap();
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String videoWebPath = "";
	String ImageWebPath = "";
	String doMain = "";
	String videoTime = "";

	void OpenVideoFoam(String result, int state, String videoTime) {
		if (videoTime != null && !videoTime.equals("")) {
			OpenVideoFoam(result, state);
		} else {
			Log.e("js", "videoTime为空");
		}
	}

	/** 打开上传页面，缩略图是1，视频是2 **/
	protected void OpenVideoFoam(String result, int state) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(result);
			int resultState = jsonObject.getInt("state");
			if (resultState == 1) {
				if (state == 1) {
					ImageWebPath = jsonObject.getString("imgUrl");
				} else {
					videoWebPath = jsonObject.getString("videoUrl");
					doMain = jsonObject.getString("domain");
				}

				if (!videoWebPath.equals("") && !ImageWebPath.equals("") && !doMain.equals("")) {
					/*
					 * ?movieId=1&movieName=1&domain=2&videoUrl=3&poster=4&
					 * videoTime=5
					 */
					String url = "http://paody.lansum.cn/foamVideo.html";
					String MovieInfoId = HelperSP.getFromSP(this, "movieID", "movieID");
					String Title = HelperSP.getFromSP(this, "movieName", "movieName");
					String UserId = HelperSP.getFromSP(this, "UserId", "UserId");
					url = url + "?movieId=" + MovieInfoId + "&movieName=" + Title + "&domain=" + doMain + "&videoUrl="
							+ videoWebPath + "&poster=" + ImageWebPath + "&videoTime=" + videoTime;
					Log.e("js", "打开网页：" + url);
					webfun.openNewWindow(url, true, false, 2, "发布视频泡沫", 1, false);
				}
			} else {
				videoWebPath = "";
				ImageWebPath = "";
				doMain = "";
				Log.e("js", "上传失败：state：" + state);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Bitmap sub = null;
	/** 滤镜处理完回调 **/
	private onMovieSaveListener listener = new onMovieSaveListener() {

		@Override
		public void onSaved(String result) {
			byte[] arr = output.toByteArray();
			output.reset();
			if (arr.length < 1024 * 1) {
				getbitmap();
				return;
			}
			Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
			/** 字幕不为空的话合并 **/
			if (sub != null) {
				bitmap = StaticMethod.mergeBitmap(bitmap, sub);
			}
			// Log.i("js", bitmap.getHeight() + ":" + bitmap.getWidth());
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSSS");
			String date = sDateFormat.format(new java.util.Date());
			StaticMethod.saveBitmap("/storage/emulated/0/a/" + date + ".jpg", bitmap);
			Bitmap newBitmap = Bitmap.createBitmap(tWidth, tHeight, Config.RGB_565);
			Canvas canvas = new Canvas(newBitmap);
			Rect baseRect;

			float multiple = 0;
			if (sWidth > sHeight) {
				multiple = (float) tWidth / sWidth;
				// 定义矩阵对象
				Matrix matrix = new Matrix();
				// 缩放原图
				matrix.postScale(multiple, multiple);

				// bitmap = Bitmap.createBitmap(bitmap, 0, 0, sWidth, sHeight,
				// matrix, true);
				int nHeight = bitmap.getHeight();
				// Log.i("js", bitmap.getHeight() + ":" + bitmap.getWidth());
				if ((tHeight - nHeight) > 0) {
					baseRect = new Rect(0, (tHeight - nHeight) / 2, tWidth, tHeight - (tHeight - nHeight) / 2);
				} else {
					baseRect = new Rect(0, 0, tWidth, tHeight);
				}
			} else {
				multiple = (float) tHeight / sHeight;
				// 定义矩阵对象
				Matrix matrix = new Matrix();
				// 缩放原图
				matrix.postScale(multiple, multiple);
				// bitmap = Bitmap.createBitmap(bitmap, 0, 0, sWidth, sHeight,
				// matrix, true);
				int nWidth = bitmap.getWidth();
				if ((tWidth - nWidth) > 0) {
					baseRect = new Rect((tWidth - nWidth) / 2, 0, tWidth - (tWidth - nWidth) / 2, tHeight);
				} else {
					baseRect = new Rect(0, 0, tWidth, tHeight);
				}
			}
			Rect frontRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			canvas.drawBitmap(bitmap, frontRect, baseRect, null);
			Log.i("js", newBitmap.getHeight() + ":" + newBitmap.getWidth() + "==" + tWidth + ":" + tHeight);
			// StaticMethod.saveBitmap("/storage/emulated/0/a/a1.png",
			// newBitmap);
			bitmap.recycle();

			byte[] out = getNV21(tWidth, tHeight, newBitmap);
			newBitmap.recycle();
			try {
				fsOutput.write(out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// image.setImageBitmap(bitmap);
			// bitmap.recycle();
			// lockobj.notify();
			getbitmap();
		}
	};

	/** bitmap转yuv nv21 **/
	public static byte[] getNV21(int inputWidth, int inputHeight, Bitmap scaled) {

		int[] argb = new int[inputWidth * inputHeight];

		scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);

		byte[] yuv = new byte[inputWidth * inputHeight * 3 / 2];
		encodeYUV420SP(yuv, argb, inputWidth, inputHeight);

		scaled.recycle();

		return yuv;
	}

	private static void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
		final int frameSize = width * height;

		int yIndex = 0;
		int uvIndex = frameSize;

		int a, R, G, B, Y, U, V;
		int index = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {

				a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
				R = (argb[index] & 0xff0000) >> 16;
				G = (argb[index] & 0xff00) >> 8;
				B = (argb[index] & 0xff) >> 0;

				// well known RGB to YUV algorithm
				Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
				U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
				V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;

				// NV21 has a plane of Y and interleaved planes of VU each
				// sampled by a factor of 2
				// meaning for every 4 Y pixels there are 1 V and 1 U. Note the
				// sampling is every other
				// pixel AND every other scanline.
				yuv420sp[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
				if (j % 2 == 0 && index % 2 == 0) {
					yuv420sp[uvIndex++] = (byte) ((V < 0) ? 0 : ((V > 255) ? 255 : V));
					yuv420sp[uvIndex++] = (byte) ((U < 0) ? 0 : ((U > 255) ? 255 : U));
				}

				index++;
			}
		}
	}

	/** 初始化滤镜 **/
	private void initMagicPreview() {
		GLSurfaceView glSurfaceView = (GLSurfaceView) mVideoView;
		String videoPath = StaticMethod.videoPathsList.get(0);
		// ResetSufaceSize(vInfo);

		mMagicVideoDisplay = new MagicVideoDisplay(this, glSurfaceView, videoPath);

		// mMagicVideoDisplay.setFilter(MagicFilterType.CRAYON);
	}

	private void initConstants() {
		Point outSize = new Point();
		getWindowManager().getDefaultDisplay().getRealSize(outSize);
		Constants.mScreenWidth = outSize.x;
		Constants.mScreenHeight = outSize.y;
	}

	/** 播放进度 **/
	Handler handler;
	Runnable videoProgress;
	private SeekBar videoProgressSeekBar;

	private void playVideo() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				/** 播放按钮监听 **/
				videoPlayImage.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (mMagicVideoDisplay.isPlaying()) {
							videoPlayImage.setImageResource(R.drawable.video_play);
							handler.removeCallbacks(videoProgress);
							mMagicVideoDisplay.pause();
						} else {
							Log.i("js", "videoProgress:" + videoProgressSeekBar.getProgress());
							if (videoProgressSeekBar.getProgress() < 90) {
								videoPlayImage.setImageResource(R.drawable.video_pause);
								handler.post(videoProgress);

								mMagicVideoDisplay.start();
							} else {
								VideoInfo vInfo = StaticMethod.videosInfo.get(0);
								ResetSufaceSize(vInfo);
								mMagicVideoDisplay.setVideoPath(vInfo.videoPath, vInfo.startVideoTime);
								mMagicVideoDisplay.start();
								videoProgressSeekBar.setProgress(0);
								videoi = 0;
								videoPlayImage.setImageResource(R.drawable.video_pause);
								handler.post(videoProgress);
							}
						}
					}
				});

				videoProgressSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onStopTrackingTouch(SeekBar arg0) {

						// TODO Auto-generated method stub
						int ti = arg0.getProgress() * videoTimes / 100;
						for (int i = 0; i < StaticMethod.videosInfo.size(); i++) {
							int tiVideo = 0;
							tiVideo = StaticMethod.videosInfo.get(i).endVideoTime
									- StaticMethod.videosInfo.get(i).startVideoTime;
							if (ti > tiVideo) {
								ti -= tiVideo;
							} else {
								videoi = i;
								videoPlayImage.setImageResource(R.drawable.video_pause);
								VideoInfo vInfo = StaticMethod.videosInfo.get(videoi);
								if (vInfo.videoFilter != 0)
									mMagicVideoDisplay.setFilter(vInfo.videoFilter);
								setSubTextView(vInfo);
								ResetSufaceSize(vInfo);

								mMagicVideoDisplay.setVideoPath(vInfo.videoPath, ti + vInfo.startVideoTime);
								break;

							}
						}

						videoTimeStart.setText(StaticMethod.secToTime(arg0.getProgress() * videoTimes / 100 + ""));
						handler.post(videoProgress);
					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
						handler.removeCallbacks(videoProgress);
					}

					@Override
					public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
						// TODO Auto-generated method stub

					}
				});
				// 使用handler时首先要创建一个handler
				handler = new Handler();
				// 要用handler来处理多线程可以使用runnable接口，这里先定义该接口
				// 线程中运行该接口的run函数
				videoProgress = new Runnable() {
					public void run() {
						if (mMagicVideoDisplay.isPlaying()) {
							int time = 0;
							int progress = 0;
							if (mMagicVideoDisplay
									.getCurrentPosition() >= (StaticMethod.videosInfo.get(videoi).endVideoTime - 500)) {
								VideoInfo vInfo = StaticMethod.videosInfo.get(videoi);
								setSubTextView(vInfo);
								videoi++;
								if (videoi < StaticMethod.videosInfo.size()) {
									vInfo = StaticMethod.videosInfo.get(videoi);
									mMagicVideoDisplay.setFilter(vInfo.videoFilter);

									mMagicVideoDisplay.setVideoPath(vInfo.videoPath, vInfo.startVideoTime);
									mMagicVideoDisplay.start();
									ResetSufaceSize(vInfo);
									setSubTextView(vInfo);
								} else {
									videoi--;
									handler.removeCallbacks(videoProgress);
									mMagicVideoDisplay.pause();
									videoPlayImage.setImageResource(R.drawable.video_play);
								}
							}
							if (videoi == 0) {
								time = mMagicVideoDisplay.getCurrentPosition()
										- StaticMethod.videosInfo.get(videoi).startVideoTime;
								Log.i("js", "getCurrentPosition:" + mMagicVideoDisplay.getCurrentPosition() + " time:"
										+ StaticMethod.videosInfo.get(videoi).endVideoTime);
							} else {
								for (int i = 0; i < videoi; i++) {
									time += StaticMethod.videosInfo.get(i).endVideoTime
											- StaticMethod.videosInfo.get(i).startVideoTime;
								}
								time += mMagicVideoDisplay.getCurrentPosition()
										- StaticMethod.videosInfo.get(videoi).startVideoTime;
							}
							progress = time * 100 / videoTimes;
							videoProgressSeekBar.setProgress(progress);
							videoTimeStart.setText(StaticMethod.secToTime(time + ""));
						}
						// // 延时1s后又将线程加入到线程队列中
						handler.postDelayed(videoProgress, 100);

					}
				};
				// handler.post(videoProgress);
			}
		});

	}

	private void ResetSufaceSize(VideoInfo vInfo) {
		StaticMethod.ResetSufaceSize(vInfo, mVideoView, recordLayout, 0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mMagicVideoDisplay != null)
			mMagicVideoDisplay.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mMagicVideoDisplay != null)
			mMagicVideoDisplay.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mMagicVideoDisplay != null)
			mMagicVideoDisplay.onDestroy();
	}

	/** 刷新视频列表 参数true取消加号的触摸事件 **/
	private void RefreshVideosScroll(Boolean isCancleTouch) {
		for (int i = 0; i < videosScroll.getChildCount(); i++) {
			if (i == 0) {
				videosScroll.getChildAt(i).findViewById(R.id.connect_left).setVisibility(View.GONE);
			} else {
				videosScroll.getChildAt(i).findViewById(R.id.connect_left).setVisibility(View.VISIBLE);
			}
			/** 添加触摸事件 **/
			videosScroll.getChildAt(i).findViewById(R.id.editor_image).setOnTouchListener(mOnTouchListener);
			ImageView connect = (ImageView) videosScroll.getChildAt(i).findViewById(R.id.connect_left);

			connect.setImageResource(R.drawable.connect);
			if (isCancleTouch)
				/** 取消触摸事件 **/
				connect.setOnTouchListener(null);
			videosScroll.findViewById(i).findViewById(R.id.editor_relative).setVisibility(View.GONE);
		}
	}

	/** 点击子项 **/
	private void clickVideoItem(int index) {
		RefreshVideosScroll(false);
		videosScroll.getChildAt(index).findViewById(R.id.editor_relative).setVisibility(View.VISIBLE);
		// 取消触摸事件
		videosScroll.getChildAt(index).findViewById(R.id.editor_image).setOnTouchListener(null);
		/** 编辑添加监听事件 **/
		videosScroll.getChildAt(index).findViewById(R.id.editor_video_text).setOnTouchListener(mOnTouchListener);
		/** 删除添加监听事件 **/
		videosScroll.getChildAt(index).findViewById(R.id.editor_delete_text).setOnTouchListener(mOnTouchListener);
		ImageView connectLeft = (ImageView) videosScroll.getChildAt(index).findViewById(R.id.connect_left);
		connectLeft.setImageResource(R.drawable.add_import);
		connectLeft.setOnTouchListener(mOnTouchListener);
		if (index != (videosScroll.getChildCount() - 1)) {
			ImageView connectRight = (ImageView) videosScroll.getChildAt(index + 1).findViewById(R.id.connect_left);
			connectRight.setImageResource(R.drawable.add_import);
			connectRight.setOnTouchListener(mOnTouchListener);
			connectRight.setVisibility(View.VISIBLE);
		}
		connectLeft.setVisibility(View.VISIBLE);
	}

	int videoListNum = 0;
	Bitmap bitmap = null;
	String text = "";

	/** 初始化视频列表 **/
	private void InitializationVideoList(final Boolean isEditorVideo) {
		videoListNum = 0;
		videoTimes = 0;
		/** 初始化前清空视频列表 **/
		if (videosScroll != null) {
			videosScroll.removeAllViews();
		}
		videoPlayImage = (ImageView) findViewById(R.id.video_play_image);

		videoTimeStart = (TextView) findViewById(R.id.video_time_start);
		videoTimeEnd = (TextView) findViewById(R.id.video_time_end);
		final Handler handler = new Handler();
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (isEditorVideo) {
					Log.e("js", "videosInfo.size()" + StaticMethod.videosInfo.size());
					if (videoListNum < StaticMethod.videosInfo.size()) {
						/** 循环添加 **/
						int time = StaticMethod.videosInfo.get(videoListNum).videoTime;
						Log.i("js", "time:" + videoListNum + " " + time);
						videoTimes += StaticMethod.videosInfo.get(videoListNum).videoTime;
						/** 获取视频时间 **/
						text = StaticMethod.secToTime(time + "");
						/** 获取视频缩略图 **/
						bitmap = StaticMethod.getVideoThumbnail(StaticMethod.videosInfo.get(videoListNum).videoPath);
					} else {
						handler.removeCallbacks(this);
						return;
					}
				} else {
					Log.e("js", "videoPathsList.size()" + StaticMethod.videoPathsList.size());
					if (videoListNum < StaticMethod.videoPathsList.size()) {
						/** 循环添加 **/
						VideoInfo vInfo = new VideoInfo();
						vInfo.videoPath = StaticMethod.videoPathsList.get(videoListNum);
						Log.e("js", "videoPath:" + videoListNum + vInfo.videoPath);
						StaticMethod.videosInfo.add(vInfo);
						StaticMethod.getVideoInfor(videoListNum);
						vInfo = StaticMethod.videosInfo.get(videoListNum);
						videoTimes += vInfo.videoTime;
						/** 获取视频时间 **/
						text = StaticMethod.secToTime(vInfo.videoTime + "");
						/** 获取视频缩略图 **/
						bitmap = StaticMethod.getVideoThumbnail(vInfo.videoPath);

						mMagicVideoDisplay.setFilter(vInfo.videoFilter);
						if (videoListNum == 0)
							ResetSufaceSize(vInfo);
					} else {
						handler.removeCallbacks(this);
						return;
					}
				}
				// StaticMethod.saveBitmap(StaticMethod.GetFilePath() +
				// i + ".png", bitmap);
				runOnUiThread(new Runnable() {
					public void run() {
						/** 设置获取item **/
						final View videoView = LayoutInflater.from(HoDragVideo.this).inflate(R.layout.editor_item,
								null);
						/** 设置id **/
						videoView.setId(videoListNum);
						/** 获取imageview并赋值 **/
						ImageView imageView = (ImageView) videoView.findViewById(R.id.editor_image);
						imageView.setImageBitmap(bitmap);
						/** 获取textview并赋值 **/
						TextView textView = (TextView) videoView.findViewById(R.id.editor_text);
						textView.setText(text);
						videoTimeEnd.setText(StaticMethod.secToTime(videoTimes + ""));
						videosScroll.addView(videoView);
						bindDrapListener(imageView);
						RefreshVideosScroll(true);
					}
				});
				videoListNum++;
				handler.post(this);
			}
		});

		playVideo();
		RefreshVideosScroll(true);

	}
	
	private void setSubTextView(VideoInfo vInfo){
		if (!vInfo.textViewPath.equals("")) {
			Bitmap bitmap = BitmapFactory.decodeFile(vInfo.textViewPath);
			if (bitmap != null) {
				subImage.setImageBitmap(bitmap);
			} else {
				subImage.setImageDrawable(getResources().getDrawable(R.drawable.transparent));
			}
		}
	}

	private View mDrapView;

	private void bindDrapListener(View v) {
		v.setOnTouchListener(mOnTouchListener);
		v.setOnDragListener(mOnDragListener);
	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			mDrapView = v;

			if (mGestureDetector.onTouchEvent(event))
				return true;

			switch (event.getAction() & MotionEvent.ACTION_MASK) {

			case MotionEvent.ACTION_UP:
				break;

			}

			return false;
		}
	};

	private OnDragListener mOnDragListener = new OnDragListener() {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// Do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				v.setAlpha(0.5F);
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				v.setAlpha(1F);
				break;
			case DragEvent.ACTION_DROP:
				View view = (View) event.getLocalState();
				View item = (View) view.getParent().getParent();
				for (int i = 0, j = videosScroll.getChildCount(); i < j; i++) {
					if (videosScroll.getChildAt(i).findViewById(R.id.editor_image) == v) {
						// 当前位置
						videosScroll.removeView(item);
						videosScroll.addView(item, i);
						break;
					}
				}
				RefreshVideosScroll(true);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				v.setAlpha(1F);
			default:
				break;
			}
			return true;
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// requestCode标示请求的标示 resultCode表示有数据
		if (data != null) {
			if (requestCode == 1) {
				Uri uri = data.getData(); // 得到Uri
				StaticMethod.audioPath = StaticMethod.getImageAbsolutePath(this, uri); // 转化为路径
				musicImage.setImageResource(R.drawable.audio);
				musicImage.setOnClickListener(null);
			} else if (requestCode == 2) {
				Uri uri = data.getData(); // 得到Uri
				String fPath = StaticMethod.getImageAbsolutePath(this, uri); // 转化为路径
				StaticMethod.videoPathsList.add(addVideoIndex, fPath);
				StaticMethod.videosInfo.clear();
				InitializationVideoList(false);
				if (addVideoIndex == 0) {
					mMagicVideoDisplay.setVideoPath(fPath, 1, false);
				}
			}
		}

	}

	/** 手势 **/
	private class DrapGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			return super.onSingleTapConfirmed(e);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			super.onLongPress(e);
			ClipData data = ClipData.newPlainText("", "");
			MyDragShadowBuilder shadowBuilder = new MyDragShadowBuilder(mDrapView);
			mDrapView.startDrag(data, shadowBuilder, mDrapView, 0);
		}

		@Override
		public boolean onDown(MotionEvent e) {
			RefreshVideosScroll(false);
			return true;
		}

		// 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发 点击事件
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			Boolean isConnect = false;
			if (mDrapView == videosHorizontal) {
				RefreshVideosScroll(true);
			} else {
				for (int i = 0; i < videosScroll.getChildCount(); i++) {
					/** 点击视频缩略图判定 **/
					if (videosScroll.getChildAt(i).findViewById(R.id.editor_image) == mDrapView) {
						clickVideoItem(i);
					}
					/** 点击连接图标判定 **/
					if (videosScroll.getChildAt(i).findViewById(R.id.connect_left) == mDrapView) {
						addVideoIndex = i;
						isConnect = true;
						musicScroll.removeAllViews();
					}
					/** 点击编辑图标判定 **/
					if (videosScroll.getChildAt(i).findViewById(R.id.editor_video_text) == mDrapView) {
						Intent intent = new Intent(HoDragVideo.this, EditorVideo.class);
						intent.putExtra("video", i);
						startActivity(intent);
						finish();
					}
					/** 点击删除图标判定 **/
					if (videosScroll.getChildAt(i).findViewById(R.id.editor_delete_text) == mDrapView) {
						if (StaticMethod.videosInfo.size() != 1) {
							StaticMethod.videosInfo.remove(i);
							StaticMethod.videoPathsList.remove(i);
							musicScroll.removeAllViews();
							InitializationVideoList(true);
						} else {
							Toast toast = Toast.makeText(HoDragVideo.this, "无法全部删除", Toast.LENGTH_SHORT); // 显示toast信息
							toast.show();
						}
					}
				}
			}
			if (isConnect) {
				Intent intent = new Intent();
				intent.setType("video/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 2);
			}
			return true;
		}
	}

	private class MyDragShadowBuilder extends View.DragShadowBuilder {

		private final WeakReference<View> mView;

		public MyDragShadowBuilder(View view) {
			super(view);
			mView = new WeakReference<View>(view);
		}

		@Override
		public void onDrawShadow(Canvas canvas) {
			canvas.scale(1.2F, 1.2F);
			super.onDrawShadow(canvas);
		}

		@Override
		public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
			// super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);

			final View view = mView.get();
			if (view != null) {
				shadowSize.set((int) (view.getWidth() * 1.5F), (int) (view.getHeight() * 1.5F));
				shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y / 2);
			} else {
				// Log.e(View.VIEW_LOG_TAG,
				// "Asked for drag thumb metrics but no view");
			}
		}
	}

}