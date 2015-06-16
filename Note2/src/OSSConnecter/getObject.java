package OSSConnecter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import XMLReader.DownloadData;

public class getObject {
	private DownloadData data = null;
	private String path;
	private String HOST;
	private String result;
	private int status;
	
	public getObject(DownloadData data, String path) {
		this.data = data;
		this.path = path;
		this.HOST = "http://"
				+ data.getBucket()
				+ ".oss-cn-shenzhen.aliyuncs.com/";
	}
	
	public void saveToFile(HttpResponse response) throws IllegalStateException, IOException {
		InputStream input = response.getEntity().getContent();
		OutputStream output = new FileOutputStream(path);
		int bytesCount = 0;
		byte[] bytes = new byte[4*1024];
		while ((bytesCount = input.read(bytes)) != -1) {
			output.write(bytes, 0, bytesCount);
		}
		input.close();
		output.close();
	}

	public void work() throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(HOST+data.getObjectKey());
		httpget.setHeader("Authorization", data.getAuthorization());
		httpget.setHeader("Date", data.getDate());
		httpget.setHeader("ContentType", "");
		if (!data.getRange().equals("0") || !data.getRange().equals("")) {
			httpget.setHeader("Range", data.getRange());
		}
		HttpResponse response = httpclient.execute(httpget);
		status = response.getStatusLine().getStatusCode();
		if (status > 199 || status < 300) {
			saveToFile(response);
			this.result = "Download Success";
		} else {
			this.result = "Download Fail";
		}
	}
	
	public String getResult() {
		return result;
	}
}
