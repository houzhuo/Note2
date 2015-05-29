package com.example.note2;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class AtyUser extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_user);
		ActionBar actionBar = getActionBar();
		getActionBar().hide();
	}

}
