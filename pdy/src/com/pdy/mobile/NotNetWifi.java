package com.pdy.mobile;

import com.pdy.mobile.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class NotNetWifi extends BaseActivity{
	private ImageView netButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.not_net_wifi);
		netButton=(ImageView) findViewById(R.id.netButton);
		netButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i=new Intent(NotNetWifi.this,App.class);
				startActivity(i);
				finish();
			}
		});
	}
}
