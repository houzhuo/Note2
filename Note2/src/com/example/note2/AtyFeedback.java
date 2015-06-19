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
		content = (EditText) findViewById(R.id.feedback_content_edit);
		name = (EditText) findViewById(R.id.feedback_name_edit);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
//		String etcontact = contact.getText().toString();
//		String etcontent = content.getText().toString();
		
		if (name.getText().toString().isEmpty() || content.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "意见和联系方式不能为空",
					Toast.LENGTH_SHORT).show();
		} else
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_LONG).show();
	}
	
	private EditText content;
	private EditText name;
}
