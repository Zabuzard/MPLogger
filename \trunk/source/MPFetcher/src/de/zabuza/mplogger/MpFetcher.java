package de.zabuza.mplogger;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import de.zabuza.mplogger.data.Item;
import de.zabuza.mplogger.data.MpEntryWrapper;
import de.zabuza.mplogger.util.MpUtil;

/**
 * Fetches market price information of items and from external databases.
 * @author Zabuza
 *
 */
public final class MpFetcher {
	
	/**
	 * URL to database that contains external market price information data.
	 */
	private static final String FETCH_DB_URL =
			"http://zabuza.square7.ch/freewar/mplogger/exportContent.php";
	/**
	 * World-parameter for fetch query.
	 */
	private static final String FETCH_DB_PARAM_WORLD = "world";
	/**
	 * Day-parameter for fetch query.
	 */
	private static final String FETCH_DB_PARAM_DAY = "d";
	/**
	 * Month-parameter for fetch query.
	 */
	private static final String FETCH_DB_PARAM_MONTH = "m";
	/**
	 * Year-parameter for fetch query.
	 */
	private static final String FETCH_DB_PARAM_YEAR = "y";
	/**
	 * Character that denotes the begin of parameters in a PHP query.
	 */
	private static final char PHP_PARAM_BEGIN = '?';
	/**
	 * Character that denotes the allocation of a parameters in a PHP query.
	 */
	private static final char PHP_PARAM_ALLOC = '=';
	/**
	 * Character that denotes the separation of parameters in a PHP query.
	 */
	private static final char PHP_PARAM_SEPARATOR = '&';
	/**
	 * Amount of worlds Freewar has and that should be tracked.
	 */
	private static final int WORLD_AMOUNT = 14;
	/**
	 * Denotes the amount of worked entries until a print occurs.
	 */
	private static final int PRINT_EVERY_ENTRY = 50;
	
	/**
	 * Mask that denotes the start of content in the external query result.
	 */
	private static final String CONTENT_MASK_START = ">Entries:";
	/**
	 * Mask that splits content in the external query result.
	 */
	private static final String CONTENT_SPLIT = ";";
	/**
	 * Index of player data in the external query result.
	 */
	private static final int CONTENT_PLAYER_INDEX = 0;
	/**
	 * Index of item data in the external query result.
	 */
	private static final int CONTENT_ITEM_INDEX = 1;
	/**
	 * Index of price data in the external query result.
	 */
	private static final int CONTENT_PRICE_INDEX = 2;
	/**
	 * Index of time stamp data in the external query result.
	 */
	private static final int CONTENT_TIMESTAMP_INDEX = 3;
	
	/**
	 * Day of the date since which external data should be pulled.
	 */
	private static final int LAST_PRINT_DAY = 03;
	/**
	 * Month of the date since which external data should be pulled.
	 */
	private static final int LAST_PRINT_MONTH = 02;
	/**
	 * Year of the date since which external data should be pulled.
	 */
	private static final int LAST_PRINT_YEAR = 2016;
	
	/**
	 * WikiHub for interaction with FreewarWiki.
	 */
	private static final WikiHub hub = new WikiHub();
	
	/**
	 * Starts the market price routine that fetches data
	 * and passes them to the FreewarWiki.
	 * @param args
	 *            Not supported
	 */
	public static void main(final String[] args) {
		mpRoutine();
	}
	
	/**
	 * Market price routine that fetches external data, processes them and
	 * passes them to the FreewarWiki using a {@link WikiHub}.
	 */
	public static void mpRoutine() {
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
		
		System.out.println("#Starting Routine...");
		System.out.println("#Simulate: " + simulate);
		System.out.println("#Fetching entries from database...");
		LinkedHashMap<Item, LinkedList<MpEntryWrapper>> entries =
				fetchMpEntries(FETCH_DB_URL);
		
		/*
		//For testing purpose only
		LinkedHashMap<Item, LinkedList<MpEntryWrapper>> entries =
				new LinkedHashMap<Item, LinkedList<MpEntryWrapper>>();
		LinkedList<MpEntryWrapper> list1 = new LinkedList<MpEntryWrapper>();
		list1.add(new MpEntryWrapper(2, "Zab|uza", "Seelen=kugel=", 37,
				"2015-03-20 7:19:02"));
		entries.put(new Item("Blaukornblume"), list1);
		LinkedList<MpEntryWrapper> list2 = new LinkedList<MpEntryWrapper>();
		list2.add(new MpEntryWrapper(5, "Zabu=za", "Seel||en=kugel", 70,
				"2015-03-20 7:19:02"));
		entries.put(new Item("Seelenkugel"), list2);
		*/
		
		if (entries == null) {
			System.out.println("#Program terminated. Fix remaining problems.");
			return;
		}
		
		System.out.println("#Passing entries to Wiki...");
		passEntriesToWiki(entries, simulate);
		System.out.println("#Program terminated successfully.");
	}
	/**
	 * Fetches market price entries from external database and checks if
	 * some item information.
	 * @param path Path to the external database interface
	 * @return Map of market price entries that should be
	 * passed to the FreewarWiki or null if any entry is currently not
	 * ready to get passed because it is not existent or no market
	 * price site has been created until now.
	 */
	private static LinkedHashMap<Item, LinkedList<MpEntryWrapper>>
		fetchMpEntries(final String path) {
		LinkedHashMap<Item, LinkedList<MpEntryWrapper>> entries =
				new LinkedHashMap<Item, LinkedList<MpEntryWrapper>>();
		
		for (int i = 1; i <= WORLD_AMOUNT; i++) {
			String query = path + PHP_PARAM_BEGIN
					+ FETCH_DB_PARAM_WORLD + PHP_PARAM_ALLOC + i
					+ PHP_PARAM_SEPARATOR
					+ FETCH_DB_PARAM_DAY + PHP_PARAM_ALLOC + LAST_PRINT_DAY
					+ PHP_PARAM_SEPARATOR
					+ FETCH_DB_PARAM_MONTH + PHP_PARAM_ALLOC + LAST_PRINT_MONTH
					+ PHP_PARAM_SEPARATOR
					+ FETCH_DB_PARAM_YEAR + PHP_PARAM_ALLOC + LAST_PRINT_YEAR;
			List<String> content = null;
			try {
				content = MpUtil.getWebContent(query);
				System.out.println(">Query result for world " + i
						+ " has size of " + content.size() + ".");
				
				boolean inContent = false;
				for (String line : content) {
					if (inContent) {
						Item item = parseItemFromLine(line);
						if (item == null) {
							continue;
						}
						MpEntryWrapper entry = parseEntryFromLine(i, line);
						
						if (!entries.containsKey(item)) {
							entries.put(item, new LinkedList<MpEntryWrapper>());
						}
						LinkedList<MpEntryWrapper> values = entries.get(item);
						values.add(entry);
						entries.put(item, values);
					}
					
					if (line.equals(CONTENT_MASK_START)) {
						inContent = true;
					}
					if (line.trim().equals("") || line.length() <= 0) {
						break;
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (content == null || content.isEmpty()) {
				continue;
			}
		}
		
		System.out.println(">Parsed " + entries.size() + " entries.");
		System.out.println(">Checking if entries have wiki problems...");
		
		boolean hasEntryWikiProblem = false;
		int counter = 0;
		for (Entry<Item, LinkedList<MpEntryWrapper>> entry : entries.entrySet()) {
			String item = entry.getKey().getName();
			Boolean[] result = hub.getItemInfo(item);
			if (!result[0]) {
				System.err.println("'" + item + "' not existant.");
				hasEntryWikiProblem = true;
			} else if (!result[1]) {
				System.err.println("'" + item + "' has no MpSite but exists.");
				hasEntryWikiProblem = true;
			}
			
			counter++;
			if (counter % PRINT_EVERY_ENTRY == 0) {
				System.out.println(">Worked " + counter + " items.");
			}
		}
		
		if (hasEntryWikiProblem) {
			return null;
		}
		
		return entries;
	}
	
	/**
	 * Parses the data of an market price entry from the
	 * external data query result.
	 * @param world World of the entry
	 * @param line Line to parse data from
	 * @return Parsed market price entry with adjusted
	 * the true item name of the query result
	 */
	private static MpEntryWrapper parseEntryFromLine(final int world,
			final String line) {
		String[] values = line.split(CONTENT_SPLIT);
		String player = values[CONTENT_PLAYER_INDEX].trim();
		String item = values[CONTENT_ITEM_INDEX].trim();
		int price = Integer.valueOf(values[CONTENT_PRICE_INDEX].trim());
		String timestamp = values[CONTENT_TIMESTAMP_INDEX].trim();
		
		return new MpEntryWrapper(world, player, item, price, timestamp);
	}
	
	/**
	 * Parses the data of an item from the external data query result.
	 * @param line Line to parse data from
	 * @return Parsed item with adjusted item name or null
	 * if item does not exist.
	 */
	private static Item parseItemFromLine(String line) {
		String[] values = line.split(CONTENT_SPLIT);
		String item = values[CONTENT_ITEM_INDEX].trim();
		Item result = new Item(item);
		if (result.getName().equals("")) {
			return null;
		}
		
		return result;
	}
	/**
	 * Passes the fetched entries to the FreewarWiki using a hub.
	 * @param entries Entries to pass
	 * @param simulate If task should only be simulated,
	 * no edit in FreewarWiki will be done if true.
	 */
	private static void passEntriesToWiki(
			final LinkedHashMap<Item, LinkedList<MpEntryWrapper>> entries,
			final boolean simulate) {
		LinkedList<Entry<Item, LinkedList<MpEntryWrapper>>> missingEntries =
				new LinkedList<Entry<Item, LinkedList<MpEntryWrapper>>>();
		
		System.out.println(">Entries to add: " + entries.size());
		
		int counter = 0;
		for (Entry<Item, LinkedList<MpEntryWrapper>> itemEntry
				: entries.entrySet()) {
			boolean wasAdded = hub.addMpEntries(itemEntry.getKey().getName(),
					itemEntry.getValue(), simulate);
			if (!wasAdded) {
				missingEntries.add(itemEntry);
			}
			
			counter++;
			if (counter % PRINT_EVERY_ENTRY == 0) {
				System.out.println(">Worked " + counter + " items.");
			}
		}
		
		if (!missingEntries.isEmpty()) {
			System.out.print("Entries that could not be added:");
			for (Entry<Item, LinkedList<MpEntryWrapper>> missingEntry
					: missingEntries) {
				System.out.println(missingEntry.getKey() + ", "
					+ missingEntry.getValue());
			}
		}
	}
	
	/**
	 * Utility class. No implementation.
	 */
	private MpFetcher() {

	}
}