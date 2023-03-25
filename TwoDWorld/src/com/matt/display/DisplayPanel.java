package com.matt.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.matt.Mouse;
import com.matt.O;
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

	public DisplayPanel(WorldManager world, Mouse mouse) {
		super();
		this.world_manager = world;
		this.mouse = mouse;

	}

	@Override
	public void paintComponent(Graphics g) {
		//Fill in the background
		g.setColor(O.BGC);
		g.fillRect(0, 0, O.screenWidth, O.screenHeight);

		//Draw Chunk Manager
		world_manager.display(g);
		displayMouseItem(g, O.MX, O.MY);

		//If needed, draw the mouse box and outline the block the mouse is touching
		if (O.displayBox && !world_manager.getPlayer().fullInv && !O.menu.inMenu && !O.creationWindow.visible) {
			int mouseX = O.MX + O.mouseOffsetX;
			int mouseY = O.MY + O.mouseOffsetY;
			g.setColor(Color.black);
			g.drawRect(mouseX - ((mouseX - O.movementOffsetX) % O.blockSize),
					   mouseY - ((mouseY - O.movementOffsetY) % O.blockSize), O.blockSize, O.blockSize);
			g.drawRect(mouseX - 1, mouseY - 1, 2, 2);
		}



		//Draw the circle around player showing the place distance
		if (O.displayCircle) {
			g.drawOval(O.screenWidth/2 - O.placeDistance, O.screenHeight/2 - O.placeDistance,
					   O.placeDistance * 2, O.placeDistance * 2);
		}

		if(g instanceof Graphics2D)
	      {
	        Graphics2D g2 = (Graphics2D)g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	        RenderingHints.VALUE_ANTIALIAS_ON);
	        g2.setFont(new Font("timesroman", 0, 12));

	        //Draw the tutorial text when it should be drawn
	        g2.setFont(O.promptFont);
	        if (!O.tutProgress[4] && O.runningTutorial) {
	        	if (!O.tutProgress[0]) {
	        		drawTutPrompt(g, "Press A and D to move");
		        } else if (!O.tutProgress[1]) {
		        	drawTutPrompt(g, "Press W or SPACE to Jump");
		        } else if (!O.tutProgress[2]) {
		        	drawTutPrompt(g, "Left Click to break blocks..");
		        } else if (!O.tutProgress[3]) {
		        	drawTutPrompt(g, ".. And Right Click to place them!");
		        } else if (!O.tutProgress[4]) {
		        	drawTutPrompt(g, "Lastly, press E to open your inventory");
		        }
	        }
	    }
		//Display the creation window
		O.creationWindow.display(g);
		//Display the Menu last
		O.menu.display(g);
	}

	public void drawTutPrompt(Graphics g, String text) {
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

	public void drawPrompt(Graphics g, String text, int x, int y) {
		//This is a method to simplify the Tutorial Prompts
		int width = g.getFontMetrics().stringWidth(text);
    	int height = g.getFontMetrics().getHeight();

    	g.setColor(O.alphaBlue);
    	g.fillRect(x - 15, y, width + 30, height + 30);
    	g.setColor(Color.black);
    	g.drawRect(x - 15, y, width + 30, height + 30);

    	g.drawString(text, x, y + 35);
	}

	//From the old mouse.java class (back when it did way too much)
	public void displayMouseItem(Graphics g, int X, int Y) {
		//If the mouse has an item, and the player is in the inventory
		if (mouse.getItem() != null && world_manager.getPlayer().fullInv) {
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
}
