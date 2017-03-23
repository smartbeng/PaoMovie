package com.pdy.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.pdy.mobile.StaticMethod.PaoPaoAudio;
import com.pdy.mobile.StaticMethod.PaoPaoText;
import com.pdy.others.AudioRecoderUtils;
import com.pdy.mobile.R;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

public class SendPaoPaoAudio extends BaseActivity {

	@Bind(R.id.content)
	EditText content;
	@Bind(R.id.title)
	TextView titleView;
	@Bind(R.id.pdy_bao)
	TextView pdyBao;
	@Bind(R.id.pdy_niao)
	TextView pdyNiao;
	@Bind(R.id.prompt_rela)
	RelativeLayout promptRela;
	@Bind(R.id.pao_pao_quan)
	Spinner paoPaoQuan;
	@Bind(R.id.audio)
	TextView audio;
	@Bind(R.id.audio_time)
	TextView audioTime;
	@Bind(R.id.delete)
	ImageView delete;
	@BindDrawable(R.drawable.voiceplay)
	Drawable voicePlay;
	@BindDrawable(R.drawable.voicepause)
	Drawable voicePause;
	@BindDrawable(R.drawable.voicerecode)
	Drawable voiceRecode;

	Boolean isSetAdapter = false;
	private ArrayAdapter<String> adapter;

	PaoPaoAudio paoPaoAudio = null;
	private AudioRecoderUtils mAudioRecoderUtils;

	Boolean isAudio = true;

	private MediaPlayer mediaPlayer;

	String audioPath = "";
	private String cookie = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_pao_pao_audio);
		ButterKnife.bind(this);
		String title = HelperSP.getFromSP(this, "movieName", "movieName");
		titleView.setText(title);
		paoPaoAudio = new PaoPaoAudio();
		mAudioRecoderUtils = new AudioRecoderUtils();
		cookie  = HelperSP.getFromSP(this, "UserId", "UserId");
		// 录音回调
		mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

			// 录音中....db为声音分贝，time为录音时长
			@Override
			public void onUpdate(double db, long time) {

				if (time <= 60000) {
					paoPaoAudio.VideoTime = (time / 1000) + "";
					String sTime = StaticMethod.secToTime(time + "");
					audioTime.setText(sTime);
				} else {
					mAudioRecoderUtils.stopRecord();
					Toast.makeText(SendPaoPaoAudio.this, "最多录制60s", 1).show();
					;
				}
			}

			// 录音结束，filePath为保存路径
			@Override
			public void onStop(String filePath) {
				isAudio = false;
				delete.setVisibility(View.VISIBLE);
				audio.setText("点击播放");
				audioPath = filePath;
				audio.setCompoundDrawablesWithIntrinsicBounds(null, voicePlay, null, null);
				mediaPlayer = new MediaPlayer();
				/**
				 * 播放完成监听
				 */
				mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						if (mp.isPlaying()) {
							handler.removeCallbacks(runnable);
							mp.release();// 释放资源
						}
						audio.setText("点击播放");
						audio.setCompoundDrawablesWithIntrinsicBounds(null, voicePlay, null, null);
					}
				});

				/**
				 * 播放过程中展示的动画
				 */
				mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {
						if (mp != null) {
							mp.start();
							mp.pause();
						}
					}
				});

				try {
					mediaPlayer.setDataSource(audioPath);
					mediaPlayer.prepare();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	
	@Bind(R.id.curr_text_num)
	TextView currTextNum;
	
	@OnTextChanged(R.id.content)
	void TextChange(){
		currTextNum.setText(content.getText().length()+"/180");
	}
	
	Map<Integer, Integer> paoPaoQuans = new HashMap<Integer, Integer>();
	private PostResult postResult;

	void GetInform(Object msg) {
		List<String> circleNames = new ArrayList<>();
		if (msg != null) {
			JSONArray datas;
			try {
				datas = new JSONObject(msg.toString()).getJSONArray("data");
				for (int i = 0; i < datas.length(); i++) {
					JSONObject data = datas.getJSONObject(i);
					String circleName = data.getString("circleName");
					int movieCircleInfoId = data.getInt("movieCircleInfoId");
					circleNames.add(circleName);
					paoPaoQuans.put(i, movieCircleInfoId);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, circleNames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			paoPaoQuan.setAdapter(adapter);
			if (circleNames.size() == 1) {
				webfun.openNewWindow(Constants.urlHost + Constants.urlSouSuo + "6", true, false, 1);
			}
		}
	}

	@Bind(R.id.upload_progress)
	RelativeLayout uploadProgress;
	@Bind(R.id.donut_progress)
	DonutProgress donutProgress;
	
	@OnClick(R.id.send)
	void Send() {
		paoPaoAudio.Content = content.getText().toString();
		if (paoPaoAudio.Content.equals("")) {
			Toast.makeText(this, "请输入内容", 1).show();
			return;
		}
		if (paoPaoAudio.VideoTime.equals("")) {
			Toast.makeText(this, "请录音后提交", 1).show();
			return;
		}

		paoPaoAudio.MovieInfoId = HelperSP.getFromSP(this, "movieID", "movieID");
		paoPaoAudio.Title = HelperSP.getFromSP(this, "movieName", "movieName");
		paoPaoAudio.UserId = HelperSP.getFromSP(this, "UserId", "UserId");
		Gson gson = new Gson();
		String message = gson.toJson(paoPaoAudio);
		Map<String, String> inform = gson.fromJson(message, HashMap.class);
		// List<NameValuePair> params = new ArrayList<NameValuePair>();
		// for (Entry<String, String> entry : inform.entrySet()) {
		// Log.e("js", "key:" + entry.getKey() + " value:" + entry.getValue());
		// params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		// }
		Log.e("js", "path:" + audioPath);
		if (paoPaoAudio.UserId == "") {
			Toast.makeText(this, "未获取到用户信息，请重新登录", 1).show();
			return;
		}

		final String path = audioPath;
		String uriAPI = "http://paody.lansum.cn/api/api/Foam/AudioFoam";

		postResult = new PostResult() {

			@Override
			public void PostCallbackResult(String result) {
				// TODO Auto-generated method stub
				Log.e("js", "接收到：" + result);
				GetResult(result);
			}
		};
		
		UploadThread thread = new UploadThread(uriAPI, inform, new String[] { path },postResult);
		thread.cookie = cookie;
		thread.start();
		thread.setUploadProgress(new UploadProgress() {
			@Override
			public void UploadCallbackResult(final float progress) {

				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						ClickFalse(progress);
						AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(SendPaoPaoAudio.this,
								R.animator.progress_anim);
						set.setInterpolator(new DecelerateInterpolator());
						set.setTarget(donutProgress);
						donutProgress.setProgress(progress);
					}
				});
			}
		});
		// StaticMethod.doPost("http://paody.lansum.cn/api/api/Foam/AudioFoam",
		// params, audioPath);
		// StaticMethod.handler = new Handler() {
		// public void handleMessage(android.os.Message msg) {
		// if (msg.what == 0) {
		// Log.e("js", "msg" + msg);
		// /** http://paody.lansum.cn/MovieHome.html?movieId=450&mao=pao */
		// GetResult(msg.obj);
		// }
		// };
		// };
	}
	void ClickFalse(float progress) {
		if (progress == 0) {
			uploadProgress.setVisibility(View.VISIBLE);
			uploadProgress.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return true;
				}
			});
		} else if (progress == 100) {
			uploadProgress.setVisibility(View.GONE);
			uploadProgress.setOnTouchListener(null);
		}
	}

	void GetResult(Object msg) {
		if (msg != null && !msg.equals("")) {
			try {
				JSONObject jsonObject = new JSONObject(msg + "");
				int state = jsonObject.getInt("state");
				/** ?movieId=450&mao=pao **/
				if (state == 1) {
					String movieId = HelperSP.getFromSP(this, "movieID", "movieID");
					this.finish();
					webfun.openNewWindow(Constants.urlPaoPaoEnd + "?movieId=" + movieId + "&mao=pao", true, false, 2);
					ToastShow("提交成功");
				} else {
					ToastShow("提交失败");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			ToastShow("提交失败");
		}
	}

	void ToastShow(final String text) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(SendPaoPaoAudio.this, text, 1).show();
			}
		});
	}

	@OnClick(R.id.pdy_bao)
	void ClickBao() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SendPaoPaoAudio.this);
		// 指定下拉列表的显示数据
		final String[] num = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		// 设置一个下拉的列表选择项
		builder.setItems(num, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pdyBao.setText("  +" + num[which]);
				paoPaoAudio.LikeInit = num[which];
			}
		});
		builder.show();
	}

	@OnClick(R.id.pdy_niao)
	void ClickNiao() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SendPaoPaoAudio.this);
		// 指定下拉列表的显示数据
		final String[] num = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		// 设置一个下拉的列表选择项
		builder.setItems(num, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pdyNiao.setText("  +" + num[which]);
				paoPaoAudio.HateInit = num[which];
			}
		});
		builder.show();
	}

	@OnClick(R.id.prompt)
	void ClickPrompt() {
		promptRela.setVisibility(View.VISIBLE);
	}

	@OnClick(R.id.close)
	void ClickClose() {
		promptRela.setVisibility(View.GONE);
	}

	@OnClick(R.id.back)
	void ClickBack() {
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		StaticMethod.doGet(Constants.urlGetPaoPao,cookie);
		StaticMethod.handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 0) {
					GetInform(msg.obj);
				}
			};
		};
		super.onResume();
	}

	@OnItemSelected(R.id.pao_pao_quan)
	void PaoPaoQuan(int position) {
		paoPaoAudio.MovieCircleInfoId = paoPaoQuans.get(position) + "";
	}

	@SuppressLint("NewApi")
	@OnTouch(R.id.audio)
	boolean AudioTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isAudio) {
				if ((this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)||
				        (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
				    //如果没有授权，则请求授权
				    this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
				}else {
				mAudioRecoderUtils.startRecord();
				audioTime.setText("00:00");
				audioTime.setVisibility(View.VISIBLE);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (isAudio) {
				if (Integer.parseInt(paoPaoAudio.VideoTime) <= 1) {
					audio.setEnabled(false);
					mAudioRecoderUtils.cancelRecord(audio);
					audioTime.setText("00:00");
					audioTime.setVisibility(View.GONE);
					Toast.makeText(this, "录制时间太短，请重新录制", 1).show();
				} else {
					mAudioRecoderUtils.stopRecord();
				}
			} else {
				AudioClick();
			}
			break;
		default:
			return false;
		}
		return true;
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int a = mediaPlayer.getCurrentPosition() / 1000;
			audioTime.setText(StaticMethod.secToTime(a));
			handler.postDelayed(this, 100);
		}
	};

	void AudioClick() {
		if (!isAudio) {
			if (mediaPlayer.isPlaying()) {
				audio.setText("点击播放");
				audio.setCompoundDrawablesWithIntrinsicBounds(null, voicePlay, null, null);
				mediaPlayer.pause();
				handler.removeCallbacks(runnable);
			} else {
				audio.setText("点击暂停");
				audio.setCompoundDrawablesWithIntrinsicBounds(null, voicePause, null, null);
				mediaPlayer.start();
				handler.post(runnable);
			}
		}
	}

	@OnClick(R.id.delete)
	void Delete() {
		isAudio = true;
		audioPath = "";
		paoPaoAudio.VideoTime = "";
		audio.setText("按住开始录音");
		audio.setCompoundDrawablesWithIntrinsicBounds(null, voiceRecode, null, null);
		delete.setVisibility(View.GONE);
		audioTime.setVisibility(View.GONE);
	}
}
