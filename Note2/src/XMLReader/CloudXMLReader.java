package XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
/**
 * use this class to read Cloud XML, save messages into CloudData object
 * you can get informations in a ArrayList by method:
 * public ArrayList<CloudData> getData()
 * @author Kobe Ebanks
 *
 */
public class CloudXMLReader {
	private CloudDefaultHandler handler = null;
	
	public CloudXMLReader(InputStream stream) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			handler = new CloudDefaultHandler();
			parser.parse(stream, handler);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<CloudData> getData() {
		return handler.getDataList();
	}
}
