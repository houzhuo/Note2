package com.example.note2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ECSConnecter.LogConnecter;
import ECSConnecter.UploadConnecter;
import OSSConnecter.putObject;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;



public class MeidaPut extends Activity implements Runnable{
	
/*	private String path = "";
	private String object = "";
	private String fileName = "";
	
	
	
	public MeidaPut(String path,String object, String fileName){
		this.path = path;
		this.object = object;
		this.fileName = fileName;
		
		
		
		
	}
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media);
		findViewById(R.id.buttonMedia).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("------------------点击监听");
				isRun = true;
				// TODO Auto-generated method stub
				Thread t = new Thread(MeidaPut.this);
				t.start();
				
				handler = new Handler() { // 这个handler发送的Message会被传递给主线程的MessageQueue。
					public void handleMessage(Message msg) { // 回调
						if (msg.what == 1) {
							if (msg.getData().getString("result")!=null){
								System.out.println(msg.getData().getString("result")+"");
								Toast.makeText(getApplicationContext(), msg.getData().getString("result")+"", Toast.LENGTH_SHORT).show();
							}else {
								System.out.println(msg.getData().getString("result")+"");
								Toast.makeText(getApplicationContext(), "服务器去哪儿了。。", Toast.LENGTH_SHORT).show();
								
							}
							
						}
						super.handleMessage(msg);
					}

				};
			}
		});
	}
	
	public static String getMd5ByFile(File file) throws IOException, NoSuchAlgorithmException {  
        String value = null;  
        FileInputStream in = new FileInputStream(file);
        MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        md5.update(byteBuffer);  
        BigInteger bi = new BigInteger(1, md5.digest());  
        value = bi.toString(16);
        in.close();
//        return value.toUpperCase();  
        return value;
    }  
	
	public static void logtest(String remark, String objectKey, String contentLength, String contentType, String filename, String id, String time, String audioTime) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("objectKey", objectKey);
		map.put("contentLength", contentLength);
		map.put("contentType", contentType);
		map.put("filename", filename);
		map.put("remark", remark);
		map.put("userCode", id);
		map.put("time", time);
		map.put("audioTime", audioTime);
		LogConnecter logconnecter = new LogConnecter(map);
		logconnecter.getXML();
		logconnecter.readXML();
		String result = logconnecter.getData().getStatus();
		System.out.println(result);
	}
	
	public static SimpleDateFormat getTime() {
		return new SimpleDateFormat("yyyy MM dd  HH:mm:ss");
	}
	
	/*public static void uptest(String path,String object, String fileName) throws NoSuchAlgorithmException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("contentType", "text/plain");
		map.put("objectKey", "10000000/"+object);
		map.put("contentLength", Long.toString(new File(path).length()));
		UploadConnecter upconnecter = new UploadConnecter(map);
		upconnecter.getXML();
		upconnecter.readXML();
		putObject up = new putObject(upconnecter.getData(), "10000000/"+object, path);
		up.work();
		if (up.getStatus() > 199 || up.getStatus() < 300) {
			logtest("testPut", "10000000/"+object, Long.toString(new File(path).length()), "application/octet-stream", fileName, "10000000", getTime().format(new Date()).toString(), "4:00");
		} else {
			System.out.println(up.getStatus());
		}
	}*/
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted() && isRun == true) {
			/*
			 * 连接注册进程
			 */
			System.out.println("-------------线程启动");
			Map<String, String> map = new HashMap<String, String>();
			map.put("contentType", "audio/wav");
			map.put("objectKey", "10000000/1434350533518.wav");
			map.put("contentLength", Long.toString(new File("/storage/emulated/0/NotesMedia/1434350533518.wav").length()));
			UploadConnecter upconnecter = new UploadConnecter(map);
			try {
				upconnecter.getXML();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			upconnecter.readXML();
			putObject up = new putObject(upconnecter.getData(), "10000000/1434350533518.wav", "/storage/emulated/0/NotesMedia/1434350533518.wav");
			try {
				up.work();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (up.getStatus() > 199 || up.getStatus() < 300) {
				try {
					logtest("testPut", "10000000/1434350533518.wav", Long.toString(new File("/storage/emulated/0/NotesMedia/1434350533518.wav").length()), "application/octet-stream", "1434350533518", "10000000", getTime().format(new Date()).toString(), "4:00");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println(up.getStatus());
			}
			//uptest("/storage/emulated/0/NotesMedia/1434350533518.wav", "1434350533518.wav", "1434350533518");

			Message m = handler.obtainMessage(); // 获取一个Message
			Bundle bundle = new Bundle(); // 获取Bundle对象
			m.what = 1; // 设置消息标识

			bundle.putLong("result", up.getStatus());
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
