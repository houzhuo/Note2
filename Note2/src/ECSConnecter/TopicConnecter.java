package ECSConnecter;

import java.io.ByteArrayInputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import HeaderGeter.HeaderGeter;
import XMLReader.TopicData;
import XMLReader.TopicXMLReader;

public class TopicConnecter {
	private final String HOST = "http://121.199.57.37:8081/Voice_Note/topic.php";
	private final String function = "topic";
	private String xml;
	private TopicData data;
	private HeaderGeter headerGeter;
	
	public TopicConnecter(Map<String, String> map) {
		headerGeter = new HeaderGeter(function, map);
	}
	
	public void getXML () throws IOException{
		Geter geter = new Geter(headerGeter, HOST);
		xml = geter.get();
	}
	
	public void readXML () throws ParserConfigurationException, SAXException, IOException {
		TopicXMLReader reader = new TopicXMLReader(new ByteArrayInputStream(xml.getBytes()));
		data = reader.getData();
	}
	
	public TopicData getData() {
		return data;
	}
}
