package com.example.note2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ECSConnecter.LoginConnecter;
import XMLReader.LoginData;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AtyLogin extends Activity implements Runnable {
	private ImageView backImage;
	private Button btnLogin;
	private TextView textSignin;
	private EditText etLoginName;
	private EditText etLoginPwd;
	private Handler handler;
	private boolean isRun = false;

	private Editor editor;
	private SharedPreferences sp;
	public static final String LOG_NAME = "logName";
	public static final String LOG_PWD = "logPwd";
	public static final String LOG_INFO = "logInfo";

	private OnClickListener btn_signin_onclickHandler = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(new Intent(getApplicationContext(), AtySignin.class));
		}
	};

	private OnClickListener btn_login_onclickHandler = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String name = etLoginName.getText().toString();
			String pwd = etLoginPwd.getText().toString();
			System.out.println(name);
			if (name.isEmpty() || pwd.isEmpty()) {
				Toast.makeText(getApplicationContext(), "�û��������벻��Ϊ��",
						Toast.LENGTH_SHORT).show();
			}else{

			isRun = true;
			Thread t = new Thread(AtyLogin.this); // �������߳�
			t.start(); // �����߳�

			handler = new Handler() { // ���handler���͵�Message�ᱻ���ݸ����̵߳�MessageQueue��
				public void handleMessage(Message msg) { // �ص�
					if (msg.what == 1) {
						if (msg.getData().getString("result")
								.equals("Success!")) {
							System.out.println("equals"+ msg.getData().getString("result"));
							Toast.makeText(getApplicationContext(), "��¼�ɹ�",Toast.LENGTH_SHORT).show();
							sp = getSharedPreferences(LOG_INFO,Activity.MODE_PRIVATE);
							editor = sp.edit();
							editor.putString(LOG_NAME, etLoginName.getText().toString());
							editor.putString(LOG_PWD, etLoginPwd.getText().toString());

							if (editor.commit()) {
								
								startActivity(new Intent(getApplicationContext(),MainActivity.class));
								finish();
							}

						} else {
							System.out.println(msg.getData()
									.getString("result") + "error");
							Toast.makeText(getApplicationContext(), "������û�������",
									Toast.LENGTH_SHORT).show();

						}

					}
					super.handleMessage(msg);
				}

			};
		}
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_login);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		initView();
		btnLogin.setOnClickListener(btn_login_onclickHandler);
		textSignin.setText(Html.fromHtml("<u>" + "ע��iNotes" + "</u>"));// �»���
		textSignin.setOnClickListener(btn_signin_onclickHandler);
		backImage.setOnClickListener(btn_image_handler);

	}

	private void initView() {
		// TODO Auto-generated method stub
		etLoginName = (EditText) findViewById(R.id.etLogin_name);
		etLoginPwd = (EditText) findViewById(R.id.etLogin_pwd);
		textSignin = (TextView) findViewById(R.id.btn_signin);
		backImage = (ImageView) findViewById(R.id.ic_Login_back);
		btnLogin = (Button) findViewById(R.id.btnlogin);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted() && isRun == true) {
			/*
			 * ����ע�����
			 */
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", etLoginName.getText().toString());
			map.put("password", etLoginPwd.getText().toString());

			LoginConnecter loginconn = new LoginConnecter(map);
			try {
				loginconn.getXML();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			loginconn.readXML();
			LoginData data = loginconn.getData();

			Message m = handler.obtainMessage(); // ��ȡһ��Message
			Bundle bundle = new Bundle(); // ��ȡBundle����
			m.what = 1; // ������Ϣ��ʶ

			bundle.putString("result", data.getResult());
			// bundle.putString("ID", data.getId()); //��������

			bundle.putLong("ID", data.getId()); // ��������

			m.setData(bundle); // ��Bundle���󱣴浽Message��
			handler.sendMessage(m); // ������Ϣ
			isRun = false;

		}
	}

}
