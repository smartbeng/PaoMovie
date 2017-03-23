package com.pdy.camera;

import java.util.ArrayList;
import java.util.List;

import com.pdy.mobile.R;
import com.pdy.information.VideoInfo;
import com.pdy.mobile.BaseActivity;
import com.pdy.mobile.StaticMethod;
import com.pdy.others.RangeSeekBar;
import com.pdy.others.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.pdy.textview.view.MyRelativeLayout;
import com.seu.magiccamera.common.utils.Constants;
import com.seu.magiccamera.common.view.FilterLayoutUtils;
import com.seu.magicfilter.display.MagicVideoDisplay;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class EditorVideo extends BaseActivity implements
		// GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener,
		com.pdy.textview.view.MyRelativeLayout.MyRelativeTouchCallBack, OnClickListener {
	/** 视频预览 */
	private MyVideoView mVideoView;
	/** 播放进度 **/
	Handler handler;
	Runnable videoProgress;
	private SeekBar videoProgressSeekBar;
	/** 播放按键控件 **/
	private ImageView videoPlayImage;
	/** 进度条开始时间 **/
	private TextView videoTimeStart;
	/** 进度条结束时间 **/
	private TextView videoTimeEnd;
	/** 视频时间 **/
	private int videoTime = 0;
	/** 视频路径 **/
	private String videoPath = "";
	/** 裁减视频菜单图标 **/
	private ImageView cutVideo;
	/** 滤镜视频菜单图标 **/
	private ImageView filterVideo;
	/** 字幕视频菜单图标 **/
	private ImageView subtitleVideo;
	/** 裁减缩略图列表 **/
	private LinearLayout cutVideoImage;
	/** 裁减缩略列表 **/
	private FrameLayout editorCutVideo;
	/** 字幕列表 **/
	private LinearLayout editorSubtitleVideo;
	/** 滤镜列表 **/
	private FrameLayout editorFilterVideo;
	/** 视频下标 **/
	int videoIndex = 0;
	/** 字幕布局 **/
	private com.pdy.textview.view.MyRelativeLayout rela;

	private int videosHorizontalXStart = -1;
	private int videosHorizontalXEnd = 0;

	private TextView textView;
	private RelativeLayout recordLayout;
	private MagicVideoDisplay mMagicVideoDisplay;
	private FrameLayout mFilterLayout;
	VideoInfo vInfo = null;
	private int totalVideoTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_video);

		videoIndex = getIntent().getIntExtra("video", 0);
		vInfo = StaticMethod.videosInfo.get(videoIndex);
		videoPath = vInfo.videoPath;
		videoTime = vInfo.videoTime;
		totalVideoTime = vInfo.totalVideoTime;
		/** 绑定控件 **/
		mVideoView = (MyVideoView) findViewById(R.id.record_preview);
		// mVideoView.setEGLContextClientVersion(2);
		// mVideoView.setRenderer(this);
		recordLayout = (RelativeLayout) findViewById(R.id.record_layout);

		// if(StaticMethod.textviewData!=null&&StaticMethod.textviewData.size()!=0){
		// rela.setTextViewParams(StaticMethod.textviewData.get(0));
		// }
		cutVideo = (ImageView) findViewById(R.id.cut_video);
		subtitleVideo = (ImageView) findViewById(R.id.subtitle_video);
		filterVideo = (ImageView) findViewById(R.id.filter_video);
		editorCutVideo = (FrameLayout) findViewById(R.id.editor_cut_video);
		editorSubtitleVideo = (LinearLayout) findViewById(R.id.editor_subtitle_video);
		editorFilterVideo = (FrameLayout) findViewById(R.id.layout_filter);
		getSeek();
		getVideoImage(videoPath);

		// max = videoTime;
		videoProgressSeekBar = (SeekBar) findViewById(R.id.video_progress);

		if (StaticMethod.textColor.length > StaticMethod.textType.length) {
			length = StaticMethod.textColor.length;
		} else {
			length = StaticMethod.textType.length;
		}
		playVideo();
		initUI();
		/** 绑定保存事件 **/
		ImageView next = (ImageView) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				exitEditVideo();
			}
		});

		/** 绑定返回事件 **/
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (rela != null) {
					if (rela.content.equals("")
							|| rela.content.equals(getResources().getString(R.string.editor_video_sub))) {
						rela.textView.setText("");
					}
					rela.setTextViewparam(vInfo.textViewParam);
					recordLayout.removeView(rela); 
					
				}
				StaticMethod.isTouch = false;
				Intent i = new Intent(EditorVideo.this, HoDragVideo.class);
				i.putExtra("isEditorVideo", true);
				startActivity(i);
				finish();
			}
		});

		cutVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				editorCutVideo.setVisibility(View.VISIBLE);
				editorFilterVideo.setVisibility(View.GONE);
				editorSubtitleVideo.setVisibility(View.GONE);
				StaticMethod.isTouch = false;
				if (rela.content.equals("")
						|| rela.content.equals(getResources().getString(R.string.editor_video_sub))) {
					rela.textView.setText("");
				}
			}
		});

		subtitleVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				editorCutVideo.setVisibility(View.GONE);
				editorFilterVideo.setVisibility(View.GONE);
				editorSubtitleVideo.setVisibility(View.VISIBLE);
				StaticMethod.isTouch = true;
				Log.e("js","content:"+rela.content);
				if (rela.content == null || rela.content.equals("")) {
					rela.textView.setText(getResources().getString(R.string.editor_video_sub));
				}
			}
		});

		filterVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				editorCutVideo.setVisibility(View.GONE);
				editorFilterVideo.setVisibility(View.VISIBLE);
				editorSubtitleVideo.setVisibility(View.GONE);
				if (rela.content.equals("")
						|| rela.content.equals(getResources().getString(R.string.editor_video_sub))) {
					rela.textView.setText("");
				}
				StaticMethod.isTouch = false;
			}
		});

		initConstants();
		initMagicPreview();
		initFilterLayout();
	}

	protected void exitEditVideo() {
		if (min != -1) {
			vInfo.startVideoTime = min;
		}
		if (max != -1) {
			vInfo.endVideoTime = max;
		}
		vInfo.videoTime = vInfo.endVideoTime - vInfo.startVideoTime;
		vInfo.videoFilter = mMagicVideoDisplay.getFilter();

		if (videosHorizontalXStart != -1) {
			vInfo.videosHorizontalXStart = videosHorizontalXStart;
		}
		if (rela != null) {
			if (rela.content.equals("") || rela.content.equals(getResources().getString(R.string.editor_video_sub))) {
				rela.textView.setText("");
			}
			vInfo.textViewPath = StaticMethod.viewSaveImage(rela);
			vInfo.textViewParam = rela.saveTextViewparam(rela.textView);
			recordLayout.removeView(rela);
		}
		StaticMethod.isTouch = false;
		Intent i = new Intent(EditorVideo.this, HoDragVideo.class);
		i.putExtra("isEditorVideo", true);
		startActivity(i);
		finish();
	}

	private void initFilterLayout() {

		mFilterLayout = (FrameLayout) findViewById(R.id.layout_filter);
		FilterLayoutUtils mFilterLayoutUtils = new FilterLayoutUtils(this, mMagicVideoDisplay);
		mFilterLayoutUtils.init();
	}

	private void initMagicPreview() {
		GLSurfaceView glSurfaceView = (GLSurfaceView) mVideoView;
		final VideoInfo vInfo = StaticMethod.videosInfo.get(videoIndex);

		mMagicVideoDisplay = new MagicVideoDisplay(this, glSurfaceView, vInfo.videoPath);

		Handler filterHandler = new Handler();

		filterHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				ResetSufaceSize(vInfo);
				mMagicVideoDisplay.setVideoPath(vInfo.videoPath, 1, false);
				mMagicVideoDisplay.setFilter(vInfo.videoFilter);

			}
		}, 100);
	}

	private void initConstants() {
		Point outSize = new Point();
		getWindowManager().getDefaultDisplay().getRealSize(outSize);
		Constants.mScreenWidth = outSize.x;
		Constants.mScreenHeight = outSize.y;
	}

	int min = -1;
	int max = -1;
	private RangeSeekBar<Integer> seekBar;
	private FrameLayout editorCutVideo1;

	/** 创建滑动条 **/
	private void getSeek() {
		// create RangeSeekBar as Integer range between 20 and 75

		seekBar = new RangeSeekBar<Integer>(0, maxSeebarTime * 1000, totalVideoTime * 1000, this);

		// if(totalVideoTime<maxSeebarTime*1000){
		// seekBar.setSelectedMaxValue(totalVideoTime);
		// }
		seekBar.setSelectedMaxValue(vInfo.endVideoTime);
		seekBar.setSelectedMinValue(vInfo.startVideoTime);

		if (vInfo.videosHorizontalXStart != 0) {
			seekBar.start = vInfo.videosHorizontalXStart * 1000;
			seekBar.invalidate();
		}
		// seekBar.setNormalizedMinValue((double)
		// StaticMethod.videosInfo.get(videoIndex).startVideoTime /
		// totalVideoTime)totalVideoTimeeekBar.setNormalizedMaxValue((double)
		// StaticMethod.videosInfo.get(videoIndex).endVideoTime /
		// totalVideoTime);
		// seekBar.setNotifyWhileDragging(true);
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
				// handle changed range values
				Log.e("js", "min:" + min + " max:" + max);
				max = maxValue;
				min = minValue;
				mMagicVideoDisplay.seekTo(startTime * 1000 + min);
			}
		});
		editorCutVideo1 = (FrameLayout) findViewById(R.id.editor_cut_video1);

		editorCutVideo1.addView(seekBar);
	}

	private List<String> mDatas;
	private int maxSeebarTime = 9;
	protected int startTime;
	protected int endTime;
	// 是否已经画缩略图
	private int[] isDrawThumArr;
	Handler imageHandler = new Handler();
	private int maxLength;
	private int currentTime;
	private VideoInfo currentVideoInfo;
	private HorizontalScrollView videosHorizontal;
	private int thumSize;

	/** 获取视频缩略图 **/
	private void getVideoImage(String path) {

		// 使用handler时首先要创建一个handler
		cutVideoImage = (LinearLayout) findViewById(R.id.cut_video_image);
		videosHorizontal = (HorizontalScrollView) findViewById(R.id.videos_horizontal);
		thumSize = Constants.mScreenWidth / maxSeebarTime;
		maxLength = StaticMethod.videosInfo.get(videoIndex).totalVideoTime / 1000;
		isDrawThumArr = new int[maxLength];

		videosHorizontal.setOnTouchListener(new OnTouchListener() {

			private int mCurrentPosX;
			int mPosX;
			int mPosY;
			private int mCurrentPosY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// TODO Auto-generated method stub
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					mPosX = (int) event.getX();
					mPosY = (int) event.getY();
				}
				if (MotionEvent.ACTION_MOVE == event.getAction()) {
					mCurrentPosX = (int) event.getX() - mPosX;
					mCurrentPosY = (int) event.getY() - mPosY;

					mPosX = (int) event.getX();
					mPosY = (int) event.getY();

					videosHorizontalXStart = (int) (videosHorizontal.getScrollX() / thumSize);
					videosHorizontalXEnd = videosHorizontalXStart + Math.min(maxSeebarTime, maxLength - startTime);
					seekBar.start = videosHorizontalXStart * 1000;
					seekBar.invalidate();
					seekBar.RefreshRangeSeekBar();
					// String startStr = getFormatTime(videosHorizontalXStart);
					// String endStr = getFormatTime(videosHorizontalXEnd);

					// EditorVideo.this.videoTimeStart.setText(startStr);
					// EditorVideo.this.videoTimeEnd.setText(endStr);

				}
				if (MotionEvent.ACTION_UP == event.getAction()) {
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							int diff = mMagicVideoDisplay.getCurrentPosition() - startTime * 1000;
							startTime = (int) (videosHorizontal.getScrollX() / thumSize);
							endTime = startTime + Math.min(maxSeebarTime, maxLength - startTime);
							seekBar.start = startTime * 1000;

							seekBar.invalidate();

							mMagicVideoDisplay.seekTo(startTime * 1000 + diff);
							refreshThumImage();

						}
					}, 500);

				}
				if (mCurrentPosX - mPosX > 0 && Math.abs(mCurrentPosY - mPosY) < 10) {

					Log.e("", "向右的按下位置" + mPosX + "移动位置" + mCurrentPosX);

				} else if (mCurrentPosX - mPosX < 0 && Math.abs(mCurrentPosY - mPosY) < 10) {

					Log.e("", "向左的按下位置" + mPosX + "移动位置" + mCurrentPosX);

				} else if (mCurrentPosY - mPosY > 0 && Math.abs(mCurrentPosX - mPosX) < 10)

				{
					Log.e("", "向下的按下位置" + mPosX + "移动位置" + mCurrentPosX);

				} else if (mCurrentPosY - mPosY < 0 && Math.abs(mCurrentPosX - mPosX) < 10)

				{
					Log.e("", "向上的按下位置" + mPosX + "移动位置" + mCurrentPosX);

				}

				return false;

			}

			private String getFormatTime(int startTime) {
				int min = startTime / 60;
				int sec = startTime % 60;
				String minStr = String.format("%02d", min);
				String secStr = String.format("%02d", sec);
				String str = minStr + ":" + secStr;
				return str;
			}
		});

		currentTime = totalVideoTime / maxLength;
		List<ImageView> ivList = new ArrayList<ImageView>();
		for (int i = 0; i < maxLength; i++) {
			final ImageView iv = new ImageView(EditorVideo.this);
			ivList.add(iv);
			iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

			final android.view.ViewGroup.LayoutParams ps = new android.view.ViewGroup.LayoutParams(thumSize,
					thumSize * 2);

			iv.setMaxWidth(thumSize);
			iv.setMaxHeight(thumSize * 2);
			iv.setLayoutParams(ps);
			cutVideoImage.addView(iv);
		}

		Log.e("js", "初始位置:" + vInfo.videosHorizontalXStart);
		final View view = cutVideoImage.getChildAt(vInfo.videosHorizontalXStart);
		videosHorizontalXStart = vInfo.videosHorizontalXStart;
		/** 初始化缩略图的位置 **/
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Log.e("js", "滑动到：" + view.getX());
				videosHorizontal.scrollTo((int) view.getX(), (int) view.getY());
			}
		}, 500);

		currentVideoInfo = StaticMethod.videosInfo.get(videoIndex);
		startTime = currentVideoInfo.startVideoTime / 1000;
		videosHorizontal.setScrollX(startTime);
		refreshThumImage();

	}

	private void refreshThumImage() {

		Thread thread = new Thread(new Runnable() {

			private int ii;

			@Override
			public void run() {
				int targetIndex = 0;
				ii = 0;
				int ssss = Math.max(videosHorizontalXStart, 0);

				for (int i = 0; i < maxSeebarTime; i++) {

					/** videos_horizontal偏移设置 **/

					ii = i + ssss;
					Log.e("js", "ii:" + ii + " videosHorizontalXStart:" + videosHorizontalXStart);
					if (ii > maxLength - 1) {
						return;
					}

					if (isDrawThumArr[ii] == 1) {
						continue;
					}

					isDrawThumArr[ii] = 1;

					final ImageView iv = (ImageView) cutVideoImage.getChildAt(ii);
					if (ii == maxLength - 1) {
						final Bitmap bitmap = StaticMethod.getVideoThumbnailAtTimeCache(videoPath,
								totalVideoTime * 1000 - 500, EditorVideo.this.getCacheDir() + "/thum");
						handler.postDelayed(new Runnable() {
							public void run() {
								iv.setImageBitmap(bitmap);
							}
						}, 1);
					} else {
						final Bitmap bitmap = StaticMethod.getVideoThumbnailAtTimeCache(videoPath, ii * 1000 * 1000,
								EditorVideo.this.getCacheDir() + "/thum");
						handler.postDelayed(new Runnable() {
							public void run() {
								iv.setImageBitmap(bitmap);

							}
						}, 1);
					}
					// TODO 自动生成的方法存根
				}
			}
		});
		thread.start();

	}

	private void ResetSufaceSize(VideoInfo vInfo) {
		StaticMethod.ResetSufaceSize(vInfo, mVideoView, recordLayout, 1);
	}

	private void playVideo() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根

				videoPlayImage = (ImageView) findViewById(R.id.video_play_image);
				videoTimeStart = (TextView) findViewById(R.id.video_time_start);
				videoTimeEnd = (TextView) findViewById(R.id.video_time_end);
				videoTimeEnd
						.setText(StaticMethod.secToTime(StaticMethod.videosInfo.get(videoIndex).totalVideoTime + ""));
				/** 播放按钮监听 **/
				videoPlayImage.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (mMagicVideoDisplay.isPlaying()) {
							videoPlayImage.setImageResource(R.drawable.video_play);
							handler.removeCallbacks(videoProgress);
							mMagicVideoDisplay.pause();
						} else {
							if (videoProgressSeekBar.getProgress() < 99) {
								videoPlayImage.setImageResource(R.drawable.video_pause);
								handler.post(videoProgress);
								mMagicVideoDisplay.start();
							}
						}
					}
				});

				videoProgressSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
						int ti = arg0.getProgress() * totalVideoTime / 100;

						mMagicVideoDisplay.seekTo(ti);

						startTime = ti / 1000;
						videosHorizontal.setScrollX(startTime * thumSize);
						seekBar.start = startTime;
						seekBar.invalidate();
						refreshThumImage();

						videoTimeStart.setText(StaticMethod.secToTime(ti + ""));
						handler.post(videoProgress);
						videoPlayImage.setImageResource(R.drawable.video_pause);
						mMagicVideoDisplay.start();
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
				{
					int progress = 0;
					progress = startTime * 100 / totalVideoTime;
					videoProgressSeekBar.setProgress(progress);
				}

				// 使用handler时首先要创建一个handler
				handler = new Handler();
				// 要用handler来处理多线程可以使用runnable接口，这里先定义该接口
				// 线程中运行该接口的run函数
				videoProgress = new Runnable() {
					public void run() {
						if (mMagicVideoDisplay.isPlaying()) {
							int time = 0;
							time = mMagicVideoDisplay.getCurrentPosition();
							// if (time >= (startTime * 1000 + max - 100)) {
							if (time >= (vInfo.endVideoTime - 100)) {
								handler.removeCallbacks(videoProgress);
								mMagicVideoDisplay.seekTo(startTime * 1000 + min);
								videoPlayImage.setImageResource(R.drawable.video_play);
								mMagicVideoDisplay.pause();
								videosHorizontal.setScrollX(startTime * thumSize);
								refreshThumImage();
							}
							int progress = 0;
							progress = time * 100 / totalVideoTime;
							videoProgressSeekBar.setProgress(progress);
							videoTimeStart.setText(StaticMethod.secToTime(time + ""));
						}
						// 延时0.1s后又将线程加入到线程队列中
						handler.postDelayed(videoProgress, 100);
					}
				};
				handler.post(videoProgress);
			}
		});

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitEditVideo();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void initUI() {
		VideoInfo vInfo = StaticMethod.videosInfo.get(videoIndex);
		rela = (MyRelativeLayout) findViewById(R.id.id_rela);
		rela.textView = (TextView) findViewById(R.id.sub_text);
		rela.RefreshContext(this);
		rela.setMyRelativeTouchCallBack(this);
		if (vInfo.textViewParam != null) {
			rela.setTextViewparam(vInfo.textViewParam);
		}

		LinearLayout color = (LinearLayout) findViewById(R.id.editor_subtitle_video_color);
		LinearLayout type = (LinearLayout) findViewById(R.id.editor_subtitle_video_type);
		for (int i = 0; i < StaticMethod.textColor.length; i++) {
			View subItem = LayoutInflater.from(this).inflate(R.layout.sub_item, null);
			ImageView item = (ImageView) subItem.findViewById(R.id.color);
			item.setTag("color" + i);
			item.setOnClickListener(this);
			item.setBackgroundColor(getResources().getColor(StaticMethod.textColor[i]));
			item.setVisibility(View.VISIBLE);
			color.addView(subItem);
		}

		for (int i = 0; i < StaticMethod.textType.length; i++) {
			View subItem = LayoutInflater.from(this).inflate(R.layout.sub_item, null);
			TextView item = (TextView) subItem.findViewById(R.id.text);
			item.setTag("type" + i);
			item.setOnClickListener(this);
			item.setText(StaticMethod.textType[i]);
			item.setVisibility(View.VISIBLE);
			type.addView(subItem);
		}

	}

	@Override
	public void touchMoveCallBack(int direction) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onTextViewMoving(TextView textView) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onTextViewMovingDone() {
		// TODO 自动生成的方法存根

	}

	int length = 0;

	@Override
	public void onClick(View v) {
		if (rela.textView != null) {
			String tag = v.getTag().toString();
			// TODO 自动生成的方法存根
			for (int i = 0; i < length; i++) {
				if (tag.equals("color" + i)) {
					rela.color = getResources().getColor(StaticMethod.textColor[i]);
					if (rela.content.equals("")) {
						rela.content = getResources().getString(R.string.editor_video_sub);
					}
					rela.addTextView(rela.textView, rela.textView.getX(), rela.textView.getY(), rela.content,
							rela.color, rela.textView.getTextSize(), rela.textView.getRotation(),
							rela.typeface);
					return;
				} else if (tag.equals("type" + i)) {
					if (rela.content.equals("")) {
						rela.content = getResources().getString(R.string.editor_video_sub);
					}
					rela.typeface = StaticMethod.typefaces[i];
					rela.addTextView(rela.textView, rela.textView.getX(), rela.textView.getY(), rela.content,
							rela.color, rela.textView.getTextSize(), rela.textView.getRotation(), rela.typeface);
					return;
				}
			}
		}
	}

	class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			MyViewHolder holder = new MyViewHolder(
					LayoutInflater.from(EditorVideo.this).inflate(R.layout.item_home, parent, false));
			return holder;
		}

		@Override
		public void onBindViewHolder(MyViewHolder holder, int position) {
			holder.tv.setText(mDatas.get(position));
		}

		@Override
		public int getItemCount() {
			return mDatas.size();
		}

		class MyViewHolder extends ViewHolder {

			TextView tv;

			public MyViewHolder(View view) {
				super(view);
				tv = (TextView) view.findViewById(R.id.id_num);
			}
		}
	}

}