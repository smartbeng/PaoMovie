package com.pdy.mobile;

import com.pdy.mobile.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class WebViewSearchActivity extends Activity{

	WebView webView;
	ProgressDialog pro;
	String  url ="";
	ImageButton imagebut_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.websearch);
		
		url= getIntent().getExtras().getString("url");
		webView=(WebView) this.findViewById(R.id.webProductDetail);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);     
		imagebut_back=(ImageButton) findViewById(R.id.imagebut_back);
		imagebut_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			     finish();	
			}
		});
		pro=new ProgressDialog(this){

	            @Override
	            public boolean onKeyDown(int keyCode, KeyEvent event)
	            {
	                if (keyCode == KeyEvent.KEYCODE_BACK)
	                {
	                	webView.stopLoading(); //ֹͣ    
	                	pro.dismiss();
                        finish();
	                }
	                return super.onKeyDown(keyCode, event);               
	            }
	           
	        };
	        webView.setWebViewClient(new WebViewClient(){

	            @Override 
	            public void onPageFinished(WebView view, String url)
	            {
	                pro.dismiss();
	                super.onPageFinished(view, url);
	            }

	            @Override
	            public void onPageStarted(WebView view, String url, Bitmap favicon)
	            {
	            	pro.setMessage("正在加载...");
	                pro.show();
	                super.onPageStarted(view, url, favicon);
	            }
	           
	        });
	       
	        webView.loadUrl(url);
	        
	        
	        
	}
	

	
}
