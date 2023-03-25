package com.matt.menu;

import java.awt.Color;
import java.util.ArrayList;

import com.matt.O;

/**
 * Singleton class that builds and stores the different possible
 * menu screens that can show up in the game.
 *
 * @author Matthew Williams
 *
 */
public class Screens {
	public static ArrayList<MenuScreen> ScreenList = new ArrayList<>();
	public static MenuScreen activeScreen;

	public static MenuScreen inGameMenu = new MenuScreen(0, 220, 200);
	public static MenuScreen ExitConfirm = new MenuScreen(1, 150, 90);

	public static void create() {
		//--------------------------------------------------------------------------------------------------------------------------
		//Main Menu Screen that shows when you press ESCAPE
		System.out.println("[Screens] Building Screen of inventory width " + O.invWidth);
		Color greenButton = new Color(143, 179, 57);
		Color backgroundColor = new Color(183, 206, 99);
		Color darkerGreen = new Color(75, 88, 66);
		//Build the inGameMenu screen
		MenuScreen s = inGameMenu;

		s.add(new MenuLabel(s, "Menu", 30).setPos(-1, 30).setUnderline(darkerGreen, 2));

		s.y -= 100;
		s.setBackgroundColor(backgroundColor);
		s.add(new MenuBorder(s, 3, O.darkerBlue));
		s.add(new MenuBorder(s.x + 3, s.y + 3, s.width - 6, s.height - 6, 3, O.lighterBlue));
		s.width -= 18;
		s.extra = 18;

		MenuButton returnB = new MenuButton(s, "Return to Game");
		MenuButton exitB = new MenuButton(s, "Exit Game");

		//Set the ID, which is where the screen leads
		returnB.setID(-1);	//Exit menu
		exitB.setID(1);		//Exit Confirm Screen

		//Extra: (Screen Height - (buttonCount * buttonHeight)) / (buttonCount + 1)
		int extras = (s.getHeight()-75 - (2 * returnB.getHeight())) / 3;
		returnB.setPos(-1, extras + 75);
		exitB.setPos(-1, returnB.y + returnB.getHeight() + extras);

		returnB.setColor(greenButton);
		exitB.setColor(greenButton);

		s.add(returnB);
		s.add(exitB);
		//--------------------------------------------------------------------------------------------------------------------------

		//Build the Exit Confirm screen
		s = ExitConfirm;
		s.y -= 200;
		s.setBackgroundColor(backgroundColor);
		s.add(new MenuBorder(s, 3, O.darkerBlue));
		s.add(new MenuBorder(s.x + 3, s.y + 3, s.width - 6, s.height - 6, 3, O.lighterBlue));
		s.add(inGameMenu);

		MenuLabel PROMPT = new MenuLabel(s, "Are you sure?", 20).setUnderline(darkerGreen, 2).setPos(-1, 7);
		PROMPT.setMarginX(5);
		PROMPT.setMarginY(6);

		MenuButton EXIT = new MenuButton(s, "Yes");
		EXIT.setType(1);
		EXIT.setID(1);
		EXIT.setPos(-1, -1);
		EXIT.setPos(EXIT.x + 27, EXIT.y + 15);
		EXIT.setColor(greenButton);

		MenuButton CANCEL = new MenuButton(s, "No");
		CANCEL.setType(0);
		CANCEL.setID(0);
		CANCEL.setPos(0, -1);
		CANCEL.setPos(PROMPT.x, CANCEL.y + 15);
		CANCEL.setColor(greenButton);

		s.add(PROMPT);
		s.add(EXIT);
		s.add(CANCEL);

		//--------------------------------------------------------------------------------------------------------------------------
	}
}
