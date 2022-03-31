package com.matt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.matt.creation.CreationSlot;
import com.matt.entity.Entity;
import com.matt.inventory.InventoryBackslot;
import com.matt.inventory.InventorySlot;
import com.matt.inventory.Slot;
import com.matt.item.Item;
import com.matt.menu.MenuButton;
import com.matt.menu.Screens;

/**
 * Represents the mouse in game, and forwards the player's actions with
 * the mouse onto the corresponding classes to be handled.
 * 
 * @author Matthew Williams
 *
 */
public class Mouse implements MouseMotionListener, MouseListener, MouseWheelListener {
	
	public int count;
	public Item item;
	
	public Mouse () {
		//Assign variables for the mouse
		this.item = null;
		this.count = 0;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		//Move the positions of the mouse
		O.MX = e.getX();
		O.MY = e.getY();
		
		//Highlight the buttons on the menu that the mouse is touching, if the player is in the menu
		if (O.menu.inMenu && Screens.activeScreen != null) {
			for (MenuButton b: Screens.activeScreen.buttons) {
				if (b.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {
					b.current_color = b.selected_color;
				} else {
					b.current_color = b.primary_color;
				}
			}
		}
		
	}
	
	public void update() {
		//If the mouse has an item, and the user exited the inventory
		if (this.item != null && !O.player.fullInv) {
			O.player.give(this.item, this.count);	//Give the item back to the player
			this.count = 0;							//Set the mouse count as 0
			this.item = null;						//Set the mouse item as null
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		//Move the positions of the mouse
		O.MX = e.getX();
		O.MY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		
		switch(e.getButton()) {
		case 1:
			O.mouseLeftDown = true;
			break;
		case 3:
			O.mouseRightDown = true;
			break;
		}
		
		if (O.player.fullInv) {
			
			Slot found_slot = null;
			if (e.getButton() == 1) {
				for (InventorySlot hslot: O.player.inventory.hotbar) {
					if (hslot.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {
						found_slot = hslot;
					}
				}
				if (found_slot == null) {
					for (InventoryBackslot[] brow: O.player.inventory.backInv) {
						for (InventoryBackslot bslot: brow) {
							if (bslot.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {
								found_slot = bslot;
							}
						}
					}
				}
				
				if (found_slot == null) {
					O.player.toggleInv();
				}
				
				O.player.leftClick(found_slot);
				
			} else if (e.getButton() == 3) {
				for (InventorySlot slot: O.player.inventory.hotbar) {
					if (slot.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {
						found_slot = slot;
					}
				}
				if (found_slot == null) {
					for (InventoryBackslot[] row: O.player.inventory.backInv) {
						for (InventoryBackslot slot: row) {
							if (slot.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {
								found_slot = slot;
							}
						}
					}
				}
				if (found_slot == null) {
					O.player.toggleInv();
				}
				O.player.rightClick(found_slot);
			}
			
			
		} else if (O.menu.inMenu) {
			
			if (e.getButton() == 1) {		//Left Click in Menu
				if (Screens.activeScreen != null) {//If you are in a menu screen
					for (MenuButton b: Screens.activeScreen.buttons) {//For every button on the screen
						if (b.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {		//If the button is touching the mouse
							b.activate();							//Activate the button
						}
					}
				}
			}
			
		} else if (O.creationWindow.visible) {
			
			if (e.getButton() == 1) {			//Left Click   in  Creation Window
				CreationSlot endSlot = null;
				for (int i = 0; i < O.creationWindow.slots.size(); i++) {
					CreationSlot slot = O.creationWindow.slots.get(i);
					if (slot.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY))  {
						endSlot = slot;
					}
				}
				O.player.leftClick(endSlot);
			}
			
		} else {
			Entity entityFound = null;
			for (Entity entity: O.world.middleEntities) {
				if (entity.getModel().hit_box.get(0).rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {
					entityFound = entity;
					break;
				}
			}
			
			if (entityFound != null) {
				
				if (e.getButton() == 1) {
					entityFound.push(0, -100, "Mouse");
				} else if (e.getButton() == 3) {
					O.world.middleEntities.remove(entityFound);
				}
				
			} else if (e.getButton() == 2) {	//Middle Click
				//Toggle if the box for the cursor is displayed or not
				O.displayBox = !O.displayBox;
			}
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!O.creationWindow.visible) {
			//If the rotation is negative
			if (e.getWheelRotation() < 0) {
				//Move the selected slot
				O.player.selected -= 1;
				//If the slot went over the edge
				if (O.player.selected < 0) {
					//Place it on the other side
					O.player.selected = O.hotCount-1;
				}
			//If the rotation is positive
			} else if (e.getWheelRotation() > 0) {
				//Move the selected slot
				O.player.selected += 1;
				//If the slot went over the edge
				if (O.player.selected > O.hotCount-1) {
					//Place it on the other side
					O.player.selected = 0;
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch(e.getButton()) {
		case 1:
			O.mouseLeftDown = false;
			break;
		case 3:
			O.mouseRightDown = false;
			break;
		}
	}
	
	public void displayItem(Graphics g, int X, int Y) {
		//If the mouse has an item, and the player is in the inventory
		if (this.item != null && O.player.fullInv) {
			g.setFont(O.fontSmall);
			//Set the color accordingly
			g.setColor(this.item.getColor());
			//Draw the item block
			g.fillRect(X-O.backItemSize / 2, Y-O.backItemSize, (2 * O.backItemSize) / 3, (2 * O.backItemSize) / 3);
			//Draw the black border for the item
			g.setColor(Color.black);
			g.drawRect(X-O.backItemSize / 2, Y-O.backItemSize, (2 * O.backItemSize) / 3, (2 * O.backItemSize) / 3);
			if (this.count < 10) {
				g.drawString("" + this.count, X, (Y-O.backItemSize/2));
			} else {
				g.drawString("" + this.count, X - 6, (Y-O.backItemSize/2));
			}
		}
	}

}
