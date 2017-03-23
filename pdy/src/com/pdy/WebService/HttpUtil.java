package com.pdy.WebService;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.pdy.common.StringUtil;

import android.util.Log;

/**
 * 
 * 
 * @author Yuhao
 * 
 */
public class HttpUtil {
	/**
	 * 获取文本内容的SHA1摘要值
	 * 
	 * @param params
	 * @param userPwd
	 * 
	 * @return
	 */
	public static String getSHA1(Map<String, String> params, String userPwd) {
		StringBuilder text = new StringBuilder();

		// 使用sha1加密，签名内容及顺序如下：
		// loginName+userPwd+TimeStamp+IBeaconId+IBeaconActionId+Power+BatteryLevel+CurrentDistance+IBSentTimeStamp+TickTack+IBTimeStamp
		// 变量userPwd的值：3D4F2BF07DC1BE38B20CD6E46949A1071F9D0E3D

		text.append(params.get("loginName"));
		text.append(userPwd);
		text.append(params.get("TimeStamp"));
		text.append(params.get("IBeaconId"));
		text.append(params.get("IBeaconActionId"));
		text.append(params.get("Power"));
		text.append(params.get("BatteryLevel"));
		text.append(params.get("CurrentDistance"));
		text.append(params.get("IBSentTimeStamp"));
		text.append(params.get("TickTack"));
		text.append(params.get("IBTimeStamp"));

		try {

			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			sha.update(text.toString().getBytes("UTF-8"), 0, text.toString().length());
			String ret = byte2hex(sha.digest());

			return ret;
		} catch (Exception exp) {
			exp.printStackTrace();
			return "";
		}
	}

	public static void Test(String LoginName, String UserPwd) {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://eip.lansum.com/Handler/ILansumEip.ashx?op=GetRegional");
		get.setHeader("Cookie", "UserName=" + LoginName + ";UserPwd=" + UserPwd + "");
		// HttpGet get = new
		// HttpGet("http://eip.lansum.com/Handler/ILansumEip.ashx?op=GetRegional");
		HttpResponse response;
		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is;

				is = response.getEntity().getContent();

				String result = inStream2String(is);

				JSONTokener jsonParser = new JSONTokener(result);
				// 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
				// 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
				JSONObject person = (JSONObject) jsonParser.nextValue();
				// 接下来的就是JSON对象的操作了

				int state = person.getInt("state");

				// String state = jsonObj.getString("state");
				Log.d("ws", state + "");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 将输入流转换成字符串
	private static String inStream2String(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return new String(baos.toByteArray());
	}

	/**
	 * sha1加密
	 * 
	 * @param text
	 * @return
	 */
	public static String getSHA1(String text) {

		try {

			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			sha.update(text.getBytes("UTF-8"), 0, text.length());
			String ret = byte2hex(sha.digest());

			return ret;
		} catch (Exception exp) {
			exp.printStackTrace();
			return "";
		}
	}

	/**
	 * 将字节数组换成成16进制的字符串
	 * 
	 * @param byteArray
	 * 
	 * @return string
	 */
	private static String byte2hex(byte[] b) {
		//  二行制转字符串   
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	/**
	 * 
	 * 请求远程服务并返回应答结果
	 * 
	 * @param params
	 * @param remoteServiceUrl
	 * @return
	 */
	public static HttpResult download(Map<String, String> params, String remoteServiceUrl) {
		return download(params, remoteServiceUrl, null);
	}

	public static HttpResult downloadAPI(Map<String, String> params, String remoteServiceUrl) {
		return downloadAPI(params, remoteServiceUrl, null);
	}

	public static HttpResult downloadAPI(Map<String, String> params, String remoteServiceUrl, String cookie) {
		HttpResult r = new HttpResult();

		// 请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();

		if (params != null) {
			for (String key : params.keySet()) {

				list.add(new BasicNameValuePair(key, params.get(key)));
			}
		}
		InputStream inputStream = null;
		try {
			HttpEntity entity = new UrlEncodedFormEntity(list, "utf-8");

			HttpGet post = new HttpGet(remoteServiceUrl);

			if (cookie != null) {
				post.setHeader("Cookie", cookie);
			}

			// post.setEntity(entity);

			DefaultHttpClient client = new DefaultHttpClient();

			// 发送Http请求并获取响应
			HttpResponse response = client.execute(post);

			Header[] cookies = response.getHeaders("Set-Cookie");
			if (cookies.length > 0) {
				r.cookie = cookies[0].getValue();

			}
			// String cookie= .getValue();

			if (response.getStatusLine().getStatusCode() == 200) {
				// 获取响应的消息实体
				entity = response.getEntity();

				// 获取输入流
				inputStream = entity.getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				String line = "";

				while ((line = reader.readLine()) != null) {
					r.result += line;
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return r;
	}

	public static HttpResult download(Map<String, String> params, String remoteServiceUrl, String cookie) {
		HttpResult r = new HttpResult();

		// 请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();

		if (params != null) {
			for (String key : params.keySet()) {
				list.add(new BasicNameValuePair(key, params.get(key)));
			}
		}
		InputStream inputStream = null;
		try {
			HttpEntity entity = new UrlEncodedFormEntity(list, "utf-8");
			HttpGet post = new HttpGet(remoteServiceUrl);

			if (cookie != null) {
				post.setHeader("Cookie", cookie);
			}

			// post.setEntity(entity);

			DefaultHttpClient client = new DefaultHttpClient();

			// 发送Http请求并获取响应
			HttpResponse response = client.execute(post);

			Header[] cookies = response.getHeaders("Set-Cookie");
			if (cookies.length > 0) {
				r.cookie = cookies[0].getValue();

			}
			// String cookie= .getValue();

			if (response.getStatusLine().getStatusCode() == 200) {
				// 获取响应的消息实体
				entity = response.getEntity();

				// 获取输入流
				inputStream = entity.getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				String line = "";

				while ((line = reader.readLine()) != null) {
					r.result += line;
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return r;
	}

	/**
	 * 
	 * 请求远程服务并返回应答结果
	 * 
	 * @param params
	 * @param remoteServiceUrl
	 * @return
	 */
	public static String post(Map<String, String> params, String remoteServiceUrl, String loginName, String pwd) {
		String result = "";

		// 请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();

		for (String key : params.keySet()) {
			list.add(new BasicNameValuePair(key, params.get(key)));
		}

		InputStream inputStream = null;
		try {
			HttpEntity entity = new UrlEncodedFormEntity(list, "utf-8");
			HttpPost post = new HttpPost(remoteServiceUrl);
			post.setHeader("Cookie", "UserId=" + loginName + ";UserPwd=" + pwd + "");

			post.setEntity(entity);
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
			HttpConnectionParams.setSoTimeout(httpParams, 3000);

			DefaultHttpClient client = new DefaultHttpClient(httpParams);

			// 发送Http请求并获取响应
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				// 获取响应的消息实体
				entity = response.getEntity();

				// 获取输入流
				inputStream = entity.getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				String line = "";

				while ((line = reader.readLine()) != null) {
					result += line;
				}
			}
		} catch (Exception exp) {
			result = null;
			exp.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public static String postAPI(Map<String, String> params, String remoteServiceUrl, String loginName, String pwd) {
		String result = "";

		// 请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		List<String> listStr = new ArrayList<String>();

		for (String key : params.keySet()) {
			listStr.add("\"" + key + "\":\"" + params.get(key) + "\"");
		}

		list.add(new BasicNameValuePair("", "{" + StringUtil.join(",", listStr) + "}"));

		InputStream inputStream = null;
		try {
			HttpEntity entity = new UrlEncodedFormEntity(list, "utf-8");
			HttpPost post = new HttpPost(remoteServiceUrl);
			post.setHeader("Cookie", "UserId=" + loginName + ";UserPwd=" + pwd + "");

			post.setEntity(entity);

			DefaultHttpClient client = new DefaultHttpClient();

			// 发送Http请求并获取响应
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				// 获取响应的消息实体
				entity = response.getEntity();

				// 获取输入流
				inputStream = entity.getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				String line = "";

				while ((line = reader.readLine()) != null) {
					result += line;
				}
			}
		} catch (Exception exp) {
			result = null;
			exp.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

}
