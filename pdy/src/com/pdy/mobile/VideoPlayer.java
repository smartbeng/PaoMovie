package com.pdy.mobile;

import com.pdy.mobile.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.VideoView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPlayer extends BaseActivity {
	@Bind(R.id.videoView)
	VideoView videoView;
	@Bind(R.id.video_control)
	ImageView videoControl;
	private String videoPath;
	private int videoButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
		ButterKnife.bind(this);
		Intent i = getIntent();
		videoPath = i.getStringExtra("videoPath");
		videoButton = i.getIntExtra("videoButton",0);
		if (videoPath != null && !videoPath.equals("")) {
			videoView.setVideoPath(videoPath);
		}
	}

	@OnClick(R.id.video_control)
	void VideoControl() {
		if (videoView.isPlaying()) {
			videoControl.setImageResource(R.drawable.video_play);
			videoView.pause();
		} else {
			videoControl.setImageResource(R.drawable.video_pause);
			videoView.start();
		}
	}
	
	@OnClick(R.id.back)
	void Back(){
		finish();
	}
	
	@OnClick(R.id.xuan_zhong)
	void XuanZhong(){
		Intent i =new Intent();
		i.putExtra("videoButton", videoButton);
		setResult(2, i);
		finish();
	}
	
}
