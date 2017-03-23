package com.pdy.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebClient extends WebViewClient {
	public String TAG="webview";
	 private Context mContext;  
     public MyWebClient(Context context){  
         super();  
         mContext = context;  
     }  
     
     @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
    	// TODO Auto-generated method stub
    	super.onScaleChanged(view, oldScale, newScale);
    }
       
     @Override  
     public void onPageStarted(WebView view, String url, Bitmap favicon) {  
         Log.d(TAG,"URL地址:" + url);  
         super.onPageStarted(view, url, favicon);  
     }  

     @Override  
     public void onPageFinished(WebView view, String url) {  
         Log.i(TAG, "onPageFinished");  
         super.onPageFinished(view, url);  
     }
}
