package XMLReader;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * Read XML which return by Cloud function
 * @author Kobe Ebanks
 *
 */
public class CloudDefaultHandler extends DefaultHandler{
	private String value = null;
	private CloudData data = null;
	ArrayList<CloudData> dataList = new ArrayList<>();
	@Override
	/**
	 * Traversal start Tag
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if(qName.equals("data")){
			data = new CloudData();
			data.setNum(attributes.getValue("num"));
		} else if(!qName.equals("root")){
		
		}
	}
	/**
	 * save messages into ArrayList<CloudData> as CloudData object
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if(qName.equals("filename")){
			data.setFilename(value);
		} else if (qName.equals("length")) {
			data.setLength(value);
		} else if (qName.equals("remark")) {
			data.setRemark(value);
		} else if(qName.equals("data")){
			dataList.add(data);
			data = null;
		}
	}
	public ArrayList<CloudData> getDataList() {
		return dataList;
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
}
