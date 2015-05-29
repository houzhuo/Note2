package com.example.note2;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "Ð»Ð»ÄúµÄ±¦¹óÒâ¼û", Toast.LENGTH_LONG).show();
	}

}
