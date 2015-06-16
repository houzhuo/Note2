package XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
/**
 * use this class to read Log XML, save message into LogData object
 * you can get information by method:
 * public LogData getData()
 * @author Kobe Ebanks
 *
 */
public class LogXMLReader {
	private LogDefaultHandler handler = null;
	
	public LogXMLReader(InputStream stream){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
		handler = new LogDefaultHandler();
		parser.parse(stream, handler);
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public LogData getData() {
		return handler.getData();
	}
}
