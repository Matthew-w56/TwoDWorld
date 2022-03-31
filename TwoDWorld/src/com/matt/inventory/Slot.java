package com.matt.inventory;

import java.awt.Rectangle;

import com.matt.O;
import com.matt.item.Item;

/**
 * A slot holds an item, and represents one spot in the inventory.
 * This class is not abstract, but it is implemented by the other
 * slot classes ie: InventorySlot, and InventoryBackslot
 * 
 * @author Matthew Williams
 *
 */
public class Slot {
	protected Item item;
	protected int count;
	protected int[] pos;
	public Rectangle rect;
	
	public Slot(Item item, int c, int x, int y) {
		//Initiate the item, position of the slot, and count of the item
		//(All regularly null, or 0 to start)
		this.item = item;
		this.pos = new int[] {x, y, O.slotSize, O.slotSize};
		this.count = c;
	}
	
	public int getItemID() {
		//return item id, or -1 if item is null
		return this.item == null ? -1 : this.item.getId();
	}
	
	public void swapWithMouse() {
		Item midItem = O.mouse.item;
		int midCount = O.mouse.count;
		
		O.mouse.item = this.item;
		O.mouse.count = this.count;
		
		this.setItem(midItem);
		this.setCount(midCount);
	}
	
	public void takeOneFromMouse() {
		if (this.count < O.maxStackSize) {
			O.mouse.count --;
			this.item = O.mouse.item;
			this.count ++;
			if (O.mouse.count <= 0) {
				O.mouse.count = 0;
				O.mouse.item = null;
			}
		} else {
			System.out.println("Caught on layer 2");
		}
	}
	
	public void takeAllFromMouse() {
		//Assumes that the mouse has an item, and that it is the same as the slot's item
		this.count += O.mouse.count;
		if (this.count > O.maxStackSize) {
			int left = this.count - O.maxStackSize;
			this.count -= left;
			O.mouse.count = left;
		} else {
			O.mouse.count = 0;
			O.mouse.item = null;
		}
	}
	
	public void giveHalfToMouse() {
		if (O.mouse.item == null && this.count > 0) {
			int transfer = this.count / 2;
			if (transfer > 0) {
				O.mouse.count = transfer;
				O.mouse.item = this.item;
				this.count -= transfer;
				if (this.count == 0) {
					this.item = null;
				}
			}
		}
	}
	
	public void incItem() {
		if (this.item != null) {
			this.count++;
		}
	}
	
	public void incItem(int count) {
		if (this.item != null) {
			this.count += count;
		}
	}
	
	public void decItem() {
		this.count--;
		if (this.count <= 0) {
			this.item = null;
		}
	}
	
	public void decItem(int count) {
		this.count -= count;
		if (this.count <= 0) {
			this.item = null;
		}
	}
	
	public void setCount(int num) {
		this.count = num;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public Item getItem() {
		return this.item;
	}
}
