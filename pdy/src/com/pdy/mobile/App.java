package com.pdy.mobile;

import java.util.List;

import com.pdy.mobile.R;
import com.pdy.moreWindow.MoreWindow;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.common.UmengMessageDeviceConfig;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class App extends BaseActivity {

	MoreWindow mMoreWindow;
	@Bind(R.id.bottomImageLin)
	LinearLayout bottomImageLin;
	@Bind(R.id.pao_ba_lin)
	LinearLayout paoBaLin;
	@Bind({ R.id.guan_zhu_dian_ying, R.id.guan_zhu_ren, R.id.chui_pao_pao })
	List<ClickChangeImage> paoBaChangeImage;
	@Bind(R.id.webviews)
	RelativeLayout webviewsRela;
	@Bind(R.id.pao_bang_lin)
	LinearLayout paoBangLin;
	@Bind({ R.id.bao_dian_bang, R.id.niao_dian_bang })
	List<ClickChangeImage> paoBangChangeImage;
	@Bind(R.id.top_lin)
	LinearLayout topLin;
	/** 锟捷碉拷影id **/
	int[] paoBaId = { 10000, 10001, 10002 };
	/** 锟捷帮拷id **/
	int[] paoBangId = { 20000, 20001 };

	int souSuoType = 0;
	public static Activity appActivity;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_main);
		appActivity = this;

		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		ButterKnife.bind(this);
		// 子物体的数量
		LinChildCount = bottomImageLin.getChildCount();
		NetworkInfo networkinfo = checkNetWorkinfo();
		if (networkinfo == null || !networkinfo.isAvailable()) { // 当前网络不可用
			Toast.makeText(App.this, "没有检测到可用网络,请开启网络!", 1000).show();
			Intent i = new Intent(App.this, NotNetWifi.class);
			startActivity(i);
			finish();
		} else {

			TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			if (networkinfo.getType() != ConnectivityManager.TYPE_WIFI) {
				Toast.makeText(App.this, "您当前不是wifi网络!", 1000).show();
			}
		}

		pull = true;
		loadmore = false;
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
		url = Constants.urlHost + Constants.urlIndex;

		addWebView(this, "锟斤拷锟斤拷");
		ClickPaoPao();
	}

	private void updateStatus() {
		String pkgName = getApplicationContext().getPackageName();

	}

	public NetworkInfo checkNetWorkinfo() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
		return networkinfo;
	}

	// 子控件数量
	private int LinChildCount;

	// 点击切换图片
	private void ChangeImage(int imagePosition) {
		switch (imagePosition) {
		case 3:
			paoBaLin.setVisibility(View.VISIBLE);
			paoBangLin.setVisibility(View.GONE);
			topLin.setVisibility(View.VISIBLE);
			break;
		case 1:
			paoBangLin.setVisibility(View.VISIBLE);
			paoBaLin.setVisibility(View.GONE);
			topLin.setVisibility(View.VISIBLE);
			break;
		default:
			topLin.setVisibility(View.GONE);
			paoBangLin.setVisibility(View.GONE);
			paoBaLin.setVisibility(View.GONE);
			break;
		}

		for (int i = 0; i < LinChildCount; i++) {
			if (i != (LinChildCount - 1) / 2) {
				ClickChangeImage image = ((ClickChangeImage) bottomImageLin.getChildAt(i));
				image.isImage = true;
				if (i == imagePosition) {
					image.SetClickOn();
				} else {
					image.SetClickOff();
				}
			}
		}
	}

	void ChangeText(int imagePosition) {
		ChangeText(imagePosition, 0);
	}

	private void ChangeText(int imagePosition, int type) {
		List<ClickChangeImage> texts = null;
		int[] webviewId = null;
		switch (type) {
		case 0:
			texts = paoBaChangeImage;
			break;
		case 1:
			texts = paoBangChangeImage;
			break;
		default:
			texts = paoBaChangeImage;
			break;
		}

		for (int i = 0; i < texts.size(); i++) {
			ClickChangeImage text = texts.get(i);
			text.isImage = false;
			if (i == imagePosition) {
				text.SetClickOn();
			} else {
				text.SetClickOff();
			}
		}
	}

	@OnClick(R.id.pao)
	/** 仿微博弹出按钮 **/
	void showMoreWindow(View view) {
		stopAudioAndVideo();
		if (null == mMoreWindow) {
			mMoreWindow = new MoreWindow(this);
			mMoreWindow.init();
		}

		mMoreWindow.showMoreWindow(view, 100);
	}

	@OnClick(R.id.pao_ba)
	void ClickPaoBa() {
		addWebView(this, "锟捷帮拷");
		souSuoType = 0;
		ChangeImage(3);
		webView.loadUrl(Constants.urlHost + Constants.urlGuanZhuDianYing);
		GuanZhuDianYing();
	}

	@OnClick(R.id.pao_bang)
	void ClickPaoBang() {
		ChangeImage(1);
		stopAudioAndVideo();
		addWebView(this, "锟捷帮拷");
		webView.loadUrl(Constants.urlHost + Constants.urlMovieRank);
	}

	@OnClick(R.id.wo_de)
	void ClickWoDe() {
		stopAudioAndVideo();
		addWebView(this, "LoadUserInfo");
		ChangeImage(4);
		webView.loadUrl(Constants.urlHost + Constants.urlWoDe + "?notificationName=LoadUserInfo");
	}

	@OnClick(R.id.pao_pao)
	void ClickPaoPao() {
		stopAudioAndVideo();
		addWebView(this, "锟斤拷锟斤拷");
		ChangeImage(0);
		webView.loadUrl(Constants.urlHost + Constants.urlIndex);
	}

	@OnClick(R.id.guan_zhu_dian_ying)
	void GuanZhuDianYing() {
		addWebView(this, "锟斤拷注锟斤拷影");
		souSuoType = 0;
		ChangeText(0);
		webView.loadUrl(Constants.urlHost + Constants.urlGuanZhuDianYing);
	}

	@OnClick(R.id.guan_zhu_ren)
	void guanZhuRen() {
		addWebView(this, "锟斤拷注锟斤拷");
		souSuoType = 5;
		ChangeText(1);
		webView.loadUrl(Constants.urlHost + Constants.urlGuanZhuRen);
	}

	@OnClick(R.id.chui_pao_pao)
	void ChuiPaoPao() {
		addWebView(this, "锟斤拷锟斤拷锟斤拷");
		souSuoType = 6;
		ChangeText(2);
		String userId = HelperSP.getFromSP(webfun.activity, "UserId", "UserId");
		Log.e("js","webView:"+Constants.urlHost + Constants.urlChuiPaoPao+"?userId="+userId);
		webView.loadUrl(Constants.urlHost + Constants.urlChuiPaoPao+"?userId="+userId);
	}

	@OnClick(R.id.bao_dian_bang)
	void BaoDianBang() {
		addWebView(this, "锟斤拷锟斤拷锟�");
		souSuoType = 0;
		ChangeText(0, 1);
		webView.loadUrl(Constants.urlHost + Constants.urlMovieRank + "?type=0");
	}

	@OnClick(R.id.niao_dian_bang)
	void NiaoDianBang() {
		addWebView(this, "锟斤拷锟斤拷");
		souSuoType = 0;
		ChangeText(1, 1);
		webView.loadUrl(Constants.urlHost + Constants.urlMovieRank + "?type=1");
	}

	@OnClick({ R.id.sou_suo, R.id.niao_sou_suo })
	void SouSuo() {
		webfun.openNewWindow(Constants.urlHost + Constants.urlSouSuo + souSuoType, true, true, 1, "", 0, false);
	}

	public static void PaoSouSuo(int type) {
		webfun.openNewWindow(Constants.urlHost + Constants.urlSouSuo + type, true, true, 1, "", 0, false);
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
