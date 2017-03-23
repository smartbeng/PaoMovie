package com.pdy.mobile;

import java.lang.ref.WeakReference;

import android.app.Activity;

public class MyActivityManager {
	  private static MyActivityManager sInstance = new MyActivityManager();  
      private WeakReference<Activity> sCurrentActivityWeakRef;  
       
       
      private MyActivityManager() {  
       
      }  
       
      public static MyActivityManager getInstance() {  
        return sInstance;  
      }  
       
      public Activity getCurrentActivity() {  
        Activity currentActivity = null;  
        if (sCurrentActivityWeakRef != null) {  
          currentActivity = sCurrentActivityWeakRef.get();  
        }  
        return currentActivity;  
      }  
 
      public void setCurrentActivity(Activity activity) {  
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);  
      }  
}
