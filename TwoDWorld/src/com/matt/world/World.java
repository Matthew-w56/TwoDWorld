package com.matt.world;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;

import com.matt.O;
import com.matt.Player;
import com.matt.block.Block;
import com.matt.creation.CreationSlot;
import com.matt.entity.Entity;
import com.matt.entity.entities.Cow;
import com.matt.entity.entities.Pig;
import com.matt.item.Item;
import com.matt.item.Items;

//TODO: BIG ONE!  Make all chunks in a continuous list, and indicies indicating which
//chunks are "active" or not.  No moving, no staching, just indicators

/**
 * Represents the World, which is largely an array of Chunks.
 * This class also has methods to handle the movement and other
 * things with entities, and the player.
 * 
 * @author Matthew Williams
 *
 */
public class World {
	
	protected Player player;
	protected ArrayList<Entity> middleEntities;
	
	//Initialize the list of chunks and blocks to use
	public ArrayList<Chunk> chunkListLeft;
	public ArrayList<Chunk> chunkListMiddle;
	public ArrayList<Chunk> chunkListRight;
	
	
	public World() {
		//Instatiate Class-based fields
		player = new Player();
		
		//Instatiate other fields
		middleEntities = new ArrayList<Entity>();
		
		chunkListLeft = new ArrayList<Chunk>();
		chunkListMiddle = new ArrayList<Chunk>();
		chunkListRight = new ArrayList<Chunk>();
	}
	
	public void generate() {
		
		//TODO: This does not belong here
		//Print out the screen resolution
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println("[World.generate] Screen Resolution: " + (int)screenSize.getWidth() + ", " + (int)screenSize.getHeight());
		
		//Add the Entities Manually (for now)
		this.middleEntities.add(new Pig(200, 355));
		this.middleEntities.add(new Cow(400, 355));
		
		//Create the Creation Recipes
		//TODO: This does not belong here
		O.creationWindow.slots.add(new CreationSlot(Items.log, 1, new Item[] {Items.leaf}, new int[] {3}));
		O.creationWindow.slots.add(new CreationSlot(Items.woodPickaxe, 1, new Item[] {Items.log, Items.stick}, new int[] {3, 2}));
		O.creationWindow.slots.add(new CreationSlot(Items.woodAxe, 1, new Item[] {Items.log, Items.stick}, new int[] {3, 2}));
		O.creationWindow.slots.add(new CreationSlot(Items.stonePickaxe, 1, new Item[] {Items.stone, Items.stick}, new int[] {3, 2}));
		O.creationWindow.slots.add(new CreationSlot(Items.stoneAxe, 1, new Item[] {Items.stone, Items.stick}, new int[] {3, 2}));
		O.creationWindow.slots.add(new CreationSlot(Items.stick, 2, new Item[] {Items.log}, new int[] {1}));
		//O.creationWindow.slots.add(new CreationSlot(Items.woodChest, 1, new Item[] {Items.log, Items.stone}, new int[] {6, 1}));
		
		this.create();
	}

	public ArrayList<Block> getCollidingBlocks(Rectangle rect) {
		//TODO: SOLID: change this to be reused code with method below it.
		
		ArrayList<Block> endList = new ArrayList<Block>();
		
		//Loop through each chunk checking for collision
		for (Chunk chunk: chunkListMiddle) {
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
						if (chunk.blocks[r][c] != null && chunk.blocks[r][c].getRect() != null && chunk.blocks[r][c].mold.getCollides()) {
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
	
	/**
	 * This method acts a lot like getCollidingBlocks(rect), but only requires
	 * a point rather than a rectangle object.  Mostly used by the mouse for placing
	 * and breaking things.
	 * 
	 * Because the colliding area is just a point, it returns a single block rather
	 * than a list of blocks.
	 * 
	 * @param x Position of the mouse
	 * @param y Position of the mouse
	 * @return List of blocks that collide with that point
	 * 
	 * TODO: Optimize this (use math not collide method)
	 */
	public Block getBlock(int x, int y) {
		//This is the same function as getCollidingBlocks(rect), but
		//This method does not require the blocks to register as collision blocks to check them
		//(Used mainly by the mouse for placing and breaking things)
		
		//TODO: This can just become math to decide the index of what
		//		block to return, none of this other stuff needed.  Change
		//		this please.
		
		//Loop through each chunk checking for collision
		try {
			for (int i = 0; i < chunkListMiddle.size(); i++) {
				Chunk chunk = chunkListMiddle.get(i);
				
				//If the mouse is within the x bounds of the chunk
				if (chunk.rect.x < x && x < (chunk.rect.x + chunk.rect.width)) {
					
					//TODO: Will be able to calculate chunk rather than loop once they are all..
					//  ..in the same list, rather than spread out.
					
					//Calculate the position in block terms, and return that block
					return chunk.blocks
							[(y - chunk.rect.y) / O.blockSize]
							[(x - chunk.rect.x) / O.blockSize];
				}
			}
		} catch (ConcurrentModificationException e) {
			System.out.println("[World.getBlock(x,y)] Caught a CME and Died!");
		}
		
		return null;
	}
	
	public boolean entityIsTouchingMiddleChunk(Entity e) {
		for (Chunk chunk: chunkListMiddle) {
			if (e.getModel().hit_box.get(0).rect.intersects(chunk.rect)) {
				return true;
			}
		}
		return false;
	}
	
	public void handleEntities() {
		
		if (!O.shouldMove) return;
		
		for (Entity entity: middleEntities) {
			//1) Decide how to move the entity
			entity.findMovement();
			
			//2) Move the entity
			moveEntity(entity);
			
			//3) Decide if the entity should jump or not
			entity.decideJump();
			
			//4) Update entity state by adjusting for gravity, and decrimenting their wander counter
			entity.updatePhysics();
			entity.wander -= Math.abs(entity.getModel().move_speed);
			
			//5) Check to see if the entity should be stached into an inactive chunk
			checkShouldStacheEntity(entity);
		}
		
	}
	
	public void handlePlayer() {
		//HERE IS WHERE WE MOVE THE PLAYER
		
		
		//Step 1) Update Physics
		
		player.updateXVelocity();
		player.updateYVelocity();
		
		//Step 2) Move the player
		
		movePlayer();
		
		//OLD CODE FROM PREVIOUS SYSTEM
		//Move with the speed both directions
		//chunkManager.move((int)O.currentPlayerSpeed, (int)O.verticalSpeed); //OLDEST
		//player.move(O.currentPlayerSpeed, O.verticalSpeed);
		
		//At end
		player.updateMidPos();
	}
	
	protected void movePlayer() {
		
		movePlayerX();
		movePlayerY();
		
	}
	
	protected void movePlayerX() {
		//This method has 2 stages: Try, then Move
		
		//Stage 1) Try
		
		Rectangle testRect = new Rectangle(player.rect);
		
		testRect.x += player.xVel;
		
		ArrayList<Block> colliding_blocks = getCollidingBlocks(testRect);
		if (colliding_blocks.size() > 0) {
			Block block = colliding_blocks.get(0);
			//Push the test rect out of any colliding blocks
			if (player.xVel < 0) testRect.x = block.getRect().x + block.getRect().width;
			else if (player.xVel > 0) testRect.x = block.getRect().x - O.playerWidth;
		}
		
		//Stage 2) Move
		
		if ((testRect.x != player.rect.x) && !O.tutProgress[0]) {
			O.tutProgress[0] = true;
		}
		
		player.rect.x = testRect.x;
	}
	
	protected void movePlayerY() {
		//This method has 2 stages: Try, then Move
		
		//Stage 1) Try
		
		Rectangle testRect = new Rectangle(player.rect);
		
		testRect.y += player.yVel;
		player.onGround = false;
		
		ArrayList<Block> colliding_blocks = getCollidingBlocks(testRect);
		if (colliding_blocks.size() > 0) {
			Block block = colliding_blocks.get(0);
			if (player.yVel > 0) { //going down
				testRect.y = block.getRect().y - testRect.height;
				player.yVel = 0;
				player.onGround = true;
			}
			//going up
			else if (player.yVel > 0) testRect.y = block.getRect().y + block.getRect().height;
		}
		
		//Stage 2) Move
		
		if (testRect.y != player.rect.y && O.tutProgress[0]) O.tutProgress[1] = true;
		
		player.rect.y = testRect.y;
	}
	
	protected void moveEntity(Entity entity) {
		
		//		-----[ Step 1) Find how much the entity should move in the X direction ]-----
		
		//Set up how much movement is desired (x), and make a dummy to try it on (testRect)
		int x = (int) entity.getModel().move_speed;
		Rectangle testRect = new Rectangle(entity.getModel().hit_box.get(0).rect);
		
		//Record the starting x position for later referal
		int startX = testRect.x;
		
		//Actually move the test rect
		testRect.x += x;
		
		ArrayList<Block> collide_list_x = getCollidingBlocks(testRect);
		if (collide_list_x.size() > 0) {
			Block b = collide_list_x.get(0);
			if (x > 0) {
				testRect.x = b.getRect().x - testRect.width;
			} else if (x < 0) {
				testRect.x = b.getRect().x  + b.getRect().width;
			}
		}
		
		//Record how much the testRect actually successfully moved
		int dx = testRect.x - startX;
		
		
		//MOVE Y-------------------------------------------------------------------
		int y = (int)entity.getModel().yVel;
		//Set a testRect as the same as entity rect
		testRect = new Rectangle(entity.getModel().hit_box.get(0).rect);
		
		//Record where it started, then move it
		int startY = testRect.y;
		testRect.y += y;
		
		//Assume on_ground to be false, then check to see if it is
		//true with the collisions
		entity.getModel().on_ground = false;
		
		ArrayList<Block> collide_list_y = getCollidingBlocks(testRect);
		if (collide_list_y.size() > 0) {
			Block b = collide_list_y.get(0);
			if (y > 0) {
				testRect.y = b.getRect().y - testRect.height;
				entity.getModel().yVel = 0;
				entity.getModel().on_ground = true;
			} else if (y < 0) {
				testRect.y = b.getRect().y + b.getRect().height;
			}
		}
		
		//Record how much the testRect actually successfully moved
		int dy = testRect.y - startY;
		
		if (entity.getModel().built) {
			//System.out.println("[Model] Movement being updated by: " + source.toUpperCase());
			entity.getModel().moveX(dx);
			entity.getModel().moveY(dy);
		}
		
		//Update the sensors on the entity to provide feedback to the entity about it's immediate surroundings
		entity.updateSensors(getCollidingBlocks(entity.getModel().hit_box.get(0).rect));
		
	}
	
	protected void checkShouldStacheEntity(Entity e) {
		if (!entityIsTouchingMiddleChunk(e)) {
			ArrayList<Chunk> list;
			//TODO: Position Checking needs to update with new movement system
			if (e.getModel().getPos()[0] > 100) {
				list = chunkListRight;
			} else {
				list = chunkListLeft;
			}
			stacheEntity(e, list.get(list.size()-1));
		}
	}
	
	protected void stacheEntity(Entity e, Chunk c) {
		//System.out.println("[Entity] Storing Entity in Side List");
		e.is_in_middle = false;				//Remove the middle toggle
		middleEntities.remove(e);	//Remove entity from the middle list
		c.addEntity(e);		//Add the entity to the chunk
		e.target_pos = new int[] {e.getModel().hit_box.get(0).rect.x - c.rect.x, e.getModel().hit_box.get(0).rect.y - c.rect.y};
									//Store the target pos for later reference
		e.home_chunk = c;	//Set the chunk as this entity's home_chunk
	}
	
	protected void pushEntityToMiddle(Entity e) {
		//System.out.println("[Entity] Retrieving Entity from Side List");
		e.is_in_middle = true;			//Set the middle toggle
		middleEntities.add(e);	//Add entity to world middle list
		if (e.home_chunk != null) {		//If the entity was in a chunk before:
			e.getModel().setPos(new int[] {e.home_chunk.rect.x + e.target_pos[0], e.home_chunk.rect.y + e.target_pos[1]}, "World Manager.pushEntity");
												//Set the entity's position as target position relative to chunk's position
			e.home_chunk = null;				//Forget old home_chunk
		}
	}
	
	
	
	
	//COPY-PASTED FROM CHUNK MANAGER BELOW --------------------------------------------------------------------------------------------------------------
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void chunkToMiddle(Chunk chunk) {
		if (chunk.s == 0) {					//From left list
			chunk.rect.x = this.getMin().rect.x - O.chunkWidth;
			chunkListLeft.remove(chunk);
		} else if (chunk.s == 2) {			//From right list
			chunk.rect.x = this.getMax().rect.x + O.chunkWidth;
			chunkListRight.remove(chunk);
		}
		
		//Add the chunk to the middle
		chunkListMiddle.add(chunk);
		//Set the s indicator as middle and add blocks
		chunk.s = 1;
		//Push the entities to the middle
		//chunk.flushEntities();
		int length = chunk.entities.size();
		for (int x = 0; x < length; x++) {
			pushEntityToMiddle(chunk.entities.get(0));
			chunk.entities.remove(0);
		}
	}
	
	public void chunkToLeft(Chunk chunk) {
		//Takes a chunk from the middle list, and puts it in the left list
		if (chunkListMiddle.contains(chunk)) {
			chunkListMiddle.remove(chunk);
			chunkListLeft.add(chunk);
			
			//---[Move Block One]---
			//Adjust the x position and set the s indicator as left
			chunk.rect.x = -O.chunkWidth;
			chunk.s = 0;
			//---------------------
			
			
			//---[Move Block Two]---
			for (Entity entity: middleEntities) {
				if (entity.getModel().hit_box.get(0).rect.intersects(chunk.rect) && !entityIsTouchingMiddleChunk(entity)) {
					middleEntities.remove(entity);
					stacheEntity(entity, chunk);
				}
			}
			//-------------------------
		}
	}
	
	public void chunkToRight(Chunk chunk) {
		//Takes a chunk from the middle list, and adds it to the right list
		if (chunkListMiddle.contains(chunk)) {
			chunkListMiddle.remove(chunk);
			chunkListRight.add(chunk);
			
			//---[Move Block One (normalize the Xposition)]---
			//Adjust the x value of the chunk, and set the s indicator as right
			chunk.rect.x = O.screenWidth;
			chunk.s = 2;
			//---------------------
			
			
			//---[Move Block Two(check for dependant entities)]---
			for (Entity entity: middleEntities) {
				if (entity.getModel().hit_box.get(0).rect.intersects(chunk.rect) && !entityIsTouchingMiddleChunk(entity)) {
					middleEntities.remove(entity);
					stacheEntity(entity, chunk);
				}
			}
			//---------------------
		}
	}
	
	public void create() {
		O.shouldMove = false;
		
		//Print the start of creation
		System.out.println("[ChunkManager.create] Chunk Creation Started");
		
		//Populate the Left List
		Chunk lastChunk = new Chunk(-O.chunkWidth, O.chunkOffsetY, 0, null);
		for (int i = 0; i < ((O.screenWidth * 3) / O.chunkWidth) + 2; i++) {
			Chunk newChunk = new Chunk(-O.chunkWidth, O.chunkOffsetY, 0, lastChunk);
			chunkListLeft.add(newChunk);
			lastChunk = newChunk;
		}
		//Since the list is reversed in how it is refered, it must be reversed here
		Collections.reverse(chunkListLeft);
		//Set the last chunk as the first to be made (now at the front of the line after reversing)
		lastChunk = chunkListLeft.get(chunkListLeft.size()-1);
		
		//Fill the middle list with chunks spaced correctly
		System.out.println("[ChunkManager.create] Chunks to fill screen: " + (int)((2 * O.chunkWidth + O.screenWidth) / O.chunkWidth));
		for (int f = 0; f < (int)((2 * O.chunkWidth + O.screenWidth) / O.chunkWidth); f++) {
			//make a new chunk
			Chunk newChunk = new Chunk(-O.chunkWidth + (O.chunkWidth * f), O.chunkOffsetY, 1, lastChunk);	//Generate chunks from left to right
			//add the new chunk to the middle list
			chunkToMiddle(newChunk);
			//update the latest chunk finished
			lastChunk = newChunk;
		}
		
		//Populate the Right List
		for (int i = 0; i < ((O.screenWidth * 3) / O.chunkWidth + 2); i++) {
			Chunk newChunk = new Chunk(O.screenWidth, O.chunkOffsetY, 2, lastChunk);
			chunkListRight.add(newChunk);
			lastChunk = newChunk;
		}
		//Same as the left chunk list, the right chunk list must be reversed
		//So that it can be refered to in the correct order during movement
		Collections.reverse(chunkListRight);
		
		//Print the end of creation, and toggle movement to start again
		O.shouldMove = true;
		System.out.println("[ChunkManager.create] Chunk Creation Finished");
	}
	
	public void displayWorld(Graphics g, Rectangle camera_frame) {
		
		for (Chunk chunk: chunkListMiddle) {
			
			int startCol = (camera_frame.x - chunk.rect.x) / O.blockSize - 1;
			if (startCol < 0) startCol = 0;
			int endCol = ((camera_frame.x + camera_frame.width - chunk.rect.x) / O.blockSize) + 1;
			if (endCol >= O.chunkSize) endCol = O.chunkSize;
			if (endCol == 0 || startCol == endCol) continue;
			
			int startRow = (camera_frame.y - chunk.rect.y) / O.blockSize - 1;
			if (startRow < 0) startRow = 0;
			int endRow = (camera_frame.y + camera_frame.height - chunk.rect.y) / O.blockSize + 1;
			if (endRow >= (O.chunkSize * O.chunkRatio)) endRow = (O.chunkSize * O.chunkRatio);
			//Assumption: unlike the column calculations above, the case will never arise that
			//The screen does not display some of a given chunk vertically (can't go above or
			//below the chunks)
			
			for (int r = startRow; r < endRow; r++) {
				for (int c = startCol; c < endCol; c++) {
					//For every block in each chunk, draw it if it isn't null
					Block b = chunk.blocks[r][c];
					if (b != null && b.mold != null) {
						b.mold.display(g, b.durability, b.rect.x - camera_frame.x, b.rect.y - camera_frame.y);
					}
				}
				//Horizontal lines for block grid
				if (O.gridLines) {
					g.setColor(O.blockGridColor);
					int depth = chunk.rect.y + (r * O.blockSize) - camera_frame.y;
					g.drawLine(chunk.rect.x - camera_frame.x, depth, chunk.rect.x + chunk.rect.width - camera_frame.x, depth);
				}
			}
			
			//Vertical lines for block grid
			if (O.gridLines) {
				g.setColor(O.blockGridColor);
				for (int c = startCol; c < endCol; c++) {
					int width = chunk.rect.x + (c * O.blockSize) - camera_frame.x;
					g.drawLine(width, chunk.rect.y - camera_frame.y, width, chunk.rect.height - camera_frame.y);
				}
			}
			
			if (O.chunkLines) {
				g.setColor(Color.black);
				g.drawLine(chunk.rect.x - camera_frame.x, chunk.rect.y - camera_frame.y, chunk.rect.x - camera_frame.x, chunk.rect.y + chunk.rect.height - camera_frame.y);
			}
		}
	}
	
	public void displayEntities(Graphics g, Rectangle camera_frame) {
		//Draw Entities
		for (int i = 0; i < middleEntities.size(); i++) {
			try {
				middleEntities.get(i).display(g, camera_frame);
			} catch (IndexOutOfBoundsException e) {}
			
		}
		
		//Draw the player
		player.display(g, camera_frame);
	}
	
	public Chunk getMin() {
		//Assign the min as the first chunk
		Chunk min = chunkListMiddle.get(0);
		//For every chunk, if it is more to the left than the old min, update the min
		for (Chunk ch: chunkListMiddle) {if (ch.rect.x < min.rect.x) {min = ch; /*System.out.println("[ChunkManager] Had to reset the min");*/}}
		//Return the leftMost chunk
		return min;
	}
	
	public Chunk getMax() {
		//Set the starting max as the first chunk
		Chunk max = chunkListMiddle.get(0);
		//If any chunk is more to the right than the old max, update the max
		for (Chunk ch: chunkListMiddle) {if (ch.rect.x > max.rect.x) {max = ch;}}
		//Return the rightMost chunk
		return max;
	}
	
	public ArrayList<Chunk> sortChunkList(ArrayList<Chunk> list) {
		ArrayList<Chunk> endList = new ArrayList<Chunk>();	//Initiate an end list
		Chunk min;
		for (int i = 0; i < list.size(); i++) {				//For every chunk in the middle
			min = getMin();
			endList.add(min);							//Add to the end list the left most chunk of the middle
			list.remove(min);							//Remove the left most chunk from the old middle
		}
		//Return the end list
		return endList;
	}
	
	public Chunk getChunkLeftOf(Chunk chunk, int l) {
		
		/*  NOTE: This function only works if the chunk has
		 *        Already need initialized completely, and is
		 *        In one of the chunk lists
		 *        
		 *  ALSO: These functions aren't being used right now,
		 *        But they could be useful later on
		 */
		
		//Initialize the end chunk
		Chunk end = null;
		//Sort the middle list
		chunkListMiddle = this.sortChunkList(chunkListMiddle);
		
		//If the chunk is in the Left List
		if (l == 0) {
			for (int i = chunkListLeft.size()-1; i >= 0; i--) {	//For every chunk in the Left List
				if (chunkListLeft.get(i) == chunk) {				//If the current chunk is the inputted chunk
					if (i != 0) {										//If i isn't 0
						end = chunkListLeft.get(i-1);						//Set the end chunk as one to the left in the list from the first chunk
					} else {											//If i IS 0
						end = null;											//Set the end chunk as null; there is none to the left of it yet
					}
				}
			}
		//If the chunk is in the Middle List
		} else if (l == 1) {
			for (int i = 0; i < chunkListMiddle.size(); i++) {		//For every chunk in the Middle List
				if (chunkListMiddle.get(i) == chunk) {					//If the current chunk is the inputted chunk
					if (i != 0) {										//If i isn't 0
						end = chunkListMiddle.get(i-1);						//Set the end chunk as one to the left in the middle list from the inputted chunk
					} else {											//If i IS 0
						if (chunkListLeft.size() > 0) {						//If the left list isn't empty
							end = chunkListLeft.get(chunkListLeft.size()-1);	//set the end chunk as the top one in the Left list
						}
					}
				}
			}
		//If the chunk is in the Right List
		} else if (l == 2) {
			for (int i = chunkListRight.size()-1; i >= 0; i--) {	//For every chunk in the Right List
				if (chunkListRight.get(i) == chunk) {					//If the current chunk is the inputted chunk
					if (i != chunkListRight.size()-1) {						//If i isn't the last one in the right list
						end = chunkListRight.get(i+1);							//set the end chunk as one to the left of the inputted chunk
					} else {												//If i IS the last one in the right list
						end = getMax();											//set the end chunk as the rightMost chunk in the middle list
					}
				}
			}
		}
		//Return the chunk assigned during the loops as the end chunk
		return end;
	}
	
	public Chunk getChunkRightOf(Chunk chunk, int l) {
		
		/*  NOTE: This function only works if the chunk has
		 *        Already need initialized completely, and is
		 *        In one of the chunk lists
		 *        
		 *  ALSO: These functions aren't being used right now,
		 *  	  But they could be useful later on
		 */
		
		//Initialize the end chunk
		Chunk end = null;
		//Sort the middle list
		chunkListMiddle = this.sortChunkList(chunkListMiddle);
		
		//If the chunk is in the Left List
		if (l == 0) {
			for (int i = chunkListLeft.size()-1; i >= 0; i--) {	//For every chunk in the Left List
				if (chunkListLeft.get(i) == chunk) {				//If the current chunk is the inputted chunk
					if (i != chunkListLeft.size()-1) {					//If i insn't the last one in the list
						end = chunkListLeft.get(i+1);						//Set the end chunk as one to the right of the inputted chunk
					} else {											//If i IS the last one in the left list
						end = getMin();										//Set the end chunk as the leftMost chunk in the middle list
																			//(Assuming it is there)
					}
				}
			}
		//If the chunk is in the Middle List
		} else if (l == 1) {
			for (int i = 0; i < chunkListMiddle.size(); i++) {
				if (chunkListMiddle.get(i) == chunk) {
					if (i != chunkListMiddle.size()-1) {				//If i isn't the very rightmost one
						end = chunkListMiddle.get(i+1);						//Set the end chunk as one to the right of the inputted chunk
					} else {											//If it is the rightMost one
						if (chunkListRight.size() > 0) {					//If the Right list isn't empty
							end = chunkListRight.get(chunkListRight.size()-1);	//Set the end chunk as the Leftmost chunk in the Right chunk list
						}
					}
				}
			}
		//If the chunk is in the Right List
		} else if (l == 2) {										
			for (int i = chunkListRight.size()-1; i >= 0; i--) {	//For every chunk in the Right List
				if (chunkListRight.get(i) == chunk) {					//If the current chunk is the inputted chunk
					if (i != 0) {											//If i isn't the very rightMost chunk
						end = chunkListRight.get(i-1);							//Set the end chunk as one to the right of the inputted chunk
					} else {												//If i IS the very rightMost chunk
						end = null;												//Set the end chunk as null; There is not chunk to the right of it yet
					}
				}
			}
		}
		
		//Return the chunk assigned during the loops as the end chunk
		return end;
	}
}
