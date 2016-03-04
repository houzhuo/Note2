package ECSConnecter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import HeaderGeter.HeaderGeter;
import XMLReader.LoginData;
import XMLReader.LoginXMLReader;

public class LoginConnecter extends Connecter{
	private final String HOST = "http://114.215.148.169:8083/landing.php";
	private final String function = "login";
	private String  xml = null;
	private LoginData data = null;
	private HeaderGeter headerGeter = null;
	
	public LoginConnecter(Map<String, String> map) {
		headerGeter = new HeaderGeter(function, map);
	}
	
	public void getXML () throws IOException {
		Geter geter = new Geter(headerGeter, HOST);
		xml = geter.get();
	}
	
	public void readXML () {
		LoginXMLReader loginreader = new LoginXMLReader(new ByteArrayInputStream(xml.getBytes()));
		data = loginreader.getData();
	}
	
	public LoginData getData() {
		return data;
	}
}
