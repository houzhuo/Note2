package HeaderGeter.XMLCreater;
/**
 * create request XML for Cloud to get list info
 * @author Kobe Ebanks
 *
 */
public class CloudXMLCreater extends XMLCreater{
	private String headers;
	private String userCode;
	private String page;
	
	public CloudXMLCreater (String userCode, String page) {
		this.userCode = userCode;
		this.page = page;
	}
	
	public void create () {
		headers = "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<root>"
				+ "<code>"
				+ userCode
				+ "</code>"
				+ "<page>"
				+ page
				+ "</page>";
	}
	
	public void setNumber (String number) {
		headers += "<number>"
				+ number
				+ "</number>";
	}
	
	/**
	 * @return the headers
	 */
	public String getHeaders () {
		headers += "</root>";
		return headers;
	}
}
