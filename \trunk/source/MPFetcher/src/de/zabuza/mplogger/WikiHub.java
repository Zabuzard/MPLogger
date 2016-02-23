package de.zabuza.mplogger;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.zabuza.mplogger.data.MpEntryWrapper;

import net.sourceforge.jwbf.core.contentRep.Article;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;

/**
 * Hubs functionalities like creation of articles to the wiki.
 * @author Zabuza
 *
 */
public final class WikiHub {
	
	/**
	 * Username in FreewarWiki.
	 */
	private static final String USER = "Wikiuser";
	/**
	 * Password in FreewarWiki.
	 */
	private static final String PASSWD = "password";
	/**
	 * Server address of FreewarWiki.
	 */
	private static final String SERVER = "http://www.fwwiki.de/";
	
	/**
	 * Edit summary for confirmation of market price sites.
	 */
	private static final String SUMMARY_CONFIRM_MP =
			"Automatisierter Beitrag."
			+ " Parameter 'MarktPreisSeite' auf 'ja' gestellt.";
	/**
	 * Edit summary for creation of market price sites.
	 */
	private static final String SUMMARY_CREATE_MP_SITE =
			"Automatisierter Beitrag."
			+ " Neue MarktPreisSeite erstellt.";
	/**
	 * Begin of edit summary for adding of market price entries.
	 */
	private static final String SUMMARY_ADD_MP_ENTRY_PRE =
			"Automatisierter Beitrag. ";
	/**
	 * End of edit summary for adding of market price entries.
	 */
	private static final String SUMMARY_ADD_MP_ENTRY_SUC =
			"x Marktpreise hinzugefügt.";
	
	/**
	 * RegEx that matches different variants for the
	 * market price site confirmation parameter.
	 */
	private static final String MP_CONFIRM_REGEX =
			"(ja|yes|true|1|ein|on|<mp>|25k)";
	/**
	 * Preamble for market price sites in FreewarWiki.
	 */
	private static final String MP_SITE_PRE = "Marktpreis:";
	/**
	 * Mask that denotes the begin of entries for a world.
	 */
	private static final String MP_SITE_WORLD_PRE = " in Welt ";
	/**
	 * Mask that denotes the end of entries for a world.
	 */
	private static final String MP_SITE_WORLD_SUC = " ==";
	/**
	 * Mask that denotes the begin of entries for a next world.
	 */
	private static final String MP_SITE_WORLD_NEXT = "== Marktpreis ";
	/**
	 * Name of the template for market price entries.
	 */
	private static final String MP_SITE_ENTRY_TEMPLATE = "MP";
	/**
	 * Delimiter for template parameters.
	 */
	private static final String MP_SITE_PARAM_DELIMITER = "|";
	/**
	 * Command that generates a signature.
	 */
	private static final String MP_SITE_SIGNATURE = "~~~~";
	
	/**
	 * Escapes a given text so that it can
	 * be safely used in FreewarWiki.
	 * @param sequence Sequence to escape
	 * @return Escaped sequence that can be safely used in FreewarWiki
	 */
	private static String escapeWikiString(final String sequence) {
		return sequence.replaceAll("=", "&#61;").replaceAll("\\|", "&#124;");
	}
	/**
	 * WikiBot that is used to access the FreewarWiki.
	 */
	private final MediaWikiBot wikiBot;
	/**
	 * Pattern for matching.
	 */
	private Pattern pattern;
	
	/**
	 * Matcher for matching.
	 */
	private Matcher matcher;
	
	/**
	 * Creates a new WikiHub that can interact with FreewarWiki.
	 */
	public WikiHub() {
		wikiBot = new MediaWikiBot(SERVER);
	}
	
	/**
	 * Adds market price entries to an item in the FreewarWiki.
	 * @param item Item to add entries of
	 * @param entries Entries to add
	 * @param simulate If task should only be simulated,
	 * no edit in FreewarWiki will be done if true.
	 * @return True if entries where added or false if a problem occurred
	 */
	public boolean addMpEntries(final String item,
			final LinkedList<MpEntryWrapper> entries,
			final boolean simulate) {
		Article article = wikiBot.getArticle(MP_SITE_PRE + item);
		boolean existant = !article.getRevisionId().trim().equals("");
		if (!existant) {
			System.err.println("Error while trying to add entries to"
					+ " MP site for '"
					+ item + "'. MP site does not exist.");
			return false;
		}
		
		String articleText = article.getText();
		
		for (MpEntryWrapper wrapper : entries) {
			String worldHeader = MP_SITE_WORLD_PRE
					+ wrapper.getWorld() + MP_SITE_WORLD_SUC;
			int worldStartSearchIndex = articleText.indexOf(worldHeader);
			int worldStart = worldStartSearchIndex + worldHeader.length() + 1; 
			int worldEnd = articleText.indexOf(MP_SITE_WORLD_NEXT, worldStart);
			
			if (worldStartSearchIndex < 0 || worldEnd < 0) {
				System.err.println("Syntax error while trying to"
						+ " add MP entry for '" + item + "'."
						+ " Could not find start or end of world "
						+ wrapper.getWorld() + ".");
				return false;
			}
			String worldText = articleText.substring(worldStart, worldEnd);
			
			String date = parseDateFromTimestamp(wrapper.getTimestamp());
			
			String mpEntryTextPre = "{{" + MP_SITE_ENTRY_TEMPLATE + MP_SITE_PARAM_DELIMITER
					+ wrapper.getPrice() + MP_SITE_PARAM_DELIMITER
					+ "Käufer: " + escapeWikiString(wrapper.getPlayer())
					+ ", Ort: Markthalle, Wann: "
					+ date;
			if (!item.equals(wrapper.getItem())) {
				mpEntryTextPre = mpEntryTextPre + ", Item: "
						+ escapeWikiString(wrapper.getItem());
			}
			String mpEntryText = mpEntryTextPre + MP_SITE_PARAM_DELIMITER
					+ MP_SITE_SIGNATURE + "}}";
			
			if (!worldText.contains("{{MPend}}")) {
				String mpTemplate = "{{MPbegin}}\n"
						+ "<!-- Hier Marktpreise im Schema "
						+ "{{MP|Marktpreis|Anmerkung|Signatur}} einfügen! -->"
						+ "\n{{MPend}}\n\n";
				worldText += mpTemplate;
			}
			
			worldText = worldText.replaceFirst("\\{\\{MPend\\}\\}",
					mpEntryText + "\n{{MPend}}");
			articleText = articleText.substring(0, worldStart)
					+ worldText + articleText.substring(worldEnd);
		}
		
		article.setText(articleText);
		article.setEditSummary(SUMMARY_ADD_MP_ENTRY_PRE
				+ entries.size() + SUMMARY_ADD_MP_ENTRY_SUC);
		if (!simulate) {
			saveArticle(article);
		}
//		System.out.println(">Added " + entries.size()
//				+ " MP entries for '" + item + "'.");
		
		return true;
	}
	/**
	 * Creates a market price site for an item and
	 * sets the market price site parameter of the item.
	 * @param item Item to create market price site for
	 * @param simulate If task should only be simulated,
	 * no edit in FreewarWiki will be done if true.
	 * @return True if the market price site was created,
	 * false if a problem occurred
	 */
	public boolean createMpSite(final String item, final boolean simulate) {
		Article article = wikiBot.getArticle(item);
		boolean existant = !article.getRevisionId().trim().equals("");
		if (!existant) {
			System.err.println("Error while trying to create MP site for '"
					+ item + "'. Item does not exist.");
			return false;
		}
		
		String articleText = article.getText();
		pattern = Pattern.compile(".*\\|MarktPreisSeite=.*",
				Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(articleText);
		if (matcher.find()) {
			System.out.println("'" + item + "' already has the parameter.");
			
			articleText = articleText.replaceFirst("\\|MarktPreisSeite=\n(?:"
					+ "(?!" + MP_CONFIRM_REGEX.substring(1)
					+ ".*)[\n]{1,5}\\|",
					"|MarktPreisSeite=\nja\n\n|");
			if (!articleText.contains("ja")) {
				System.err.println("Syntax error while trying to"
						+ " confirm MP for '" + item + "'."
						+ " Could not find parameter.");
				return false;
			}
		} else {
			System.out.println("'" + item + "' has no parameter.");
			
			articleText = articleText.replaceFirst("\n\\}\\}[\n\\s]*$",
					"\n|MarktPreisSeite=\nja\n\n\\}\\}");
			if (!articleText.contains("|MarktPreisSeite=")) {
				System.err.println("Syntax error while trying to"
						+ " confirm MP for '" + item + "'."
						+ " Could not find end of template.");
				return false;
			}
		}
		article.setText(articleText);
		article.setEditSummary(SUMMARY_CONFIRM_MP);
		if (!simulate) {
			saveArticle(article);
		}
		System.out.println(">Confirmed MP for '" + item + "'.");
		
		article = wikiBot.getArticle(MP_SITE_PRE + item);
		article.setText("{{ers:Marktpreisseite}}");
		article.setEditSummary(SUMMARY_CREATE_MP_SITE);
		if (!simulate) {
			saveArticle(article);
		}
		System.out.println(">Created MP Site for '" + item + "'.");
		
		return true;
	}
	/**
	 * Gets information of an item in FreewarWiki and returns them.
	 * @param item Item to get information of
	 * @return 2-dim Array with [0] if item exists and [1] if
	 * item exists and has the market price site parameter set
	 */
	public Boolean[] getItemInfo(final String item) {
		Boolean[] result = new Boolean[2];
		
		if (item.equals("Zauber der Erfahrung")
				|| item.equals("Wolkenblume")) {
			result[0] = true;
			result[1] = true;
			return result;
		}
		
		Article article = wikiBot.getArticle(item);
		result[0] = !article.getRevisionId().trim().equals("");
		
		if (!result[0]) {
			result[1] = false;
			return result;
		}
		
		boolean hasMpSite = false;
		String articleText = article.getText();
		pattern = Pattern.compile(".*\\|MarktPreisSeite=[\n]?[\\s]?"
				+ MP_CONFIRM_REGEX + ".*",
				Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(articleText);
		if (matcher.find()) {
			hasMpSite = true;
		}
		result[1] = hasMpSite;
		
		return result;
	}

	/**
	 * Login to the FreewarWiki.
	 */
	private void login() {
		if (!wikiBot.isLoggedIn()) {
			wikiBot.login(USER, PASSWD);
		}
	}
	
	/**
	 * Parses the date, format "dd.mm.yyyy", from a
	 * time stamp in database format.
	 * @param timestamp Time stamp in database format
	 * @return Parsed date in format "dd.mm.yyyy"
	 */
	private String parseDateFromTimestamp(String timestamp) {
		String year = timestamp.substring(0, 4);
		String month = timestamp.substring(5, 7);
		String day = timestamp.substring(8, 10);
		String delimiter = ".";
		
		return day + delimiter + month + delimiter + year;
	}
	/**
	 * Saves the article using its current
	 * information by connecting to the FreewarWiki.
	 * @param article Article to save
	 */
	private void saveArticle(final Article article) {
		login();
		article.save();
	}
}
