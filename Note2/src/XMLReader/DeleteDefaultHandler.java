package XMLReader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * read XML return by Delete function
 * @author Kobe Ebanks
 *
 */
public class DeleteDefaultHandler extends DefaultHandler{
	private String value = null;
	private DeleteData data = new DeleteData();
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
	}
	/**
	 * save message into DeleteData object
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
	
	public void startDocument() throws SAXException {
		super.startDocument();
	}
	
	public void endDocument() throws SAXException {
		super.endDocument();
	}
	
	public DeleteData getData() {
		return data;
	}
}
