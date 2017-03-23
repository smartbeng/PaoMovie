/*
 * Barebones implementation of displaying camera preview.
 * 
 * Created by lisah0 on 2012-02-24
 */
package net.sourceforge.zbar.android.CameraTest;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private PreviewCallback previewCallback;
    private AutoFocusCallback autoFocusCallback;

    private Context context;
    
    public CameraPreview(Context context, Camera camera,
                         PreviewCallback previewCb,
                         AutoFocusCallback autoFocusCb) {
        super(context);
        this.context=context;
        mCamera = camera;
        previewCallback = previewCb;
        autoFocusCallback = autoFocusCb;

        /* 
         * Set camera to continuous focus if supported, otherwise use
         * software auto-focus. Only works for API level >=9.
         */
        /*
        Camera.Parameters parameters = camera.getParameters();
        for (String f : parameters.getSupportedFocusModes()) {
            if (f == Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
                mCamera.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                autoFocusCallback = null;
                break;
            }
        }
        */

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            Log.d("DBG", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Camera preview released in activity
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        /*
         * If your preview can change or rotate, take care of those events here.
         * Make sure to stop the preview before resizing or reformatting it.
         */
        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        try {
        	
        	
        	
//        	Camera.Parameters params = mCamera.getParameters();
//
//        	Size pictureSize ;
//        	Size previewSize;
//        	List<Size> supportedPictureSizes
//        				= SupportedSizesReflect.getSupportedPictureSizes(params);
//        	List<Size> supportedPreviewSizes
//        				= SupportedSizesReflect.getSupportedPreviewSizes(params);
//
//        	if ( supportedPictureSizes != null &&
//        		supportedPreviewSizes != null &&
//        		supportedPictureSizes.size() > 0 &&
//        		supportedPreviewSizes.size() > 0) {
//
//        		//2.x
//        		 pictureSize = supportedPictureSizes.get(0);
//
//        		int maxSize = 1280;
//        		if(maxSize > 0){
//        			for(Size size : supportedPictureSizes){
//        				if(maxSize >= Math.max(size.width,size.height)){
//        					pictureSize = size;
//        					break;
//        				}
//        			}
//        		}
//        	
//        		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        		Display display = windowManager.getDefaultDisplay();
//        		DisplayMetrics displayMetrics = new DisplayMetrics();
//        		display.getMetrics(displayMetrics);
//
//        		previewSize = getOptimalPreviewSize(
//        							supportedPreviewSizes,
//        							display.getWidth(),
//        							display.getHeight()); 
//
//        		params.setPictureSize(pictureSize.width, pictureSize.height);
//        		params.setPreviewSize(previewSize.width, previewSize.height);
//        	
//        	}
//        	
//        	mCamera.setParameters(params);
//            // Hard code camera surface rotation 90 degs to match Activity view in portrait
//            mCamera.setDisplayOrientation(90);

        	
        	Camera.Parameters parameters = mCamera.getParameters();
            if(this.getResources().getConfiguration().orientation!=Configuration.ORIENTATION_LANDSCAPE){
             parameters.set("orientation", "portrait");
             mCamera.setDisplayOrientation(90);//针对android2.2和之前的版本
            parameters.setRotation(90);//去掉android2.0和之前的版本
           }else{
             parameters.set("orientation", "landscape");
             mCamera.setDisplayOrientation(0);
             parameters.setRotation(0);
            }
            mCamera.setParameters(parameters); 
        	
        	
        	
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(previewCallback);
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCallback);
        } catch (Exception e){
        	mCamera.release();//如果在设置摄像头的时候出现异常，在这里释放资源
            Log.d("DBG", "Error starting camera preview: " + e.getMessage());
        }
    }
    

        private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
                final double ASPECT_TOLERANCE = 0.1;
                double targetRatio = (double) w / h;
                if (sizes == null) return null;

                Size optimalSize = null;
                double minDiff = Double.MAX_VALUE;

                int targetHeight = h;

                // Try to find an size match aspect ratio and size
                for (Size size : sizes) {
                    double ratio = (double) size.width / size.height;
                    if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
                    if (Math.abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size;
                        minDiff = Math.abs(size.height - targetHeight);
                    }
                }

                // Cannot find the one match the aspect ratio, ignore the requirement
                if (optimalSize == null) {
                    minDiff = Double.MAX_VALUE;
                    for (Size size : sizes) {
                        if (Math.abs(size.height - targetHeight) < minDiff) {
                            optimalSize = size;
                            minDiff = Math.abs(size.height - targetHeight);
                        }
                    }
                }
                return optimalSize;
            }
}
