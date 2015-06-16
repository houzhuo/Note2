package XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
/**
 * use this class to read Upload XML, save messages into UploadData object
 * you can get informations by method: 
 * public UploadData getData()
 * @author Kobe Ebanks
 *
 */
public class UploadXMLReader {
	private UploadDefaultHandler handler = null;
		
	public UploadXMLReader(InputStream stream) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			handler = new UploadDefaultHandler();
			parser.parse(stream, handler);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public UploadData getData() {
		return handler.getData();
	}
}
