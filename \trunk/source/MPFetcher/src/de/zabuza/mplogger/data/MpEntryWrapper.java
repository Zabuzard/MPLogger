package de.zabuza.mplogger.data;

/**
 * Wrapper for a market price entry that contains
 * information like world, player, item, price and time stamp.
 * @author Zabuza
 *
 */
public class MpEntryWrapper {
	
	/**
	 * World of the market price entry.
	 */
	private final int world;
	/**
	 * Player of the market price entry.
	 */
	private final String player;
	/**
	 * Item of the market price entry.
	 */
	private final String item;
	/**
	 * Price of the market price entry.
	 */
	private final int price;
	/**
	 * Time stamp of the market price entry.
	 */
	private final String timestamp;
	
	/**
	 * Creates a new market price entry wrapper with given information.
	 * @param thatWorld World of the entry
	 * @param thatPlayer Player of the entry
	 * @param thatItem Item of the entry
	 * @param thatPrice Price of the entry
	 * @param thatTimestamp Time stamp of the entry
	 */
	public MpEntryWrapper(final int thatWorld, final String thatPlayer,
			final String thatItem, final int thatPrice,
			final String thatTimestamp) {
		world = thatWorld;
		player = thatPlayer;
		item = thatItem;
		price = thatPrice;
		timestamp = thatTimestamp;
	}
	
	/**
	 * @return the item
	 */
	public String getItem() {
		return item;
	}

	/**
	 * @return the player
	 */
	public String getPlayer() {
		return player;
	}
	
	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the world
	 */
	public int getWorld() {
		return world;
	}
	
	@Override
	public String toString() {
		return "MpEntryWrapper [player=" + player + ", item=" + item
				+ ", price=" + price + ", timestamp=" + timestamp + "]";
	}
}
