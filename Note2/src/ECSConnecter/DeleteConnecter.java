package ECSConnecter;
/**
 * get response data
 * the data is a DeleteData object include status
 * @author Kobe Ebanks
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import HeaderGeter.HeaderGeter;
import XMLReader.DeleteData;
import XMLReader.DeleteXMLReader;

public class DeleteConnecter extends Connecter{
	/**
	 * the url of manage file on server
	 */
	private final String HOST = "http://114.215.148.169:8083/delete.php";
	/**
	 * function name
	 */
	private final String function = "delete";
	/**
	 * response xml data
	 */
	private String xml = null;
	/**
	 * response data, return in getData() method as a DeleteData object
	 */
	private DeleteData data = null;
	/**
	 * HeaderGeter object
	 */
	private HeaderGeter headerGeter = null;
	/**
	 * structure function 
	 * @param map: include filename, userCode
	 */
	public DeleteConnecter(Map<String, String> map) {
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
	 * get response data as a DeleteData object
	 */
	public void readXML() {
		DeleteXMLReader deleteReader = new DeleteXMLReader(new ByteArrayInputStream(xml.getBytes()));
		data = deleteReader.getData();
	}
	/**
	 * get response data
	 * @return
	 */
	public DeleteData getData() {
		return data;
	}
}
