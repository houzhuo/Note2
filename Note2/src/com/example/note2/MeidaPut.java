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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;



public class MeidaPut extends Thread{
	
	private String path = "";
	private String object = "";
	private String fileName = "";
	
	public  static String resultData;
	
	public MeidaPut(String path,String object, String fileName){
		this.path = path;
		this.object = object;
		this.fileName = fileName;
	
	}
		public String getResult(){
		return resultData;
		}
	
		public void upload(){
				System.out.println("------------------点击监听");
				isRun = true;
				// TODO Auto-generated method stub
				Thread t = new Thread(MeidaPut.this);
				t.start();

				handler = new Handler() { // 这个handler发送的Message会被传递给主线程的MessageQueue。
					
					public void handleMessage(Message msg) { // 回调
						if (msg.what == 1) {
							resultData = msg.getData().getString("result");
							if (resultData!=null){
								System.out.println(resultData+"");	
								
							}else {
								System.out.println(resultData+"");								
							}
							
						}
						super.handleMessage(msg);
					}
					
				};
				
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
		System.out.println("result  "+result);
	}
	
	public static SimpleDateFormat getTime() {
		return new SimpleDateFormat("yyyy MM dd  HH:mm:ss");
	}
	
	public static void uptest(String path,String object, String fileName) throws NoSuchAlgorithmException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("contentType", "text/plain");
		map.put("objectKey", "10000000/"+object);
		map.put("contentLength", Long.toString(new File(path).length()));
		UploadConnecter upconnecter = new UploadConnecter(map);
		upconnecter.getXML();
		upconnecter.readXML();
		putObject up = new putObject(upconnecter.getData(), "10000000/"+object, path);
		up.work();
		status = up.getStatus();
		if (up.getStatus() > 199 || up.getStatus() < 300) {
			logtest("testPut", "10000000/"+object, Long.toString(new File(path).length()), "application/octet-stream", fileName, "10000000", getTime().format(new Date()).toString(), "4:00");
		} else {
			System.out.println(up.getStatus());
			
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted() && isRun == true) {
			
			
			 
			System.out.println("-------------线程启动");
			
			try {
				uptest(path, object, fileName);
			} catch (NoSuchAlgorithmException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Message m = handler.obtainMessage(); // 获取一个Message
			Bundle bundle = new Bundle(); // 获取Bundle对象
			m.what = 1; // 设置消息标识
			
			bundle.putLong("result", status);
			// bundle.putString("ID", data.getId()); //保存数据

			//bundle.putLong("ID", data.getId()); // 保存数据

			m.setData(bundle); // 将Bundle对象保存到Message中
			handler.sendMessage(m); // 发送消息
			isRun = false;

		}
		
	}
private boolean isRun = false;
private Handler handler;
private static int status;
	
}
