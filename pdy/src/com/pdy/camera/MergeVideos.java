package com.pdy.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.coremedia.iso.IsoFile;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import android.app.ProgressDialog;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.os.Environment;

public class MergeVideos extends AsyncTask<String, Integer, String> {
	
	//The working path where the video files are located
	private String workingPath; 
	//The file names to merge
	private ArrayList<String> videosToMerge;
	static final long[] ROTATE_270 = new long[]{0, -1, 1, 0, 0, 0, 1, 0, 0};
	public String outFileName="";
	private MergeVideos(String workingPath, ArrayList<String> videosToMerge,String outFileName) {
		this.workingPath = workingPath;
		this.videosToMerge = videosToMerge;
		this.outFileName=outFileName;
	}
	
	@Override
	protected void onPreExecute() {
	
	};
	
	@Override
	protected String doInBackground(String... params) {
		int count = videosToMerge.size();
		try {
			Movie[] inMovies = new Movie[count];
			for (int i = 0; i < count; i++) {
				File file = new File(workingPath, videosToMerge.get(i));
				if(file.exists()) {
					FileInputStream fis = new FileInputStream(file);
					FileChannel fc = fis.getChannel();
					inMovies[i] = MovieCreator.build(fc);
					fis.close();
					fc.close();
				}
			}
			List<Track> videoTracks = new LinkedList<Track>();
			//List<Track> audioTracks = new LinkedList<Track>();
			
			for (Movie m : inMovies) {
				for (Track t : m.getTracks()) {
//					if (t.getHandler().equals("soun")) {
//						audioTracks.add(t);
//					}
					if (t.getHandler().equals("vide")) {
						videoTracks.add(t);
					}
					if (t.getHandler().equals("")) {
						
					}
				}
			}
			
			Movie result = new Movie();
			
//			if (audioTracks.size() > 0) {
//				result.addTrack(new AppendTrack(audioTracks
//						.toArray(new Track[audioTracks.size()])));
//			}
			if (videoTracks.size() > 0) {
				result.addTrack(new AppendTrack(videoTracks
						.toArray(new Track[videoTracks.size()])));
			}
			IsoFile out = new DefaultMp4Builder()
			.build(result);
			
			//rotate video
			
			out.getMovieBox().getMovieHeaderBox().setMatrix(ROTATE_270);
			
			long timestamp=new Date().getTime();
			String timestampS="" + timestamp;
			File storagePath = new File(workingPath);             
			storagePath.mkdirs();  
			
			File myMovie = new File(storagePath,outFileName); 
			
			FileOutputStream fos = new FileOutputStream(myMovie);
			FileChannel fco = fos.getChannel();
			fco.position(0);
			out.getBox(fco);
			fco.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String mFileName = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		mFileName += "/output.mp4";
		return mFileName;
	}
	
	/**处理结束**/
	@Override
	protected void onPostExecute(String value) {
		super.onPostExecute(value);
		
	}
	
}