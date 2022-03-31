package com.matt.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.matt.O;

/**
 * Design Container for the Menu system (see docs for Menu)
 * This represents a screen in the menu, that can be brought up by
 * a player's actions or by a button activation. Stores menu items
 * and provides the needed functionality.
 * 
 * @author Matthew Williams
 *
 */
public class MenuScreen {
	
	public int id, x, y;
	public int extra = 0;
	int width = O.screenWidth - 7;
	int height = O.screenHeight - 30;
	public ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
	public ArrayList<MenuScreen> backScreens = new ArrayList<MenuScreen>();
	public ArrayList<MenuBorder> borders = new ArrayList<MenuBorder>();
	public ArrayList<MenuLabel> labels = new ArrayList<MenuLabel>();
	Color bgc;
	
	public MenuScreen(int id) {
		this.id = id;
		Screens.ScreenList.add(this);
	}
	
	public MenuScreen(int id, int x, int y, int width, int height) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		Screens.ScreenList.add(this);
	}
	
	public MenuScreen(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.x = (O.screenWidth - width) / 2;
		this.y = (O.screenHeight - height) / 2;
		Screens.ScreenList.add(this);
	}
	
	public void setBackgroundColor(Color color) {
		//Set the backgroundcolor of the screen
		this.bgc = color;
	}
	
	public int getHeight() {
		//Return the screen's height
		return this.height;
	}
	
	public int getWidth() {
		//Return the screen's width
		return this.width;
	}
	
	public void setDisplay(boolean tf) {
		if (tf) {
			//System.out.println("[MenuScreen] Setting screen id " + id + " as activeScreen");
			Screens.activeScreen = this;
		} else {
			//System.out.println("[MenuScreen] Setting activeScreen as NULL");
			Screens.activeScreen = null;
		}
	}
	
	public void add(MenuButton button) {
		//Add a button to be displayed and looked for by the mouse
		this.buttons.add(button);
	}
	
	public void add(MenuScreen screen) {
		//Add an other screen to be displayed in the background of this one
		this.backScreens.add(screen);
	}
	
	public void add(MenuBorder border) {
		//Add a border to be displayed around/on this screen
		this.borders.add(border);
	}
	
	public void add(MenuLabel label) {
		//Add a label to be written on this screen
		this.labels.add(label);
	}
	
	public void move(int x, int y) {
		//Move this screen
		this.x += x;
		this.y += y;
	}
	
	public void displayBackground(Graphics g) {
		//Display all the screen you want behind you
		for (MenuScreen screen: backScreens) {
			screen.display(g);
		}
	}
	
	public void display(Graphics g) {
		this.displayBackground(g);
		
		if (this.bgc != null) {
			g.setColor(this.bgc);
			g.fillRect(this.x, this.y, this.width + extra, this.height);
		}
		
		//Draw the labels
		for (MenuLabel ml: labels) {
			ml.display(g);
		}
		
		//Display this screen, it's borders, and it's pieces
		for (MenuButton b: buttons) {
			b.draw(g);
		}
		
		//Draw the borders
		for (MenuBorder mb: borders) {
			mb.display(g);
		}
		
	}
}
