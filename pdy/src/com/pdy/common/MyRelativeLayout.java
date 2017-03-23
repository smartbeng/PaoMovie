package com.pdy.common;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by wyq on 2015/12/16.
 */
public class MyRelativeLayout extends RelativeLayout {
  //  private static final String TAG ="CustomShareBoard";
    private Context context;
    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;

    }

    public MyRelativeLayout(Context context) {
        super(context);
        this.context= context;
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context= context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) { 
        if(!isOffset()) {
          //  Log.i(TAG,"不需要偏移");
            if (this.getPaddingBottom()!=0) {
                this.setPadding(0, 0, 0, 0);
            }
        } else{
        	//Log.i(TAG, "需要偏移");
                this.setPadding(0, 0, 0, getNavigationBarHeight());
                invalidate(); 
        }
        super.onLayout(true, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private int getNavigationBarHeight() {
        Resources resources = this.context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
    public boolean isOffset() {
        return getDecorViewHeight() > getScreenHeight();
    }


    public int getDecorViewHeight(){
    	
        return ((Activity)this.context).getWindow().getDecorView().getHeight();
    }

    public int getScreenHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(dm);//当前activity
        return dm.heightPixels;
    }
    
    private InputWindowListener listener;

    



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(listener==null)
        	return;
        if (oldh > h) {
         //  Log.d("size","input window show");
            listener.show();
        } else{
        //	Log.d("size","input window hidden");
            listener.hidden();
        }
    }

    public void setListener(InputWindowListener listener) {
        this.listener = listener;
    }
}