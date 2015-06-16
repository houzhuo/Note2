package HeaderGeter.XMLCreater;
/**
 * create request XML for Upload to get info to connect OSS
 * @author Kobe Ebanks
 *
 */
public class UploadXMLCreater extends XMLCreater{
	private String headers;
	private String objectKey;
	private String contentType;
	private String contentLength;
	
	/**
	 * get necessary into a xml
	 * @param objectKey
	 * @param contentType
	 * @param MD5
	 * @param contentLength
	 */
	public UploadXMLCreater(String objectKey, String contentType, String contentLength) {
		this.objectKey = objectKey;
		this.contentType = contentType;
		this.contentLength = contentLength;
	}
	
	public void create(){
		headers = "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<root>"
				+ "<contentType>"
				+ contentType
				+ "</contentType>"
				+ "<object>"
				+ objectKey
				+ "</object>"
				+ "<contentLength>"
				+ contentLength
				+ "</contentLength>";
	}
	
	/**
	 * add cachecontrol into the xml
	 * @param cacheControl
	 */
	public void setCacheControl(String cacheControl) {
		headers = headers
				+ "<cacheControl>"
				+ cacheControl
				+ "</cacheControl>";
	}
	
	/**
	 * add contentEncoding into the xml
	 * @param contentEncoding
	 */
	public void setcontentEncoding(String contentEncoding) {
		headers = headers
				+ "<contentEncoding>"
				+ contentEncoding
				+ "</contentEncoding>";
	}
	/**
	 * 
	 * @param meta
	 */
//	public void setOSSmeta(String meta) {}
	
	/**
	 * @return the headers
	 */
	public String getHeaders() {
		headers =headers
				+ "</root>";
		return headers;
	}
}
