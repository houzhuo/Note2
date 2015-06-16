package XMLReader;
/**
 * save headers which download needed
 * @author Kobe Ebanks
 *
 */
public class DownloadData {
	/**
	 * Authorization for OSS
	 */
	private String Authorization;
	/**
	 * GMT time
	 */
	private String date;
	/**
	 * bucket name
	 */
	private String bucket;
	/**
	 * 断点续传标记
	 */
	private String range = "0";
	/**
	 * 
	 */
	private String objectKey;
	
	
	public String getAuthorization() {
		return Authorization;
	}
	public void setAuthorization(String authorization) {
		Authorization = authorization;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getBucket() {
		return bucket;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public String getObjectKey() {
		return objectKey;
	}
	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}
}
