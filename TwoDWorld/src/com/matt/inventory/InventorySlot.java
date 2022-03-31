package com.matt.inventory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.matt.O;
import com.matt.item.Item;

/**
 * Holds an item, and makes up part of the inventory as a whole.
 * 
 * @author Matthew Williams
 *
 */
public class InventorySlot extends Slot {
	
	public InventorySlot(Item item, int c, int x, int y) {
		super(item, c, x, y);
		this.rect = new Rectangle(x, y, O.slotSize, O.slotSize);
	}
	
	public void display(Graphics g) {
		//------[Draw the slot itself]------
		g.setColor(Color.gray);
		g.fillOval(pos[0], pos[1], pos[2], pos[3]);
		g.setColor(Color.black);
		g.drawOval(pos[0], pos[1], pos[2], pos[3]);
		
		//------[Draw the item, if the slot isn't empty]------
		if (this.item != null) {
			this.item.display(g, pos[0] + O.slotSize / 5, pos[1] + O.slotSize / 5, 1);
			if (O.player.inventory.hotbar[O.player.selected] == this && !O.player.fullInv) {
				
				//-----[Display the item's name]-----
				g.setColor(Color.black);
				g.setFont(O.itemNameFont);
				g.drawString(this.item.getDisplayName(), (pos[0] + (pos[2] / 2)) - (g.getFontMetrics().stringWidth(this.item.getDisplayName()) / 2), pos[1] - g.getFontMetrics().getHeight() - O.itemTextBuffer);
			}
			
			//-----[Display the item count]-----
			g.setColor(Color.black);
			int x;
			//If the number is a single digit
			if (this.count < 10) {
				x = (pos[0] + O.slotSize) - O.itemCountFont.getSize() - O.margin * 2;
			} else {
				//If the number is double digits
				x = (int) ((pos[0] + O.slotSize) - (O.itemCountFont.getSize() * 1.55)) - O.margin * 2;
			}
			g.setColor(Color.black);
			g.setFont(O.itemCountFont);
			g.drawString("" + this.count, x, (int)(pos[1] + O.slotSize / 1.4));
		}
	}
}
