package com.example.note2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.PrivateCredentialPermission;

import ECSConnecter.CloudConnecter;
import XMLReader.CloudData;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.example.note2.AtyEditNote.MediaListCellData;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class AtyCloud extends Activity implements Runnable, OnClickListener {

	private CloudAdapter adapter;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_cloud);
		Toast.makeText(getApplicationContext(), "下拉进行刷新", Toast.LENGTH_SHORT).show();
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
	

		mListView = (PullToRefreshListView) findViewById(R.id.list_view);
		mListView.setMode(Mode.PULL_FROM_START);
		adapter = new CloudAdapter(this);
		mListView.setAdapter(adapter);

		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				System.err.println("刷新开始");
				updateList();
				Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
				mListView.onRefreshComplete();
			}
		});
		
		registerForContextMenu(mListView);
		findViewById(R.id.ic_cloud_back).setOnClickListener(this);
	}

	public void updateList() {
		isRun = true;

		Thread t = new Thread(AtyCloud.this); // 创建新线程
		t.start(); // 开启线程

		handler = new Handler() { // 这个handler发送的Message会被传递给主线程的MessageQueue。
			public void handleMessage(Message msg) { // 回调
				if (msg.what == 1) {

					int i = 0;
					for (i = 0; i < 10; i++) {
						String filename = msg.getData().getString(
								"filename" + i);
						String large = msg.getData().getString("large" + i);
						adapter.add(new CloudMediaListCellData(filename, large));
						System.out.println("filename" + filename);
						System.out.println("large" + large);
					}

					adapter.notifyDataSetChanged();

				}
				super.handleMessage(msg);
			}

		};
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		
		case R.id.download:
			 
			CloudMediaListCellData d = (CloudMediaListCellData) adapter.getItem(info.position);
			System.out.println("-------------------"+d.name+"");
			String filename = d.name;
			String filenameWithWav = d.name.substring(filename.length()-4);
			mediaGet mediaGet = new mediaGet(filename, filenameWithWav);
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted() && isRun == true) {
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

			Message m = handler.obtainMessage(); // 获取一个Message
			Bundle bundle = new Bundle(); // 获取Bundle对象
			m.what = 1;
			for (int i = 0; i < data.size(); i++) {
				System.out.println(i);
				System.err.println(data.get(i).getFilename());
				bundle.putString("filename" + i, data.get(i).getFilename());
				bundle.putString("large" + i,
						Integer.parseInt(data.get(i).getLength()) / 1024
								/ 1024 /1.0+ "M");
			}
			m.setData(bundle); // 将Bundle对象保存到Message中
			handler.sendMessage(m); // 发送消息
			// System.err.println(data.get(2).getFilename());

			isRun = false;
			// bundle.putString("filename", data.get(1).getFilename());
		}
	}

	/*
	 * private class FinishRefresh extends AsyncTask<Void, Void, Void>{
	 * protected Void doInBackground(Void... params) { try { Thread.sleep(2000);
	 * } catch (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * return null; }
	 * 
	 * protected void onPostExecute(Void result){ mListView.onRefreshComplete();
	 * } }
	 */

	private Handler handler;
	private boolean isRun = false;
	private PullToRefreshListView mListView;

	static class CloudAdapter extends BaseAdapter {
		private Context context;
		private List<CloudMediaListCellData> list = new ArrayList<AtyCloud.CloudMediaListCellData>();

		public CloudAdapter(Context context) {
			this.context = context;
		}

		public void add(CloudMediaListCellData data) {
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
				convertView = LayoutInflater.from(context).inflate(
						R.layout.cloud_media_list_cell, null);
			}

			CloudMediaListCellData data = (CloudMediaListCellData) getItem(position);

			ImageView ivIcon = (ImageView) convertView
					.findViewById(R.id.cloud_ivIcon);
			TextView cloud_name = (TextView) convertView
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

		public CloudMediaListCellData(String name, String large) {
			this.name = name;
			this.large = large;

			/*
			 * else { iconId = R.drawable.icon_sound; type = MediaType.SOUND; }
			 */
		}

		public CloudMediaListCellData(String name, String large, int id) {

			this(name, large);
			this.id = id;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}

}
