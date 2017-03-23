package com.pdy.camera;

import android.os.AsyncTask;

public class FFmpegAsyncTask extends AsyncTask<String, Integer, String> {

	// onPreExecute方法用于在执行后台任务前做一些UI操作
	@Override
	protected void onPreExecute() {

	}

	// doInBackground方法内部执行后台任务,不可在此方法内修改UI
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	// onProgressUpdate方法用于更新进度信息
	@Override
	protected void onProgressUpdate(Integer... progresses) {

	}

	// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
	@Override
	protected void onPostExecute(String result) {

	}

	// onCancelled方法用于在取消执行中的任务时更改UI
	@Override
	protected void onCancelled() {
		
	}
}
