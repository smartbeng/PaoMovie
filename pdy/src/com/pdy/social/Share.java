package com.pdy.social;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.pdy.mobile.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;

public class Share {

	public Share(BaseActivity act) {
		this.act = act;

	}

	Activity act;
	public ShareAction shareAction;

	public void openShare(final String title, final String content, final String url, final String img) {
		
		act.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String targeturl = url.replace("file:///android_asset/zs/", "http://www.98o.com/fishmobile/");
				shareAction = new ShareAction(act);
				shareAction.setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
						
						SHARE_MEDIA.QQ,
						SHARE_MEDIA.QZONE
						//, SHARE_MEDIA.SINA
						)
				.withTitle(title).withText(content).withMedia(new UMImage(act, img)).withTargetUrl(targeturl)
				.setCallback(umShareListener);
				shareAction.open();
//				new ShareAction(act)
//						.setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//								
//								SHARE_MEDIA.QQ,
//								SHARE_MEDIA.QZONE
//								
//								//, SHARE_MEDIA.SINA
//								)
//						.withTitle(title).withText(content).withMedia(new UMImage(act, img)).withTargetUrl(targeturl)
//						.setCallback(umShareListener).open();

				String[] mPermissionList = new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE,
						Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP,
						Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS };
			}
			
		});
		
		// ActivityCompat.requestPermissions(act,mPermissionList, 100);

	}
	
	

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Log.d("plat", "platform" + platform);
			if (platform.name().equals("WEIXIN_FAVORITE")) {
				Toast.makeText(act, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(act, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
			}
		}
		
		

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(act, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
			if (t != null) {
				Log.d("throw", "throw:" + t.getMessage());
			}
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(act, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		}
	};

}
