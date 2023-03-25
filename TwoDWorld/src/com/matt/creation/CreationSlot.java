package com.matt.creation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.matt.O;
import com.matt.item.Item;

/**
 * A Creation Slot represents one way that the player
 * can take one thing and turn it into another.  It
 * stores the required material list, and the product.
 *
 * @author Matthew Williams
 *
 */
public class CreationSlot {

	public Item product;
	public int product_count;
	public Item[] requireds;
	public int[] required_counts;
	public Rectangle rect;

	public CreationSlot(Item product, int count, Item[] required_items, int[] required_counts) {
		this.product = product;
		this.product_count = count;
		this.requireds = required_items;
		this.required_counts = required_counts;
	}

	public void addTo(CreationWindow window) {
		window.slots.add(this);
	}

	public void findPos(int startX, int startY, int gridX, int i) {
		int endX, endY;
		endX = ((i % gridX) * O.creationSlotWidth) + startX;
		endY = ((i / gridX) * O.creationSlotHeight) + startY;
		if (this.rect == null) {
			this.rect = new Rectangle(endX, endY, O.creationSlotWidth, O.creationSlotHeight);
		} else {
			this.rect.x = endX;
			this.rect.y = endY;
		}
	}

	public void move(int dy) {
		if (this.rect != null) {
			this.rect.y += dy;
		}
	}

	public Item getProduct() {
		return this.product.getNew();

	}

	public void draw(Graphics g) {
		try {
			//Fill the slot's background, then draw the product
			g.setColor(O.lighterBlue);
			g.fillRect(this.rect.x, this.rect.y, O.creationSlotWidth, O.creationSlotHeight);
			g.setColor(this.product.getColor());
			g.fillRect(this.rect.x + 30, this.rect.y + 30, 40, 40);

			//Outline the slot, and the product
			g.setColor(Color.black);
			g.drawRect(this.rect.x, this.rect.y, this.rect.width, this.rect.height);
			g.drawRect(this.rect.x + 30, this.rect.y + 30, 40, 40);

			//Draw in the name of the product, and its count
			g.setFont(O.fontMedium);
			g.drawString(this.product.getDisplayName(), ((this.rect.width - g.getFontMetrics().stringWidth(this.product.getDisplayName())) / 2) + this.rect.x, this.rect.y + 20);
			g.setFont(O.fontSmall);
			g.drawString(this.product_count + "", this.rect.x + 60, this.rect.y + 67);

			//Display all the required blocks, and their counts
			int Xpos = this.rect.x + 10;
			int Ypos = this.rect.y + this.rect.height - 35;
			for (int i = 0; i < this.requireds.length; i++) {
				g.setColor(this.requireds[i].getColor());
				g.fillRect(Xpos, Ypos, 30, 30);
				g.setColor(Color.black);
				g.drawRect(Xpos, Ypos, 30, 30);
				g.setFont(O.fontSmall);
				g.drawString("x" + this.required_counts[i], Xpos + 10, Ypos + 20);
				g.drawString(this.requireds[i].getDisplayName().replaceAll(" Block", ""), Xpos, Ypos - 3);
				Xpos += 40;
			}
			//TODO: Have a parent class or structure check if the player can
			//		create this item, and set a boolean in this class.
			/*
			//Draw the tint, if the player cannot create it
			if (!O.player.canCreate(this)) {
				g.setColor(O.backTint);
				g.fillRect(this.rect.x + 1, this.rect.y + 1, this.rect.width - 1, this.rect.height - 1);
			}*/
		} catch (NullPointerException e) {}
	}
}
