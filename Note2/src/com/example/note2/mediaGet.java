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
		System.out.println("------------------�������");
		isRun = true;
		// TODO Auto-generated method stub
		Thread t = new Thread(MediaGet.this);
		t.start();
		
		
		handler = new Handler() { // ���handler���͵�Message�ᱻ���ݸ����̵߳�MessageQueue��
			

			public void handleMessage(Message msg) { // �ص�
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
			
			 * ����ע�����
			 
			System.out.println("-------------�߳�����");
			
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

			Message m = handler.obtainMessage(); // ��ȡһ��Message
			Bundle bundle = new Bundle(); // ��ȡBundle����
			m.what = 1; // ������Ϣ��ʶ
			
			
			bundle.putString("result", down.getResult());
			// bundle.putString("ID", data.getId()); //��������

			//bundle.putLong("ID", data.getId()); // ��������

			m.setData(bundle); // ��Bundle���󱣴浽Message��
			handler.sendMessage(m); // ������Ϣ
			isRun = false;

		}
		
	}
	
	

private boolean isRun = false;
private Handler handler;

	
}*/