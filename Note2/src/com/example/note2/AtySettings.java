package com.example.note2;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AtySettings extends Activity implements OnClickListener{
	private ImageView imageView;
	
	private Editor editor;
	public static final String LOG_INFO = "logInfo";
	private SharedPreferences sp;
	
	private TextView settings_general;
	private TextView settings_private;
	private TextView settings_message;
	private TextView settings_history;
	private TextView settings_net;
	private TextView settings_update;
	private TextView settings_about;
	private TextView settings_logout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.aty_settings);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		super.onCreate(savedInstanceState);
		
		initView();
		
		
		
		
		imageView = (ImageView) findViewById(R.id.ic_settings_back);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		settings_general=	(TextView) findViewById(R.id.settings_general);
		settings_private=	(TextView) findViewById(R.id.settings_private);
		settings_message=	(TextView) findViewById(R.id.settings_message);
		settings_history=	(TextView) findViewById(R.id.settings_history);
		settings_net=		(TextView) findViewById(R.id.settings_net);
		settings_update=	(TextView) findViewById(R.id.settings_update);
		settings_about=		(TextView) findViewById(R.id.settings_about);
		settings_logout=	(TextView) findViewById(R.id.settings_logout);
		settings_logout.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.settings_logout:
			logout();
			break;

		default:
			break;
		}
	}
	public void clearSharedPreferences()
    {
		sp = getSharedPreferences(LOG_INFO,Activity.MODE_PRIVATE);
		editor = sp.edit();
        
        if (editor.clear().commit()) {
			startActivity(new Intent(getApplicationContext(),AtyLogin.class));
		}
    }

/**
     * ExitDialog×¢Ïú°´Å¥
     */
    protected void logout()
    {
        clearSharedPreferences();
        System.exit(0);
    }
}
