package com.matt.creation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.matt.O;

/**
 * The Creation Window goes with the menu system in a way, and
 * stores and displays the creation slots, allowing the player
 * to select and use different creation choices.
 *
 * @author Matthew Williams
 *
 */
public class CreationWindow {

	public boolean visible = false;
	int gridX, gridY, width, height, posX, posY, slotStartX, slotStartY;
	public ArrayList<CreationSlot> slots = new ArrayList<>();
	Rectangle exitButton;
	Font titleFont = new Font("arial", 1, 25);

	public CreationWindow() {
		this.width = O.creationGridX * O.creationSlotWidth + 30;
		this.height = O.creationGridY * O.creationSlotHeight + 115;
		this.posX = (O.screenWidth - this.width) / 2;
		this.posY = (O.screenHeight - this.width) / 2;
		this.gridX = O.creationGridX;
		this.gridY = O.creationGridY;
		this.slotStartX = this.posX + 15;
		this.slotStartY = this.posY + 100;
	}

	public void add(CreationSlot slot) {
		slot.addTo(this);
	}

	public void toggle() {
		if (this.visible) {
			this.hide();
		} else {
			this.show();
		}
	}

	//Shows all the creation slots.  Those that are currently available are shown first
	public void show() {
		ArrayList<CreationSlot> slots = new ArrayList<>(this.slots);
		int counter = 0;
		//TODO: Find a way to check which slots are creatable without having the window have
		//		access to the player.
		/*
		//Register all the slots that can be created currently
		for (int i = 0; i < slots.size(); i++) {
			CreationSlot slot = slots.get(i);
			if (O.player.canCreate(slot)) {
				slot.findPos(slotStartX, slotStartY, gridX, counter);
				counter++;
				slots.remove(slot);
			}
		}*/
		//Register the other slots
		for (CreationSlot slot: slots) {
			slot.findPos(slotStartX, slotStartY, gridX, counter);
			counter++;
		}

		this.visible = true;
	}

	public void hide() {
		for (CreationSlot slot: this.slots) {
			slot.rect.x = this.slotStartX;
			slot.rect.y = this.slotStartY;
		}
		this.visible = false;
	}

	public void draw(Graphics g) {
		//Fill the background
		g.setColor(O.lightBlue);
		//Draw the bi-colored border
		g.fillRect(this.posX, this.posY, this.width, this.height);
		for (int c = 0; c < 6; c++) {
			if (c < 2) {
				g.setColor(O.darkerBlue);
			} else {
				g.setColor(O.lighterBlue);
			}
			g.drawRect(this.posX + c, this.posY + c, this.width - (2 * c), this.height - (2 * c));
		}

		//Write the title
		g.setColor(Color.black);
		g.setFont(titleFont);
		g.drawString("The Instrumentation Creation Station!", posX + 40, posY + 48);

		//Draw the inside zone
		g.drawRect(slotStartX, slotStartY, gridX * O.creationSlotWidth, gridY * O.creationSlotWidth);
	}

	public void display(Graphics g) {
		if (this.visible) {
			this.draw(g);
			for (CreationSlot slot: this.slots) {
				slot.draw(g);
			}
		}
	}
}
