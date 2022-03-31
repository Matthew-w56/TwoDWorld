package com.matt.world;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.JFrame;

import com.matt.MyPanel;
import com.matt.O;
import com.matt.block.Block;
import com.matt.creation.CreationSlot;
import com.matt.entity.Entity;
import com.matt.entity.entities.Cow;
import com.matt.entity.entities.Pig;
import com.matt.item.Item;
import com.matt.item.Items;
import com.matt.menu.Screens;

/**
 * Represents the World, which is largely an array of Chunks.
 * More on the movement and management of the world and it's
 * objects can be found in ChunkManager.
 * 
 * @author Matthew Williams
 *
 */
public class World extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public MyPanel panel;
	public ArrayList<Entity> middleEntities = new ArrayList<Entity>();
	
	public World() {
		//Set the size, location, title, and panel listeners for the screen
		setSize(O.screenWidth + 7, O.screenHeight + 30);
		setLocationRelativeTo(null);
		setTitle(O.screenTitle);
		setResizable(O.resize);
		
		//Create and add the panel that listens, and draws everything
		this.panel = new MyPanel();
		add(panel);
	}
	
	public void addListeners() {
		System.out.println("[World] Adding Listeners..");
		
		//Add the exit listener for the X press
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				O.world.close();
			}
		});
		
		//Add the key listener
		addKeyListener(panel);
		//Add the mouse listener
		addMouseListener(O.mouse);
		//Add the mouse motion listener
		addMouseMotionListener(O.mouse);
		//Add the mouse wheel listener
		addMouseWheelListener(O.mouse);
	}
	
	public void generate() {
		
		Screens.create();
		System.out.println("[World] Menu Screens Generated");
		
		//Create the Chunk Manager, and print out the success
		O.chunkManager.create();
		System.out.println("[World] Chunk Manager Created");
		System.out.println();
		
		//Print out the screen resolution
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println("[World.generate] Screen Resolution: " + (int)screenSize.getWidth() + ", " + (int)screenSize.getHeight());
		
		//Add the Entities Manually (for now)
		this.middleEntities.add(new Pig(200, 355));
		this.middleEntities.add(new Cow(400, 355));
		
		//Create the Creation Recipes
		O.creationWindow.slots.add(new CreationSlot(Items.log, 1, new Item[] {Items.leaf}, new int[] {3}));
		O.creationWindow.slots.add(new CreationSlot(Items.woodPickaxe, 1, new Item[] {Items.log, Items.stick}, new int[] {3, 2}));
		O.creationWindow.slots.add(new CreationSlot(Items.woodAxe, 1, new Item[] {Items.log, Items.stick}, new int[] {3, 2}));
		O.creationWindow.slots.add(new CreationSlot(Items.stonePickaxe, 1, new Item[] {Items.stone, Items.stick}, new int[] {3, 2}));
		O.creationWindow.slots.add(new CreationSlot(Items.stoneAxe, 1, new Item[] {Items.stone, Items.stick}, new int[] {3, 2}));
		O.creationWindow.slots.add(new CreationSlot(Items.stick, 2, new Item[] {Items.log}, new int[] {1}));
		O.creationWindow.slots.add(new CreationSlot(Items.woodChest, 1, new Item[] {Items.log, Items.stone}, new int[] {6, 1}));
	}
	
	public void close() {
		System.out.println();
		System.out.println();
		System.out.println();
		
		//Notify the threads that the program is over, and close the screen
		O.going = false;
		System.out.println("[World] Shutting Down");
		O.world.dispose();
	}
	
	public void start() {
		//Set the screen to visible
		setVisible(true);
	}

	public ArrayList<Block> getCollidingBlocks(Rectangle rect) {
		
		ArrayList<Block> endList = new ArrayList<Block>();
		
		//Loop through each chunk checking for collision
		for (Chunk chunk: ChunkManager.chunkListMiddle) {
			if (chunk.rect.intersects(rect)) {
				
				//Mathmatically find the area in the chunk you need to search
				int start = (int)(rect.x - chunk.rect.x) / O.blockSize;
				int length = 2 + (rect.width / O.blockSize);
				int startY = (int)(rect.y - chunk.rect.y) / O.blockSize;
				int height = 2 + (rect.height / O.blockSize);
				
				//Logically check to make sure the numbers are in the boundaries
				if (start < 0) {
					length += start;
					start = 0;
				} else if (start + length >= O.chunkSize) {
					length = O.chunkSize - start;
				}
				if (startY < 0) {
					height += startY;
					startY = 0;
				} else if (startY + height >= O.chunkSize * O.chunkRatio) {
					height = O.chunkSize * O.chunkRatio - startY;
				}
				
				//Loop through selected regions and check for collisions
				for (int c = start; c < start + length; c++) {
					for (int r = startY; r < startY + height; r++) {
						if (chunk.blocks[r][c] != null && chunk.blocks[r][c].getRect() != null && chunk.blocks[r][c].getCollides()) {
							if (chunk.blocks[r][c].getRect().intersects(rect)) {
								endList.add(chunk.blocks[r][c]);
							}
						}
					}
				}
			}
		}
		
		return endList;
	}
	
	public ArrayList<Block> getBlocks(int x, int y) {
		//This is the same function as getCollidingBlocks(rect), but
		//This method does not require the blocks to register as collision blocks to check them
		//(Used mainly by the mouse for placing and breaking things)
		
		ArrayList<Block> endList = new ArrayList<Block>();
		
		//Loop through each chunk checking for collision
		try {
			Chunk chunk;
			for (int i = 0; i < ChunkManager.chunkListMiddle.size(); i++) {
				chunk = ChunkManager.chunkListMiddle.get(i);
				if (chunk.rect.contains(x, y)) {
					
					//Mathmatically find the area in the chunk you need to search
					int start = (int)(x - chunk.rect.x) / O.blockSize;
					int length = 2 + (O.playerWidth / O.blockSize);
					int startY = (int)(y - chunk.rect.y) / O.blockSize;
					int height = 2 + (O.playerHeight / O.blockSize);
					
					//Logically check to make sure the numbers are in the boundaries
					if (start < 0) {
						length += start;
						start = 0;
					} else if (start + length >= O.chunkSize) {
						length = O.chunkSize - start;
					}
					if (startY < 0) {
						height += startY;
						startY = 0;
					} else if (startY + height >= O.chunkSize * O.chunkRatio) {
						height = O.chunkSize * O.chunkRatio - startY;
					}
					
					//Loop through selected regions and check for collisions
					for (int c = start; c < start + length; c++) {
						for (int r = startY; r < startY + height; r++) {
							if (chunk.blocks[r][c] != null && chunk.blocks[r][c].getRect() != null) {
								if (chunk.blocks[r][c].getRect().contains(x, y)) {
									endList.add(chunk.blocks[r][c]);
								}
							}
						}
					}
				}
			}
		} catch (ConcurrentModificationException e) {
			
		}
		
		return endList;
	}
}
