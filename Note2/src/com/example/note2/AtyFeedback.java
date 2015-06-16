package com.example.note2;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class AtyFeedback extends Activity implements OnClickListener {
	private OnClickListener btnback_onclickHandler = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_feedback);
		findViewById(R.id.btn_feedback).setOnClickListener(this);
		findViewById(R.id.ic_feedback_back).setOnClickListener(btnback_onclickHandler);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		contact = (EditText) findViewById(R.id.feedback_content_edit);
		contact = (EditText) findViewById(R.id.feedback_contact_edit);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		String etcontact = contact.getText().toString();
		String etcontent = content.getText().toString();
		
		if (etcontact.isEmpty() && etcontent.isEmpty()) {
			Toast.makeText(getApplicationContext(), "用户名或密码不能为空",
					Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(getApplicationContext(), "谢谢您的宝贵意见", Toast.LENGTH_LONG).show();
	}
	
	private EditText content;
	private EditText contact;
}
