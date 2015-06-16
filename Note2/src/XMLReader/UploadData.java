package XMLReader;
/**
 * save the headers which upload needed
 * @author Kobe Ebanks
 *
 */
public class UploadData {
	/**
	 * Authorization for OSS
	 */
	private String Authorization;
	/**
	 * file's MD5
	 */
	private String MD5;
	/**
	 * file's type
	 */
	private String contentType;
	/**
	 * bucket's name
	 */
	private String bucket;
	/**
	 * GMT time
	 */
	private String date;
	/**
	 * file's length
	 */
	private String contentLength;
	/**
	 * 
	 */
	private String cacheControl = "no-cache";
	/**
	 * file's content-encoding
	 */
	private String contentEncoding = "UTF-8";
	
	public String getAuthorization() {
		return Authorization;
	}
	public void setAuthorization(String authrization) {
		Authorization = authrization;
	}
	public String getMD5() {
		return MD5;
	}
	public void setMD5(String mD5) {
		this.MD5 = mD5;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getBucket() {
		return bucket;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContentLength() {
		return contentLength;
	}
	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}
	public String getCacheControl() {
		return cacheControl;
	}
	public void setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
	}
	public String getContentEncoding() {
		return contentEncoding;
	}
	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}
}
