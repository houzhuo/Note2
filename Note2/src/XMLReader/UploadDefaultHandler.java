package XMLReader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * read XML which return by Upload function
 * @author Kobe Ebanks
 *
 */
public class UploadDefaultHandler extends DefaultHandler{
	private String value = null;
	private UploadData data = new UploadData();
	@Override
	/**
	 * Traversal start Tag
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
//		data = new UploadData();
	}
	/**
	 * save messages into UploadData object
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if (qName.equals("authorization")) {
			data.setAuthorization(value);
		} else if (qName.equals("MD5")) {
			data.setMD5(value);
		} else if (qName.equals("contentType")) {
			data.setContentType(value);
		} else if (qName.equals("bucket")) {
			data.setBucket(value);
		} else if (qName.equals("date")) {
			data.setDate(value);
		} else if (qName.equals("contentLength")) {
			data.setContentLength(value);
		} else if (qName.equals("cacheControl")) {
			data.setCacheControl(value);
		} else if (qName.equals("contentEncoding")) {
			data.setContentEncoding(value);
		}
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		value = new String(ch, start, length);
	}
	/**
	 * identity start parse
	 */
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}
	/**
	 * identity end parse
	 */
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}
	public UploadData getData() {
		return data;
	}
}
