package com.matt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.matt.creation.CreationSlot;
import com.matt.inventory.Inventory;
import com.matt.item.Item;

/* Hacksaw Notes:
 *  Completely deleted the instance variable
 *  Block lastTaken = null;
 *  because I saw no use beyond setting it to the
 *  most recently taken item.  No benefit or use.
 */

/**
 * Represents the player, and receives the mouse and keyboard events
 * related to the player.
 * 
 * Hacksaw: But it shouldn't deal with input.  To be fixed soon
 * 
 * @author Matthew Williams
 *
 */

//TODO: Gut out all the functionality that does not belong here.  Maybe have a separate Mouse Input Manager?

public class Player {
	//Start up variables for the Player class
	public Rectangle rect;
	public double speed;
	public Inventory inventory;
	public int selected, midX, midY;
	public boolean fullInv = false;
	
	public Player() {
		rect = new Rectangle(O.playerX, O.playerY, O.playerWidth, O.playerHeight);
		speed = O.playerSpeed;
		this.inventory = new Inventory();
		selected = 0;
		midX = rect.x + (rect.width / 2);
		midY = rect.y + (rect.height / 2);
	}
	
	public void move(double dx, double dy) {
		//TODO: Implement player.move
		//TODO: But maybe move should be handled on a higher level.  Maybe a generic moveEntity method with a desired dx and dy?
		
		//At end
		midX = rect.x + (rect.width / 2);
		midY = rect.y + (rect.height / 2);
	}
	
	public void listen(int k, boolean down) {
		//Set the key based on the inputted key, and whether it should be set up or down
		if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) {
			O.PR = down;
		} else if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) {
			O.PL = down;
		} else if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W || k == KeyEvent.VK_SPACE) {
			O.PU = down;
		} else if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) {
			O.PD = down;
		} else if (k == KeyEvent.VK_SHIFT) {
			O.LSHIFT = down;
		} else if (down) {
			//Check all the numbers in the range for hotbar slots, and see if that is what they pressed
			for (int x = 0; x < O.hotCount; x++) {
				//48 is the value for 0, so when x=0, it is checking if they pressed 1
				if (k == 49 + x) {
					selected = x;
				}
			}
		}
	}
	
	public void leftClick(CreationSlot slot) {			//Left click in the Creation Menu
		if (this.canCreate(slot)) {
			for (int x = 0; x < slot.requireds.length; x++) {
				this.takeByID(slot.requireds[x].getId(), slot.required_counts[x]);
			}
			this.give(slot.product.getNew(), slot.product_count);
		}
	}
	
	//TODO: This isn't being called right now.  Once this is reinstated, this method needs to move
	public boolean canCreate(CreationSlot slot) {
		try {
			
			for (int x = 0; x < slot.requireds.length; x++) {
				if (!this.has(slot.requireds[x].getId(), slot.required_counts[x])) {
					return false;
				}
			}
		} catch (NullPointerException e) {
			return false;
		}
		
		return true;
	}
	
	public boolean give(Item item, int count) {
		return this.inventory.add(item, count);
	}
	
	public boolean takeBySlot(int slot, int count) {
		try {
			inventory.takeBySlot(slot, count).getBlockMold();
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public boolean takeByID(int id, int count) {
		if (inventory.has(id, count)) {
			return inventory.takeByID(id, count);
		}
		return false;
	}
	
	public boolean has(int id, int count) {
		//Check to see if the player has at least a certain number of an item
		//Mainly used with crafting
		return this.inventory.has(id, count);
	}
	
	public Item getSelectedItem() {
		return this.inventory.getSelectedItem(selected);
	}
	
	public void toggleInv() {
		if (!this.fullInv && O.creationWindow.visible) {
			O.creationWindow.hide();
		}
		//Toggle the full-screen version of the inventory
		this.fullInv = !this.fullInv;
		inventory.full = !inventory.full;
		
		if (O.tutProgress[3] && !O.tutProgress[4]) {
			O.tutProgress[4] = true;
		}
	}
	
	public void display(Graphics g) {
		//Fill in middle area, then black outline, then display the inventory
		g.setColor(O.playerColor);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		g.setColor(Color.black);
		g.drawRect(rect.x, rect.y, rect.width, rect.height);
		
		//TODO: Refactor this to remove the need for the additional parameters
		this.inventory.display(g, selected, fullInv);
	}
}
