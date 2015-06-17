package com.example.note2;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import ECSConnecter.DownloadConnecter;
import OSSConnecter.getObject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class mediaGet extends Thread{
	
	private String filename;
	private String filenameWithWav;
	
	public mediaGet(String filename,String filenameWithWav){
		this.filename = filename;
		this.filenameWithWav = filenameWithWav;
	}
	
	public void downloadMedia(){
		System.out.println("------------------�������");
		isRun = true;
		// TODO Auto-generated method stub
		Thread t = new Thread(mediaGet.this);
		t.start();
		
		handler = new Handler() { // ���handler���͵�Message�ᱻ���ݸ����̵߳�MessageQueue��
			

			public void handleMessage(Message msg) { // �ص�
				if (msg.what == 1) {
					if (msg.getData().getString("result")!=null){
						System.out.println(msg.getData().getString("result")+"");
							
						
					}else {
						System.out.println(msg.getData().getString("result")+"");								
						
					}
					
				}
				super.handleMessage(msg);
			}

		};
	
}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted() && isRun == true) {
			/*
			 * ����ע�����
			 */
			System.out.println("-------------�߳�����");
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("filename", filename);
			map.put("fileLength", Long.toString(new File("D:/Wisemen.mp3").length()));
			map.put("userCode", "10000000");
			DownloadConnecter downconnecter = new DownloadConnecter(map);
			try {
				downconnecter.getXML();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			downconnecter.readXML();
			getObject down = new getObject(downconnecter.getData(), "D:/Wisemen.mp3");
			try {
				down.work();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(down.getResult());

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

	
}