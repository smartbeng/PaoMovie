package com.pdy.mobile;

import com.pdy.mobile.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
import android.widget.ViewSwitcher.ViewFactory;

public class LoadingMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_main);
		imageSwitcher = (ImageSwitcher) findViewById(R.id.loadings);

		imageSwitcher.setFactory(new MyImageFacotry());

		imageSwitcher.setOnTouchListener(new OnTouchListener()

		{

			public boolean onTouch(View arg0, MotionEvent arg1)

			{

				if (arg1.getAction() == MotionEvent.ACTION_DOWN)

				{
					
					Log.e("js","按下");
					touchDownX = arg1.getX();

					return true;

				}

				else if (arg1.getAction() == MotionEvent.ACTION_UP)

				{
					Log.e("js","弹起");
					touchUpX = arg1.getX();

					if (touchDownX - touchUpX > 100)// 左滑

					{
						Log.e("js","左滑");
						if (index >0)

						{
							Log.e("js","index>0："+index);
							imageSwitcher.setInAnimation(getApplicationContext(), R.anim.switcher_in_right);

							imageSwitcher.setOutAnimation(getApplicationContext(), R.anim.switcher_out_right);

							imageSwitcher.setImageResource(arrayImage[index]);

							index--;

						}

					}

					else

					{

						Log.e("js","右划");
						if (index<2)

						{
							Log.e("js","index<3");
							imageSwitcher.setInAnimation(getApplicationContext(), R.anim.switcher_in_left);

							imageSwitcher.setOutAnimation(getApplicationContext(), R.anim.switcher_out_left);

							imageSwitcher.setImageResource(arrayImage[index]);

							index++;

						}

					}

					return true;

				}

				return false;

			}
		});

	}

	private ImageSwitcher imageSwitcher;

	private float touchDownX, touchUpX;

	int index = 0;

	int[] arrayImage = { R.drawable.newfeature_1, R.drawable.newfeature_2, R.drawable.newfeature_3 };

	private class MyImageFacotry implements ViewFactory

	{

		public View makeView()

		{
			
			ImageView  view = (ImageView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.loading_item, imageSwitcher,
					false);
//			imageView = (ImageView)view.findViewById(R.id.item_image_loading);
//			imageView.setImageResource(arrayImage[index]);
//			imageView.setVisibility(View.VISIBLE);
			return view;

		}

	}

}
