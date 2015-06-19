package com.example.note2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class AtyWelcome extends Activity implements Runnable {
	
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.aty_welcome);
		Thread t = new Thread(AtyWelcome.this);
		t.start();
						
	}

	@Override
	public void run() {
		
		sp = getSharedPreferences(AtyLogin.LOG_INFO, Activity.MODE_PRIVATE);
		String name = sp.getString(AtyLogin.LOG_NAME, "null");
		String pwd = sp.getString(AtyLogin.LOG_PWD, "null");
		// TODO Auto-generated method stub
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (name.equals("null")) {
			startActivity(new Intent(getApplicationContext(), AtyLogin.class));
			System.out.println("NOT equals:"+name);
			finish();
			
		}else {
			startActivity(new Intent(getApplicationContext(), MainActivity.class));
			System.out.println("equals:"+name);
			finish();
		}
		
	}

}
