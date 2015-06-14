package com.example.note2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ECSConnecter.TopicConnecter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AtyTopic extends Activity implements Runnable{

	private TextView textView;
	private Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic);
		
		textView = (TextView) findViewById(R.id.topic2);
		button = (Button) findViewById(R.id.topic1);
		
		Thread t = new Thread(AtyTopic.this); // 创建新线程
		t.start(); // 开启线程
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("aimStr", "我爱北京天安门，天安门上太阳升");
		
		TopicConnecter conn = new TopicConnecter(map);
		try {
			conn.getXML();
			try {
				conn.readXML();
			} catch (ParserConfigurationException | SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TopicData data = conn.getData();
			ArrayList<String> list = conn.getData().getResult();
			for (String string : list) {
				System.out.println(string);
			}
	}
	
}