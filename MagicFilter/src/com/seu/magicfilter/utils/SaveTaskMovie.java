package com.seu.magicfilter.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.MemoryFile;
import android.util.Log;

public class SaveTaskMovie extends AsyncTask<Bitmap, Integer, String>{
	
	private onMovieSaveListener mListener;
	private Context mContext;
	private ByteArrayOutputStream mOut;
	public SaveTaskMovie(Context context, ByteArrayOutputStream out, onMovieSaveListener listener){
		this.mContext = context;
		this.mListener = listener;
		this.mOut = out;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(final String result) {
		// TODO Auto-generated method stub
		
		if (mListener != null) 
        	mListener.onSaved(result);     
		
		
	}

	@Override
	protected String doInBackground(Bitmap... params) {
		// TODO Auto-generated method stub
		
		return saveBitmap(params[0]);
	}
	
	private String saveBitmap(Bitmap bitmap) {
		
		
			
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, mOut);
			
			bitmap.recycle();
			return "";
		
		
	}
	
	public interface onMovieSaveListener{
		void onSaved(String result);
	}
}
