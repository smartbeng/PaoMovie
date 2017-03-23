package com.seu.magicfilter.camera;

import java.io.IOException;
import java.util.List;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.SurfaceHolder;

public class VideoEngine {
	private MediaPlayer player = null;
	private int mCameraID = 0;
	static String videoPath = "/sdcard/video4.mp4";

	public boolean isPlaying() {
		if (player != null)
			return player.isPlaying();
		return false;
	}

	public void start() {
		if (player != null)
			player.start();
	}

	public void seekTo(int msec) {
		if (player != null)
			player.seekTo(msec);
	}

	public int getCurrentPosition() {
		if (player != null)
			return player.getCurrentPosition();
		return 0;
	}

	public MediaPlayer getPlayer() {
		return player;
	}

	public void releaseCamera() {
		if (player != null) {

			player.release();
			player = null;
		}
	}

	public Boolean isPlay = true;

	public void setVideoPath(String path, int pseekto, Boolean isPlay)
			throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
		videoPath = path;
		if (player != null) {
			player.stop();
			player.reset();
			player.setDataSource(videoPath);
			player.prepareAsync();
			this.isPlay = isPlay;
			seekto = pseekto;

		}
	}

	public class MyCallBack implements SurfaceHolder.Callback {
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			player.setDisplay(holder);

		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {

		}
	}

	public int seekto = -1;

	public void startPreview(SurfaceTexture surfaceTexture) {
		if (player == null) {
			player = new MediaPlayer();

			player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					if (seekto > -1) {
						mp.seekTo(seekto);
						mp.start();
						if (!isPlay) {
							mp.pause();
						}
						seekto = -1;
					}
				}
			});
			Surface surface = new Surface(surfaceTexture);
			player.setSurface(surface);
			player.setLooping(true);
			surface.release();
			try {
				player.setDataSource(videoPath);
				player.prepareAsync();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// player.start();
		}
	}

	public int getOrientation() {

		return 0;
	}

	public boolean isFlipHorizontal() {
		return false;
	}

	public void setRotation(int rotation) {

	}

	public void pause() {
		if (player != null)
			player.pause();

	}

}
