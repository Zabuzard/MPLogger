package de.zabuza.mplogger.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Item that contains information like a name
 * and has some methods to adjust problematic names.
 * @author Zabuza
 *
 */
public final class Item {
	
	/**
	 * Pattern for matching.
	 */
	private static Pattern pattern;
	/**
	 * Matcher for matching.
	 */
	private static Matcher matcher;
	
	/**
	 * Validates an item name and may return a corrected version.
	 * @param name Name to validate
	 * @return Inputted name if it is not problematic or a
	 * correct version if it is known to be problematic
	 */
	public static String validateItemName(String name) {
		boolean itemExceptionFound = false;
		String validName = name;
		
		pattern = Pattern.compile("\\A.*Gewebeprobe.*\\z",
				Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(name);
		if (matcher.find()) {
			itemExceptionFound = true;
			validName = "Gewebeprobe";
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Puppe.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Puppe von Beispieluser";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*personalisierter Hinzauber.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "personalisierter Hinzauber";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Zeichnung.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Zeichnung von Beispiel-NPC";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Blutprobe.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Blutprobe";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Seelenstein.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Seelenstein von Beispielopfer";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Wein.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Wein von Beispielsponsor";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Geschenk.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Geschenk von Beispielsponsor";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Schnaps.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Schnaps von Beispielsponsor";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Kaktussaft.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Kaktussaft von Beispielsponsor";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Largudsaft.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Largudsaft von Beispielsponsor";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Cocktail.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Cocktail von Beispielsponsor";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Tee.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Tee von Beispielsponsor";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Zaubertruhe von.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Zaubertruhe von Beispieluser";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Rückangriff.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "starker Rückangriffszauber";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Tagebuch.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Tagebuch Tag 125";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Notizblock.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Notizblock";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Freundschaftsring.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Freundschaftsring";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Ehering.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Ehering";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Foliant.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Foliant der Blutprobenwesen";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\A.*Hirtenstab.*\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Hirtenstab";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\AKyokaen\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\AKnorpel-Monster aus Draht\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Knorpel-Monster aus Draht (Item)";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\ASchatztruhe\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Zaubertruhe";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\ASprengkapsel\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "Sumpfgasbombe";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\AKürbling\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "";
			}
		}
		if (!itemExceptionFound) {
			pattern = Pattern.compile("\\Aschrumpelige Gardusinen\\z",
					Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(name);
			if (matcher.find()) {
				itemExceptionFound = true;
				validName = "";
			}
		}
		
		return validName;
	}
	
	/**
	 * Name of the item.
	 */
	private final String name;
	
	/**
	 * Creates a new item with a given name.
	 * Name could be changed if it is known as problematic.
	 * @param thatName Name of the item
	 */
	public Item(final String thatName) {
		name = validateItemName(thatName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Item)) {
			return false;
		}
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return "Item [name=" + name + "]";
	}

}
