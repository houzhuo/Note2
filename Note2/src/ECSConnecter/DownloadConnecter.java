package ECSConnecter;
/**
 * get request header data
 * the data is a DownloadData object include Authorization, url, date, contentType(,range, cacheControl)
 * @author Kobe Ebanks
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import HeaderGeter.HeaderGeter;
import XMLReader.DownloadData;
import XMLReader.DownloadXMLReader;

public class DownloadConnecter extends Connecter{
	/**
	 * the url of manage file on server
	 */
	private final String HOST = "http://121.199.57.37:8081/Voice_Note/download.php";
	/**
	 * function name
	 */
	private final String function = "download";
	/**
	 * request xml data
	 */
	private String xml = null;
	/**
	 * request data, return in getData() method as a DownloadData object
	 */
	private DownloadData data = null;
	/**
	 * HeaderGeter object
	 */
	private HeaderGeter headerGeter = null;
	/**
	 * structure function
	 * @param map: include messages: filename, fileLength, useCode(, cacheControl(default: no-cache), range)
	 */
	public DownloadConnecter(Map<String, String> map) {
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
	 * get request header data as a  DownloadData object 
	 */
	public void readXML() {
		DownloadXMLReader downloadReader = new DownloadXMLReader(new ByteArrayInputStream(xml.getBytes()));
		data = downloadReader.getData();
	}
	/**
	 * get request header data
	 * @return
	 */
	public DownloadData getData() {
		return data;
	}
}
