package ECSConnecter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import HeaderGeter.HeaderGeter;
import XMLReader.SigninData;
import XMLReader.SigninXMLReader;

public class SigninConnecter extends Connecter{
	private final String HOST = "http://114.215.148.169:8083/register.php";
	private final String function = "signin";
	private String xml = null;
	private SigninData data;
	private HeaderGeter headerGeter = null;
	
	public SigninConnecter(Map<String, String> map) {
		headerGeter = new HeaderGeter(function, map);
	}
	
	public void getXML () throws IOException {
		Geter geter = new Geter(headerGeter, HOST);
		xml = geter.get();
	}
	
	public void readXML() {
		SigninXMLReader signinreader = new SigninXMLReader(new ByteArrayInputStream(xml.getBytes()));
		data =signinreader.getData();
	}
	
	public SigninData getData() {
		return data;
	}
}
