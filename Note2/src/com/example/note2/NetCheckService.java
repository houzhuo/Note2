package com.example.note2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class NetCheckService extends Service  {
	private Handler handler;
	private Runnable runnable;
	
	public class LocalBinder extends Binder{
		NetCheckService getService(){
			return NetCheckService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	private IBinder mBinder = new LocalBinder();
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		handler = new Handler(Looper.getMainLooper());
		runnable = new  Runnable() {
			public void run() {
				isOpenNetwork();
				handler.postDelayed(this, 3000);
			}
		};
		handler.postDelayed(runnable, 3000);
		
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
	
	/** 
	 * 对网络连接状态进行判断 
	 * @return  true, 可用； false， 不可用 
	 */  
	private boolean isOpenNetwork() {  
	    ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if(connManager.getActiveNetworkInfo() != null) {  
	        return connManager.getActiveNetworkInfo().isAvailable();  
	    } else {
			Toast.makeText(getApplicationContext(), "当前网络连接不可用", Toast.LENGTH_SHORT).show();
		} 
	  
	    return false; 
	} 


}
