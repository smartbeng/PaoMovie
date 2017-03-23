/*
 *HelpActivity.java
 *Classes：com.winhom.dooct.main.HelpActivity
 *wangxiaojun Create at 2013-6-6 下午4:36:37	
 */
package com.pdy.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.pdy.mobile.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Description: com.winhom.dooct.main.HelpActivity
 * 
 * @version $Revision: 1.0 $
 * @author wangxiaojun
 * @email draculawang@hotmail.com
 * @date: 2013-6-6
 * @time: 下午4:36:37
 */
public class GuideActivity extends Activity {
	/**
	 * HelpActivity
	 */
	ViewPager mViewPager;
	List<View> mListViews = new ArrayList<View>();
	MyPagerAdapter myPagerAdapter;
	int currentpage;
	int[] drawables = new int[] { R.drawable.start1, R.drawable.start2, R.drawable.start3 };
	private int flaggingWidth;// 互动翻页所需滚动的长度是当前屏幕宽度的1/3
	private GestureDetector gestureDetector; // 用户滑动

	/**
	 * @see com.winhom.dooct.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_lay);
		// if(Location.tag==0)
		{

			if (HelperSP.getFromSP(GuideActivity.this, "First_info", "First") != null) {
				Intent intent = new Intent(GuideActivity.this, LoadingActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
				return;
			}
		}

		gestureDetector = new GestureDetector(new GuideViewTouch());

		// 获取分辨率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		flaggingWidth = dm.widthPixels / 3;

		mViewPager = (ViewPager) findViewById(R.id.viewpagerGuide);
		for (int i = 0; i < drawables.length; i++) {

			View layItem = LayoutInflater.from(GuideActivity.this).inflate(R.layout.help_lay_item, null);
			ImageView iv = (ImageView) layItem.findViewById(R.id.iv_pic);
			// iv.setImageBitmap(readBitMap(this, drawables[i]));
			iv.setImageResource(drawables[i]);
			mListViews.add(layItem);

			ImageView ivindex = new ImageView(GuideActivity.this);
			ivindex.setId(i);

		}
		myPagerAdapter = new MyPagerAdapter();
		mViewPager.setAdapter(myPagerAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(final int arg0) {
				currentpage = arg0;

				if (currentpage == drawables.length - 1) {

//					Button butStart = (Button) (mListViews.get(currentpage)).findViewById(R.id.butStart);
//					butStart.setVisibility(View.VISIBLE);
//					butStart.setOnClickListener(new View.OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							GoToMainActivity();
//
//						}
//					});

				}

			}

			public void onPageScrolled(final int arg0, float arg1, int arg2) {

				Log.d("k", "onPageScrolled - " + arg0);
			}

			public void onPageScrollStateChanged(int arg0) {

			}

		});

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}

	private class GuideViewTouch extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (currentpage == 2) {
				if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())
						&& (e1.getX() - e2.getX() <= (-flaggingWidth) || e1.getX() - e2.getX() >= flaggingWidth)) {
					if (e1.getX() - e2.getX() >= flaggingWidth) {
						GoToMainActivity();
						return true;
					}
				}
			}
			return false;
		}

	}

	private void GoToMainActivity() {
		// TODO Auto-generated method stub
		HelperSP.saveToSP(GuideActivity.this, "First_info", "First", "First");
		{
			Intent intent = new Intent(GuideActivity.this, LoadingActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}

	}

	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			Log.d("k", "destroyItem");
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {

			Log.d("k", "finishUpdate");
		}

		@Override
		public int getCount() {
			Log.d("k", "getCount");
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			Log.d("k", "instantiateItem");
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			Log.d("k", "isViewFromObject");
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			Log.d("k", "restoreState");
		}

		@Override
		public Parcelable saveState() {
			Log.d("k", "saveState");
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			Log.d("k", "startUpdate");
		}
	}

	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
