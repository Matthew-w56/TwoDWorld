package com.matt.world;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.matt.O;
import com.matt.block.Block;
import com.matt.entity.Entity;

/**
 * A Chunk is basically a 2 dimensional array of blocks.
 * A chunk is helpful for the ChunkManager to manage
 * the world more effectively, and to allow the dynamic
 * caching and retrieval of blocks.
 * 
 * @author Matthew Williams
 *
 */
public class Chunk {
	//Create the chunk variables
	public Rectangle rect;
	public ArrayList<int[]> reqLeft, reqRight;
	public ArrayList<Entity> entities;
	Block[][] blocks = new Block[O.chunkSize * O.chunkRatio][O.chunkSize];
	int[] topPos = new int[O.chunkSize];
	Chunk chunkBefore;
	ArrayList<Block> reqLeftBlocks, reqRightBlocks;
	public int endPosL, endPosR, treeOffset, s;
	public boolean done;
	
	public Chunk(int x, int y, int s, Chunk ch) {
		this.chunkBefore = ch;
		this.treeOffset = 0;
		this.entities = new ArrayList<Entity>();
		this.rect = new Rectangle(x, y, O.chunkWidth, O.chunkHeight * O.chunkRatio);
		reqLeft = new ArrayList<int[]>();
		reqRight = new ArrayList<int[]>();
		reqLeftBlocks = new ArrayList<Block>();
		reqRightBlocks = new ArrayList<Block>();
		this.s = s;
		this.endPosL = O.midline;
		this.endPosR = O.midline;
		this.fill();
		if (s == 1) {s++;}
		this.create(s, ch);
	}
	
	public Chunk() {
		this.rect = new Rectangle(-O.chunkWidth, -O.chunkHeight, O.chunkWidth, O.chunkHeight);
	}
	
	public void fill() {
		for (int r = 0; r < O.chunkSize * O.chunkRatio; r++) {
			for (int c = 0; c < O.chunkSize; c++) {
				this.blocks[r][c] = new Block();
				this.blocks[r][c].setRect(new Rectangle(this.rect.x + (c * O.blockSize), this.rect.y + (r * O.blockSize), O.blockSize, O.blockSize));
			}
		}
	}
	
	public void create(int s, Chunk chunk2) {
		//Call the create method with a previous chunk input
		O.worldGenerator.create(this, s, chunk2);
	}
	
	public void setBlock(int r, int c, Block block) {
		//Set a block on the list with the input
		blocks[r][c] = block;
	}
	
	public void reqBlockL(int[] pos, Block block) {
		//Add a pos (row, column, block id) to the left list
		this.reqLeft.add(pos);
		this.reqLeftBlocks.add(block);
	}
	
	public void reqBlockR(int[] pos, Block block) {
		//Add a pos (row, column, block id) to the right list
		this.reqRight.add(pos);
		this.reqRightBlocks.add(block);
	}
	
	public int getPos(int c) {
		return this.topPos[c];
	}
	
	public void updateBlocks() {
		//System.out.println("[Chunk.updateBlocks] Updating Blocks");
		//For every block in the chunk, update their position
		for (int r = 0; r < O.chunkSize * O.chunkRatio; r++) {
			for (int c = 0; c < O.chunkSize; c++) {
				if (blocks[r][c] != null) {
					blocks[r][c].setPos(rect.x + (c * O.blockSize), rect.y + (r * O.blockSize));
				}
			}
		}
	}
	
	public void addPos(int r, int c) {
		//Add a block to a list of blocks that make up the top layer of ground
		this.topPos[c] = r;
	}
	
	public int[] getPosList() {
		return this.topPos;
	}
	
	public void addEntity(Entity e) {
		this.entities.add(e);
	}
	
	public void flushEntities() {
		int length = this.entities.size();
		for (int x = 0; x < length; x++) {
			this.entities.get(0).push_to_middle();
			this.entities.remove(0);
		}
	}
	
	public void setDone(boolean b) {
		this.done = b;
	}
}
