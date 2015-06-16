package com.example.note2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.note2.AtyEditNote.MediaAdapter;
import com.example.note2.AtyEditNote.MediaListCellData;
import com.example.note2.AtyEditNote.MediaType;
import com.example.note2.R.layout;

import ECSConnecter.CloudConnecter;
import ECSConnecter.SigninConnecter;
import XMLReader.CloudData;
import XMLReader.SigninData;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AtyDowload extends ListActivity implements Runnable {
	
	private CloudAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_download);
		
		adapter = new CloudAdapter(this);
		setListAdapter(adapter);
		
		//isRun = true;
		Thread t = new Thread(); // 创建新线程
		t.start(); // 开启线程

		handler = new Handler() { // 这个handler发送的Message会被传递给主线程的MessageQueue。
			public void handleMessage(Message msg) { // 回调
				if (msg.what == 1) {
					String filename = msg.getData().getString("filename");				
					String large = msg.getData().getString("large");
					System.out.println("filename"+filename);
					
					adapter.add(new CloudMediaListCellData(filename,large));
					adapter.notifyDataSetChanged();
					
				}
				super.handleMessage(msg);
			}

		};
		
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted() ) {
			/*
			 * 连接注册进程&& isRun == true
			 */
			Map<String, String> map = new HashMap<String, String>();
			map.put("userCode", "10000000");
			map.put("page", "1");
			map.put("number", "10");

			CloudConnecter cloudconn = new CloudConnecter(map);
			try {
				cloudconn.getXML();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cloudconn.readXML();
			ArrayList<CloudData> data = cloudconn.getData();

			/*
			 * LoginConnecter loginconn = new LoginConnecter(map); try {
			 * loginconn.getXML(); } catch (IOException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 * loginconn.readXML(); LoginData data = loginconn.getData();
			 */
			Message m = handler.obtainMessage(); // 获取一个Message
			Bundle bundle = new Bundle(); // 获取Bundle对象
			m.what = 1; // 设置消息标识
			for (CloudData cloudData : data) {
				System.out.println("" + cloudData.getFilename());
				bundle.putString("filename", cloudData.getFilename());
				bundle.putString("large", Integer.parseInt(cloudData.getLength())/1024.0/1024.0+"M"); // 保存数据
	
				m.setData(bundle); // 将Bundle对象保存到Message中
				handler.sendMessage(m); // 发送消息
				//isRun = false;

		}
		}
	}
	
	
	
	private Handler handler;
	//private boolean isRun = false;
	
	static class CloudAdapter extends BaseAdapter{
		private Context context;
		private List<CloudMediaListCellData> list = new ArrayList<AtyDowload.CloudMediaListCellData>();
 		
		public CloudAdapter(Context context){
			this.context =context;
		}
		public void add(CloudMediaListCellData data){
			list.add(data);
		}
		@Override
		public int getCount() {
			
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.cloud_media_list_cell, null);
			}
			
			CloudMediaListCellData data = (CloudMediaListCellData) getItem(position);
			
			ImageView ivIcon = (ImageView) convertView.findViewById(R.id.cloud_ivIcon);
			TextView cloud_name  = (TextView) convertView
					.findViewById(R.id.cloud_name);
			TextView cloud_large = (TextView) convertView
					.findViewById(R.id.cloud_large);
			ivIcon.setImageResource(R.drawable.icon_sound);
			cloud_name.setText(data.name);
			cloud_large.setText(data.large);
			return convertView;
		}
		
	}
	static class CloudMediaListCellData {

		
		int id = -1;
		
		String name = "";
		String large = "";

		public CloudMediaListCellData(String name ,String large) {
			this.name = name;
			this.large = large;

			/*
			 * else { iconId = R.drawable.icon_sound; type = MediaType.SOUND; }
			 */
		}

		public CloudMediaListCellData(String name, String large, int id) {

			this(name ,large);
			this.id = id;
		}

	}
	
	
	

}
