package XMLReader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SigninDefaultHandler extends DefaultHandler{
	private String value = null;
	private SigninData data = new SigninData();
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if (qName.equals("result")) {
			data.setResult(value);
		} else if (qName.equals("id")) {
			data.setId(value);
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		value = new String(ch,start, length);
	}
	
	public void startDocument() throws SAXException {
		super.startDocument();
	}
	
	public void endDocument() throws SAXException {
		super.endDocument();
	}
	
	public SigninData getData() {
		return data;
	}
}
