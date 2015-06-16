package XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
/**
 * use this class to read Delete XML, save message into DeleteData object
 * you can get information by method:
 * public DeleteData getData()
 * @author Kobe Ebanks
 *
 */
public class DeleteXMLReader {
	private DeleteDefaultHandler handler = null;
	
	public DeleteXMLReader(InputStream stream){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
		handler = new DeleteDefaultHandler();
		parser.parse(stream, handler);
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DeleteData getData() {
		return handler.getData();
	}
}
