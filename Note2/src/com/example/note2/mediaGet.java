/*package com.example.note2;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.example.note2.db.NotesDB;

import ECSConnecter.DownloadConnecter;
import OSSConnecter.getObject;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class MediaGet extends Thread{
	
	private String filename;
	private String filenameWithWav;
	private NotesDB dowloadDb;
	private SQLiteDatabase dbWrite;
	
	public MediaGet(String filename,String filenameWithWav){
		this.filename = filename;
		this.filenameWithWav = filenameWithWav;
	}
	
	public void downloadMedia(){
		System.out.println("------------------点击监听");
		isRun = true;
		// TODO Auto-generated method stub
		Thread t = new Thread(MediaGet.this);
		t.start();
		
		
		handler = new Handler() { // 这个handler发送的Message会被传递给主线程的MessageQueue。
			

			public void handleMessage(Message msg) { // 回调
				if (msg.what == 1) {
					if (msg.getData().getString("result")!=null){
						System.out.println(msg.getData().getString("result")+"");
						
						ContentValues cv = new ContentValues();
						cv.put(NotesDB.COLUMN_NAME_DOWLOAD_NAME, filename);
						dbWrite.insert(NotesDB.TABLE_NAME_NOTES, null, cv);
						
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
				"NotesMedia");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted() && isRun == true) {
			
			 * 连接注册进程
			 
			System.out.println("-------------线程启动");
			
			String path = new File(getMediaDir(),filename).toString();
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("filename", filenameWithWav);
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

	
}*/