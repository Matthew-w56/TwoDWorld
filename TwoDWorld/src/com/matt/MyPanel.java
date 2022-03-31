package com.matt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import com.matt.item.Items;
import com.matt.menu.Screens;

/**
 * Represents the screen that the game is being displayed in.
 * 
 * @author Matthew Williams
 *
 */
public class MyPanel  extends JPanel implements KeyListener {
	
	private static final long serialVersionUID = 1L;
	
	public MyPanel() {super();}
	
	@Override
	public void keyPressed(KeyEvent e) {
		//Respond to key press
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			if (O.player.fullInv) {		//If you are in the inventory
				O.player.toggleInv();		//Exit the inventory
			} else if (!(O.player.fullInv || O.menu.inMenu)) {	//Else, If you are not in the inventory or menu
				O.menu.show(Screens.inGameMenu);//Show the menu
			} else if (O.menu.inMenu) {		//Else, If you are in the menu
				O.menu.hide(); 					//Hide the menu
			}
			break;
		
		case KeyEvent.VK_G:
			//Toggle the chunk lines
			O.chunkLines = !O.chunkLines;
			O.player.give(Items.stonePickaxe.getNew(), 1);
			break;
			
		case KeyEvent.VK_B:
			//Toggle the block grid lines
			O.gridLines = !O.gridLines;
			break;
			
		case KeyEvent.VK_E:
			//Toggle the inventory
			if (!O.menu.inMenu) {
				O.player.toggleInv();
			}
			break;
		
		case KeyEvent.VK_P:
			//Color change to test item/block object disjoint
			O.player.inventory.getSelectedItem().setColor(Color.black);
		
		case KeyEvent.VK_V:
			//Toggle the entity lines
			O.entityOutlines = !O.entityOutlines;
			break;
		
		case KeyEvent.VK_C:
			//Open the creation window
			if (!O.menu.inMenu) {
				O.creationWindow.toggle();
			}
			break;
		
		case KeyEvent.VK_T:
			//Toggles all togglers at once
			O.displayCircle = !O.displayCircle;
			O.entityOutlines = !O.entityOutlines;
			O.gridLines = !O.gridLines;
			O.chunkLines = !O.chunkLines;
			break;
		
		default:
			//Otherwise, pass the key to the player
			O.player.listen(e.getKeyCode(), true);
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		//Set a key as false
		O.player.listen(e.getKeyCode(), false);
	}

	@Override public void keyTyped(KeyEvent e) {}
	
	@Override
	public void paintComponent(Graphics g) {
		//Fill in the background
		g.setColor(O.BGC);
		g.fillRect(0, 0, O.screenWidth, O.screenHeight);
		
		//Draw Chunk Manager
		O.chunkManager.display(g);
		
		//Update the mouse
		O.mouse.update();
		
		//If needed, draw the mouse box and outline the block the mouse is touching
		if (O.displayBox && !O.player.fullInv && !O.menu.inMenu && !O.creationWindow.visible) {
			int mouseX = O.MX + O.mouseOffsetX;
			int mouseY = O.MY + O.mouseOffsetY;
			g.setColor(Color.black);
			g.drawRect(mouseX - ((mouseX - O.movementOffsetX) % O.blockSize), 
					   mouseY - ((mouseY - O.movementOffsetY) % O.blockSize), O.blockSize, O.blockSize);
			g.drawRect(mouseX - 1, mouseY - 1, 2, 2);
		}
		
		//Draw Entities
		for (int i = 0; i < O.world.middleEntities.size(); i++) {
			try {
				O.world.middleEntities.get(i).display(g);
			} catch (IndexOutOfBoundsException e) {}
			
		}
		
		//Draw the player
		O.player.display(g);
		
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
}
