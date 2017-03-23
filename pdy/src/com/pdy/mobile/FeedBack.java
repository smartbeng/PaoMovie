package com.pdy.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedBack extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_feedback);
		ButterKnife.bind(this);
	}
	
	@OnClick(R.id.back)
	void Back(){
		finish();
	}
	
	@Bind(R.id.content)
	EditText content;
	@Bind(R.id.email)
	EditText email;
	
	@OnClick(R.id.submit)
	void Submit(){
		String contentSentence = content.getText().toString();
		String emailSentence = email.getText().toString();
		if(contentSentence==null||contentSentence.equals("")){
			Toast.makeText(this, "请输入您的意见再提交！！！", 1).show();
			return;
		}
		
		if(emailSentence==null){
			emailSentence="";
		}
		String url = Constants.urlSubmitFeedBack+"?Content="+contentSentence+"&Email="+emailSentence;
		finish();
//		StaticMethod.doPost(url,null);
//		StaticMethod.handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if (msg.what == 0) {
//				Log.e("js", "msg" + msg);
//				Log.e("js", "obj" + msg.obj.toString());
//				GetResult(msg.obj);
//			}
//		}
//
//		private void GetResult(Object obj) {
//			// TODO Auto-generated method stub
//			if(obj==null||obj.equals("")){
//				return;
//			}
//			
//			
//		};
//	};
	}
}
