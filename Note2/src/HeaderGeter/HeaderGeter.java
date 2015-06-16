package HeaderGeter;

import java.util.Map;

import HeaderGeter.XMLCreater.CloudXMLCreater;
import HeaderGeter.XMLCreater.DeleteXMLCreater;
import HeaderGeter.XMLCreater.DownloadXMLCreater;
import HeaderGeter.XMLCreater.LogXMLCreater;
import HeaderGeter.XMLCreater.LoginXMLCreater;
import HeaderGeter.XMLCreater.SigninXMLCreater;
import HeaderGeter.XMLCreater.TopicXMLCreater;
import HeaderGeter.XMLCreater.UploadXMLCreater;

/**
 * Connecters use this to get xml which used to connect ECS server
 * Upload and Download function will return a request xml include headers, which needed to be used while connect OSS
 * Log, Cloud and Delete function will return a status in response xml
 * @author Kobe Ebanks
 *
 */
public class HeaderGeter {
	private String function = null;
	private String filename = null;
	private String objectKey = null;
	private String contentType = null;
	private String contentLength = null;
	private String fileLength = null;
	private String remark = null;
	private String page = null;
	private String number = null;
	private String userCode = null;
	private String xml = null;
	private String time = null;
	private String name = null;
	private String password =null;
	private String aimStr = null;//要进行关键字分析的片段
	private String audioTime;
	/**
	 * 
	 * @param function: choose an function to do
	 * @param map: save the keys and values needed
	 */
	public HeaderGeter(String function, Map<String, String> map) {
		this.function = function.toLowerCase();
		for(Map.Entry<String, String>entry:map.entrySet()) {
			if (entry.getKey().equals("filename")) {
				this.filename = entry.getValue();
			} else if (entry.getKey().equals("objectKey")) {
				this.objectKey = entry.getValue();
			} else if (entry.getKey().equals("contentType")) {
				this.contentType = entry.getValue();
			} else if (entry.getKey().equals("contentLength")) {
				this.contentLength = entry.getValue();
			} else if (entry.getKey().equals("remark")) {
				this.remark = entry.getValue();
			} else if (entry.getKey().equals("page")) {
				this.page = entry.getValue();
			} else if (entry.getKey().equals("number")) {
				this.number = entry.getValue();
			} else if (entry.getKey().equals("userCode")) {
				this.userCode = entry.getValue();
			} else if (entry.getKey().equals("fileLength")) {
				this.fileLength = entry.getValue();
			} else if (entry.getKey().equals("time")) {
				this.time = entry.getValue();
			} else if (entry.getKey().equals("audioTime")) {
				this.audioTime = entry.getValue();
			} else if (entry.getKey().equals("name")) {
				this.name = entry.getValue();
			} else if (entry.getKey().equals("password")) {
				this.password = entry.getValue();
			} else if (entry.getKey().equals("aimStr")) {
				this.aimStr = entry.getValue();
			}
		}
	}
	
	public void getUploadXML() {
		UploadXMLCreater creater = new UploadXMLCreater(objectKey, contentType, contentLength);
		creater.create();
		xml = creater.getHeaders();
	}
	
	public void getCloudeXML() {
		CloudXMLCreater creater = new CloudXMLCreater(userCode, page);
		creater.create();
		if (number != null) {
			creater.setNumber(number);
		}
		xml = creater.getHeaders();
	}
	
	public void getDownloadXML() {
		DownloadXMLCreater creater = new DownloadXMLCreater(filename, fileLength, userCode);
		creater.create();
		xml = creater.getHeaders();
	}
	
	public void getDeleteXML() {
		DeleteXMLCreater creater = new DeleteXMLCreater(filename, userCode);
		creater.create();
		xml = creater.getHeaders();
	}
	
	public void getLogXML() {
		LogXMLCreater creater = new LogXMLCreater(objectKey, contentLength, contentType, filename, userCode, time, audioTime);
		creater.create();
		if (!remark.isEmpty()) {
			creater.setRemark(remark);
		}
		xml = creater.getHeaders();
	}
	
	public void getSigninXML() {
		SigninXMLCreater creater = new SigninXMLCreater(name, password);
		creater.create();
		xml = creater.getHeaders();
	}
	
	public void getLoginXML() {
		LoginXMLCreater creater = new LoginXMLCreater(name, password);
		creater.create();
		xml = creater.getHeaders();
	}
	
	public void getTopicXML() {
		TopicXMLCreater creater = new TopicXMLCreater(aimStr);
		creater.create();
		xml = creater.getHeaders();
	}
	
	public void work() {
		if (function.equals("upload")) {
			this.getUploadXML();
		} else if (function.equals("download")) {
			this.getDownloadXML();
		} else if (function.equals("cloud")) {
			this.getCloudeXML();
		} else if (function.equals("delete")) {
			this.getDeleteXML();
		} else if (function.equals("log")) {
			this.getLogXML();
		} else if (function.equals("signin")) {
			this.getSigninXML();
		} else if (function.equals("login")) {
			this.getLoginXML();
		} else if (function.equals("topic")) {
			this.getTopicXML();
		} else {
			xml = null;
		}
	}
	/**
	 * get xml data
	 * Upload and Download function will return a request xml include headers, which needed to be used while connect OSS
	 * Log, Cloud and Delete function will return a status in response xml
	 * @return
	 */
	public String getXml() {
		return xml;
	}
}
