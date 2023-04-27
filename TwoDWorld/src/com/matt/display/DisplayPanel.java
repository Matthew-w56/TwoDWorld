package com.matt.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.matt.Mouse;
import com.matt.O;
import com.matt.block.Block;
import com.matt.world.WorldManager;

/**
 * Represents the screen that the game is being displayed in.
 *
 * Concerns of this class:
 *
 *   Taking info from the world
 *   displaying that info
 *   adding any other lines, details, menus, and such that need to be dealt with.
 *
 * @author Matthew Williams
 *
 */
public class DisplayPanel  extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected WorldManager world_manager;
	protected Mouse mouse;
	protected Rectangle camera_frame;

	public DisplayPanel(WorldManager world, Mouse mouse, Rectangle camera_frame) {
		super();
		this.world_manager = world;
		this.mouse = mouse;
		this.camera_frame = camera_frame;
	}

	@Override
	public void paintComponent(Graphics g) {
		
		//Fill in the background
		g.setColor(O.BGC);
		g.fillRect(0, 0, O.screenWidth, O.screenHeight);
		
		//draw the world, generally
		world_manager.displayWorld(g, camera_frame);
		
		//Draw first stage of optional things
		drawOptionalBackgroundMarkings(g);
		
		world_manager.displayEntities(g, camera_frame);
		
		//Draw any chunk lines, block lines, placement range circle, etc.
		drawOptionalMarkings(g);
		
		//Draw the item the mouse is holding, if in inventory and has one
		if (world_manager.getPlayer().fullInv) displayMouseItem(g, this.mouse.x, this.mouse.y);
		
		
		
		//If the tutorial is still active, draw the most recent prompt
		if (!O.tutProgress[4]) drawTutorialPrompts(g);
		
		//Display the creation window
		O.creationWindow.display(g);
		
		//Display the Menu last
		O.menu.display(g);
	}
	
	private void drawOptionalMarkings(Graphics g) {
		//If needed, draw the mouse box and outline the block the mouse is touching

		//Draw the circle around player showing the place distance
		if (O.displayCircle) {
			g.drawOval(O.screenWidth/2 - O.placeDistance, O.screenHeight/2 - O.placeDistance,
					   O.placeDistance * 2, O.placeDistance * 2);
		}
	}
	
	/**
	 * Draws any additional markings, outlines, etc. that are meant to go behind
	 * the player and any entities.  This includes the box around the mouse,
	 * the outline around the block being focused on, and the chunk and block lines
	 * @param g
	 */
	private void drawOptionalBackgroundMarkings(Graphics g) {
		//Draw the box around the block that the mouse is touching, and a small box around the mouse pos itself
		if (O.displayBox && !world_manager.getPlayer().fullInv && !O.menu.inMenu && !O.creationWindow.visible) {
			g.setColor(Color.black);
			
			Block hoveredBlock = world_manager.getBlock(mouseWorldX(), mouseWorldY());
			if (hoveredBlock == null) return;
			Rectangle hbRect = hoveredBlock.rect;
			g.drawRect(hbRect.x - camera_frame.x, hbRect.y - camera_frame.y, O.blockSize, O.blockSize);
			g.drawRect(mouse.x - 1, mouse.y - 1, 2, 2);
		}
	}
	
	private void drawTutorialPrompts(Graphics g) {
		//This chunk preps a g2 object for text drawing in the method
		Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(O.promptFont);
        
		if (!O.tutProgress[0]) {
    		drawTutPrompt(g2, "Press A and D to move");
        } else if (!O.tutProgress[1]) {
        	drawTutPrompt(g2, "Press W or SPACE to Jump");
        } else if (!O.tutProgress[2]) {
        	drawTutPrompt(g2, "Left Click to break blocks..");
        } else if (!O.tutProgress[3]) {
        	drawTutPrompt(g2, ".. And Right Click to place them!");
        } else if (!O.tutProgress[4]) {
        	drawTutPrompt(g2, "Lastly, press E to open your inventory");
        }
	}
	
	private void drawTutPrompt(Graphics g, String text) {
		//This is a method to simplify the Tutorial Prompts
		int width = g.getFontMetrics().stringWidth(text);
    	int height = g.getFontMetrics().getHeight();
    	int x = (O.screenWidth - width) / 2;

    	g.setColor(O.alphaBlue);
    	g.fillRect(x - 15, 95, width + 30, height + 30);
    	g.setColor(Color.black);
    	g.drawRect(x - 15, 95, width + 30, height + 30);

    	g.drawString(text, x, 130);
	}
	
	private void displayMouseItem(Graphics g, int X, int Y) {
		//If the mouse has an item, and the player is in the inventory
		if (mouse.getItem() != null) {
			g.setFont(O.fontSmall);
			//Set the color accordingly
			g.setColor(mouse.getItem().getColor());
			//Draw the item block
			g.fillRect(X-O.backItemSize / 2, Y-O.backItemSize, (2 * O.backItemSize) / 3, (2 * O.backItemSize) / 3);
			//Draw the black border for the item
			g.setColor(Color.black);
			g.drawRect(X-O.backItemSize / 2, Y-O.backItemSize, (2 * O.backItemSize) / 3, (2 * O.backItemSize) / 3);
			if (mouse.getCount() < 10) {
				g.drawString("" + mouse.getCount(), X, (Y-O.backItemSize/2));
			} else {
				g.drawString("" + mouse.getCount(), X - 6, (Y-O.backItemSize/2));
			}
		}
	}
	
	private int mouseWorldX() {
		return camera_frame.x + mouse.x;
	}
	
	private int mouseWorldY() {
		return camera_frame.y + mouse.y;
	}
}
