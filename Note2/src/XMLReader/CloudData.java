package XMLReader;
/**
 * Save one object's message which will display at this request
 * @author Kobe Ebanks
 *
 */
public class CloudData {
	/**
	 * object's file name
	 */
	private String filename;
	/**
	 * object's length
	 */
	private String length;
	/**
	 * user set it
	 */
	private String remark;
	
	private String num;
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
