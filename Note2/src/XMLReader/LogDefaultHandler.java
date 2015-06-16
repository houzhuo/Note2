package XMLReader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * read XML which return by Log function
 * @author Kobe Ebanks
 *
 */
public class LogDefaultHandler extends DefaultHandler{
	private String value = null;
	private LogData data = new LogData();
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
	}
	/**
	 * save message into LogData object
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if (qName.equals("result")) {
			data.setStatus(value);
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		value = new String(ch, start, length);
	}
	
	public void startDocment() throws SAXException {
		super.startDocument();
	}
	
	public void endDocument() throws SAXException {
		super.endDocument();
	}
	
	public LogData getData() {
		return data;
	}
}
