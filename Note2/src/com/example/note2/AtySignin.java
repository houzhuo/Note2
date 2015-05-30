package com.example.note2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ECSConnecter.SigninConnecter;
import XMLReader.SigninData;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AtySignin extends Activity implements Runnable {
	private ImageView imageView;
	private Button button;
	private EditText etSigninName;
	private EditText etSigninPwd;
	private Handler handler;
	private boolean isRun = false;

	private OnClickListener btn_onclick_handler = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String name = etSigninName.getText().toString();
			String pwd =  etSigninName.getText().toString();
			if (name.isEmpty() && pwd.isEmpty()) {
				Toast.makeText(getApplicationContext(), "�û��������벻��Ϊ��", Toast.LENGTH_SHORT).show();
			}
				
			
			isRun = true;
			Thread t = new Thread(AtySignin.this); // �������߳�
			t.start(); // �����߳�

			handler = new Handler() { // ���handler���͵�Message�ᱻ���ݸ����̵߳�MessageQueue��
				public void handleMessage(Message msg) { // �ص�
					if (msg.what == 1) {
						if (msg.getData().getString("result")!=null){				
							System.out.println(msg.getData().getString("result"));
							System.out.println(msg.getData().getString("ID"));
							Toast.makeText(getApplicationContext(), "�˺��ѱ�ע��", Toast.LENGTH_SHORT).show();
						}else {
							System.out.println(msg.getData().getString("result")+"");
							Toast.makeText(getApplicationContext(), "ע��ɹ�", Toast.LENGTH_SHORT).show();
							
							startActivity(new Intent(getApplicationContext(), AtyLogin.class));
						}
						
					}
					super.handleMessage(msg);
				}

			};
			
		}
	};
	private OnClickListener btn_image_handler = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_signin);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		initView();
		button.setOnClickListener(btn_onclick_handler);
		imageView.setOnClickListener(btn_image_handler);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted() && isRun == true) {
			/*
			 * ����ע�����
			 */
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", etSigninName.getText().toString());
			map.put("password", etSigninPwd.getText().toString());

			SigninConnecter signconn = new SigninConnecter(map);
			try {
				signconn.getXML();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			signconn.readXML();
			SigninData data = signconn.getData();

			/*
			 * LoginConnecter loginconn = new LoginConnecter(map); try {
			 * loginconn.getXML(); } catch (IOException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 * loginconn.readXML(); LoginData data = loginconn.getData();
			 */
			Message m = handler.obtainMessage(); // ��ȡһ��Message
			Bundle bundle = new Bundle(); // ��ȡBundle����
			m.what = 1; // ������Ϣ��ʶ
			System.out.println("" + data.getResult());
			bundle.putString("result", data.getResult());
			// bundle.putString("ID", data.getId()); //��������

			bundle.putString("ID", data.getId()); // ��������

			m.setData(bundle); // ��Bundle���󱣴浽Message��
			handler.sendMessage(m); // ������Ϣ
			isRun = false;

		}
	}

	private void initView() {
		etSigninName = (EditText) findViewById(R.id.etSignin_name);
		etSigninPwd = (EditText) findViewById(R.id.etSignin_pwd);
		imageView = (ImageView) findViewById(R.id.ic_signin_back);
		button = (Button) findViewById(R.id.aty_login_btnlogin);		
	}

}
