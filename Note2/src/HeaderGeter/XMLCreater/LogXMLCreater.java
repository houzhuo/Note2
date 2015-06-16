package HeaderGeter.XMLCreater;
/**
 * create request XMLfor Log to get status
 * @author Kobe Ebanks
 *
 */
public class LogXMLCreater extends XMLCreater{
	private String headers;
	private String objectKey;
	private String contentType;
	private String contentLength;
	private String filename;
	private String userCode;
	private String time;
	private String audioTime;
	
	public LogXMLCreater (String objectKey, String contentLength, String contentType, String filename, String userCode, String time, String audioTiem) {
		this.objectKey = objectKey;
		this.contentLength = contentLength;
		this.contentType = contentType;
		this.filename = filename;
		this.userCode = userCode;
		this.time = time;
		this.audioTime = audioTiem;
	}
	
	public void create () {
		headers = "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<root>"
				+ "<filename>"
				+ filename
				+ "</filename>"
				+ "<code>"
				+ userCode
				+ "</code>"
				+ "<objectKey>"
				+ objectKey
				+ "</objectKey>"
				+ "<contentType>"
				+ contentType
				+ "</contentType>"
				+ "<contentLength>"
				+ contentLength
				+ "</contentLength>"
				+ "<time>"
				+ time
				+ "</time>"
				+ "<audioTime>"
				+ audioTime
				+ "</audioTime>";
	}
	
	public void setRemark (String remark) {
		headers += "<remark>"
				+ remark
				+ "</remark>";
	}
	
	/**
	 * @return the headers
	 */
	public String getHeaders () {
		headers += "</root>";
		return headers;
	}
}
