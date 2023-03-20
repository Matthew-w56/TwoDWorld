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
import com.matt.entity.model.ModelBlock;
import com.matt.item.Item;
import com.matt.item.Items;

/**
 * Represents the World, which is largely an array of Chunks.
 * More on the movement and management of the world and it's
 * objects can be found in ChunkManager.
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
	
	public ArrayList<Block> getBlocks(int x, int y) {
		//This is the same function as getCollidingBlocks(rect), but
		//This method does not require the blocks to register as collision blocks to check them
		//(Used mainly by the mouse for placing and breaking things)
		
		ArrayList<Block> endList = new ArrayList<Block>();
		
		//Loop through each chunk checking for collision
		try {
			Chunk chunk;
			for (int i = 0; i < chunkListMiddle.size(); i++) {
				chunk = chunkListMiddle.get(i);
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
							if (chunk.blocks[r][c] != null && chunk.blocks[r][c].getRect() != null
								&& chunk.blocks[r][c].getRect().contains(x, y)) {
								endList.add(chunk.blocks[r][c]);
							}
						}
					}
				}
			}
		} catch (ConcurrentModificationException e) { }
		
		return endList;
	}
	
	public boolean entityIsTouchingMiddleChunk(Entity e) {
		for (Chunk chunk: chunkListMiddle) {
			if (e.getModel().hit_box.get(0).rect.intersects(chunk.rect)) {
				return true;
			}
		}
		return false;
	}
	
	public void moveEntities() {
		/*
		 * THIS IS NEXT!
		 * This method is meant to be the hub for movement handling including collision
		 * checks, velocity realization, and side effect causing (any side effects from
		 * the movement).  This does not move the camera.
		 * 
		 * Also, maybe this would be better handled in WorldManager.
		 * 
		 */
		for (Entity entity: middleEntities) {
			if (O.shouldMove) {
				entity.findMovement();					//Decide how to move
				entity.updatePhysics();					//Update Forced Variables such as gravity
			}
			//entity.move("passive entity (update)");	//Move
			entity.wander -= Math.abs(entity.getModel().move_speed);
			entity.decideJump();
			
			updateEntity(entity);
			checkShouldStacheEntity(entity);
		}
	}
	
	protected void updateEntity(Entity e) {
		//MOVE X --------------------------------------------------------------------------
		int x = (int) e.getModel().move_speed;
		//Set up a testRect at the same position as the entity
		Rectangle testRect = new Rectangle(e.getModel().hit_box.get(0).rect);
		
		//Record the starting x position for later referal
		int startX = testRect.x;
		
		//Actually move the test rect
		testRect.x += x;
		
		//Reset model sensors
		for (ModelBlock sensor: e.getModel().sensors) {
			sensor.setActive(false);
		}
		
		ArrayList<Block> collide_list = getCollidingBlocks(testRect);
		if (collide_list.size() > 0) {
			Block b = collide_list.get(0);
			if (x > 0) {
				testRect.x = b.getRect().x - testRect.width;
			} else if (x < 0) {
				testRect.x = b.getRect().x  + b.getRect().width;
			}
			
			for (Block block: collide_list) {
				for (ModelBlock sensor: e.getModel().sensors) {
					if (sensor.rect.intersects(block.getRect())) {
						sensor.setActive(true);
					}
				}
			}
		}
		
		//Return how much the testRect actually successfully moved
		int dx = testRect.x - startX;
		
		
		//MOVE Y-------------------------------------------------------------------
		int y = (int)e.getModel().yVel;
		//Set a testRect as the same as entity rect
		testRect = new Rectangle(e.getModel().hit_box.get(0).rect);
		
		//Record where it started, then move it
		int startY = testRect.y;
		testRect.y += y;
		
		//Assume on_ground to be false, then check to see if it is
		//true with the collisions
		e.getModel().on_ground = false;
		
		ArrayList<Block> collide_list_y = getCollidingBlocks(testRect);
		if (collide_list_y.size() > 0) {
			Block b = collide_list_y.get(0);
			if (y > 0) {
				testRect.y = b.getRect().y - testRect.height;
				e.getModel().yVel = 0;
				e.getModel().on_ground = true;
			} else if (y < 0) {
				testRect.y = b.getRect().y + b.getRect().height;
			}
		}
		
		//Record how much the testRect actually successfully moved, and return it
		int dy = testRect.y - startY;
		
		if (e.getModel().built) {
			//System.out.println("[Model] Movement being updated by: " + source.toUpperCase());
			e.getModel().moveX(dx);
			e.getModel().moveY(dy);
		}
		
	}
	
	/*
	 
	 public int check_movementX(int x) {
		
	}
	
	public int check_movementY(int y) {
		
	}
	 
	 */
	
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
	
	
	
	
	
	
	
	
	
	
	
	
	/*THIS entire section that is commented out represents the previous MOVE functionality.
	 * This is left here for future reference as the movement system is re-instated
	 *
	 
	public void move(int dx, int dy) {
		//Update physics based on gravity and speed maximums and such
		updatePhysics();
		
		//try to move in the x position, and actually move what was successful
		moveX(tryX(dx));
		
		//try to move in the y position, and actually move what was successful
		moveY(tryY(dy));
	}
	
	public void updatePhysics() {
		//Take the toll of gravity on the vertical speed of the player
		O.verticalSpeed -= O.gravity;
		//VS is Current Vertical Speed
		//If the VS is over the Max Speed
		if (O.verticalSpeed > O.maximumSpeed) {
			//the VS is set as the Max Speed
			O.verticalSpeed = O.maximumSpeed;
			
		//If the VS is under the Min Speed
		} else if (O.verticalSpeed < -O.maximumSpeed) {
			//the VS is set as the Min Speed
			O.verticalSpeed = -O.maximumSpeed;
		}
		
		//If the up key is pressed and the player is on the ground
		if (O.PU && O.onGround) {
			//Set the Vertical Speed as the Jump Height
			O.verticalSpeed = O.jumpHeight;
			//Set the OnGround Variable as False
			O.onGround = false;
		}
	}
	
	public int tryX(int dx) {
		//Set up a testRect at the same position as the player, but not refering to the player
		Rectangle testRect = new Rectangle(player.rect);
		
		//Move the test rect
		testRect.x -= dx;
		
		//If the player is colliding with any blocks, set the position relative to the first block found
		ArrayList<Block> collide_list = getCollidingBlocks(testRect);
		if (collide_list.size() > 0) {
			Block block = collide_list.get(0);
			if (dx > 0) {
				testRect.x = block.getRect().x + O.blockSize;
			} else if (dx < 0) {
				testRect.x = block.getRect().x - O.playerWidth;
			}
		}
		
		//Return the distance moved, reversed because of the way that moving right, is the
		//Chunks moving left and vice-versa
		return -(testRect.x - player.rect.x);
	}
	
	public void moveX(int dx) {
		//System.out.println("[ChunkManager.moveX] Moving the world by X: " + dx);
		
		//HACKSAW NATIVE CODE BLOCK - Move player, not world.
		
		O.player.rect.x -= dx;
		
		//HACKSAW NATIVE CODE BLOCK
		
		
		//Move all the chunks by (dx)
		for (Chunk ch: chunkListMiddle) {
			ch.rect.x += dx;
		}
		for (Entity e: O.world.middleEntities) {
			e.getModel().push(dx, 0, "Chunk Manager x");
		}
		//Update the player's X position
		O.X -= dx;
		//If dx had an effect, and tutorial isn't toggled yet, toggle it.
		if ((dx > 1 || dx < -1) && !O.tutProgress[0]) {
			O.tutProgress[0] = true;
		}
		
		//Set the leftmost Chunk as the leftmost Chunk
		Chunk leftChunk = getMin();
		//Set the rightmost Chunk as the rightmost Chunk
		Chunk rightChunk = getMax();
		
		//Update the y position of all the chunks
		O.chunkY = chunkListMiddle.get(0).rect.y;
		
		//If they hit the left point
		if (leftChunk.rect.intersects(points[0])) {
			//Move the chunk to the left list
			chunkToLeft(leftChunk);
			
			//Else, if they hit the right point
		} else if(rightChunk.rect.intersects(points[1])) {
			//Move the chunk to the right list
			chunkToRight(rightChunk);
		}
		
		//Set the X offset as the X position of the chunk % Block Size
		//O.movementOffsetX = chunkListMiddle.get(0).rect.x % O.blockSize;
		
		
		//Decide if you need to add new chunks
		//If the leftChunk is still off the screen a little to the left, you don't need a new chunk Left
		//If the rightChunk is still off the screen a little to the right, you don't need a new chunk Right
		if (!leftChunk.rect.intersects(off[0])) { //If chunk needed Left
			
			//If the list is empty, populate it
			if (chunkListLeft.size() == 0) {
				chunkListLeft.add(new Chunk(-O.chunkWidth, O.chunkY, 0, getMin()));
			}
			
			//add the most recent chunk in the left list to the middle
			chunkToMiddle(chunkListLeft.get(chunkListLeft.size()-1));
			
		} else if (!rightChunk.rect.intersects(off[1])) { //Chunk needed Right
			
			//If the list is empty, populate it
			if (chunkListRight.size() == 0) {
				chunkListRight.add(new Chunk(O.screenWidth, O.chunkY, 2, getMax()));
			}
			
			//add the most recent chunk in the right list to the middle
			chunkToMiddle(chunkListRight.get(chunkListRight.size()-1));
		}
		//For every chunk in the middle
		for (Chunk chunk: chunkListMiddle) {
			//If the chunk isn't at the expected y position, set it to the normalized y position
			if (chunk.rect.y != O.chunkY) {
				chunk.rect.y = O.chunkY;
				System.out.println("[ChunkManager.movex] Had to normalize Y position");
				
			}
			//Update the chunk's blocks
			//chunk.updateBlocks();
		}
	}
	
	public int tryY(int dy) {
		//Set a testRect as the same as player rect, without refering the player rect
		Rectangle testRect = new Rectangle(player.rect);
		
		//Move the test rect
		testRect.y -= dy;
		//Update onGround
		O.onGround = false;
		
		ArrayList<Block> collide_list = world.getCollidingBlocks(testRect);
		if (collide_list.size() > 0) {
			Block block = collide_list.get(0);
			if (dy > 0) {
				testRect.y = block.getRect().y + O.blockSize;
			} else if (dy < 0) {
				testRect.y = block.getRect().y - testRect.height;
				O.verticalSpeed = 0;
				O.onGround = true;
			}
		}
		
		//Return the distance moved, reversed because of the way Y positions are referenced, starting 0 at the top
		//rather than 0 at the bottom
		return -(testRect.y - player.rect.y);
	}
	
	public void moveY(int dy) {
		
		player.rect.y -= dy;
		
		//Move every chunk by (dy)
		for (Chunk ch: chunkListMiddle) {
			ch.rect.y += dy;
		}
		for (Entity e: O.world.middleEntities) {
			e.getModel().push(0, dy, "Chunk Manager Y");
		}
		
		//If dy had an effect, and tutorial isn't toggled yet, toggle it.
		if (dy != 0 && O.tutProgress[0]) {O.tutProgress[1] = true;}
		
		//Update Y-related Variables
		O.Y += dy;
		O.chunkY += dy;
		O.movementOffsetY = O.chunkY % O.blockSize;
		
		for (Chunk chunk: chunkListMiddle) {
			//If the chunk isn't at the expected y position, set it to the normalized y position
			if (chunk.rect.y != O.chunkY) {
				chunk.rect.y = O.chunkY;
			}
			//Update the chunk's blocks
			chunk.updateBlocks();
		}
	}*/
	
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
	
	public void reverseList(ArrayList<Chunk> chunkList) {
		//Reverse the list, used in the CREATE method
		Collections.reverse(chunkList);
	}
	
	public void create() {
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
		reverseList(chunkListLeft);
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
		reverseList(chunkListRight);
		
		//Print the end of creation, and toggle movement to start again
		O.shouldMove = true;
		System.out.println("[ChunkManager.create] Chunk Creation Finished");
	}

	public void display(Graphics g) {
		//Set Should Move as false to avoid problems while displaying the screen
		O.shouldMove = false;
		
		for (Chunk chunk: chunkListMiddle) {
			for (int r = 0; r < chunk.blocks.length; r++) {
				for (int c = 0; c < chunk.blocks[r].length; c++) {
					//For every block in each chunk, draw it if it isn't null
					Block b = chunk.blocks[r][c];
					if (b != null && b.mold != null) {b.mold.display(g, b.durability, b.rect.x, b.rect.y);}
				}
			}
		}
		
		//Draw the lines between the different chunks
		if (O.chunkLines) {
			g.setColor(Color.black);
			for (int i = 0; i < chunkListMiddle.size(); i++) {
				g.drawRect(chunkListMiddle.get(i).rect.x, chunkListMiddle.get(i).rect.y, O.chunkWidth, O.chunkHeight);
			}
		}
		
		//Draw Entities
		for (int i = 0; i < middleEntities.size(); i++) {
			try {
				middleEntities.get(i).display(g);
			} catch (IndexOutOfBoundsException e) {}
			
		}
		
		//Draw the player
		player.display(g);
		
		//Flag that movement can continue again
		O.shouldMove = true;
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
