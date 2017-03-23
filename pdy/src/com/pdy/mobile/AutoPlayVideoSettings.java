package com.pdy.mobile;

import java.util.List;

import com.pdy.mobile.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AutoPlayVideoSettings extends Activity {
	@Bind({ R.id.wifi, R.id.network_wifi, R.id.no_wifi_network })
	List<CheckBox> checkBoxs;
	int netState = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_play_video_settings);
		ButterKnife.bind(this);
	
	}

	@OnClick(R.id.wifi)
	void Wifi() {
		ChangeState(0);
	}

	@OnClick(R.id.network_wifi)
	void NetworkWifi() {
		ChangeState(1);
	}

	@OnClick(R.id.no_wifi_network)
	void NoWifiNetwork() {
		ChangeState(2);
	}
	
	@OnClick(R.id.back)
	void Back(){
		finish();
	}
	
	@OnClick(R.id.determine)
	void Determine(){
		Intent i = new Intent();
		i.putExtra("netState", netState);
		setResult(1, i);
		finish();
	}
	

	void ChangeState(int state) {
		netState = state;
		for (int i = 0; i < checkBoxs.size(); i++) {
			if (i == state) {
				checkBoxs.get(i).setChecked(true);
			} else {
				checkBoxs.get(i).setChecked(false);
			}
		}
	}
}
