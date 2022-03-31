package com.matt.inventory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.matt.O;
import com.matt.item.Item;

/**
 * Holds an item in the back inventory.  Differs from an inventorySlot
 * in that it is smaller.
 * 
 * @author Matthew Williams
 *
 */
public class InventoryBackslot extends Slot {
	
	public InventoryBackslot(Inventory inv, Item item, int c, int row, int col) {
		//Initiate the item, position of the slot, and count of the item
		//(All regularly null, or 0 to start)
		super(item, c, row, col);
		this.item = item;
		this.count = c;
		inv.backInv[row][col] = this;
		this.rect = new Rectangle(inv.fullRect.x + O.backInvMargin + (col * O.backSlotOffset), inv.fullRect.y + O.backInvMargin + (row * O.backSlotOffset), O.backSlotSize, O.backSlotSize);
		this.pos = new int[] {inv.fullRect.x + O.backInvMargin + (col * O.backSlotOffset), inv.fullRect.y + O.backInvMargin + (row * O.backSlotOffset), O.backSlotSize, O.backSlotSize};
	}
	
	public void display(Graphics g, int x, int y) {
		g.setColor(Color.gray);
		g.fillOval(pos[0], pos[1], pos[2], pos[3]);
		g.setColor(Color.black);
		g.drawOval(pos[0], pos[1], pos[2], pos[3]);
		
		if (this.item != null) {
			this.item.display(g, this.rect.x, this.rect.y, 1);
			//Display the item count
			g.setColor(Color.black);
			int _x;
			//If the number is a single digit
			if (this.count < 10) {
				_x = (pos[0] + O.backSlotSize) - (O.backItemMargin * 2);
			} else {
				//If the number is double digits
				_x = (int) ((pos[0] + O.backSlotSize) - (O.backItemMargin * 2.7));
			}
			g.drawString("" + this.count, _x, (pos[1] + O.backSlotSize) - (O.backItemMargin * 2)+3);
		}
	}
}
