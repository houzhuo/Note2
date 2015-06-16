package ECSConnecter;
/**
 * get response data
 * the data is a LogData object, include status
 * @author Kobe Ebanks
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import HeaderGeter.HeaderGeter;
import XMLReader.LogData;
import XMLReader.LogXMLReader;

public class LogConnecter extends Connecter{
	/**
	 * the url manage file on servicer
	 */
	private final String HOST = "http://121.199.57.37:8081/Voice_Note/log.php";
	/**
	 * function name
	 */
	private final String function = "log";
	/**
	 * response xml data
	 */
	private String xml = null;
	/**
	 * response data, return in getData() method as a LogData object
	 */
	private LogData data = null;
	/**
	 * HeaderGeter object
	 */
	private HeaderGeter headerGeter = null;
	/**
	 * structure function
	 * @param map: include message: objectKey, contentLength, filename, useCodem, contentType(, remark)
	 */
	public LogConnecter(Map<String, String> map) {
		headerGeter = new HeaderGeter(function, map);
	}
	/**
	 * get response xml data
	 * @throws IOException 
	 */
	public void getXML() throws IOException {
		Geter geter = new Geter(headerGeter, HOST);
		xml = geter.get();
	}
	/**
	 * get response data as a LogData
	 */
	public void readXML() {
		LogXMLReader logReader = new LogXMLReader(new ByteArrayInputStream(xml.getBytes()));
		data = logReader.getData();
	}
	/**
	 * get response data
	 * @return
	 */
	public LogData getData() {
		return data;
	}
}
