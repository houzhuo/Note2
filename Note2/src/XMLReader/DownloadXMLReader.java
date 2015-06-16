package XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
/**
 * use this class to read Download XML, save messages into DownloadData object
 * you can get informations by method:
 * public DownloadData getData()
 * @author Kobe Ebanks
 *
 */
public class DownloadXMLReader {
	private DownloadDefaultHandler handler = null;
	
	public DownloadXMLReader(InputStream stream) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
		handler = new DownloadDefaultHandler();
		parser.parse(stream, handler);
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DownloadData getData() {
		return handler.getData();
	}
}
