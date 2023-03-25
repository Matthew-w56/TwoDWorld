package com.matt.display;

import javax.swing.JFrame;

import com.matt.InputManager;
import com.matt.Main;
import com.matt.Mouse;
import com.matt.O;
import com.matt.menu.Screens;
import com.matt.world.WorldManager;

/* Hacksaw Notes:
 *  This class is completely new to the Hacksaw version.  The idea is that info about the screen position
 *  will be kept here, and updated based on the player's movement, but not directly altered by the player
 *  or chunkManager.
 *  So, this class needs to be passed the info it needs, preferably without relying on O.[class] references
 *  because those will be next to go, and to be able to display the screen given a list of chunks intersecting
 *  the screen rect, entities, the player rect, and any other info needed.  I think this class will query
 *  the World class with a rect of the screen position and dimensions, and it will return a list of blocks
 *  and entities that will need to be displayed. Then this class can do the math needed to decide where to
 *  display each thing (probably with a global offset variable for each axis that will be applied to the
 *  items' current position (for blocks this will be a final number, for entities it will only change
 *  with their movement).
 */

public class UIManager extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final int FPS = 60;

	private DisplayPanel panel;
	private WorldManager world_manager;

	public UIManager(WorldManager _world_manager, Mouse mouse) {
		super();

		//Set the size, location, and title for the screen
		setSize(O.screenWidth + 7, O.screenHeight + 30);
		setLocationRelativeTo(null);
		setTitle(O.screenTitle);
		setResizable(O.resize);

		//Create and add the panel that listens, and draws everything
		this.world_manager = _world_manager;
		this.panel = new DisplayPanel(_world_manager, mouse);
		this.add(panel);
	}

	public void addListeners(InputManager input_manager) {
		addWindowListener(input_manager);
		addKeyListener(input_manager);
		addMouseListener(input_manager);
		addMouseMotionListener(input_manager);
		addMouseWheelListener(input_manager);
	}

	public void begin() {
		System.out.println("[UIManager] Added Listeners");
		Screens.create();
		System.out.println("[UIManager] Menu Screens Generated");

		new Thread() {
			@Override public void run() {
				graphicsLoop();
			}
		}.start();
	}

	protected void graphicsLoop() {
		this.setVisible(true);

		System.out.println("[UIManager] Started");
		while (Main.going) {
			repaint();
			//TODO: Call this method regularly somewhere else
			world_manager.tickMouseWatcher();
			try {Thread.sleep(1000 / FPS);} catch (InterruptedException e) {}
		}
		System.out.println("[UIManager] Shutting Down");
		this.dispose();
	}

}
