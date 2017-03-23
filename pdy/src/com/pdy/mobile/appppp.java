package com.pdy.mobile;

import java.io.File;
import java.util.Map.Entry;

import com.pdy.mobile.R;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yixia.weibo.sdk.VCamera;
import com.yixia.weibo.sdk.util.DeviceUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class appppp extends Application {

	private PushAgent mPushAgent;
	private IUmengRegisterCallback callbackPushRegister = new IUmengRegisterCallback() {

		@Override
		public void onSuccess(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFailure(String arg0, String arg1) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		StaticMethod.context = getApplicationContext();
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

			}

			@Override
			public void onActivityStarted(Activity activity) {

			}

			@Override
			public void onActivityResumed(Activity activity) {
				MyActivityManager.getInstance().setCurrentActivity(activity);
			}

			@Override
			public void onActivityPaused(Activity activity) {

			}

			@Override
			public void onActivityStopped(Activity activity) {

			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

			}

			@Override
			public void onActivityDestroyed(Activity activity) {

			}
		});
		File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (DeviceUtils.isZte()) {
			if (dcim.exists()) {
				VCamera.setVideoCachePath(dcim + "/Camera/pdy/");
			} else {
				VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/Camera/pdy/");
			}
		} else {
			VCamera.setVideoCachePath(dcim + "/Camera/pdy/");
		}
/*		// 开启log输出,ffmpeg输出到logcat
		VCamera.setDebugMode(true);
		
		// 初始化拍摄SDK，必须
		VCamera.initialize(getApplicationContext());*/
		
		
		StaticMethod.context = getApplicationContext();
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setDebugMode(false);
		mPushAgent.register(callbackPushRegister);
		UMShareAPI.get(this);
		Config.DEBUG = false;

		UmengMessageHandler messageHandler = new UmengMessageHandler() {
			/**
			 * 参考集成文档的1.6.3 http://dev.umeng.com/push/android/integration#1_6_3
			 */
			// @Override
			// public void dealWithCustomMessage(final Context context, final
			// UMessage msg) {
			// new Handler().post(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// // 对自定义消息的处理方式，点击或者忽略
			// boolean isClickOrDismissed = true;
			// if(isClickOrDismissed) {
			// //自定义消息的点击统计
			// UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
			// } else {
			// //自定义消息的忽略统计
			// UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
			// }
			// Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
			// }
			// });
			// }
			//
			// /**
			// * 参考集成文档的1.6.4
			// * http://dev.umeng.com/push/android/integration#1_6_4
			// * */
			@Override
			public Notification getNotification(Context context, UMessage msg) {
				switch (msg.builder_id) {
				case 1:
					NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
					RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
							R.layout.notification_view);
					myNotificationView.setTextViewText(R.id.notification_title, msg.title);
					myNotificationView.setTextViewText(R.id.notification_text, msg.text);
					myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
					myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
					builder.setContent(myNotificationView).setSmallIcon(getSmallIconId(context, msg))
							.setTicker(msg.ticker).setAutoCancel(true);

					return builder.build();

				default:
					// 默认为0，若填写的builder_id并不存在，也使用默认。
					return super.getNotification(context, msg);
				}
			}
		};
		// mPushAgent.setMessageHandler(messageHandler);

		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK 参考集成文档的1.6.2
		 * http://dev.umeng.com/push/android/integration#1_6_2
		 */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

			@Override
			public void launchApp(final Context context, final UMessage msg) {
				new Handler(getMainLooper()).post(new Runnable() {
					@Override
					public void run() {

						Intent intent = new Intent(context, UIWebview.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							// splashEnable 0显示 1不显示 2不做操作
						intent.putExtra("splashEnable", 1);
						intent.putExtra("animate", 2);
						intent.putExtra("topEnable", 1);
						Bundle bundle = new Bundle();
						bundle.putString("comefrom", "1");
						if (msg.extra != null) {
							for (Entry<String, String> entry : msg.extra.entrySet()) {
								String key = entry.getKey();
								String value = entry.getValue();
								if (value != null) {
									value = value.replace("http://www.98o.com/fishmobile/",
											"file:///android_asset/zs/");
									if (value.matches(".+\\?.+")) {
										value = value + "&newwin=1";
									} else {
										value = value + "?newwin=1";
									}
								}

								Log.e("Umeng...launchApp", "key  :" + key + "    value :" + value);

								bundle.putString(key, value);

							}
						}
						intent.putExtras(bundle);
						context.startActivity(intent);
					}
				});
			}
		};
		// 使用自定义的NotificationHandler，来结合友盟统计处理消息通知
		// 参考http://bbs.umeng.com/thread-11112-1-1.html
		// CustomNotificationHandler notificationClickHandler = new
		// CustomNotificationHandler();
		mPushAgent.setNotificationClickHandler(notificationClickHandler);

		// if (MiPushRegistar.checkDevice(this)) {
		// MiPushRegistar.register(this, "2882303761517400865",
		// "5501740053865");
		// }
	}

	// 各个平台的配置，建议放在全局Application或者程序入口
	{
		// 微信
		PlatformConfig.setWeixin("wx5b5ff559e1b78fa8", "7b2d8cb5b6566efe63abd93852d2d5c9");

		// 新浪微博
		// PlatformConfig.setSinaWeibo("929254273",
		// "dc82fb5d6b10dfa2621a4d5fdbd7a97c");

		// QQ
		PlatformConfig.setQQZone("1105884323", "ClKcNZreDO4GxAlu");

	}
}
