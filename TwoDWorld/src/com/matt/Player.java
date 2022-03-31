package com.matt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.matt.block.Block;
import com.matt.block.Blocks;
import com.matt.creation.CreationSlot;
import com.matt.entity.Entity;
import com.matt.inventory.Inventory;
import com.matt.inventory.Slot;
import com.matt.item.Item;
import com.matt.item.ItemTool;
import com.matt.item.Items;

/**
 * Represents the player, and receives the mouse and keyboard events
 * related to the player.
 * 
 * @author Matthew Williams
 *
 */
public class Player {
	//Start up variables for the Player class
	public Rectangle rect;
	public double speed;
	public Inventory inventory;
	public int selected, midX, midY;
	public Block lastTaken = null;
	public boolean fullInv = false;
	
	public Player() {
		rect = new Rectangle(O.playerX, O.playerY, O.playerWidth, O.playerHeight);
		speed = O.playerSpeed;
		this.inventory = new Inventory();
		selected = 0;
		//Set the middle point of the player for distance calculations
		midX = O.playerX + (O.playerWidth / 2);
		midY = O.playerY + (O.playerHeight / 2);
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
			for (int x = 1; x <= O.hotCount; x++) {
				//48 is the value for 0, so when x=1, it is checking if they pressed 1
				if (k == 48 + x) {
					selected = x-1;
				}
			}
		}
	}
	
	public void leftClick(Block block) {				//Left click in the World
		Item selectedItem = inventory.getSelectedItem();												//Record the selected item
		if (selectedItem == null || selectedItem.getTypeId() == O.item) {
			int distance = (int)Math.hypot(O.MX - midX, O.MY - midY);
			if (distance <= O.placeDistance && block != null && block.getHarvestLevel() == 0) {
				
			}
		} else if (selectedItem.getTypeId() == O.toolItem) {
			ItemTool tool = (ItemTool)selectedItem;
			//Meant for breaking, but what? (Post mark: I have no idea what that comment meant)
			int distance = (int)Math.hypot(O.MX - midX, O.MY - midY);
			if (distance <= O.placeDistance && block != null && tool.getHarvestLevel() >= block.getHarvestLevel()) {
				
			}
		}
		if (selectedItem == null || selectedItem.getTypeId() == 0 || selectedItem.getTypeId() == 1) {	//If the player isn't holding aynthing, or is holding a block, or tool that can break
			int distance = (int)Math.hypot(O.MX - midX, O.MY - midY);									//Calculate the distance between the block and the player
			if (block != null && block.getId() != 0 && distance <= O.placeDistance) {						//If the block hits the mouse, and doesn't hit the player, and isn't sky
				Item newItem = block.getDroppedItem();															//Get the Item from the Block
				block = new Block();																			//Set the Block as sky
				if (newItem != null && !O.player.give(newItem, 1)) {											//If You can't give the player the item
					block = newItem.getBlock();																		//Set the block as the item again
				}
				
				if (O.tutProgress[1]) {O.tutProgress[2] = true;}										//Toggle the tutorial marker
			}
		}
	}
	
	public void breakAt(Block block) {
		int distance = (int)Math.hypot(O.MX - midX, O.MY - midY);
		if (distance <= O.placeDistance) {
			Item holding = inventory.getSelectedItem();
			if (holding == null || holding.getTypeId() != O.toolItem) {
				if (block.getHarvestLevel() == 0) {
					block.hit(Items.hand);
				}
			} else if (holding.getTypeId() == O.toolItem) {
				ItemTool tool = (ItemTool)holding;
				if (tool.getHarvestLevel() >= block.getHarvestLevel()) {
					block.hit(tool);
				}
			}
		}
	}
	
	public void tickMouseWatcher() {
		if (O.mouseLeftDown) {
			
			if (!O.player.fullInv && !O.menu.inMenu && !O.creationWindow.visible) {
				try {
					Block hitB = O.world.getBlocks(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY).get(0);
					breakAt(hitB);
				} catch (IndexOutOfBoundsException p) {}
			}
			
		} else if (O.mouseRightDown) {
			
			if (!O.player.fullInv && !O.menu.inMenu && !O.creationWindow.visible) {
				try {
					O.world.getBlocks(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY).get(0).activate();
				} catch (IndexOutOfBoundsException p) {}
			}
			
		}
		Blocks.tick();
	}
	
	public void leftClick(CreationSlot slot) {			//Left click in the Creation Menu
		if (this.canCreate(slot)) {
			for (int x = 0; x < slot.requireds.length; x++) {
				this.takeByID(slot.requireds[x].getId(), slot.required_counts[x]);
			}
			this.give(slot.product.getNew(), slot.product_count);
		}
	}
	
	public void leftClick(Slot slot) {					//Left click in the Inventory
		if (slot != null) {
			if (slot.getItem() == O.mouse.item) {
				slot.takeAllFromMouse();
			} else {
				slot.swapWithMouse();
			}
		}
	}
	
	public void rightClick(Block block) {				//Right click in the World
		Item selectedItem = inventory.getSelectedItem();		//Record the selected item
		if (selectedItem != null && selectedItem.getTypeId() == O.placeableItem) {	//If the item you are holding is placeable
			int distance = (int)Math.hypot(O.MX - midX, O.MY - midY);		//Record the distance between the block & player
			
			//If you can place there, block isn't null, it's close enough, not touching player, and you can place over that block you are looking at:
			if (block != null && distance <= O.placeDistance && (!block.getRect().intersects(this.rect) || !selectedItem.getBlock().getCollides()) && block.getPlaceOver()) {
				for (Entity entity: O.world.middleEntities) {								 //Refer to item.getBlock.getCollides
					if (block.getRect().intersects(entity.getModel().hit_box.get(0).rect) && selectedItem.getBlock().getCollides()) {
						return;
					}
				}
				
				if (O.player.takeBySlot(O.player.selected, 1)) {
					block = new Block(O.player.lastTaken);
					if (O.tutProgress[2] && !O.tutProgress[3]) {O.tutProgress[3] = true;}
				}
			}
		}
	}
	
	public void rightClick(Slot slot) {					//Right click in the inventory
		if (slot != null) {
			if (O.mouse.item == null && slot.getCount() > 0) {
				slot.giveHalfToMouse();
			} else {
				slot.takeOneFromMouse();
			}
		}
	}
	
	public boolean canCreate(CreationSlot slot) {
		boolean can = true;
		try {
			
			for (int x = 0; x < slot.requireds.length; x++) {
				if (!this.has(slot.requireds[x].getId(), slot.required_counts[x])) {
					can = false;
				}
			}
		} catch (NullPointerException e) {
			can = false;
		}
		
		return can;
	}
	
	public boolean give(Item item, int count) {
		return this.inventory.add(item, count);
	}
	
	public boolean takeBySlot(int slot, int count) {
		try {
			this.lastTaken = inventory.takeBySlot(slot, count).getBlock();
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
	
	public void toggleInv() {
		if (!this.fullInv && O.creationWindow.visible) {
			O.creationWindow.hide();
		}
		//Toggle the full iscreen version of the inventory
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
		this.inventory.display(g);
	}
}
