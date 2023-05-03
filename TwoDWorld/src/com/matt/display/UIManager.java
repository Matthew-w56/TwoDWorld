package com.matt.display;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.matt.InputManager;
import com.matt.Main;
import com.matt.Mouse;
import com.matt.O;
import com.matt.Player;
import com.matt.block.BlockMolds;
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
 *  items' current position.
 */

public class UIManager extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final int FPS = 60;

	private DisplayPanel panel;
	private WorldManager world_manager;
	private Mouse mouse;
	
	protected Rectangle camera_frame;

	public UIManager(WorldManager _world_manager, Mouse mouse) {
		super();

		//Set the size, location, and title for the screen
		setSize(O.screenWidth+7, O.screenHeight+30);
		setLocationRelativeTo(null);
		setTitle(O.screenTitle);
		setResizable(O.resize);
		
		//For some reason, these specific numbers (-17, and -40) make it so that
		//the camera_frame matches the size of the drawable window.  No idea why,
		//but some trial and error resulted in these numbers.  They are just kind
		//of "magic numbers" for now.
		camera_frame = new Rectangle(0, 0, this.getWidth()-17, this.getHeight()-40);

		//Create and add the panel that listens, and draws everything
		this.world_manager = _world_manager;
		this.panel = new DisplayPanel(_world_manager, mouse, camera_frame);
		this.mouse = mouse;
		this.add(panel);
		
		//Print out the screen resolution
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println("[UIManager.constructor] Screen Resolution: " + (int)screenSize.getWidth() + ", " + (int)screenSize.getHeight());
	}

	public void addListeners(InputManager input_manager) {
		addWindowListener(input_manager);
		addKeyListener(input_manager);
		addMouseListener(input_manager);
		addMouseMotionListener(this.mouse);
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
		
		updateCameraFramePosition();
		
		while (Main.going) {
			//Readjust the position of the frame
			updateCameraFramePosition();
			//Repaint the drawable screen
			repaint();
			//Check to see if the mouse is being pressed, and if so: send that
			//corresponding action to the world_manager
			tickMouseWatcher();
			//Wait for the next frame
			try {Thread.sleep(1000 / FPS);} catch (InterruptedException e) {}
		}
		
		//Game play is over
		System.out.println("[UIManager] Shutting Down");
		this.dispose();
	}
	
	public void tickMouseWatcher() {
		
		if (O.mouseLeftDown) {
			if (!world_manager.getPlayer().fullInv && !O.menu.inMenu && !O.creationWindow.visible) {
				world_manager.breakAt(getMouseX(), getMouseY());
			}

		} else if (O.mouseRightDown) {
			if (!world_manager.getPlayer().fullInv && !O.menu.inMenu && !O.creationWindow.visible) {
				world_manager.activate(getMouseX(), getMouseY());
			}
		}
		
		//This should be done by the world_manager.
		BlockMolds.tick();
	}
	
	protected void updateCameraFramePosition() {
		Player player = world_manager.getPlayer();
		camera_frame.x = player.midX - (camera_frame.width / 2);
		camera_frame.y = player.midY - (camera_frame.height / 2);
	}
	
	/**
	 * Returns the position of the mouse in world coordinates.
	 * Using mouse.x only gives the screen position of the
	 * mouse.
	 * 
	 * @return World coordinates of mouse
	 */
	public int getMouseX() {
		return camera_frame.x + this.mouse.x;
	}
	
	/**
	 * Returns the position of the mouse in world coordinates.
	 * Using mouse.y only gives the screen position of the
	 * mouse.
	 * 
	 * @return World coordinates of mouse
	 */
	public int getMouseY() {
		return camera_frame.y + this.mouse.y;
	}
	
	

}
