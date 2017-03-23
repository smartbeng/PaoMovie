package com.pdy.others;

import com.pdy.mobile.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ClickChangeImage extends ImageView {

	private Drawable clickDrawable;
	private Drawable drawable;

	public ClickChangeImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.BottomClick);
		clickDrawable = a.getDrawable(R.styleable.BottomClick_clickDrawable);
		drawable = a.getDrawable(R.styleable.BottomClick_drawable);
	}
	
	public void SetClickOn(){
		setImageDrawable(clickDrawable);
	}
	public void SetClickOff(){
		setImageDrawable(drawable);
	}

}
