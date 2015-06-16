package XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class TopicXMLReader {
	private TopicDefautlHandler handler = null;
	private TopicData data = null;
	
	public TopicXMLReader(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		handler = new TopicDefautlHandler();
		parser.parse(stream, handler);
		data = handler.getData();
		
	}
	
	public TopicData getData() {
		return data;
	}

}
