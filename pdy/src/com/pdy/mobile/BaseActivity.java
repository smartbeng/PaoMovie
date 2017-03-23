package com.pdy.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.pdy.mobile.R;
import com.pdy.common.MyRelativeLayout;
import com.pdy.social.Share;
import com.pdy.webview.MyWebChromeClient;
import com.pdy.webview.WebViewScrollChanged;
import com.pdy.webview.WebViewScrollChanged.ScrollInterface;
import com.umeng.message.PushAgent;
import com.umeng.socialize.UMShareAPI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;
import pl.droidsonroids.gif.GifImageView;

public class BaseActivity extends Activity {
	protected ProgressDialog mProgressDialog;

	public ProgressDialog showProgress(String title, String message) {
		return showProgress(title, message, -1);
	}

	public ProgressDialog showProgress(String title, String message, int theme) {   
		if (mProgressDialog == null) {
			if (theme > 0)
				mProgressDialog = new ProgressDialog(this, theme);
			else
				mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
			mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
		}

		if (!com.yixia.weibo.sdk.util.StringUtils.isEmpty(title))
			mProgressDialog.setTitle(title);
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
		return mProgressDialog;
	}

	public void hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
	protected WebViewScrollChanged webView;

	protected PtrClassicFrameLayout mPtrFrame;
	protected String url;

	protected boolean pull;
	protected boolean loadmore;
	private Dialog dd;
	protected long delaytime = 100;
	protected int resultCode;
	protected PushAgent mPushAgent;

	public String jsParam1;
	public String jsParam2;
	public String jsCallback;
	/** 0 webview 1 webview名字 2 上一个activity **/
	public static HashMap<Integer, List<Object>> webViews = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		this.resultCode = resultCode;
		if (resultCode == 1) {
			final String str = data.getStringExtra("url");

		} else if (resultCode == 2001) {// 登入成功
			webView.reload();
		} else if (resultCode == -10) {
			this.setResult(-9);
			this.finish();
		} else if (resultCode == -9) {

			// this.finish();
		} else if (resultCode == 2002) {// 关闭窗口

			// this.finish();
		}

		else if (resultCode == 3001) {// 隐藏动态

			webView.evaluateJavascript("" + jsCallback + "('" + jsParam1 + "','" + jsParam2 + "')", null);
		}

		else if (resultCode == 3002) {// 举报
			// webfun.openNewWindow(
			// "file:///android_asset/zs/Report.html?reporttype="
			// + jsParam1 + "&reportid=" + jsParam2 + ""
			// + "&newwin=1", false, false, 1);
			// this.finish();
		} else if (resultCode == 20000) {// setCity

			final String str = data.getStringExtra("city");
			// Log.d(tags.webview, str);
			// webView.evaluateJavascript("pdy.cb.setCity('" + str + "')",
			// null);
			// webview.loadUrl("javascript:yzc.cb.setCity('" + str + "')");
		} else if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE) {
			mywebChromeClient.onActivityResult(requestCode, resultCode, data);
		}

		else {
			UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
		}

	}

	RelativeLayout webviewsRela;

	/** 鏂板缓webview **/
	protected void addWebView(Activity activity, String webViewName) {
		webviewsRela = (RelativeLayout) activity.findViewById(R.id.webviews);
		View subItem = LayoutInflater.from(activity).inflate(R.layout.webview_item, null);
		StaticMethod.webViewId++;
		WebViewScrollChanged webViewScrollChanged = (WebViewScrollChanged) subItem.findViewById(R.id.webView);
		webViewScrollChanged.setId(StaticMethod.webViewId);
		webviewsRela.addView(subItem);
		webview(webViewScrollChanged);
		prtFrame((PtrClassicFrameLayout) subItem.findViewById(R.id.rotate_header_list_view_frame));
		List<Object> webViewInform = new ArrayList<>();
		webViewInform.add(webViewScrollChanged);
		webViewInform.add(webViewName);
		webViewInform.add(StaticMethod.lastBaseActivity);
		int webViewId = webViewScrollChanged.getId();
		Log.e("js", "web:" + webViewId);
		webViews.put(webViewId, webViewInform);
	}

	/** 绉婚櫎webview **/
	protected Boolean removeWebView(String webViewName) {
		int removeKey = 0;
		for (java.util.Map.Entry<Integer, List<Object>> infor : webViews.entrySet()) {
			if (infor.getValue() != null) {
				if (infor.getValue().get(1).equals(webViewName)) {
					removeKey = infor.getKey();
					break;
				}
			}
		}
		if (removeKey == 0) {
			return false;
		} else {
			webViews.remove(removeKey);
			return true;
		}

	}

	/** 绉婚櫎webview **/
	protected void removeLastWebView() {
		int child = webviewsRela.getChildCount();
		int id = webviewsRela.getChildAt(child - 1).getId();
		if (webViews.containsKey(id)) {
			webviewsRela.removeView((View) webViews.get(id).get(0));
			webViews.remove(id);
		}
	}

	/** 鑾峰彇webview **/
	protected WebViewScrollChanged getWebView(String webViewName) {
		for (java.util.Map.Entry<Integer, List<Object>> infor : webViews.entrySet()) {
			if (infor.getValue() != null) {
				if (infor.getValue().get(1).equals(webViewName)) {
					return ((WebViewScrollChanged) infor.getValue().get(0));
				}
			}
		}
		return null;
	}

	/** 娓呴櫎鎵�鏈墂ebview **/
	protected void RefreshWebview(String webViewName, String funcName) {
		for (java.util.Map.Entry<Integer, List<Object>> infor : webViews.entrySet()) {
			if (infor.getValue() != null) {
				if (infor.getValue().get(1).equals(webViewName)) {
					((WebViewScrollChanged) infor.getValue().get(0)).loadUrl("javascript:" + funcName);
				}
			}
		}
	}

	protected Activity GetWebViewActivity() {
		int child = webviewsRela.getChildCount();
		if (child == 0) {
			return null;
		}
		PtrClassicFrameLayout a = (PtrClassicFrameLayout) (((RelativeLayout) webviewsRela.getChildAt(child - 1)))
				.getChildAt(0);
		WebViewScrollChanged b = (WebViewScrollChanged) a.getChildAt(0);
		int id = b.getId();
		if (webViews.containsKey(id)) {
			Activity activity = (Activity) webViews.get(id).get(2);
			return activity;
		}
		return null;
	}

	/** 娓呴櫎鎵�鏈墂ebview **/
	protected void removeAllWebview() {
		webviewsRela.removeAllViews();
		webViews.clear();
	}

	protected void prtFrame(PtrClassicFrameLayout view) {

		// PtrFrameLayout.DEBUG = true;
		mPtrFrame = view;
		if (false == (pull || loadmore)) {
			return;
		}

		mPtrFrame.disableWhenHorizontalMove(true);
		mPtrFrame.setLastUpdateTimeRelateObject(this);
		mPtrFrame.setPtrHandler(new PtrHandler2() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
				if (pull == false) {
					return false;
				}
				return PtrDefaultHandler.checkContentCanBePulledDown(frame, webView, header);
			}

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				// Log.d("onRefreshBegin", "msg");
				webView.reload();
			}

			@Override
			public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
				// Log.d("checkCanDoLoadMore", "msg");
				// ;
				if (loadmore == false) {
					return false;
				}
				return PtrDefaultHandler2.checkContentCanBePulledUp(frame, webView, footer);
			}

			@SuppressLint("NewApi")
			@Override
			public void onLoadMoreBegin(final PtrFrameLayout frame) {
				webView.evaluateJavascript("lansum.getMoreData && lansum.getMoreData()", null);
				mPtrFrame.postDelayed(new Runnable() {
					@Override
					public void run() {

						frame.refreshComplete();
					}
				}, 1000);

			}
		});
		// the following are default settings
		mPtrFrame.setResistance(1.7f);
		mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
		mPtrFrame.setDurationToClose(200);
		mPtrFrame.setDurationToCloseHeader(1000);
		// default is false
		mPtrFrame.setPullToRefresh(false);
		// default is true
		mPtrFrame.setKeepHeaderWhenRefresh(true);
		mPtrFrame.postDelayed(new Runnable() {
			@Override
			public void run() {
				// mPtrFrame.autoRefresh();
			}
		}, 100);
	}

	protected Share share;

	protected void loadShare() {

		share = new Share(this);
	}

	// public LocationClient mLocationClient;
	// public MyLocationListener myListener = new MyLocationListener();
	public String setLocationStr;
	public static WebViewFun webfun;
	private MyWebChromeClient mywebChromeClient;
	protected boolean isLoadingAnimate;
	public boolean isFull;
	private MyRelativeLayout rootlayout;
	protected int heightDifference;
	protected int screenHeight;

	protected void loadLocation() {

		// mLocationClient = new LocationClient(getApplicationContext()); //
		// 澹版槑LocationClient绫�
		// myListener.activity = this;
		// mLocationClient.registerLocationListener(myListener); // 娉ㄥ唽鐩戝惉鍑芥暟
		// initLocation();

	}

	// private void initLocation() {
	// LocationClientOption option = new LocationClientOption();
	// option.setLocationMode(LocationMode.Hight_Accuracy);//
	// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
	// option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
	// int span = 1000;
	// option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
	// option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
	// option.setOpenGps(true);// 可选，默认false,设置是否使用gps
	// option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
	//
	// option.setIgnoreKillProcess(false);//
	// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
	// option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
	//
	// mLocationClient.setLocOption(option);
	// }

	int navheight = 0;
	private ImageView loadingImg;
	private int splashEnable;
	protected boolean scroolColor;

	private GifImageView loading;

	protected void stopAudioAndVideo() {
		webView.evaluateJavascript("lansum.stopAudioAndVideo()", null);
	}

	protected void setRootlayout() {
		rootlayout = (MyRelativeLayout) this.findViewById(R.id.rootlayout);
		rootlayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			/**
			 * the result is pixels
			 */
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {

				Rect r = new Rect();
				rootlayout.getWindowVisibleDisplayFrame(r);

				screenHeight = rootlayout.getRootView().getHeight();

				heightDifference = screenHeight - (r.bottom);
				if (heightDifference < 200) {
					if (heightDifference > 0) {
						navheight = r.top;
					}

					heightDifference = 0;
				} else {
					heightDifference = heightDifference - navheight;
				}
				// Log.d("size", "Keyboard Size, screenHeight:" +
				// screenHeight);
				// Log.d("size", "Keyboard Size, rootheight:" +
				// rootlayout.getHeight());
				//
				// Log.d("size", "Keyboard Size, heightDifference:" +
				// heightDifference);
				// Log.d("size", "Keyboard Size, top:" + r.top);
				// Log.d("size", "Keyboard Size, bottom:" + r.bottom);

				webView.evaluateJavascript("lansum.setFixedInput(" + screenHeight + "," + heightDifference + ")", null);

				// boolean visible = heightDiff > screenHeight / 3;
			}
		});

	}

	private int webviewImageHeight = -1;

	@SuppressLint("NewApi")
	protected void webview(WebViewScrollChanged view) {
		final ImageView topBackground = (ImageView) findViewById(R.id.topBackground);
		setRootlayout();
		loadLocation();
		loading = (GifImageView) findViewById(R.id.gif);
		// 启动界面
		loadingImg = (ImageView) findViewById(R.id.iv_loading);
		Intent intent = getIntent();
		// ILansumEip.Test(this);
		splashEnable = intent.getIntExtra("splashEnable", 1);
		// 鏄惁鏄剧ず鍚姩椤甸潰
		if (splashEnable == 0) {
			// if (isLoadingAnimate) {
			loading.setVisibility(View.GONE);
			loadingImg.setVisibility(View.VISIBLE);

			// 旋转
			// Animation hyperspaceJumpAnimation = AnimationUtils
			// .loadAnimation(this, R.anim.loading_animate);
			// loadingImg.setAnimation(hyperspaceJumpAnimation);
			// }
		} else if (splashEnable == 1) {
			// 否则不显示加载页面
			loadingImg.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
		}

		// 初始化
		webView = view;

		// dd= DialogHelper.createLoadingDialog(this, "jiazai");
		// dd.show();

		// webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		WebSettings webSettings = webView.getSettings();

		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setSupportZoom(false);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setAllowFileAccess(true);
		webSettings.setAllowContentAccess(true);
		webSettings.setAllowFileAccessFromFileURLs(true);
		webSettings.setAllowUniversalAccessFromFileURLs(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setGeolocationEnabled(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setAppCachePath(getApplicationContext().getCacheDir().getPath());
		webSettings.setDefaultTextEncodingName("gbk");
		// 屏幕自适应
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		} else {
			webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		}
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webSettings.setDisplayZoomControls(false);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			webSettings.setLoadsImagesAutomatically(true);
		} else {
			webSettings.setLoadsImagesAutomatically(false);
		}
		scroolColor = false;
		// 判断是否监听
		if (scroolColor) {
			topBackground.setAlpha(0);
		}
		// 滚动监听
		webView.setOnCustomScroolChangeListener(new ScrollInterface() {

			public void onSChanged(int l, int t, int oldl, int oldt) {
				// 判断是否监听
				if (scroolColor) {
					int iii = 0;
					if (webviewImageHeight == -1) {
						webviewImageHeight = (int) (webView.getHeight() * 0.5);
					}
					float webcontent = webView.getContentHeight() * webView.getScale();// webview鐨勯珮搴�

					float webnow = webView.getHeight() + webView.getScrollY();// 褰撳墠webview鐨勯珮搴�

					if (webView.getScrollY() > webviewImageHeight) {
						iii = (webView.getScrollY() - webviewImageHeight) + 1;
						if (iii > 255)
							iii = 255;

					} else {
						iii = 0;

					}
					topBackground.setAlpha(iii);
				}
			}
		});

		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		String title = "";
		webView.setHorizontalScrollBarEnabled(false);
		webView.setHorizontalFadingEdgeEnabled(false);
		webView.setVerticalFadingEdgeEnabled(false);
		webfun = new WebViewFun(this);

		loadShare();
		webView.addJavascriptInterface(webfun, "android");

		// 加载index.html
		// webView.loadUrl("file:///android_asset/test/test.html");

		webView.loadUrl(url);
		mywebChromeClient = new MyWebChromeClient(this) {
			private TextView topText;

			//
			@Override
			public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
				//
				AlertDialog.Builder b2 = new AlertDialog.Builder(BaseActivity.this).setTitle("鏍囬").setMessage(message)
						.setPositiveButton("ok", new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								result.confirm();

							}
						});
				//
				b2.setCancelable(false);
				b2.create();
				b2.show();
				return true;
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				if (!title.equals("") && title != null && !title.equals("鐧诲綍")) {
					topText = (TextView) findViewById(R.id.topText);
					if (topText != null) {
						if (title.length() < 10) {
							Log.i("js", "web-title1:" + title);
							topText.setText(title);
						} else
							Log.i("js", "web-title2:" + title);
					} else {
						Log.i("js", "web-title:null");
					}

				}
			}
		};

		webView.setWebChromeClient(mywebChromeClient);

		webView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 这些页面添加顶部导航
				if (url != null) {
					if (view.getUrl().matches(".*error.html.*")) {
						return false;
					}
					if (!url.matches(".*(qq.com).*|.*(qq\\?code).*|.*(weibo.cn).*|.*(sinaWeibo\\?state).*")) {
						Intent i = new Intent(BaseActivity.this, UIWebview.class);
						if (url.matches(".*(navbarstyle=hidden).*")) {
							i.putExtra("topEnable", 0);
						} else if (url.matches(".*(navbarstyle=0).*")) {
							i.putExtra("topEnable", 0);
							i.putExtra("scroolColor", true);
						} else {
							i.putExtra("topEnable", 1);
						}
						// splashEnable 0显示 1不显示 2不做操作
						i.putExtra("splashEnable", 1);
						i.putExtra("animate", 2);
						i.putExtra("url", url);
						i.putExtra("pull", true);
						i.putExtra("loadmore", false);
						StaticMethod.lastBaseActivity = BaseActivity.this;
						stopAudioAndVideo();
						BaseActivity.this.startActivity(i);
						overridePendingTransition(R.anim.slide_right_in, R.anim.none);
					}else{
						webView.loadUrl(url);
					}
					return true;
				} else {
					return false;
				}

				// 网页存储以便调用后台webview
				// System.out.println("shouldOverrideUrlLoading---->"+url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Log.d("js", "error");
				super.onReceivedError(view, errorCode, description, failingUrl);
				// 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
				String url = java.net.URLEncoder.encode(failingUrl);
				webView.loadUrl("file:///android_asset/www/error.html?failingUrl=" + url);

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (mPtrFrame != null) {
					mPtrFrame.refreshComplete();
				}

				new Handler().postDelayed(new Runnable() {
					public void run() {
						hideLoading();
						ValueCallback<String> resultCallback = new ValueCallback<String>() {

							@SuppressLint("NewApi")
							@Override
							public void onReceiveValue(String value) {
								if (value != null && value.length() > 40) {// Load
																			// more

									loadmore = true;
								}
								else{
									loadmore = false;
								}

							}
						};
						webView.evaluateJavascript("lansum.getMoreData && lansum.getMoreData.toString()", resultCallback);
					}
				}, delaytime);

				ValueCallback<String> evalCallback = new ValueCallback<String>() {

					@SuppressLint("NewApi")
					@Override
					public void onReceiveValue(String value) {
						if (value == null || value.equals("null") || value.equals("")) {
							return;
						}
						webviewImageHeight = (int) Float.parseFloat(value);
					}
				};
				// TODO Auto-generated method stub
				// float height =
				webView.evaluateJavascript("getImageHeight()", evalCallback);
			}
		});
		// webView.loadUrl(Constants.urlHost);//+"/Home/Index");
		// webView.loadUrl("http://192.168.2.105/CitySelect.aspx");
	}

	private Object indexOf(char c) {
		// TODO Auto-generated method stub
		return null;
	}

	public void hideLoading() {
		// TODO Auto-generated method stub
		loading.setVisibility(View.GONE);
		loadingImg.setVisibility(View.GONE);
		// mPtrFrame.setVisibility(View.VISIBLE);
		// dd.hide();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// webView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// webView.onPause();
	}

	public void syncCookie(String url, String value) {

		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();

		cookieManager.setCookie(url, value);

		CookieSyncManager.getInstance().sync();
	}

	public void clearCookie() {

		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();

		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
	}

}
