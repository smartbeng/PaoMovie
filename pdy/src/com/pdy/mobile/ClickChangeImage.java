package com.pdy.mobile;

import com.pdy.mobile.R;

import android.R.color;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClickChangeImage extends LinearLayout {

	private Drawable clickDrawable;
	private Drawable drawable;
	private int textColor;
	private int textClickColor;
	protected Boolean isImage = true;

	public ClickChangeImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomClick);
		clickDrawable = a.getDrawable(R.styleable.BottomClick_clickDrawable);
		drawable = a.getDrawable(R.styleable.BottomClick_drawable);
		textClickColor = a.getColor(R.styleable.BottomClick_textClickColor, color.white);
		textColor = a.getColor(R.styleable.BottomClick_textColor, color.white);
	}

	public void SetClickOn() {
		if (isImage) {
			((ImageView) getChildAt(0)).setImageDrawable(clickDrawable);
			((TextView) getChildAt(1)).setTextColor(textClickColor);
		}else{
			((ImageView) getChildAt(1)).setBackground(clickDrawable);
			((TextView) getChildAt(0)).setTextColor(textClickColor);
		}
	}

	public void SetClickOff() {
		if (isImage) {
			((ImageView) getChildAt(0)).setImageDrawable(drawable);
			((TextView) getChildAt(1)).setTextColor(textColor);
		}else{
			((ImageView) getChildAt(1)).setBackground(drawable);
			((TextView) getChildAt(0)).setTextColor(textColor);
		}
	}

}
