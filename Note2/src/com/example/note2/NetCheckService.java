package com.example.note2;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class NetCheckService extends Service implements Runnable {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Thread t = new Thread();
		t.start();
		
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
	
	public class localService extends Binder{
		 NetCheckService getService(){
			return NetCheckService.this;
		}
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Play Service Created", Toast.LENGTH_LONG).show();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				isOpenNetwork();
			}
		} , 0, 2000);
	}
}
