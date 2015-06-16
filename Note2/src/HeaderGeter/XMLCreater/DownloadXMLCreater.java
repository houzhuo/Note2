package HeaderGeter.XMLCreater;
/**
 * create request XML for Download to get request info to connect OSS
 * @author Kobe Ebanks
 *
 */
public class DownloadXMLCreater extends XMLCreater{
	private String headers;
	private String filename;
	private String fileLength;
	private String userCode;
	
	public DownloadXMLCreater (String filename, String fileLength, String user_code) {
		this.filename = filename;
		this.fileLength = fileLength;
		this.userCode = user_code;
	}
	
	public void create () {
		headers = "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<root>"
				+ "<filename>"
				+ filename
				+ "</filename>"
				+ "<fileLength>"
				+ fileLength
				+ "</fileLength>"
				+ "<code>"
				+ userCode
				+ "</code>";
	}
	
	/**
	 * @return the headers
	 */
	public String getHeaders () {
		headers += "</root>";
		return headers;
	}
}
