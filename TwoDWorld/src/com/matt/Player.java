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
 * @author Matthew Williams
 *
 */

//TODO: Gut out all the functionality that does not belong here.  Maybe have a separate Mouse Input Manager?

public class Player {

	private final double SPEED = 2.4;
	private final double JUMP_HEIGHT = 15.0;
	private final double MAX_SPEED_Y = 20.1;
	private final double MAX_SPEED_X = 10.5;
	private final double GROUND_FRICTION = 1.8;
	private final double AIR_FRICTION = 0.5;
	//TODO: Make this global?  Or maybe a variable in the world class (pass in through updateYVelocity())
	private final double GRAVITY = 0.8;

	//Start up variables for the Player class
	public Inventory inventory;
	public Rectangle rect;

	public double xVel, yVel;

	public int selected, midX, midY, direction;
	public boolean fullInv = false, onGround = false;
	public boolean keyRight, keyLeft, keyUp, keyDown, keyLShift;
	
	private final int startX = 720, startY = 2900;

	public Player() {
		rect = new Rectangle(startX, startY, O.playerWidth, O.playerHeight);
		this.inventory = new Inventory();

		selected = 0;
		xVel = 0;
		yVel = 0;

		updateMidPos();
	}

	public void listen(int k, boolean down) {
		//Set the key based on the inputted key, and whether it should be set up or down
		if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) {
			keyRight = down;
		} else if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) {
			keyLeft = down;
		} else if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W || k == KeyEvent.VK_SPACE) {
			keyUp = down;
		} else if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) {
			keyDown = down;
		} else if (k == KeyEvent.VK_SHIFT) {
			keyLShift = down;
		} else if (down) {
			//Check all the numbers in the range for hotbar slots, and see if that is what they pressed
			for (int x = 0; x < O.hotCount; x++) {
				//48 is the value for 0, so when x=0, it is checking if they pressed 1
				if (k == 49 + x) {
					selected = x;
				}
			}
		}

		updateDirection();
	}

	public void updateYVelocity() {
		//Gravity Adjustment
		yVel += GRAVITY;

		//Decide if the player should jump
		if (keyUp && onGround) {
			jump();
		}

		//Keep speed within Maximum bounds
		if (yVel >  MAX_SPEED_Y) yVel =  MAX_SPEED_Y;
		if (yVel < -MAX_SPEED_Y) yVel = -MAX_SPEED_Y;
	}

	public void updateXVelocity() {
		//Movement adjustment
		xVel += (SPEED * direction);

		//Ground or Air Friction adjustment
		if (xVel != 0) {
			if (onGround) {  //On Ground
				if (xVel > 0) xVel = Math.max(xVel - GROUND_FRICTION, 0);
				if (xVel < 0) xVel = Math.min(xVel + GROUND_FRICTION, 0);
			} else { 		 //In Air
				if (xVel > 0) xVel = Math.max(xVel -    AIR_FRICTION, 0);
				if (xVel < 0) xVel = Math.min(xVel +    AIR_FRICTION, 0);
			}
		}

		//keep speed within Maximum bounds
		if (xVel >  MAX_SPEED_X) xVel =  MAX_SPEED_X;
		if (xVel < -MAX_SPEED_X) xVel = -MAX_SPEED_X;
	}

	/**
	 * Sets the point that represents the middle of
	 * the player's rectangle hitbox.  These values
	 * are used when calculating the distance from
	 * an object to the player.
	 */
	public void updateMidPos() {
		midX = rect.x + (rect.width / 2);
		midY = rect.y + (rect.height / 2);
	}

	/**
	 * Set the direction of the player by looking at
	 * whether the user is moving left, right, both, or
	 * neither.
	 *
	 * Right = 1, Left = -1, Neither = 0
	 */
	protected void updateDirection() {
		if (keyRight) direction = 1;
		if (keyLeft) direction = -1;
		if (keyRight && keyLeft) direction = 0;
		if (!keyRight && !keyLeft) direction = 0;
	}

	/**
	 * Not a huge fan of this being here.  Checks to see
	 * if the player can create the item in the slot, and if
	 * so it gives the item to the player and takes the
	 * ingredients.
	 * @param slot
	 */
	public void leftClick(CreationSlot slot) {			//Left click in the Creation Menu
		if (this.canCreate(slot)) {
			for (int x = 0; x < slot.requireds.length; x++) {
				this.takeByID(slot.requireds[x].getId(), slot.required_counts[x]);
			}
			this.give(slot.product.getNew(), slot.product_count);
		}
	}

	public void jump() {
		this.yVel = -JUMP_HEIGHT;
		onGround = false;
	}

	//TODO: This isn't being called right now.  Once this is reinstated, (this method needs to move?)
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

	/**
	 * Adds the given item to the player's inventory
	 * @param item Item type to give player
	 * @param count Number of this item to give the player
	 * @return True if the item fit.  False otherwise.
	 */
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

	/**
	 * Draws the player at it's rect location.
	 *
	 * @param g Graphics object passed from JFrame.repaint
	 */
	public void display(Graphics g, Rectangle camera_frame) {
		
		//Fill in middle area, then black outline, then display the inventory
		g.setColor(O.playerColor);
		g.fillRect(O.playerX, O.playerY, rect.width, rect.height);
		g.setColor(Color.black);
		g.drawRect(O.playerX, O.playerY, rect.width, rect.height);

		//TODO: Refactor this to remove the need for the additional parameters
		this.inventory.display(g, selected, fullInv);
	}
}
