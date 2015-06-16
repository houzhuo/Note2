package ECSConnecter;
/**
 * Use request xml data to get response xml data
 * @author Kobe Ebanks
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import HeaderGeter.HeaderGeter;

public class Geter {
	/**
	 * the url manage file on servicer
	 */
	private String HOST = null;
	/**
	 * HeaderGeter object, use to get xml data
	 */
	private HeaderGeter headerGeter = null;
	/**
	 * response xml data
	 */
	private String xml = "";
	private String hostXML = "";
	/**
	 * structure function
	 * @param headerGeter: save headerGeter object
	 * @param HOST: save the url 
	 */
	public Geter(HeaderGeter headerGeter, String HOST) {
		this.headerGeter = headerGeter;
		this.HOST = HOST;
	}
	/**
	 * get response xml data
	 * @return
	 * @throws IOException 
	 */
	public String get() throws IOException {
		headerGeter.work();
		this.hostXML = headerGeter.getXml();
				
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			URL url = new URL(HOST);
//			URL url = new URL("http://121.42.42.246/testXML.php");
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Pragma", "no-cache");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Content-Type", "text/xml");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(this.hostXML);
			out.flush();
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				xml += line;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.close();
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xml;
	}
}
