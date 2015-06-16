package HeaderGeter.XMLCreater;

public class SigninXMLCreater  extends XMLCreater{
	private String headers;
	private String name;
	private String password;
 
	public SigninXMLCreater(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public void create() {
		headers = "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<root>"
				+ "<name>"
				+ name
				+ "</name>"
				+ "<password>"
				+ password
				+ "</password>"
				+ "</root>";
	}
	
	public String getHeaders () {
		return headers;
	}
}
