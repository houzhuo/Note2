package com.example.note2;

import java.util.jar.Attributes.Name;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AtyUser extends Activity {
	SharedPreferences sp;
	
	private TextView etID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.aty_user);
		ActionBar actionBar = getActionBar();
		getActionBar().hide();
		initView();
		sp = getSharedPreferences(AtyLogin.LOG_INFO	, Activity.MODE_PRIVATE);
		String  name = sp.getString(AtyLogin.LOG_NAME, "null");
		String  pwd = sp.getString(AtyLogin.LOG_PWD, "null");
		etID.setText(name);
		Toast.makeText(getApplicationContext(),name + pwd , Toast.LENGTH_SHORT).show();
	}
	private void initView() {
		// TODO Auto-generated method stub
		etID = (TextView) findViewById(R.id.user_name);
		
	}

}
