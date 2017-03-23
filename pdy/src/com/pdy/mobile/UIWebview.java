package com.pdy.mobile;

import com.pdy.mobile.R;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.UMShareAPI;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UIWebview extends BaseActivity {

	private TextView txtTop;
	private String title;
	private int topEnable;
	private RelativeLayout top;
	private int animate;
	private ImageView leftButton;
	private String funcName;
	private String notificationName;
	private int splashEnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uiwebview);

		txtTop = (TextView) findViewById(R.id.topText);
		top = (RelativeLayout) findViewById(R.id.top);

		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

		isLoadingAnimate = true;
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable(null);
		IUmengRegisterCallback callbackPushRegister = new IUmengRegisterCallback() {

			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(String arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		};
		mPushAgent.register(callbackPushRegister);
		mPushAgent.onAppStart();
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		title = intent.getStringExtra("title");
		animate = intent.getIntExtra("animate", 0);
		topEnable = intent.getIntExtra("topEnable", 0);
		funcName = intent.getStringExtra("funcName");
		splashEnable = intent.getIntExtra("splashEnable", 1);
		notificationName = intent.getStringExtra("notificationName");
		scroolColor = intent.getBooleanExtra("scroolColor", false);
		Log.i("js", "funcName:" + funcName + "notificationName:" + notificationName);

		if(url.matches(".*Report.*")){
			topEnable = 1;
		}else if(url.matches(".*MovieSearch.*")){
			findViewById(R.id.top_background).setVisibility(View.GONE);
			findViewById(R.id.top1).setVisibility(View.VISIBLE);
			
		}
		// 是否显示顶部导航
		if (topEnable == 1) {
			top.setVisibility(View.VISIBLE);
		} else if (topEnable == 0) {
			top.setVisibility(View.GONE);
		}
		
		// 判断是否向左打开页面
		if (animate == 2) {
			leftButton = (ImageView) findViewById(R.id.leftButton);
			leftButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_l));
			leftButton.setVisibility(View.VISIBLE);
			leftButton.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					stopAudioAndVideo();
					overridePendingTransition(R.anim.none, R.anim.push_bottom_out);
					finish();
					removeLastWebView();
				}
			});
		} else if (animate == 3) {
			leftButton = (ImageView) findViewById(R.id.leftButton);
			leftButton.setImageDrawable(getResources().getDrawable(R.drawable.navbar_close));
			leftButton.setVisibility(View.VISIBLE);
			leftButton.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					overridePendingTransition(R.anim.none, R.anim.push_bottom_out);
					finish();
					removeLastWebView();
				}
			});
		}
		pull = intent.getBooleanExtra("pull", true);
		loadmore = intent.getBooleanExtra("loadmore", false);

		if (url == null) {
			url = "file:///android_asset/zs/test/testcanvas.html";
			url = "file:///android_asset/zs/ticket.html";
			url = "http://192.168.2.61/home/index.html?navbarstyle=1";
			url = "file:///android_asset/zs/home/index.html?navbarstyle=1";
		}

		// 设置顶部文字
		if (title != null && txtTop != null && !title.equals("")) {
			Log.i("js", "title1:" + title);
			txtTop.setText(title);
		}
		String urlName = "";
		try{
			urlName= url.substring(url.lastIndexOf('/') + 1);
			urlName = urlName.substring(0,url.indexOf(".")-2);
		}catch (Exception e) {
			// TODO: handle exception
		}
		addWebView(this, urlName);
		webView.loadUrl(url);
	}

	// Activity的启动模式(launchMode),通过这个方法接受Intent
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	// @Override
	// public void finish() {
	// moveTaskToBack(true);// true对任何Activity都适用
	//
	// }
	//

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			stopAudioAndVideo();

			CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(UIWebview.this);
			cookieSyncManager.sync();
			CookieManager cookieManager = CookieManager.getInstance();
			if (cookieManager.getCookie(Constants.urlHost) == null && url != null && url.matches(".+login")) {
				finish();
				System.exit(0);
				return true;
			}
			if (isFull) {
				return super.onKeyDown(keyCode, event);
			}
			String currentUrl = webView.getUrl();
			Log.d("currentUrl", currentUrl);
			if (currentUrl.matches(".+newwin=1.*|.+error.html.+") || currentUrl.matches("^http.+")) {
				Log.d("currentUrl", "false");
				this.setResult(2002);
				this.finish();
				if (animate == 1) {
					overridePendingTransition(R.anim.none, R.anim.push_bottom_out);
				} else if (animate == 2) {
					overridePendingTransition(R.anim.none, R.anim.slide_right_out);
				}
				return true;
			}

			else {
				Log.d("currentUrl", "true");
				this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						webView.evaluateJavascript("history.go(-1)", null);
					}

				});

				return false;
			}
		} else {

			return super.onKeyDown(keyCode, event);
		}
	}

}
