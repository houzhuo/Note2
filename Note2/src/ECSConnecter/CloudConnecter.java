package ECSConnecter;
/**
 * get response data
 * the data is an ArrayList<CloudData> include filename, length and remark of objects
 * default ten objects once
 * @author Kobe Ebanks
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import HeaderGeter.HeaderGeter;
import XMLReader.CloudData;
import XMLReader.CloudXMLReader;

public class CloudConnecter extends Connecter{
	/**
	 * the url of manage file on server
	 */
	private final String HOST = "http://121.199.57.37:8081/Voice_Note/cloud.php";
	/**
	 * function name
	 */
	private final String function = "cloud";
	/**
	 * response xml data
	 */
	private String xml = null;
	/**
	 * response data, return in getData() method
	 * @type:A rrayList<CloudData>
	 */
	private ArrayList<CloudData> data = null;
	/**
	 * HeaderGeter object, use to get xml data
	 */
	private HeaderGeter headerGeter = null;
	/**
	 * structure function  
	 * @param map: need message in it: userCode(, page(if is not first page, this is necessary), number(default ten))
	 */
	public CloudConnecter(Map<String, String> map) {
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
	 * get response data in an ArrayList<CloudData>
	 */
	public void readXML() {
		CloudXMLReader cloudReader = new CloudXMLReader(new ByteArrayInputStream(xml.getBytes()));
		data = cloudReader.getData();
	}
	/**
	 * get response data
	 * @return
	 */
	public ArrayList<CloudData> getData() {
		return data;
	}
}
