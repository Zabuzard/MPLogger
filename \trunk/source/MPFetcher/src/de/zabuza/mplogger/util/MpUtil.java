package de.zabuza.mplogger.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class for market price processing.
 * 
 * @author Zabuza
 *
 */
public final class MpUtil {
	
	/**
	 * Gets the content of a web page and returns it as list of lines.
	 * @param path Path to the web page
	 * @return List of lines from the content
	 * @throws IOException If an I/O-Exception occurs
	 */
	public static List<String> getWebContent(String path) throws IOException {
		URL url = new URL(path);
		BufferedReader site = new BufferedReader(new InputStreamReader(url.openStream()));
		List<String> content = new LinkedList<String>();
		
		String line = site.readLine();
		while (line != null){
			content.add(line);
			line = site.readLine();
		}
		
		site.close();
		return content;
	}
	
	/**
	 * Utility class. No implementation.
	 */
	private MpUtil() {

	}
}