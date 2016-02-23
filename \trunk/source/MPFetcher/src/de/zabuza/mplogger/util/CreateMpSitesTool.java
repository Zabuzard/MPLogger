package de.zabuza.mplogger.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.zabuza.mplogger.WikiHub;

/**
 * Utility tool for creation of market price sites.
 * @author Zabuza
 *
 */
public class CreateMpSitesTool {
	/**
	 * Path to the file that contains external data.
	 */
	private static final String FILEPATH = "C:\\Users\\Zabuza\\Desktop\\";
	
	/**
	 * Gets the content of a file and returns it as list of lines.
	 * @param path Path to the file
	 * @return List of lines from the content
	 * @throws IOException If an I/O-Exception occurs
	 */
	public static List<String> getFileContent(String path) throws IOException {
		BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		List<String> content = new ArrayList<String>();
		
		String line = file.readLine();
		while (line != null){
			content.add(line);
			line = file.readLine();
		}
		
		file.close();
		return content;
	}
	/**
	 * Gets the content of a file that contains a table of items that have no
	 * market price site and generates them.
	 * @param args
	 *            Not supported
	 * @throws IOException 
	 */
	public static void main(final String[] args) throws IOException {
		/*
		 * If false the program will login to FreewarWiki
		 * and manipulate articles. If true the program simulates
		 * the process. This is a safety protection.
		 * 
		 * ********************** CAUTION **********************
		 * This, of course, may cause a heavy amount of web-traffic.
		 * If you use this frequently the server can interpret this as DDos!
		 * This could lead to legal consequences for the executor
		 * of this program!
		 * So use "false" only if you know what you do.
		 * ********************** CAUTION **********************
		 */
		boolean simulate = true;
		
		String filename = "noMpSiteItems.csv";
		List<String> list = getFileContent(FILEPATH + filename);
		WikiHub hub = new WikiHub();
		
		for (String item : list) {
			hub.createMpSite(item, simulate);
		}
	}
	
	/**
	 * Utility class. No implementation.
	 */
	private CreateMpSitesTool() {

	}
}
