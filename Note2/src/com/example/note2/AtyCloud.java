package com.example.note2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ECSConnecter.CloudConnecter;
import ECSConnecter.DownloadConnecter;
import OSSConnecter.getObject;
import XMLReader.CloudData;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note2.db.NotesDB;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;




public class AtyCloud extends ListActivity implements Runnable, OnClickListener {

	private CloudAdapter adapter;
	private PullToRefreshListView mListView;
	

	


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
				((IPullToRefresh<ListView>) mListView).onRefreshComplete();
			}
		});
		
		registerForContextMenu(getListView());
		findViewById(R.id.ic_cloud_back).setOnClickListener(this);
		
		findViewById(R.id.ic_cloud_download).setOnClickListener(this);
		
	}

	public void updateList() {
		isRun = true;

		Thread t = new Thread(AtyCloud.this); // 创建新线程
		t.start(); // 开启线程

		handler = new Handler() { // 这个handler发送的Message会被传递给主线程的MessageQueue。
			public void handleMessage(Message msg) { // 回调
				if (msg.what == 1) {
					
					int i = dataSize;
					for (i = dataSize-1; i >1; i--) {
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
	  super.onCreateContextMenu(menu, v, menuInfo);  
	  MenuInflater inflater = getMenuInflater();  
	  inflater.inflate(R.menu.download, menu);  
	} 
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// 相应上下文的操作
		switch (item.getItemId()) {
		
		case R.id.media_download:
			
			System.out.println("--------------"+info.position+"");
			CloudMediaListCellData d = (CloudMediaListCellData)adapter.getItem(info.position - 1);
			
			System.out.println("-------------------"+d.name+"");
			String filenameWithOutWav = d.name;
			String filename = d.name+".wav";
			MediaGet mediaGet = new MediaGet(filename, filenameWithOutWav);
			mediaGet.downloadMedia();
			
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
			map.put("number", "30");

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
			dataSize = data.size();
			for (int i = 0; i < dataSize; i++) {
				System.out.println(i);
				System.err.println(data.get(i).getFilename());
				bundle.putString("filename" + i, data.get(i).getFilename());
				bundle.putString("large" + i,
						Integer.parseInt(data.get(i).getLength()) / 1024
								/ 1024 + "M");
			}
			m.setData(bundle); // 将Bundle对象保存到Message中
			handler.sendMessage(m); // 发送消息
			// System.err.println(data.get(2).getFilename());

			isRun = false;
			// bundle.putString("filename", data.get(1).getFilename());
		}
	}



	private Handler handler;
	private boolean isRun = false;
	//private PullToRefreshListView mListView;
	
	private int dataSize;

	static class CloudAdapter extends BaseAdapter {
		private Context context;
		private List<CloudMediaListCellData> list = new ArrayList<AtyCloud.CloudMediaListCellData>();

		public CloudAdapter(Context context) {
			this.context = context;
		}

		public void add(CloudMediaListCellData data) {
			list.add(data);
		}
		
		/*public String getTextString(int postion){
			return list.get(postion).getName();
		}*/
		
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
		switch (v.getId()) {
		case R.id.ic_cloud_back:
			
			finish();
			break;
		case R.id.ic_cloud_download:
			Intent downloadIntent = new Intent(AtyCloud.this,AtyDownload.class);
			startActivity(downloadIntent);
			break;
		default:
			break;
		}
	}

	
	
	
	
	
	class MediaGet extends Thread{
		
		private String filename;
		private String filenameWithOutWav;
		private NotesDB dowloadDb;
		private SQLiteDatabase dbWrite;
		private String path;
		
		public MediaGet(String filename,String filenameWithOutWav){
			this.filename = filename;
			this.filenameWithOutWav = filenameWithOutWav;
		}
		
		public void downloadMedia(){
			System.out.println("------------------点击监听");
			isRun = true;
			// TODO Auto-generated method stub
			Thread t = new Thread(MediaGet.this);
			t.start();
			
			dowloadDb = new NotesDB(AtyCloud.this);
			dbWrite = dowloadDb.getWritableDatabase();
			
			handler = new Handler() { // 这个handler发送的Message会被传递给主线程的MessageQueue。
				

				public void handleMessage(Message msg) { // 回调
					if (msg.what == 1) {
						if (msg.getData().getString("result")!=null){
							System.out.println(msg.getData().getString("result")+"");
							
							ContentValues cv = new ContentValues();
							cv.put(NotesDB.COLUMN_NAME_DOWNLOAD_NAME, filename);
							cv.put(NotesDB.COLUMN_NAME_DOWNLOAD_PATH, path);
							dbWrite.insert(NotesDB.TABLE_NAME_DOWNLOAD, null, cv);
							
						}else {
							System.out.println(msg.getData().getString("result")+"");				
						}
						
					}
					super.handleMessage(msg);
				}

			};
		
	}
		
		public File getMediaDir() {
			File dir = new File(Environment.getExternalStorageDirectory(),
					"NotesMediaDownload");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!Thread.currentThread().isInterrupted() && isRun == true) {
				/*
				 * 连接注册进程
				 */
				System.out.println("-------------线程启动");
				
				 path = new File(getMediaDir(),filename).toString();
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("filename", filenameWithOutWav);
				map.put("fileLength", Long.toString(new File(getMediaDir(),path).length()));
				map.put("userCode", "10000000");
				DownloadConnecter downconnecter = new DownloadConnecter(map);
				try {
					downconnecter.getXML();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				downconnecter.readXML();
				getObject down = new getObject(downconnecter.getData(), path);
				try {
					down.work();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(down.getResult()+"");

				Message m = handler.obtainMessage(); // 获取一个Message
				Bundle bundle = new Bundle(); // 获取Bundle对象
				m.what = 1; // 设置消息标识
				
				
				bundle.putString("result", down.getResult());
				// bundle.putString("ID", data.getId()); //保存数据

				//bundle.putLong("ID", data.getId()); // 保存数据

				m.setData(bundle); // 将Bundle对象保存到Message中
				handler.sendMessage(m); // 发送消息
				isRun = false;

			}
			
		}
		
		

	private boolean isRun = false;
	private Handler handler;

		
	}
}
