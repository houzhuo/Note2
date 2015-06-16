package XMLReader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * read XML which return by Download function
 * @author Kobe Ebanks
 *
 */
public class DownloadDefaultHandler extends DefaultHandler{
	private String value = null;
	private DownloadData data =  new DownloadData();
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
	}
	/**
	 * save messages into DownloadData object
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if (qName.equals("authorization")) {
			data.setAuthorization(value);
		} else if (qName.equals("bucket")) {
			data.setBucket(value);
		} else if (qName.equals("date")) {
			data.setDate(value);
		} else if (qName.equals("range")) {
			data.setRange(value);
		} else if (qName.equals("objectKey")) {
			data.setObjectKey(value);
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
	
	public DownloadData getData() {
		return data;
	}
}
