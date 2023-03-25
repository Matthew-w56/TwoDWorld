package com.matt.menu;

import java.awt.Graphics;

/**
 * Menu System:<p>
 * For this game I wanted a simple menu library that would accomplish
 * what I had in mind for the game and not much more. Integrating existing
 * libraries within the game in a clean way wasn't working out how I would
 * have liked it, so I made this simple system with screens, buttons, etc,
 * that would allow the desired functionality within the game. It doesn't
 * look pretty, but it works well.
 *
 * @author Matthew Williams
 *
 */
public class Menu {

	public boolean inMenu = false;

	public Menu() {}

	public void show(MenuScreen screen) {
		//Show the menu
		inMenu = true;
		Screens.activeScreen = screen;
	}

	public void hide() {
		//Hide the menu
		inMenu = false;
		Screens.activeScreen = null;
	}

	public void display(Graphics g) {
		//If you are in the menu,
		if (inMenu) {
			//Display the active screen
			if (Screens.activeScreen != null) {
				Screens.activeScreen.display(g);
			}
		}
	}
}
