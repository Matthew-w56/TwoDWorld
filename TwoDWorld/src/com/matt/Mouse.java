package com.matt;

import com.matt.inventory.Slot;
import com.matt.item.Item;


/**
 * Right now, this class only handles what item the mouse is holding in
 * an inventory setting.  Any mouse listening happens in UIManager.java,
 * and the events are distributed from there.
 * 
 * @author Matthew Williams
 *
 */
public class Mouse {
	
	public int count;
	public Item item;
	
	public Mouse () {
		this.item = null;
		this.count = 0;
	}
	
	/**
	 * Sets the currently held item to the given item at the
	 * given count.  Prints that there has been an error if the
	 * mouse already was holding something.
	 * 
	 * @param item Item to give
	 * @param count Count of the item
	 */
	public void giveItem(Item item, int count) {
		if (this.item == null) {
			this.item = item;
			this.count = count;
		} else {
			System.out.println("[Mouse.giveItem] Attempted to give " + item + " to mouse while mouse already held " + this.item + "!");
		}
	}
	
	/**
	 * Empties any held item or count held by the mouse.
	 * Does not return or do anything else.
	 */
	public void clearItem() {
		this.item = null;
		this.count = 0;
	}
	
	/**
	 * Returns the item being held by the mouse.
	 * Note: This does not remove the item from the mouse,
	 * just returns what item it is.  If no item is being
	 * held, null is returned.
	 * 
	 * @return Item held by the mouse
	 */
	public Item getItem() {
		return this.item;
	}
	
	/**
	 * Returns the quantity of the item being held by the mouse.
	 * Note: This does not remove the item from the mouse,
	 * just returns what quantity of it is.  If no item is being
	 * held, 0 is returned.
	 * 
	 * @return Quantity of item being held by the mouse
	 */
	public int getCount() {
		return this.count;
	}
	
	//Taken from Slot.java 
	
	public void swapItemsWithSlot(Slot slot) {
		Item midItem = item;
		int midCount = count;
		
		if (slot.getItem() == null) {
			clearItem();
		} else {
			item = slot.getItem();
			count = slot.getCount();
		}
		
		slot.setItem(midItem);
		slot.setCount(midCount);
	}
	
	public void giveOneItemToSlot(Slot slot) {
		if (slot.getCount() < O.maxStackSize) {
			count --;
			slot.setItem(item);
			slot.incItem();
			
			if (count <= 0) {
				clearItem();
			}
		}
	}
	
	public void giveAllItemsToSlot(Slot slot) {
		//Assumes that the mouse has an item, and that it is the same as the slot's item
		slot.incItem(count);
		if (slot.getCount() > O.maxStackSize) {
			int left = slot.getCount() - O.maxStackSize;
			slot.decItem(left);
			count = left;
		} else {
			clearItem();
		}
	}
	
	public void takeHalfItemsFromSlot(Slot slot) {
		if (item == null && slot.getCount() > 1) {
			int transfer = slot.getCount() / 2;
			count = transfer;
			item = slot.getItem();
			slot.decItem(transfer);
		}
	}

}
