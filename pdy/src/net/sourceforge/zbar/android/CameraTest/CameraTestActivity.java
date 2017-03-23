/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 * 
 * Created by lisah0 on 2012-02-24
 */
package net.sourceforge.zbar.android.CameraTest;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import com.pdy.mobile.WebViewSearchActivity;
import com.pdy.mobile.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CameraTestActivity extends Activity
{
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    TextView scanText;
    Button scanButton;

    ImageScanner scanner;
    FrameLayout preview;
    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    } 

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.zbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();
        
        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        preview = (FrameLayout)findViewById(R.id.cameraPreview);
         
        preview.addView(mPreview);

        scanText = (TextView)findViewById(R.id.scanText);

        scanButton = (Button)findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (barcodeScanned) {
                        barcodeScanned = false;
                        scanText.setText("Scanning...");
                        mCamera.setPreviewCallback(previewCb);
                        mCamera.startPreview();
                        previewing = true;
                        mCamera.autoFocus(autoFocusCB);
                    }
                }
            });
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing)
                    mCamera.autoFocus(autoFocusCB);
            }
        };
        private String strCaptureFilePath = Environment
                .getExternalStorageDirectory() + "/DCIM/Camera/";// 保存图像的路径

        String str;
    PreviewCallback previewCb = new PreviewCallback() {
            @SuppressWarnings("deprecation")
			public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();
                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);
                int result = scanner.scanImage(barcode);
                if (result != 0) {

//                	Bitmap bitmap = null; 
//                    Rect rect = new Rect(0, 0, size.width, size.height);
//                    YuvImage img = new YuvImage(data, ImageFormat.NV21,size.width, size.height, null); 
//                    if(img!=null){
//                    	  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
//                          if (img.compressToJpeg(rect, 60, baos)) 
//                           { 
//                       	   try {
//							bitmap =  BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
//							baos.close();
//						} catch (Exception e) {
//							bitmap=null;
//							e.printStackTrace();
//						}
//                    }
//                  }
                	
 
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                  
                    
                    SymbolSet syms = scanner.getResults();
                    
                    for (Symbol sym : syms) {
                        scanText.setText("barcode result " + sym.getData());
                        barcodeScanned = true;
                        
                        str = sym.getData();
                        
                        //Toast.makeText(CameraTestActivity.this,sym.getData() , 1000).show();
                        
                    }
                    
                    if(str.indexOf("http:")!=-1){
                    	
                    	AlertDialog dialog = new AlertDialog.Builder(CameraTestActivity.this).create();
                    	dialog.setTitle("提示");
                    	dialog.setMessage("是否打开链接:"+str);
                    	dialog.setButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								finish();
							}
						});
                    	dialog.setButton2("打开", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								startActivity(new Intent(CameraTestActivity.this,
		    							WebViewSearchActivity.class).putExtra("url", str));
								 finish();
							}
						});
                    	dialog.show();
    				}else {
    					//startActivity(new Intent(CameraTestActivity.this,
    					//		WebViewSearchActivity.class).putExtra("url", "http://www.baidu.com/s?wd="+str));
//    					
//    					final Intent sendIntent = new Intent(CameraTestActivity.this, HtmlUIActivity.class);//这种方法今天才学的，记下！方便这样写,坑爹的有些教程，这块没有给Inent绑定
//                        Bundle bundle = new Bundle();
//                        bundle.putString("zbarStr", str);
//                        sendIntent.putExtras(bundle);
//                       
    					
    					AlertDialog dialog = new AlertDialog.Builder(CameraTestActivity.this).create();
                    	dialog.setTitle("提示");
                    	dialog.setMessage("是否复制:"+str);
                    	dialog.setButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								copy(str, getBaseContext()); 
								// CameraTestActivity.this.setResult(100, sendIntent);
			                     //CameraTestActivity.this.finish();
			                   
			                     finish();
							}
						});
                    	dialog.setButton2("复制", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								copy(str, getBaseContext()); 
								// CameraTestActivity.this.setResult(100, sendIntent);
			                    // CameraTestActivity.this.finish();
			                     Toast.makeText(getBaseContext(), "文本已复制到粘贴板", 1000) .show(); 
			                    
			                     finish();
							}
						});
                    	dialog.show();
    				
    					
    					
    					
    					
    				}
                    
                    

                 
                  
//                    if(bitmap!=null && !bitmap.isRecycled())
//                   	{
//                   		bitmap.isRecycled();
//                   		bitmap=null;
//                   	}
//                    
                    
                   
                }
            }
        };
        
        
    
 
        public static void copy(String content, Context context) { 
        	// 得到剪贴板管理器 
        	ClipboardManager cmb = (ClipboardManager) context .getSystemService(Context.CLIPBOARD_SERVICE); 
        	cmb.setText(content.trim()); 
        	} 
        
        
    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                autoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        };
        
        
}
