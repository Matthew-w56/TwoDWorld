package com.matt.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;

import com.matt.O;
import com.matt.block.Block;
import com.matt.entity.Entity;

/**
 * ChunkManager movess and stores/retrieves chunks as the player moves around.
 * This is the class that took the most time and debugging.<p>
 * 
 * The ChunkManager does a lot in terms of managing and moving the world around.
 * Notable methods to inspect are:
 *   moveX(): Handles all possible problems and requirements with the player moving side to side
 *   moveY(): Same deal, but with verticle movement. Simpler.
 *   tryX() / tryY(): Temporarily moves just the player to access how much movement will be allowed
 *   chunkToMiddle(): Retrieval proccess of a chunk.
 *   chunkToLeft(): Storage proccess of a chunk.
 *   move(): Concatenation of all the proccesses within movement
 * 
 * @author Matthew Williams
 *
 */
public class ChunkManager {
	
	//Initialize the list of chunks and blocks to use
	public static ArrayList<Chunk> chunkListLeft = new ArrayList<Chunk>();
	public static ArrayList<Chunk> chunkListMiddle = new ArrayList<Chunk>();
	public static ArrayList<Chunk> chunkListRight = new ArrayList<Chunk>();
	
	//Chunk Detection Rectangles
	Rectangle[] points = new Rectangle[] {new Rectangle(-O.chunkWidth - 1, 0, 1, O.screenHeight),
			   new Rectangle(O.screenWidth + O.chunkWidth, 0, 1, O.screenHeight)};
	Rectangle[] off = new Rectangle[] {new Rectangle(-1, 0, 1, O.screenHeight),
		    new Rectangle(O.screenWidth, 0, 1, O.screenHeight)};

	
	public ChunkManager() {}
	
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
		Rectangle testRect = new Rectangle(O.player.rect);
		
		//Move the test rect
		testRect.x -= dx;
		
		//If the player is colliding with any blocks, set the position relative to the first block found
		ArrayList<Block> collide_list = O.world.getCollidingBlocks(testRect);
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
		return -(testRect.x - O.playerX);
	}
	
	public void moveX(int dx) {
		//System.out.println("Moving the world by X: " + dx);
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
		O.movementOffsetX = chunkListMiddle.get(0).rect.x % O.blockSize;
		
		
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
				//System.out.println("[ChunkManager.movex] Had to normalize Y position");
				
			}
			//Update the chunk's blocks
			chunk.updateBlocks();
		}
	}
	
	public int tryY(int dy) {
		//Set a testRect as the same as player rect, without refering the player rect
		Rectangle testRect = new Rectangle(O.playerX, O.playerY, O.playerWidth, O.playerHeight);
		
		//Move the test rect
		testRect.y -= dy;
		//Update onGround
		O.onGround = false;
		
		ArrayList<Block> collide_list = O.world.getCollidingBlocks(testRect);
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
		return -(testRect.y - O.playerY);
	}
	
	public void moveY(int dy) {
		
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
	}
	
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
		chunk.flushEntities();
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
			for (Entity entity: O.world.middleEntities) {
				if (entity.getModel().hit_box.get(0).rect.intersects(chunk.rect) && entity.find_chunks() == 0) {
					O.world.middleEntities.remove(entity);
					entity.store_in_chunk(chunk);
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
			for (Entity entity: O.world.middleEntities) {
				if (entity.getModel().hit_box.get(0).rect.intersects(chunk.rect) && entity.find_chunks() == 0) {
					O.world.middleEntities.remove(entity);
					entity.store_in_chunk(chunk);
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
					if (chunk.blocks[r][c] != null) {chunk.blocks[r][c].display(g);}
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
