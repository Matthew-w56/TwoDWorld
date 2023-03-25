package com.matt.inventory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.matt.O;
import com.matt.item.Item;

/**
 * An Inventory holds items, and contains the methods and functionalities
 * required to be interacted with by the player.  It is split into two
 * sections: the hotbar, and the back inventory.  The hotbar is always
 * visible to the player, while the back inventory is available temporarily
 * for the player when the right key is pressed.
 *
 * @author Matthew Williams
 *
 */
public class Inventory {

	Rectangle rect, fullRect;
	public InventorySlot[] hotbar;
	public boolean full;			//"full" is an indicator to whether the inv. is fullscreen at the moment
	public InventoryBackslot[][] backInv;

	public Inventory() {
		//TODO: I hate this being here.
		O.setupScreenSize();
		this.rect = new Rectangle((O.screenWidth - O.invWidth) / 2, O.screenHeight - O.invMarginBottom - O.invHeight, O.invWidth, O.invHeight);
		this.fullRect = new Rectangle((O.screenWidth - O.backInvWidth) / 2, rect.y - O.backInvHeight, O.backInvWidth, O.backInvHeight);
		this.hotbar = new InventorySlot[O.hotCount];
		this.backInv = new InventoryBackslot[O.backRows][O.backCols];
		this.full = false;

		//TODO: A) This loop makes me sad just looking at it.  B) Make sure that counter is the right n value, and not n+1
		int counter = 0;
		for (int i = rect.x + O.margin; counter < O.hotCount; i += (O.slotSize + (O.margin * 2))) {
			this.hotbar[counter] = new InventorySlot(null, 0, i, rect.y + O.margin, counter);
			counter ++;
		}
		for (int r = 0; r < O.backRows; r++) {
			for (int c = 0; c < O.backCols; c++) {
				new InventoryBackslot(this, null, 0, r, c);
			}
		}

	}

	public void display(Graphics g, int selectedSlot, boolean fullInv) {
		g.setColor(O.invColor);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);

		g.setColor(Color.black);
		g.drawRect(rect.x, rect.y, rect.width, rect.height);
		g.fillOval(rect.x + O.margin + (selectedSlot * (O.slotSize + (2 * O.margin))) - 4, rect.y + O.margin - 4, O.slotSize + 8, O.slotSize + 8);

		g.setColor(O.selectedColor);
		g.fillOval(rect.x + O.margin + (selectedSlot * (O.slotSize + (2 * O.margin))) - 3, rect.y + O.margin - 3, O.slotSize + 6, O.slotSize + 6);

		g.setColor(Color.lightGray);
		g.drawLine(rect.x + 2, rect.y + 2, rect.x - 2 + rect.width, rect.y + 2);

		if (this.full) {
			g.setColor(O.backTint);
			g.fillRect(0, 0, O.screenWidth, O.screenHeight);

			g.setColor(O.invBGC);
			g.fillRect(fullRect.x, fullRect.y, fullRect.width, fullRect.height);

			g.setColor(Color.black);
			g.drawRect(fullRect.x, fullRect.y, fullRect.width, fullRect.height);

			for (InventoryBackslot[] r: this.backInv) {
				for (InventoryBackslot slot: r) {
					if (slot != null) {
						slot.display(g, 0, 0);
					}

				}
			}

		} else {
			g.setColor(Color.gray);
			g.fillRect(rect.x + (rect.width - 50) / 2, rect.y - 20, 50, 20);
			g.setColor(Color.black);
			g.drawRect(rect.x + (rect.width - 50) / 2, rect.y - 20, 50, 20);
			g.drawRect(rect.x + (rect.width - 50) / 2 + 3, rect.y - 17, 44, 1);
			g.drawRect(rect.x + (rect.width - 50) / 2 + 8, rect.y - 10, 34, 1);
		}
		g.setColor(O.invBubbleColor);
		for (InventorySlot slot: hotbar) {
			slot.display(g, selectedSlot, fullInv);
		}
	}

	public boolean add(Item item) {
		boolean successful = false;
		//System.out.println("[Inventory] Giving the player Item ID: " + item.getID());

		//Check for existing and addable stacks in the hotbar for the item
		for (InventorySlot slot: hotbar) {
			if (!successful && slot.getItemID() == item.getId() && slot.getCount() < slot.getItem().getStackSize()) {
				slot.incItem();
				successful = true;
			}
		}
		//If that didn't work, check the back inventory area the same way
		if (!successful) {
			for (InventoryBackslot[] brow: backInv) {
				for (InventoryBackslot bslot: brow) {
					if (!successful && bslot.getItemID() == item.getId() && bslot.getCount() < bslot.getItem().getStackSize()) {
						bslot.incItem();
						successful = true;
					}
				}
			}
			//If you still haven't found one, check for an empty slot in the hotbar
			if (!successful) {
				for (InventorySlot slot: hotbar) {
					if (slot.getItemID() == -1 && !successful) {
						slot.setItem(item);
						slot.setCount(1);
						successful = true;
					}
				}
				//If none of those worked, check for an empty slot in the back inventory area
				if (!successful) {
					for (InventoryBackslot[] brow: backInv) {
						for (InventoryBackslot bslot: brow) {
							if (bslot.getItemID() == -1 && !successful) {
								bslot.setItem(item);
								bslot.setCount(1);
								successful = true;
							}
						}
					}
				}
			}
		}

		return successful;
	}

	public boolean add(Item item, int count) {
		boolean given = false;
		for (int x = 0; x < count; x++) {
			given = this.add(item);
		}
		return given;
	}

	public boolean canTake(int slot) {
		//Checks if you can take an item from the hotbar
		return hotbar[slot].getCount() > 0;
	}

	public Item takeBySlot(int slot, int count) {
		//Set the taken item as null
		Item item = null;
		//If the slot selected has an item in it
		if (hotbar[slot].getCount() >= count) {
			//Take the item, and update the item taken
			item = hotbar[slot].getItem();
			hotbar[slot].decItem(count);
		}
		//Return the item taken
		return item;
	}

	public boolean takeByID(int id, int count) {
		int taken = 0;
		if (this.has(id, count)) {
			for (InventorySlot slot: this.hotbar) {
				if (taken < count && slot.getItemID() == id) {
					if (slot.getCount() >= count - taken) {
						slot.decItem(count - taken);
						taken = count;
					} else {
						taken += slot.getCount();
						slot.decItem(slot.getCount());
					}
				}
			}
			if (taken < count) {
				for (InventoryBackslot[] row: this.backInv) {
					for (InventoryBackslot slot: row) {
						if (slot.getItemID() == id) {
							if (slot.getCount() >= count - taken) {
								slot.decItem(count - taken);
								taken = count;
							} else {
								taken += slot.getCount();
								slot.decItem(slot.getCount());
							}
						}
					}
				}
			}
			if (taken >= count) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean has(int id, int req) {
		int count = 0;
		for (Slot s: this.hotbar) {
			if (s.getItemID() == id) {
				count += s.getCount();
			}
		}
		if (count >= req) {
			return true;
		} else {
			for (Slot[] line: this.backInv) {
				for (Slot s: line) {
					if (s.getItemID() == id) {
						count += s.getCount();
					}
				}
			}
			if (count >= req) {
				return true;
			}
		}
		return false;
	}

	public Item getSelectedItem(int selected) {
		//Return the selected item
		//This will return null if the slot is empty
		return hotbar[selected].getItem();
	}
}
