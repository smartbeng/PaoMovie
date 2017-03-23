package com.pdy.mobile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.lzyzsd.circleprogressexample.MyActivity;
import com.google.gson.Gson;
import com.pdy.mobile.StaticMethod.PaoPaoImage;
import com.pdy.mobile.StaticMethod.PaoPaoText;
import com.pdy.mobile.R;

import android.app.ActionBar.LayoutParams;
import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

public class SendPaoPaoPic extends BaseActivity implements OnClickListener {

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

	PaoPaoImage paoPaoImage = null;
	private String cookie = "";

	/** 泡泡圈状态 **/
	public int paoPaoState = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		paoPaoState = 0;
		setContentView(R.layout.send_pao_pao_image);
		ButterKnife.bind(this);
		String title = HelperSP.getFromSP(this, "movieName", "movieName");
		titleView.setText(title);
		paoPaoImage = new PaoPaoImage();
		cookie = HelperSP.getFromSP(this, "UserId", "UserId");
	}

	@Bind(R.id.curr_text_num)
	TextView currTextNum;

	@OnTextChanged(R.id.content)
	void TextChange() {
		currTextNum.setText(content.getText().length() + "/180");
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

	@Bind(R.id.upload_progress)
	RelativeLayout uploadProgress;
	@Bind(R.id.donut_progress)
	DonutProgress donutProgress;
	private PostResult postResult;
	private String mCurrentPhotoPath;

	@OnClick(R.id.send)
	void Send() {
		List<String> paths = new ArrayList<>();
		paoPaoImage.Content = content.getText().toString();
		if (paoPaoImage.Content.equals("")) {
			Toast.makeText(this, "请输入内容", 1).show();
			return;
		}
		int child = imageGrid.getChildCount() - 1;
		if (child == 0) {
			Toast.makeText(this, "请至少选择一张图片", 1).show();
			return;
		} else {
			for (int i = 0; i < child; i++) {
				String path = imageGrid.getChildAt(i).getTag() + "";
				Log.e("js", "path :" + path);
				if (!path.equals(""))
					paths.add(path);
			}
		}
		paoPaoImage.MovieInfoId = HelperSP.getFromSP(this, "movieID", "movieID");
		paoPaoImage.Title = HelperSP.getFromSP(this, "movieName", "movieName");
		paoPaoImage.UserId = HelperSP.getFromSP(this, "UserId", "UserId");
		Gson gson = new Gson();
		String message = gson.toJson(paoPaoImage);
		Map<String, String> inform = gson.fromJson(message, HashMap.class);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : inform.entrySet()) {
			Log.e("js", "key:" + entry.getKey() + " value:" + entry.getValue());
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		if (paoPaoImage.UserId == "") {
			Toast.makeText(this, "未获取到用户信息，请重新登录", 1).show();
			return;
		}

		final String[] path = (String[]) paths.toArray(new String[paths.size()]);
		String uriAPI = "http://paody.lansum.cn/api/api/Foam/ImgFoam";

		postResult = new PostResult() {

			@Override
			public void PostCallbackResult(String result) {
				// TODO Auto-generated method stub
				Log.e("js", "接收到：" + result);
				GetResult(result);
			}
		};

		UploadThread thread = new UploadThread(uriAPI, inform, path, postResult);

		thread.cookie = cookie;
		thread.setUploadProgress(new UploadProgress() {
			@Override
			public void UploadCallbackResult(final float progress) {

				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						ClickFalse(progress);
						AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(SendPaoPaoPic.this,
								R.animator.progress_anim);
						set.setInterpolator(new DecelerateInterpolator());
						set.setTarget(donutProgress);
						donutProgress.setProgress(progress);
					}
				});
			}
		});
		thread.start();

		// StaticMethod.doPost("http://paody.lansum.cn/api/api/Foam/ImgFoam",
		// params);
		// StaticMethod.handler = new Handler() {
		// public void handleMessage(android.os.Message msg) {
		// if (msg.what == 0) {
		// Log.e("js", "msg" + msg);
		// /** http://paody.lansum.cn/MovieHome.html?movieId=450&mao=pao */
		// GetResult(msg.obj);
		// }
		// };
		// };
	}

	void ClickFalse(float progress) {
		if (progress == 0) {
			uploadProgress.setVisibility(View.VISIBLE);
			uploadProgress.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return true;
				}
			});
		} else if (progress == 100) {
			uploadProgress.setVisibility(View.GONE);
			uploadProgress.setOnTouchListener(null);
		}
	}

	void GetResult(Object msg) {
		if (msg != null && !msg.equals("")) {
			try {
				JSONObject jsonObject = new JSONObject(msg + "");
				int state = jsonObject.getInt("state");
				/** ?movieId=450&mao=pao **/
				if (state == 1) {
					String movieId = HelperSP.getFromSP(this, "movieID", "movieID");
					this.finish();
					webfun.openNewWindow(Constants.urlPaoPaoEnd + "?movieId=" + movieId + "&mao=pao", true, false, 2);
					ToastShow("提交成功");
				} else {
					ToastShow("提交失败");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			ToastShow("提交失败");
		}
	}

	void ToastShow(final String text) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(SendPaoPaoPic.this, text, 1).show();
			}
		});
	}

	@OnClick(R.id.pdy_bao)
	void ClickBao() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SendPaoPaoPic.this);
		// 指定下拉列表的显示数据
		final String[] num = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		// 设置一个下拉的列表选择项
		builder.setItems(num, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pdyBao.setText("  +" + num[which]);
				paoPaoImage.LikeInit = num[which];
			}
		});
		builder.show();
	}

	@OnClick(R.id.pdy_niao)
	void ClickNiao() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SendPaoPaoPic.this);
		// 指定下拉列表的显示数据
		final String[] num = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		// 设置一个下拉的列表选择项
		builder.setItems(num, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pdyNiao.setText("  +" + num[which]);
				paoPaoImage.HateInit = num[which];
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

	@SuppressLint("NewApi")
	@OnClick(R.id.add_image)
	void ClickAddImage() {
		if ((this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)||
		        (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
		    //如果没有授权，则请求授权
			this.requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1); 
		}else {
		// create Intent to take a picture and return control to the calling application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		CharSequence[] items = { "相册", "相机" };
		new AlertDialog.Builder(this).setTitle("选择图片来源").setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					startActivityForResult(Intent.createChooser(intent, "选择图片"), 3);
				} else {
					File photoFile = null;
					try {
						photoFile = createImageFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					Uri outputFileUri = null;
					if (android.os.Build.VERSION.SDK_INT < 24) {
						outputFileUri = Uri.fromFile(photoFile);

					} else {
						outputFileUri = FileProvider.getUriForFile(SendPaoPaoPic.this,
								"com.sat.android.fileprovider", photoFile);
					}
					intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
					startActivityForResult(intent, 4);
				}
			}
		}).create().show();
		}
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    //.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
	    File storageDir = new File(StaticMethod.GetFilePath());
	    /*File storageDir = new File(Environment.getExternalStorageDirectory(), "images");
	    if (!storageDir.exists()) storageDir.mkdirs();*/
	    Log.d("TAH",storageDir.toString());
	    //创建临时文件,文件前缀不能少于三个字符,后缀如果为空默认未".tmp"
	    File image = File.createTempFile(
	            imageFileName,  /* 前缀 */
	            ".jpg",         /* 后缀 */
	            storageDir      /* 文件夹 */
	    );
	    mCurrentPhotoPath = image.getAbsolutePath();
	    return image;
	}

	@Bind(R.id.image_grid)
	GridLayout imageGrid;
	int imageId = 4000;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		paoPaoQuan.setSelection(paoPaoState);
		// requestCode标示请求的标示 resultCode表示有数据
		Log.d("js", requestCode + "//" + resultCode + "**" + data);
		if (requestCode==4||data != null) {
			int childCount = imageGrid.getChildCount();
			if (childCount > 9) {
				Toast.makeText(this, "最多选择9张图片", 1).show();
			} else {
				imageId++;
				String fPath = null;
				if (requestCode == 4) {
					fPath = mCurrentPhotoPath;
				}else{
					Uri uri = data.getData(); // 得到Uri
					if((uri!=null&&!uri.equals(""))){
						fPath = StaticMethod.getImageAbsolutePath(this, uri); // 转化为路径
					}
				}
				Bitmap b = BitmapFactory.decodeFile(fPath);
				b = StaticMethod.getThumImg(b, 100);
				ImageView image = new ImageView(this);
				image.setLayoutParams(new LayoutParams(130, 130));
				image.setScaleType(ScaleType.FIT_XY);
				image.setId(imageId);
				image.setTag(fPath);
				image.setImageBitmap(b);
				image.setOnClickListener(this);
				imageGrid.addView(image, childCount - 1);
				}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (paoPaoState == 0) {
			StaticMethod.doGet(Constants.urlGetPaoPao, cookie);
			StaticMethod.handler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					if (msg.what == 0) {
						GetInform(msg.obj);
					}
				};
			};
		} else {
			paoPaoQuan.setSelection(paoPaoState);
		}
		super.onResume();
	}

	@OnItemSelected(R.id.pao_pao_quan)
	void PaoPaoQuan(int position) {
		paoPaoImage.MovieCircleInfoId = paoPaoQuans.get(position) + "";
		paoPaoState = position;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final ImageView image = (ImageView) imageGrid.findViewById(v.getId());
		if (image != null) {
			new AlertDialog.Builder(SendPaoPaoPic.this).setTitle("确认要删除吗？").setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 点击“确认”后的操作
							imageGrid.removeView(image);
						}
					}).setNegativeButton("返回", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 点击“返回”后的操作,这里不设置没有任何操作
						}
					}).show();
		}
	}

}
