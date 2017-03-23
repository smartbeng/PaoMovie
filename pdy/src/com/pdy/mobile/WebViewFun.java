package com.pdy.mobile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.sourceforge.zbar.android.CameraTest.CameraTestActivity;

import com.pdy.mobile.R;
import com.github.lzyzsd.circleprogressexample.MyActivity;
import com.pdy.WebService.HttpResult;
import com.pdy.WebService.HttpUtil;
import com.pdy.camera.CameraMain;
import com.pdy.camera.CameraMain2;
import com.pdy.camera.HoDragVideo;
import com.pdy.social.Share;
import com.pdy.webview.WebViewScrollChanged;
import com.umeng.message.UTrack.ICallBack;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class WebViewFun {
	BaseActivity activity;

	public WebViewFun(BaseActivity activity) {
		this.activity = activity;
	}

	@JavascriptInterface
	public void hh() {
		Log.d("js", "share:");
		// this.activity.share.openShare(title, content, url, imgurl);
	}

	@JavascriptInterface
	public void openShare(String title, String content, String imgurl, String url) {
		Log.d("js", "share:" + title);
		this.activity.share.openShare(title, content, url, imgurl);
	}

	String addRightBarButtonItemFromJSData = "";
	String addLeftBarButtonItemFromJSData = "";
	String setRightBarButtonItemStateFromJSData = "";
	String setNavBarTintColorFromJSData = "";
	private RelativeLayout top;

	// web调用android
	@JavascriptInterface
	public void callHandler(final String methodName, final String data, final String callbackName) {

		Log.e("js", "Method:" + methodName);
		Log.e("js", "data:" + data);
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (methodName.equals("setPasswordFromJS")) {
					HelperSP.saveToSP(activity, "UserPwd", "UserPwd", data);
					// 登录成功
				} else if (methodName.equals("loginSuccessFromJS")) {
					loginSuccessFromJS();
					// 鎵撳紑鏂扮獥鍙�
				} else if (methodName.equals("openAttendanceFromJS")) {
					openAttendanceFromJS(data);
					// 设置抬头
				} else if (methodName.equals("setTitleFromJS")) {
					setTitleFromJS(data);
					// 从右往左推出页面
				} else if (methodName.equals("pushViewControllerFromJS")) {
					pushViewControllerFromJS(data);
					// 添加右上角按钮
				} else if (methodName.equals("addRightBarButtonItemFromJS")) {
					if (!data.equals(addRightBarButtonItemFromJSData)) {
						addRightBarButtonItemFromJS(data);
						addRightBarButtonItemFromJSData = data;
						setRightBarButtonItemStateFromJSData = "";
					}
					// 从下往上弹出
				} else if (methodName.equals("presentViewControllerFromJS")) {
					presentViewControllerFromJS(data);
					// 添加左上角按钮
				} else if (methodName.equals("addLeftBarButtonItemFromJS")) {
					if (!data.equals(addLeftBarButtonItemFromJSData)) {
						addLeftBarButtonItemFromJS(data);
						addLeftBarButtonItemFromJSData = data;
					}
					// 关闭web
				} else if (methodName.equals("dismissViewControllerFromJS")) {
					dismissViewControllerFromJS();
					// 调用页面刷新
				} else if (methodName.equals("postNotificationFromJS")) {
					postNotificationFromJS(data);
				} else if (methodName.equals("popViewControllerFromJS")) {
					popViewControllerFromJS();
					// 鍏抽棴褰撳墠椤甸潰
				} else if (methodName.equals("popToRootViewControllerFromJS")) {
					popToRootViewControllerFromJS();
					// 鎵撳紑瑙嗛椤甸潰
				} else if (methodName.equals("publishVideoFromJS")) {
					publishVideoFromJS(data);
					// 鎵撳紑鍙戞场娉℃枃瀛楅〉闈�
				} else if (methodName.equals("publishTextFromJS")) {
					publishTextFromJS(data);
					// 鎵撳紑鍥剧墖缂栬緫椤甸潰
				} else if (methodName.equals("publishPictureFromJS")) {
					publishPictureFromJS(data);
					// 鎵撳紑璇煶缂栬緫椤甸潰
				} else if (methodName.equals("publishSoundFromJS")) {
					publishSoundFromJS(data);
					// 璁剧疆鍙宠竟鐨勬寜閽�
				} else if (methodName.equals("setRightBarButtonItemStateFromJS")) {
					if (!data.equals(setRightBarButtonItemStateFromJSData)) {
						setRightBarButtonItemStateFromJS(data);
						setRightBarButtonItemStateFromJSData = data;
					}
					// 鍒嗕韩椤甸潰
				} else if (methodName.equals("presentShareViewFromJS")) {
					presentShareViewFromJS();
				} else if (methodName.equals("setNavBarTintColorFromJS")) {
					if (!data.equals(setNavBarTintColorFromJSData)) {
						setNavBarTintColorFromJS(data);
						setNavBarTintColorFromJSData = data;
					}
				}  else if (methodName.equals("didPublishVideoFromJS")) {
					didPublishVideoFromJS(data);
				} else {
					Log.e("js", "Method not found:" + methodName);
				}

			}

		});

	}

	protected void didPublishVideoFromJS(String data) {
		// TODO Auto-generated method stub
		HoDragVideo.hoDragVideo.finish();
		CameraMain2.cameraMain2.finish();
		openNewWindow(data, true, false, 2);
	}

	protected void setNavBarTintColorFromJS(final String data) {
		// TODO Auto-generated method stub
		final Activity a = MyActivityManager.getInstance().getCurrentActivity();
		a.runOnUiThread(new Runnable() {
			public void run() {
				top = (RelativeLayout) a.findViewById(R.id.top1);
				if (top != null) {
					if (data.equals("white")) {
						top.setVisibility(View.GONE);
					} else {
						top.setVisibility(View.VISIBLE);
					}
				}
			}
		});

	}

	// 澹伴煶缂栬緫椤甸潰
	protected void publishSoundFromJS(String data) {
		// TODO Auto-generated method stub
		if (IsLogIn()) {
			SaveMoveInfo(data);
			activity.startActivity(new Intent(activity, SendPaoPaoAudio.class));
		} else {
			presentViewControllerFromJS(Constants.urlHost + Constants.urlLogIn);
		}
	}

	// 鍥剧墖缂栬緫椤甸潰
	protected void publishPictureFromJS(String data) {
		// TODO Auto-generated method stub
		if (IsLogIn()) {
			SaveMoveInfo(data);
			activity.startActivity(new Intent(activity, SendPaoPaoPic.class));
		} else {
			presentViewControllerFromJS(Constants.urlHost + Constants.urlLogIn);
		}
	}

	// 鍒嗕韩
	protected void presentShareViewFromJS() {
		// TODO Auto-generated method stub
		ValueCallback<String> resultCallback = new ValueCallback<String>() {

			@Override
			public void onReceiveValue(String value) {
				Log.e("js", "value:" + value);
			}
		};

	}

	// 璁剧疆鍙宠竟鐨勬寜閽�
	protected void setRightBarButtonItemStateFromJS(String data) {
		// TODO Auto-generated method stub
		int tag = 0;
		boolean selectState = false;
		try {
			JSONObject jsonObject = new JSONObject(data);
			tag = jsonObject.getInt("tag");
			selectState = jsonObject.getBoolean("selectState");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] imageInfo = selectImageNames.get(tag);
		ImageView rightButton = (ImageView) activity.findViewById(Integer.parseInt(imageInfo[2]));
		String imageName = "";
		if (selectState) {
			imageName = imageInfo[1];
		} else {
			imageName = imageInfo[0];
		}
		int imageId = activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName());
		if (imageId == 0) {
			rightButton.setVisibility(View.GONE);
		} else {
			rightButton.setImageDrawable(activity.getResources().getDrawable(imageId));
			rightButton.setVisibility(View.VISIBLE);
		}
	}

	// 鎵撳紑鍙戞场娉￠〉闈�
	protected void publishTextFromJS(String data) {
		// TODO Auto-generated method stub
		if (IsLogIn()) {
			SaveMoveInfo(data);
			activity.startActivity(new Intent(activity, SendPaoPaoTxt.class));
		} else {
			presentViewControllerFromJS(Constants.urlHost + Constants.urlLogIn);
		}
	}

	Boolean IsLogIn() {
		if (HelperSP.getFromSP(activity, "UserId", "UserId") != null)
			return true;
		return false;
	}

	void SaveMoveInfo(String data) {
		String movieID = "";
		String movieName = "";
		try {
			JSONObject jsonObject = new JSONObject(data);
			movieID = jsonObject.getString("movieID");
			movieName = jsonObject.getString("movieName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HelperSP.saveToSP(activity, "movieID", "movieID", movieID);
		HelperSP.saveToSP(activity, "movieName", "movieName", movieName);
	}

	protected void publishVideoFromJS(String data) {
		// TODO Auto-generated method stub
		if (IsLogIn()) {
			SaveMoveInfo(data);
			activity.startActivity(new Intent(activity, CameraMain2.class));
		} else {
			presentViewControllerFromJS(Constants.urlHost + Constants.urlLogIn);
		}
	}

	// 鍏抽棴褰撳墠椤甸潰
	protected void popToRootViewControllerFromJS() {
		activity.finish();
	}

	// 淇敼瀵嗙爜
	protected void popViewControllerFromJS() {
		// TODO Auto-generated method stub
		closeWindowRight();
	}

	// 调用页面刷新
	protected void postNotificationFromJS(String json) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(json);
			String notificationName = jsonObject.getString("notificationName");
			String funcName = jsonObject.getString("funcName");
			// 是否调用页面刷新
			if (notificationName != null && !notificationName.equals("") && funcName != null && !funcName.equals("")) {
				activity.RefreshWebview(notificationName, funcName);
			} else {
				Log.i("js", "notificationName鎴杅uncName涓虹┖");
			}
			loginSuccessFromJS();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 关闭
	protected void dismissViewControllerFromJS() {
		// TODO Auto-generated method stub
		closeWindowDown();
	}

	// 添加左上角按钮
	protected void addLeftBarButtonItemFromJS(String json) {
		// TODO Auto-generated method stub
		// 解析json，ui赋值，点击绑定事件
		try {
			JSONObject jsonObject = new JSONObject(json);
			final String buttonText = jsonObject.getString("title");
			final String imageName = jsonObject.getString("image").toLowerCase();
			final String JsMethod = jsonObject.getString("funcName");
			activity.runOnUiThread(new Runnable() {

				private ImageView leftButton;
				private TextView leftText;

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (buttonText != null && !buttonText.equals("")) {
						leftText = (TextView) activity.findViewById(R.id.leftText);
						leftText.setText(buttonText);
						leftText.setVisibility(View.VISIBLE);
						leftText.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								activity.webView.loadUrl("javascript:" + JsMethod);
							}
						});
					} else if (imageName != null && !imageName.equals("")) {
						leftButton = (ImageView) activity.findViewById(R.id.leftButton);
						Log.i("js", imageName);
						int imageId = activity.getResources().getIdentifier(imageName, "drawable",
								activity.getPackageName());
						leftButton.setImageDrawable(activity.getResources().getDrawable(imageId));
						leftButton.setVisibility(View.VISIBLE);
						// String
						// path=Environment.getExternalStorageDirectory()+File.separator+imageName+".png";
						// Bitmap bm = BitmapFactory.decodeFile(path);
						// rightButton.setImageBitmap(bm);
						leftButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								activity.webView.loadUrl("javascript:" + JsMethod);
							}
						});
					}
				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 从下往上弹出页面
	public void presentViewControllerFromJS(String url) {
		// TODO Auto-generated method stub
		openNewWindow(url, true, false, 1, "");
	}

	// 浠庝笅寰�涓婂脊鍑洪〉闈�
	protected void presentViewControllerFromJSNotitle(String url) {

		openNewWindow(url, true, true, 1, "");
		// TODO Auto-generated method stub
	}

	Map<Integer, String[]> selectImageNames = new HashMap<Integer, String[]>();
	int[] id = { R.id.rightButton1, R.id.rightButton2, R.id.rightButton3 };
	int j = 0;

	// 添加右上角按钮
	@SuppressLint("NewApi")
	protected void addRightBarButtonItemFromJS(String json) {
		// TODO Auto-generated method stub
		// 解析json，ui赋值，点击绑定事件
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray data = jsonObject.getJSONArray("data");
			j = 0;
			for (int i = 0; i < data.length(); i++) {
				JSONObject jsonObject2 = (JSONObject) JSONObject.wrap(data.get(i));
				final String buttonText = jsonObject2.getString("title");
				final String normalImageName = jsonObject2.getString("normalImage").toLowerCase();
				final String selectImageName = jsonObject2.getString("selectImage").toLowerCase();
				final int tag = jsonObject2.getInt("tag");
				final Boolean selectState = jsonObject2.getBoolean("selectState");
				final String JsMethod = jsonObject2.getString("funcName");
				/** 0姝ｅ父鍥�1鐐瑰嚮鍥�2鎸夐挳Id **/
				String[] imageNames = new String[3];
				imageNames[0] = normalImageName;
				imageNames[1] = selectImageName;
				imageNames[2] = id[i] + "";
				selectImageNames.put(tag, imageNames);
				activity.runOnUiThread(new Runnable() {
					private TextView rightText;

					@SuppressWarnings("deprecation")
					@Override
					public void run() {

						// TODO Auto-generated method stub
						if (!buttonText.equals("")) {
							rightText = (TextView) activity.findViewById(R.id.rightText);
							rightText.setText(buttonText);
							if (buttonText.equals("发送")) {
								rightText.setTextColor(activity.getResources().getColor(R.color.bottom_text_Color));
							}
							rightText.setVisibility(View.VISIBLE);
							rightText.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									activity.webView.loadUrl("javascript:" + JsMethod);
								}
							});
						} else if (!normalImageName.equals("") || !selectImageName.equals("")) {
							ImageView rightButton = null;
							rightButton = (ImageView) activity.findViewById(id[j]);
							String imageName = "";
							if (selectState) {
								imageName = selectImageName;
							} else {
								imageName = normalImageName;
							}
							int imageId = activity.getResources().getIdentifier(imageName, "drawable",
									activity.getPackageName());
							if (imageId == 0) {
								rightButton.setVisibility(View.GONE);
							} else {
								rightButton.setImageDrawable(activity.getResources().getDrawable(imageId));
								rightButton.setVisibility(View.VISIBLE);
							}

							j++;
							// String
							// path=Environment.getExternalStorageDirectory()+File.separator+imageName+".png";
							// Bitmap bm = BitmapFactory.decodeFile(path);
							// rightButton.setImageBitmap(bm);
							rightButton.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									activity.webView.loadUrl("javascript:" + JsMethod);
								}
							});
						}
					}
				});
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 从右往左推出页面
	public void pushViewControllerFromJS(String url) {
		if (url.equals("LXSettingViewController")) {
			activity.startActivity(new Intent(activity, Setting.class));
		} else {
			openNewWindow(url, true, false, 2, "");
		}
	}

	// 抬头
	private TextView txtTop;
	// 考勤跳转
	private ImageView add;

	private String url;

	String text = "";

	// 设置抬头
	protected void setTitleFromJS(final String title) {
		// TODO Auto-generated method stub
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				txtTop = (TextView) activity.findViewById(R.id.topText);
				// 设置顶部文字
				if (title != null && txtTop != null) {
					if (title.length() > 5) {
						text = title.substring(0, 5) + "...";
					} else {
						text = title;
					}
					if (text != title && text != title.subSequence(0, 5)) {
						txtTop.setText(text);
					}
				}
			}

		});
	}

	protected void openAttendanceFromJS(String url) {
		Log.i("js", url);
		// TODO Auto-generated method stub
		openNewWindow(url, true, true, 2, "");
	}

	@JavascriptInterface
	public void openNewWindow(final String url, final boolean pull, final boolean loadmore, final int animate) {
		openNewWindow(url, pull, loadmore, animate, "", 0, false);

	}

	@JavascriptInterface
	public void openNewWindow(final String url, final boolean pull, final boolean loadmore, final int animate,
			final String title) {
		openNewWindow(url, pull, loadmore, animate, title, 0, false);

	}

	// topEnable 0关闭顶部导航 1蓝色导航背景
	@JavascriptInterface
	public void openNewWindow(final String url, final boolean pull, final boolean loadmore, final int animate,
			final String title, final int topEnable, final Boolean scroolColor) {
		this.activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				activity.webView.clearCache(true);
				boolean pull_l = pull;
				final String currentUrl = activity.webView.getUrl();
				// if (url.equals(currentUrl)) {
				// return;
				// }
				Intent i = null;
				// if (url.matches(".+home/index.+")) {
				if (true) {
					i = new Intent(activity, UIWebview.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				}

				i.putExtra("url", url);
				i.putExtra("pull", pull_l);
				i.putExtra("loadmore", loadmore);
				i.putExtra("animate", animate);
				i.putExtra("title", title);

				i.putExtra("topEnable", topEnable);
				i.putExtra("scroolColor", scroolColor);

				activity.startActivityForResult(i, 1);// open data

				if (animate == 1) {
					activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.none);
				} else if (animate == 2) {
					activity.overridePendingTransition(R.anim.slide_right_in, R.anim.none);
				} else if (animate == 3) {

				}
			}
		});

	}

	@JavascriptInterface
	public String ajax(final String url) {
		String urlttt = null;
		if (url != null) {
			urlttt = url.replaceAll(":", "").replaceAll("/", "");
		}
		final String url2 = urlttt;
		String rrrr = HelperSP.getFromSP(activity, url2, url2);

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				final HttpResult hr = HttpUtil.download(null, url);
				if (hr != null && hr.result != null && hr.result.equals("") == false) {
					activity.runOnUiThread(new Runnable() {

						@SuppressLint("NewApi")
						@Override
						public void run() {
							String execFun = "pdy.loadRes('" + hr.result + "')";
							Log.d("js", execFun);
							activity.webView.evaluateJavascript(execFun, null);

						}

					});
					HelperSP.saveToSP(activity, url2, url2, hr.result);
				}

			}
		});

		thread.start();

		return rrrr;

	}

	@JavascriptInterface
	public void confirm(String title, String content, final String callback) {

		AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.setTitle(title);
		dialog.setMessage(content);
		dialog.setCancelable(false);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int buttonId) {
				activity.runOnUiThread(new Runnable() {

					@SuppressLint("NewApi")
					@Override
					public void run() {
						// TODO Auto-generated method stub

						activity.webView.evaluateJavascript(callback + "(true)", null);
					}

				});
			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int buttonId) {
				activity.runOnUiThread(new Runnable() {

					@SuppressLint("NewApi")
					@Override
					public void run() {
						// TODO Auto-generated method stub

						activity.webView.evaluateJavascript(callback + "(false)", null);
					}

				});

			}
		});
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.show();

	}

	@JavascriptInterface
	public void closeWindowDown() {
		Log.d(tags.webview, "closeWindow");
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.finish();
				activity.overridePendingTransition(R.anim.none, R.anim.push_bottom_out);
				activity.removeLastWebView();
				if (activity.GetWebViewActivity() != null) {
					activity = (BaseActivity) activity.GetWebViewActivity();
				}
			}
		});

	}

	@JavascriptInterface
	public void closeWindowRight() {
		Log.d("js", "closeWindow2");
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.finish();
				activity.overridePendingTransition(R.anim.none, R.anim.slide_right_out);
				activity.removeLastWebView();
				if (activity.GetWebViewActivity() != null) {
					activity = (BaseActivity) activity.GetWebViewActivity();
				}
			}
		});

	}

	@JavascriptInterface
	public void presentLoginFromJS() {
		Log.d(tags.webview, "presentLoginFromJS");
		this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String url = null;
				String str = activity.webView.getUrl();
				if (str.indexOf("file:") == 0) {
					url = "file:///android_asset/zs/form/phoneLogin.html?returnUrl=../friendCircle/friendCircle.html";
				} else {

					url = "http://devftp.lansum.com/fashhtml/form/phoneLogin.html?returnUrl=../friendCircle/friendCircle.html";
				}
				Intent i = new Intent(activity, UIWebview.class);
				i.putExtra("url", url);
				i.putExtra("pull", true);
				i.putExtra("loadmore", false);
				i.putExtra("animate", 1);
				i.putExtra("title", "");
				i.putExtra("topEnable", 0);
				// this.activity.startActivityForResult(i, 0001);// open data
				// activity.startActivityForResult(i, 2);// refresh data

				// activity.overridePendingTransition(R.anim.push_up_in,
				// R.anim.push_up_out);
			}
		});

	}

	public void logoutFromJS(final String userid) {
		Log.d(tags.webview, "logoutFromJS");
		this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					activity.mPushAgent.removeAlias(userid, "LXMessageAliasTypePraise", null);
					activity.mPushAgent.removeAlias(userid, "LXMessageAliasTypeComment", null);
					activity.mPushAgent.removeAlias(userid, "LXMessageAliasTypeSystem", null);

					activity.clearCookie();

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

	@JavascriptInterface
	public void switchPraise(final int isRemove, final String userid) {
		Log.d(tags.webview, "switchPraise");
		this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (isRemove == 1) {
						activity.mPushAgent.removeAlias(userid, "LXMessageAliasTypePraise", null);
					} else {

						activity.mPushAgent.addExclusiveAlias(userid, "LXMessageAliasTypePraise", null);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

	@JavascriptInterface
	public void switchComment(final int isRemove, final String userid) {
		Log.d(tags.webview, "switchComment");
		this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (isRemove == 1) {
						activity.mPushAgent.removeAlias(userid, "LXMessageAliasTypeComment", null);
					} else {

						activity.mPushAgent.addExclusiveAlias(userid, "LXMessageAliasTypeComment", null);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

	@JavascriptInterface
	public void switchSystem(final int isRemove, final String userid) {
		Log.d(tags.webview, "switchSystem");
		this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (isRemove == 1) {
						activity.mPushAgent.removeAlias(userid, "LXMessageAliasTypeSystem", null);
					} else {

						activity.mPushAgent.addExclusiveAlias(userid, "LXMessageAliasTypeSystem", null);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

	@JavascriptInterface
	public void switchFilmNews(final int isRemove, final String userid) {
		Log.d(tags.webview, "switchFilmNews");
		this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (isRemove == 1) {
						activity.mPushAgent.removeAlias(userid, "LXMessageAliasTypeFilmNews", null);
					} else {

						activity.mPushAgent.addExclusiveAlias(userid, "LXMessageAliasTypeFilmNews", null);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

	@JavascriptInterface
	public void loginCloseFromJS() {
		Log.d(tags.webview, "loginCloseFromJS");

		this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				activity.setResult(2002);
				activity.finish();
			}
		});

	}

	@JavascriptInterface
	public void setCity(final String txt) {
		this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent ii = new Intent();
				ii.putExtra("city", txt);
				activity.setResult(20000, ii);
				activity.finish();
			}
		});
	}

	@JavascriptInterface
	public void setMobileCookie(String filename, String values) {
		HelperSP.saveToSP(activity, filename, filename, values);
	}

	@JavascriptInterface
	public void removeCookie(String filename) {
		HelperSP.deleteToSP(activity, filename);
	}

	@JavascriptInterface
	public void clearCache() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(activity.getApplicationContext(), "清除缓存成功", Toast.LENGTH_SHORT).show();
			}

		});
	}

	/*
	 * 获取cookie 传给页面
	 */
	@JavascriptInterface
	public String getMobileCookie(String filename) {
		return HelperSP.getFromSP(activity, filename, filename);

	}

	@SuppressLint("NewApi")
	@JavascriptInterface
	public String setTest(String filename) {
		activity.webView.evaluateJavascript("API.getTest('getTest222')", null);
		return "callback-" + filename;

	}

	@JavascriptInterface
	public int getWidth() {
		WindowManager wm = activity.getWindowManager();

		int width = wm.getDefaultDisplay().getWidth();
		return width;

	}

	@JavascriptInterface
	public int getHeight() {
		WindowManager wm = activity.getWindowManager();

		int height = wm.getDefaultDisplay().getHeight();
		return height;

	}

	@JavascriptInterface
	public void hideLoading() {
		activity.hideLoading();

	}

	// @JavascriptInterface
	// public void mobileGetLocation() {
	// activity.setLocationStr = "yzc.cb.setLocationAndroid";
	// activity.mLocationClient.start();
	//
	// }
	//
	// @JavascriptInterface
	// public void mobileGetLocationHome() {
	// activity.setLocationStr = "home-getCity";
	// activity.mLocationClient.start();
	//
	// }
	//
	// @JavascriptInterface
	// public void mobileGetLocationCity() {
	// activity.setLocationStr = "city-getCity";
	// activity.mLocationClient.start();
	//
	// }

	private void OpenNewWindow(String url) {
		if (!TextUtils.isEmpty(url)) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(url);
			intent.setData(content_url);
			activity.startActivity(intent);
		} else {
			Toast.makeText(activity, "URL地址为空!", 1000).show();
		}
	}

	@JavascriptInterface
	public void HideLoading() {
		Log.d("js", "hideloading");
		// relative_loading.setVisibility(View.GONE);
	}

	/*
	 * 拨打电话方法
	 */
	@JavascriptInterface
	public void mobileCall(final String phone) {

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
				activity.startActivity(intent2);
			}
		});

	}

	/**
	 * 调用相机
	 */
	@JavascriptInterface
	public void camraZbar() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				Intent intent1 = new Intent(activity, CameraTestActivity.class);
				activity.startActivityForResult(intent1, 0);

			}
		});

	}

	/**
	 * 定位成功后 返回给页面
	 */
	@JavascriptInterface
	public void setLocation(final String str) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				activity.webView.loadUrl("javascript:setLocation('" + str + "')");

			}
		});
	}

	/**
	 * 扫描结果传个网页
	 */
	@JavascriptInterface
	public void setCamraZbar(final String str) {

		handler.post(new Runnable() {
			public void run() {
				activity.webView = (WebViewScrollChanged) activity.findViewById(R.id.webView);
				activity.webView.loadUrl("javascript:setCamraZbar('" + str + "')");
			}
		});

	}

	/**
	 * 获取经纬度集合
	 * 
	 * @param str
	 */
	@JavascriptInterface
	public void locationList(final String str) {

		if (TextUtils.isEmpty(str)) {
			Toast.makeText(activity, "鏆傛棤鏁版嵁!", 1000).show();
			return;
		}
		// startActivity(new Intent(activity, OverlayDemo.class)
		// .putExtra("locationStr", str));

		// runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// startActivity(new
		// Intent(activity,OverlayDemo.class).putExtra("locationStr",
		// str));
		// //Toast.makeText(htmlUIActivity, str+"", 1000).show();
		// //System.out.println("str----------"+str);
		// }
		// });
	}

	@JavascriptInterface
	public void resultLat(String str) {

		if (str.indexOf("E") != -1) {
			// Toast.makeText(htmlUIActivity, str+"eeeeeeeee", 1000).show();
		} else {
			System.out.println("location-------->" + str);
			HelperSP.saveToSP(activity, "Location_Info", "Location_lat", str.substring(0, str.indexOf(",")));
			HelperSP.saveToSP(activity, "Location_Info", "Location_lng",
					str.substring(str.indexOf(",") + 1, str.length()));
			// dialog.dismiss();
			// mLocClient.stop();
			setLocation(str);
		}

	}

	String userCookieName = "UserInfo";
	private ICallBack callbackPush = new ICallBack() {

		@Override
		public void onMessage(boolean arg0, String arg1) {
			Log.d("js", "arg0:" + arg0 + " arg1:" + arg1);

		}
	};

	@JavascriptInterface
	public void loginSuccessFromJS() {
		Log.d(tags.webview, "loginSuccessFromJS");
		handler.post(new Runnable() {

			@Override
			public void run() {
				Log.d("js", "loginSuccess");
				CookieManager cookieManager = CookieManager.getInstance();
				String CookieStr = cookieManager.getCookie(Constants.urlHost);
				try {
					String cookieDecode = URLDecoder.decode(CookieStr, "UTF-8");
					HelperSP.saveToSP(activity, "cookie", "cookie", cookieDecode);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String cookie = HelperSP.getFromSP(activity, "cookie", "cookie");
				Log.e("js", "cookie:" + cookie);
				// 从cookie中获取userID
				final String UserId = GetCookieParamInt(CookieStr, "userId");
				String filename = userCookieName;

				// 保存字段
				HelperSP.saveToSP(activity, "UserId", "UserId", UserId);

				try {
					// 添加Alias
					activity.mPushAgent.addExclusiveAlias(UserId, "LXMessageAliasTypePraise", callbackPush);
					activity.mPushAgent.addExclusiveAlias(UserId, "LXMessageAliasTypeComment", callbackPush);
					activity.mPushAgent.addExclusiveAlias(UserId, "LXMessageAliasTypeSystem", callbackPush);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("js", "loginSuccess end" + UserId);
			}
		});
	}

	private String GetCookieParam(String CookieStr, String key) {
		Pattern p = Pattern.compile(key + "=([^;]+)", Pattern.MULTILINE);
		Matcher m = p.matcher(CookieStr);
		if (m.find()) {

			String UserName = m.group(1);
			UserName = UserName.replaceAll("%40", "@");
			return UserName;
		} else {

			return "";
		}

	}

	public String GetCookieParamInt(String CookieStr, String key) {
		String urlDeCode = null;
		try {
			urlDeCode = URLDecoder.decode(CookieStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return "";
		}

		Pattern p = Pattern.compile(key + "\"\\:(\\d+)", Pattern.MULTILINE);
		Matcher m = p.matcher(urlDeCode);
		if (m.find()) {

			String UserName = m.group(1);
			UserName = UserName.replaceAll("%40", "@");
			return UserName;
		} else {

			return "";
		}

	}

	@JavascriptInterface
	public void LogOut(String values) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Log.d("js", "LoginOut");
				String filename = userCookieName;
				HelperSP.deleteToSP(activity, "UserInfo");
			}
		});

	}

	@JavascriptInterface
	public void debugTestCallback(final String values) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Log.d("js", "debugTestCallback:" + values);
				// webView.loadUrl("javascript:debugTestCallback('" + values +
				// "')");

			}
		});

	}

	/*
	 * 保存cookie
	 */
	/*
	 * @JavascriptInterface public void setMobileCookie(String filename, String
	 * values) { HelperSP.saveToSP(activity, filename, filename, values); }
	 */

	/*
	 * 获取cookie 传给页面
	 */
	@JavascriptInterface
	public void getMobileCookie(String filename, String callbackFunName) {
		activity.webView.loadUrl("javascript:setMobileCookieText('" + filename + "' ,'" + callbackFunName + "' ,'"
				+ HelperSP.getFromSP(activity, filename, filename) + "' )");
	}

	@JavascriptInterface
	public void showGuide() {

		Intent i = new Intent(activity, GuideActivity.class);
		activity.startActivity(i);

	}

	public void setUserCookie(String cookie) {
		CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(activity);
		cookieSyncManager.sync();
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setCookie(Constants.urlHost, cookie);
		CookieSyncManager.getInstance().sync();

	}

	public static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

		}

	};

}
