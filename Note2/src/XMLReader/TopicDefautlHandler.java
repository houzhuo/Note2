package XMLReader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TopicDefautlHandler extends DefaultHandler{
	private String value = null;
	private TopicData data = new TopicData();
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (!qName.equalsIgnoreCase("root")) {
			data.addResult(value);
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		value = new String(ch, start, length);
	}
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
	
	public TopicData getData() {
		return data;
	}
}
