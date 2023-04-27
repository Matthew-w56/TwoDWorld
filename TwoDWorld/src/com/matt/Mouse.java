package com.matt;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.matt.inventory.Slot;
import com.matt.item.Item;
import com.matt.menu.MenuButton;
import com.matt.menu.Screens;


/**
 * This class handles some methods that deal with exchanges between
 * the mouse and a slot in terms of item sharing and stacking.  It also
 * acts as a MouseMotionListener, which allows it to update it's current
 * screen position (x and y) whenever the mouse is moved.
 * 
 * @author Matthew Williams
 *
 */
public class Mouse implements MouseMotionListener {
	
	private static final int MouseOffsetX = -9;
	private static final int MouseOffsetY = -32;

	public int count;
	public Item item;
	public int x = 0, y = 0;

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

	@Override
	public void mouseDragged(MouseEvent e) {
		//Move the positions of the mouse
		this.x = e.getX() + MouseOffsetX;
		this.y = e.getY() + MouseOffsetY;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		//Move the positions of the mouse
		this.x = e.getX() + MouseOffsetX;
		this.y = e.getY() + MouseOffsetY;
		
		//TODO: Resolve this reference
		//Highlight the buttons on the menu that the mouse is touching, if the player is in the menu
		if (O.menu.inMenu && Screens.activeScreen != null) {
			for (MenuButton b: Screens.activeScreen.buttons) {
				if (b.rect.contains(this.x, this.y)) {
					b.current_color = b.selected_color;
				} else {
					b.current_color = b.primary_color;
				}
			}
		}

	}

}
