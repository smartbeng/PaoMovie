package com.pdy.others;

import com.pdy.camera.TrimmVideo;
import com.pdy.information.VideoInfo;
import com.pdy.mobile.StaticMethod;

import android.os.Handler;

public class Null {
/**
 * 				i=0;
				Handler handler = new Handler();
 *
 * 
 * 
				Runnable ffmpegRun = new Runnable() {
					ffmpeg -i a.mp4 -filter_complex
 "colorchannelmixer=.393:.769:.189:0:.349:.686:.168:0:.272:.534:.131[a];mov
ie=a.png[b];[a][b]overlay=x=0:y=0" out.mp4
					@Override
					public void run() {
						VideoInfo vInfo = StaticMethod.videosInfo.get(i);
						// TODO Auto-generated method stub
						trimmVideo = new TrimmVideo(vInfo.videoPath, vInfo.startVideoTime/1000, (vInfo.endVideoTime-vInfo.startVideoTime)/1000+1,"video"+i+".mp4");
						String cmdInput = "ffmpeg -d stdout -loglevel verbose -y -i "+ StaticMethod.GetFilePath()+"video"+i+".mp4";
						String cmdSize = " -s " + recordLayout.getWidth() + "x" + recordLayout.getHeight();
						String cmdFilter = "";
						
						if (vInfo.videoFilter != 0) {
							cmdFilter = StaticMethod.filter1;
						}
						
					}
				};
 				String cmd ="";
//				cmd = "ffmpeg -d stdout -loglevel verbose -i /storage/emulated/0/a.mp4 -pix_fmt nv21 /storage/emulated/0/a.yuv";
				cmd = "ffmpeg -d stdout -loglevel verbose -i /storage/emulated/0/a.mp4 -r 25 /storage/emulated/0/a.yuv";
				cmd = "ffmpeg -d stdout -loglevel verbose -video_size 480x480 -i /storage/emulated/0/a.yuv /storage/emulated/0/outa.mp4";
//				cmd = "ffmpeg -d stdout -loglevel verbose -video_size 480x480 -i /storage/emulated/0/a.yuv -pix_fmt nv21 /storage/emulated/0/outa.mp4";
				cmd = "ffmpeg -d stdout -loglevel verbose -i /storage/emulated/0/aa.mp4 -vf colorchannelmixer=.3:.4:.3:0:.3:.4:.3:0:.3:.4:.3 /storage/emulated/0/outb.mp4";
				com.yixia.camera.UtilityAdapter.FFmpegRun("", cmd);
//				String cmd = "";
//				String cmd0 = "ffmpeg -d stdout -loglevel verbose";
//				for (int i = 0; i < StaticMethod.videosInfo.size(); i++) {
//					VideoInfo vInfo = StaticMethod.videosInfo.get(i);
//					trimmVideo = new TrimmVideo(vInfo.videoPath, vInfo.startVideoTime/1000, (vInfo.endVideoTime-vInfo.startVideoTime)/1000+1,"video"+i+".mp4");
//					String cmd1 = " -i " + StaticMethod.GetFilePath()+"video"+i+".mp4";
//					String cmd2 = "";
////					if (vInfo.videoFilter != 0) {
//						cmd2 = StaticMethod.filter1;
////					}
//					String cmd3 = " -s " + recordLayout.getWidth() + "x" + recordLayout.getHeight();
//					String cmd4 = "";
//					//if (vInfo.videoText != null)
//						cmd4 =" -i "+ StaticMethod.viewSaveImage(vInfo.videoText);
//					String cmd5 = " " + StaticMethod.GetFilePath() + "video1" + i + ".mp4";
//					cmd = cmd0 + cmd1 + cmd2 + cmd5;
//					int result = com.yixia.camera.UtilityAdapter.FFmpegRun("1", cmd);
//					Log.i("js", "result1:" + result);
////					cmd=cmd0+cmd5+cmd4+ " " + StaticMethod.GetFilePath() + "video2" + i + ".mp4";
////					result = com.yixia.camera.UtilityAdapter.FFmpegRun("", cmd);
////					Log.i("js", "result2:" + result);
//				}	

//					
//				}
				// cmd = "ffmpeg -d stdout -loglevel verbose -i
				// /sdcard/aaabbb.mp4 -i "
				// + " -filter_complex 'overlay=x=0:y=0'/sdcard/bbbaaa.mp4";
				// int result = com.yixia.camera.UtilityAdapter.FFmpegRun("",
				// cmd);
				// Log.i("js", "result2:" + result);
				//
			
  **/
}
