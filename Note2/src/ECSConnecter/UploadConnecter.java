package ECSConnecter;
/**
 * get request header data
 * the data is an UploadDate object include Authorization, MD5, contentType, bucket, date, contentLength(, cacheControl(default: no-cache), contentEncoding(default: UTF-8), ossmeta(default: null))
 * @author Kobe Ebanks
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import HeaderGeter.HeaderGeter;
import XMLReader.UploadData;
import XMLReader.UploadXMLReader;

public class UploadConnecter extends Connecter{
	/**
	 * the url of manage file on server
	 */
	private final String HOST = "http://114.215.148.169:8083/upload.php";
	/**
	 * function name
	 */
	private final String function = "upload";
	/**
	 * request xml data
	 */
	private String xml = null;
	/**
	 * resquest data, return in getData() method as an UploadData object
	 */
	private UploadData data = null;
	/**
	 * HeaderGeter object
	 */
	private HeaderGeter headerGeter = null;
	/**
	 * structure function 
	 * @param map: include message: objectKey, contentType, MD5, contentLength(, cacheControl(default: no-cache), contentEncoding(default: UTF-8), ossmeta(default: null))
	 */
	public UploadConnecter(Map<String, String> map) {
		headerGeter = new HeaderGeter(function, map);
	}
	/**
	 * get request xml data
	 * @throws IOException 
	 */
	public void getXML() throws IOException {
		Geter geter = new Geter(headerGeter, HOST);
		xml = geter.get();
	}
	/**
	 * get request header data as an UploadData object
	 */
	public void readXML() {		
		UploadXMLReader uploadReader = new UploadXMLReader(new ByteArrayInputStream(xml.getBytes()));
		data = uploadReader.getData();
	}
	/**
	 * get request header data
	 * @return
	 */
	public UploadData getData() {
		return data;
	}
}
