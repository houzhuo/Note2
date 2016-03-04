package OSSConnecter;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import XMLReader.UploadData;

public class putObject {
	private UploadData data = null;
	private String objectKey;
	private String path;
	private String HOST;
	private int status;
	
	public putObject(UploadData data, String objectKey, String path) {
		this.data = data;
		this.objectKey = objectKey;
		this.path = path;
		this.HOST = "http://"
				+ data.getBucket()
				+ ".oss-cn-qingdao.aliyuncs.com/";
	}
	
	public FileEntity openFile() {
		File file = new File(path);
//		FileEntity entity = new FileEntity(file);
		FileEntity entity = new FileEntity(file, "audio/wav");
		entity.setContentEncoding(data.getContentEncoding());
		entity.setContentType(data.getContentType());
		return entity;
	}
	
	public void work() throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(HOST+objectKey);
		httpput.setHeader("Authorization", data.getAuthorization());
		httpput.setHeader("Content-Type", data.getContentType());
		httpput.setHeader("Date", data.getDate());
		httpput.setEntity(openFile());
		HttpResponse response = httpclient.execute(httpput);
		httpput.abort();
		status = response.getStatusLine().getStatusCode();
	}

	public int getStatus() {
		return status;
	}
}
