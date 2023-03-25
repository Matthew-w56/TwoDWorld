package com.matt.inventory;

import com.matt.item.Item;

/**
 * Inimplemented.  Will store a single type of item
 * in a ChestInventory.
 *
 * @author Matthew Williams
 *
 */
public class ChestInventorySlot extends Slot {
	public ChestInventorySlot(Item item, int count, int x, int y) {
		super(item, count, x, y);
	}
}
