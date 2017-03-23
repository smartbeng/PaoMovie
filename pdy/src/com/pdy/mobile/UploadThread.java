package com.pdy.mobile;

import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by DaveBobo on 2016/10/26.
 */
public class UploadThread extends Thread implements PostResult, UploadProgress {

	private String[] fileList;
	private String url;
	Map<String, String> params;
	int progress = 0;

	public void PostCallbackResult(String result) {
		result = this.result;
	}

	public void UploadCallbackResult(float progress) {
		progress = this.progress;
	}

	public String cookie;

	PostResult postResult;
	UploadProgress uploadProgress;
	// public UploadThread(String uriAPI, Map<String, String> params2, String[]
	// fileList2,PostResult postResult) {
	// this.url = uriAPI;
	// this.fileList = fileList2;
	// this.params = params2;
	// }

	public UploadThread(String uriAPI, Map<String, String> inform, String[] fileList2,
			com.pdy.mobile.PostResult postResult) {
		// TODO Auto-generated constructor stub
		this.url = uriAPI;
		this.fileList = fileList2;
		this.params = inform;
		this.postResult = postResult;
	}

	public void setUploadProgress(UploadProgress uploadProgress) {
		this.uploadProgress = uploadProgress;
	}

	Boolean isResult = false;
	String result = "";
	int lastProgress = -1;

	private void httpUpload() {
		// �ϴ����ָ��ߵ�������
		String boundary = "---------------------------7e01ee305f069a"; // Content-Type
		String reqCon = "-----------------------------7e01ee305f069a"; // �������ı�Content-Type������-
		String prefix = "--";
		String end = "\r\n";
		try {
			lastProgress = -1;
			int readSize = 0;
			URL httpUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
			if (cookie != null) {
				conn.setRequestProperty("Cookie", "UserInfo={\"userId\":" + cookie + "}");
			}
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// conn.setRequestProperty("Content-Type",
			// "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			if (params != null) {
				Iterator it = this.params.entrySet().iterator();

				while (it.hasNext()) {
					Map.Entry item = (Map.Entry) it.next();
					String name = item.getKey().toString();
					String value = item.getValue().toString();
					byte[] arr = value.getBytes("utf-8");
					out.writeBytes(prefix + boundary + end);
					out.writeBytes("Content-Disposition: form-data;" + "name=\"" + name + "\"" + end);// ʵ������
					out.writeBytes(end);
					out.write(arr);
					out.writeBytes(end);
				}
			}
			int filesSize = 0;
			for (String fileName : fileList) {
				File file = new File(fileName);
				filesSize += file.length();
			}
			Log.e("js", "filesSize:" + filesSize);
			for (String fileName : fileList) {
				String[] fList = fileName.split("\\\\");
				String fname = fList[fList.length - 1];
				String[] fList2 = fname.split("\\.");
				String fname2 = fList2[0];
				out.writeBytes(prefix + boundary + end);
				out.writeBytes(
						"Content-Disposition: form-data;" + "name=\"" + fname2 + "\";filename=\"" + fname + "\"" + end);// ʵ������
				out.writeBytes("Content-Type: application/octet-stream" + end);
				out.writeBytes(end);
				File file = new File(fileName);
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] b = new byte[1024 * 4];
				int len;
				while ((len = fileInputStream.read(b)) != -1) {
					readSize++;
					progress = readSize * 100 * 4096 / filesSize - 1;
					if (progress < 0) {
						progress = 0;
					}
					if (progress > lastProgress) {
						lastProgress = progress;
						uploadProgress.UploadCallbackResult(progress);
						Log.e("js", "progress:" + progress + " readSize:" + readSize * 100 * 4096 + " filesSize:"
								+ filesSize);
					}
					out.write(b, 0, len);
				}
				out.writeBytes(end);
			}
			uploadProgress.UploadCallbackResult(99);
			out.writeBytes(prefix + boundary + prefix + end);
			out.flush();
			InputStream inputStream = conn.getInputStream();
			uploadProgress.UploadCallbackResult(100);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			if (out != null) {
				out.close();
			}
			if (reader != null) {
				reader.close();
			}

			postResult.PostCallbackResult(sb.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		httpUpload();
	}
}