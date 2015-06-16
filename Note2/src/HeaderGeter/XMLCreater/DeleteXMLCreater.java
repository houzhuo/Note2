package HeaderGeter.XMLCreater;
/**
 * create request XML for Delete to get status
 * @author Kobe Ebanks
 *
 */
public class DeleteXMLCreater extends XMLCreater{
	private String headers;
	private String filename;
	private String userCode;
	
	public DeleteXMLCreater (String filename, String userCode) {
		this.filename =filename;
		this.userCode = userCode;
	}
	
	public void create  () {
		headers = "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<root>"
				+ "<filename>"
				+ filename
				+ "</filename>"
				+ "<code>"
				+ userCode
				+ "</code>";
	}
	
	public String getHeaders () {
		headers += "</root>";
		return headers;
	}
}
