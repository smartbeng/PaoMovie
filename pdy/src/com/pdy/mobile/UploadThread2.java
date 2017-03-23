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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
public class UploadThread2 extends Thread {

	private String[] fileList;
	private String url;
	Map<String, String> params;

	public String cookie;

	public UploadThread2(String uriAPI, Map<String, String> params2, String[] fileList2) {
		this.url = uriAPI;
		this.fileList = fileList2;
		this.params = params2;
	}

	private void httpUpload() {
		// �ϴ����ָ��ߵ�������
		String boundary = "---------------------------7e01ee305f069a"; // Content-Type
		String reqCon = "-----------------------------7e01ee305f069a"; // �������ı�Content-Type������-
		String prefix = "--";
		String end = "\r\n";
		try {
			URL httpUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
			if (cookie != null) {
				conn.setRequestProperty("Cookie", cookie);
			}
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			Iterator it = this.params.entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry item = (Map.Entry) it.next();
				String name = item.getKey().toString();
				String value = item.getValue().toString();

				out.writeBytes(prefix + boundary + end);
				out.writeBytes("Content-Disposition: form-data;" + "name=\"" + name + "\"" + end);// ʵ������
				out.writeBytes(end);
				out.writeBytes(value);
				out.writeBytes(end);
			}
			for (String fileName : fileList) {
				String[] fList = fileName.split("\\\\");
				String fname = fList[fList.length - 1];
				String[] fList2 = fname.split("\\.");
				String fname2 = fList2[0];
				out.writeBytes(prefix + boundary + end);
				out.writeBytes(
						"Content-Disposition: form-data;" + "name=\"" + fname2 + "\";filename=\"" + fname + "\"" + end);// ʵ������
				out.writeBytes(end);
				FileInputStream fileInputStream = new FileInputStream(new File(fileName));
				byte[] b = new byte[1024 * 4];
				int len;
				while ((len = fileInputStream.read(b)) != -1) {
					out.write(b, 0, len);
				}
				out.writeBytes(end);

			}

			out.writeBytes(prefix + boundary + prefix + end);
			out.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
				Log.e("js","respose:" + sb.toString());
				if (out != null) {
					out.close();
				}
				if (reader != null) {
					reader.close();
				}
			}
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