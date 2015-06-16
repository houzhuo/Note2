package XMLReader;

import java.util.ArrayList;

public class TopicData {
	private ArrayList<String> result;
	
	public TopicData() {
		this.result = new ArrayList<String>();
	}
	
	public ArrayList<String> getResult() {
		return result;
	}
	
	public void addResult(String str) {
		result.add(str);
	}
	
}
