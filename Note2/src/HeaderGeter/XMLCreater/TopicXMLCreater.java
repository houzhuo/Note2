package HeaderGeter.XMLCreater;

public class TopicXMLCreater {
	private String headers;
	private String aimStr;
	
	public TopicXMLCreater(String aimStr) {
		this.aimStr = aimStr;
	}
	
	public void create() {
		headers = "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<root>"
				+ "<str>"
				+ aimStr
				+ "</str>"
				+ "</root>";
	}
	
	public String getHeaders() {
		return headers;
	}
}
