package com.pdy.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.pdy.mobile.StaticMethod.PaoPaoText;
import com.pdy.mobile.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

public class SendPaoPaoTxt extends BaseActivity {

	@Bind(R.id.content)
	EditText content;
	@Bind(R.id.title)
	TextView titleView;
	@Bind(R.id.pdy_bao)
	TextView pdyBao;
	@Bind(R.id.pdy_niao)
	TextView pdyNiao;
	@Bind(R.id.prompt_rela)
	RelativeLayout promptRela;
	@Bind(R.id.pao_pao_quan)
	Spinner paoPaoQuan;
	Boolean isSetAdapter = false;
	private ArrayAdapter<String> adapter;

	PaoPaoText paoPaoText = null;
	private String cookie = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_pao_pao_txt);
		ButterKnife.bind(this);
		String title = HelperSP.getFromSP(this, "movieName", "movieName");
		titleView.setText(title);
		paoPaoText = new PaoPaoText();
		cookie   = HelperSP.getFromSP(this, "UserId", "UserId");
	}

	Map<Integer, Integer> paoPaoQuans = new HashMap<Integer, Integer>();

	void GetInform(Object msg) {
		List<String> circleNames = new ArrayList<>();
		if (msg != null) {
			JSONArray datas;
			try {
				datas = new JSONObject(msg.toString()).getJSONArray("data");
				for (int i = 0; i < datas.length(); i++) {
					JSONObject data = datas.getJSONObject(i);
					String circleName = data.getString("circleName");
					int movieCircleInfoId = data.getInt("movieCircleInfoId");
					circleNames.add(circleName);
					paoPaoQuans.put(i, movieCircleInfoId);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, circleNames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			paoPaoQuan.setAdapter(adapter);
			if (circleNames.size() == 1) {
				webfun.openNewWindow(Constants.urlHost + Constants.urlSouSuo + "6", true, false, 1);
			}
		}
	}

	@OnClick(R.id.send)
	void Send() {
		paoPaoText.Content = content.getText().toString();
		if (paoPaoText.Content.equals("")) {
			Toast.makeText(this, "请输入内容", 1).show();
			return;
		}
		paoPaoText.MovieInfoId = HelperSP.getFromSP(this, "movieID", "movieID");
		paoPaoText.Title = HelperSP.getFromSP(this, "movieName", "movieName");
		paoPaoText.UserId = HelperSP.getFromSP(this, "UserId", "UserId");
		Gson gson = new Gson();
		String message = gson.toJson(paoPaoText);
		Map<String, String> inform = gson.fromJson(message, HashMap.class);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : inform.entrySet()) {
			Log.e("js", "key:" + entry.getKey() + " value:" + entry.getValue());
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		if (paoPaoText.UserId == "") {
			Toast.makeText(this, "未获取到用户信息，请重新登录", 1).show();
			return;
		}
		StaticMethod.doPost("http://paody.lansum.cn/api/api/Foam/TextFoam", params,cookie);
		StaticMethod.handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 0) {
					Log.e("js", "msg" + msg);
					/** http://paody.lansum.cn/MovieHome.html?movieId=450&mao=pao */
					GetResult(msg.obj);
				}
			};
		};
	}

	void GetResult(Object msg) {
		if (msg != null) {
			try {
				JSONObject jsonObject = new JSONObject(msg + "");
				int state = jsonObject.getInt("state");
				/** ?movieId=450&mao=pao **/
				if (state == 1) {
					String movieId = HelperSP.getFromSP(this, "movieID", "movieID");
					this.finish();
					webfun.openNewWindow(Constants.urlPaoPaoEnd + "?movieId=" + movieId + "&mao=pao", true, false, 2);
				} else {
					Toast.makeText(this, "提交失败", 1).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Bind(R.id.curr_text_num)
	TextView currTextNum;
	
	@OnTextChanged(R.id.content)
	void TextChange(){
		currTextNum.setText(content.getText().length()+"/180");
	}

	@OnClick(R.id.pdy_bao)
	void ClickBao() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SendPaoPaoTxt.this);
		// 指定下拉列表的显示数据
		final String[] num = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		// 设置一个下拉的列表选择项
		builder.setItems(num, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pdyBao.setText("  +" + num[which]);
				paoPaoText.LikeInit = num[which];
			}
		});
		builder.show();
	}

	@OnClick(R.id.pdy_niao)
	void ClickNiao() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SendPaoPaoTxt.this);
		// 指定下拉列表的显示数据
		final String[] num = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		// 设置一个下拉的列表选择项
		builder.setItems(num, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pdyNiao.setText("  +" + num[which]);
				paoPaoText.HateInit = num[which];
			}
		});
		builder.show();
	}

	@OnClick(R.id.prompt)
	void ClickPrompt() {
		promptRela.setVisibility(View.VISIBLE);
	}

	@OnClick(R.id.close)
	void ClickClose() {
		promptRela.setVisibility(View.GONE);
	}

	@OnClick(R.id.back)
	void ClickBack() {
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		StaticMethod.doGet(Constants.urlGetPaoPao,cookie);
		StaticMethod.handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 0) {
					GetInform(msg.obj);
				}
			};
		};
		super.onResume();
	}

	@OnItemSelected(R.id.pao_pao_quan)
	void PaoPaoQuan(int position) {
		paoPaoText.MovieCircleInfoId = paoPaoQuans.get(position) + "";
	}

}
