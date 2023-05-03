package com.matt.world;

import java.util.ConcurrentModificationException;
import java.util.Random;

import com.matt.O;
import com.matt.block.Block;
import com.matt.block.BlockMolds;

/**
 * Generates the landscape of one chunk given a previous chunk (for
 * structures like trees that have a chance of generating between
 * two chunks and need to be incorporated in both)
 *
 * @author Matthew Williams
 *
 */
public class WorldGenerator {

	static Random random = new Random();
	static int lastBlockIndex = O.chunkSize - 1;
	private static int groundVariation = 0;		//Variation from previous each block gets (chance 1/gv)
	
	private static final int totalVariation = 10;		//Variation from the Midline allowed in chunk generation
	private static final int cliffHeight = 7;			//Height of a cliff
	
	private static final int rockDepth = 3;				//Depth at which rocks can start showing up
	private static final int dirtDepth = 24;			//Depth at which green blocks (dirt) stops generating
	private static final int rockChance = 30;			//Chance for a block to be a rock (1/rc)
	
	private static final int treeChance = 10;			//Chance for a tree to show up (1/tc)
	private static final int shrubChance = 3;			//Chance for a shrub to show (1/sc)
	private static final int cliffChance = 200;			//Chance for a cliff to appear (1/cc)
	
	//for s, 0 is left of chunk2, 2 is right of chunk2
	public static void create(Chunk chunk, int s, Chunk chunk2) {
		setGroundLine(chunk, s, chunk2);	//Decide where the sky will meet the ground
		addGround(chunk);					//Fill in the dirt and rocks and sky accordingly
		addTrees(chunk, s, chunk2);			//Add trees along the top of the ground

		if (chunk2 != null) {
			//Reconsile the chunk's Req lists, and the neighboring
			//chunk's Req lists, if there is another chunk
			reconsile(chunk, s, chunk2);
			
			int newChunkIndex;
			if (s == 0) newChunkIndex = chunk2.index - 1;
			else newChunkIndex = chunk2.index + 1;
			
			//Mark this chunk as done and give it its index
			chunk.setDone(true, newChunkIndex);
		} else {
			//Mark this chunk as done and mark it as the first chunk (index of 0)
			chunk.setDone(true, 0);
		}
		
	}

	public static void setGroundLine(Chunk chunk, int s, Chunk chunk2) {
		if (groundVariation == 0) {
			for (int c = 0; c < O.chunkSize; c++) {
				chunk.addPos(O.midline, c);		//Fill in the ground as flat
			}
		} else {
			int last = O.midline;
			for (int c = 0; c < O.chunkSize; c++) {
				if (random.nextInt(cliffChance) == 1) {
					if (last >= O.midline) {
						chunk.addPos(last - cliffHeight, c);
						last -= cliffHeight;
					} else {
						chunk.addPos(last + cliffHeight, c);
						last += cliffHeight;
					}
				} else {
					int num = random.nextInt(groundVariation * 2);
					if (num == 1 && last - 1 > O.midline - totalVariation) {
						chunk.addPos(--last, c);
					} else if (num == 2 && last + 1 < O.midline + totalVariation) {
						chunk.addPos(++last, c);
					} else {
						chunk.addPos(last, c);
					}
				}

			}
		}
	}

	public static void addGround(Chunk chunk) {
		//Loop through rows and columns and create blocks
		for (int r = 0; r < O.chunkSize * O.chunkRatio; r++) {						//For every row in this chunk
			for (int c = 0; c < O.chunkSize; c++) {									//For every column in that row
				if (r < chunk.getPos(c)) {												//Above Midline
					chunk.setBlock(r, c, new Block(BlockMolds.air));											//Set air block
				} else if (r >= chunk.getPos(c) && r < chunk.getPos(c) + rockDepth) {//Inbetween Midline, and rock depth
					chunk.setBlock(r, c, new Block(BlockMolds.dirt));											//Set green block
				} else if (r == (O.chunkSize * O.chunkRatio) - 1) {						//Bottom Block
					chunk.setBlock(r, c, new Block(BlockMolds.bottom));											//Set bottom block
				} else if (r >= (chunk.getPos(c) + dirtDepth)) {						//Below the dirt line, and above the bottom block
					chunk.setBlock(r, c, new Block(BlockMolds.stone));											//Set rock block
				} else {															//Inbetween the rock line and dirt line (mixed area)
					if (random.nextInt(rockChance) <= r - (chunk.getPos(c) + rockDepth)) {//chance
						chunk.setBlock(r, c, new Block(BlockMolds.stone));											//Set rock block
					} else {															//The rest of the time
						chunk.setBlock(r, c, new Block(BlockMolds.dirt));											//Set green block
					}
				}
			}
		}
	}

	public static void addTrees(Chunk chunk, int s, Chunk chunk2) {
		int r;
		int startBound = 0;
		int endBound = O.chunkSize;
		if (chunk2 != null && chunk2.treeOffset > 0) {
			if (s == 0) {
				endBound -= chunk2.treeOffset;
			} else if (s == 2) {
				startBound += chunk2.treeOffset;
			}
			chunk2.treeOffset = 0;
		}
		for (int c = startBound; c < endBound; c++) {
			r = chunk.getPos(c);
			if (random.nextInt(treeChance) == 1) {
				if (c > 1 && c < lastBlockIndex-1) {
					//System.out.println("[WorldGenerator] Tree.");
					//Trunk of the tree
					chunk.setBlock(r-1, c, new Block(BlockMolds.wood));
					chunk.setBlock(r-2, c, new Block(BlockMolds.wood));
					chunk.setBlock(r-3, c, new Block(BlockMolds.wood));
					chunk.setBlock(r-4, c, new Block(BlockMolds.leaf));

					//Leaf blocks around it
					chunk.setBlock(r-4, c-1, new Block(BlockMolds.leaf));	chunk.setBlock(r-4, c-2, new Block(BlockMolds.leaf));
					chunk.setBlock(r-4, c+1, new Block(BlockMolds.leaf));	chunk.setBlock(r-4, c+2, new Block(BlockMolds.leaf));
					chunk.setBlock(r-6, c, new Block(BlockMolds.leaf));		chunk.setBlock(r-6, c+1, new Block(BlockMolds.leaf));
					chunk.setBlock(r-6, c-1, new Block(BlockMolds.leaf));	chunk.setBlock(r-5, c, new Block(BlockMolds.leaf));
					chunk.setBlock(r-5, c+1, new Block(BlockMolds.leaf));	chunk.setBlock(r-5, c-1, new Block(BlockMolds.leaf));
					chunk.setBlock(r-5, c+2, new Block(BlockMolds.leaf));	chunk.setBlock(r-5, c-2, new Block(BlockMolds.leaf));

				} else if (c < 2) {
					//Tree on left side
					//System.out.println("[WorldGenerator] Creating tree on left side of chunk");
					chunk.setBlock(r-1, c, new Block(BlockMolds.wood));		chunk.setBlock(r-2, c, new Block(BlockMolds.wood));
					chunk.setBlock(r-3, c, new Block(BlockMolds.wood));		chunk.setBlock(r-4, c, new Block(BlockMolds.leaf));
					chunk.setBlock(r-6, c, new Block(BlockMolds.leaf));		chunk.setBlock(r-5, c, new Block(BlockMolds.leaf));
					chunk.setBlock(r-4, c+1, new Block(BlockMolds.leaf));	chunk.setBlock(r-6, c+1, new Block(BlockMolds.leaf));
					chunk.setBlock(r-5, c+1, new Block(BlockMolds.leaf));	chunk.setBlock(r-4, c+2, new Block(BlockMolds.leaf));
					chunk.setBlock(r-5, c+2, new Block(BlockMolds.leaf));

					if (c > 0) { //c == 1
						chunk.setBlock(r-4, c-1, new Block(BlockMolds.leaf));
						chunk.setBlock(r-6, c-1, new Block(BlockMolds.leaf));
						chunk.setBlock(r-5, c-1, new Block(BlockMolds.leaf));
						chunk.reqBlockL(new int[] {r-4, lastBlockIndex}, new Block(BlockMolds.leaf));
						chunk.reqBlockL(new int[] {r-5, lastBlockIndex}, new Block(BlockMolds.leaf));
						chunk.treeOffset = 2;
					} else if (c == 0) {
						chunk.reqBlockL(new int[] {r-4, lastBlockIndex}, new Block(BlockMolds.leaf));
						chunk.reqBlockL(new int[] {r-6, lastBlockIndex}, new Block(BlockMolds.leaf));
						chunk.reqBlockL(new int[] {r-5, lastBlockIndex}, new Block(BlockMolds.leaf));
						chunk.reqBlockL(new int[] {r-4, lastBlockIndex-1}, new Block(BlockMolds.leaf));
						chunk.reqBlockL(new int[] {r-5, lastBlockIndex-1}, new Block(BlockMolds.leaf));
						chunk.treeOffset = 3;
					}
				} else {
					//Tree on right side
					//System.out.println("[WorldGenerator] Creating tree on right side of chunk");
					chunk.setBlock(r-1, c, new Block(BlockMolds.wood));		chunk.setBlock(r-2, c, new Block(BlockMolds.wood));
					chunk.setBlock(r-3, c, new Block(BlockMolds.wood));		chunk.setBlock(r-4, c, new Block(BlockMolds.leaf));
					chunk.setBlock(r-6, c, new Block(BlockMolds.leaf)); 	chunk.setBlock(r-5, c, new Block(BlockMolds.leaf));
					chunk.setBlock(r-4, c-1, new Block(BlockMolds.leaf));	chunk.setBlock(r-6, c-1, new Block(BlockMolds.leaf));
					chunk.setBlock(r-5, c-1, new Block(BlockMolds.leaf));	chunk.setBlock(r-4, c-2, new Block(BlockMolds.leaf));
					chunk.setBlock(r-5, c-2, new Block(BlockMolds.leaf));

					if (c < O.chunkSize-1) {
						chunk.setBlock(r-4, c+1, new Block(BlockMolds.leaf));
						chunk.setBlock(r-6, c+1, new Block(BlockMolds.leaf));
						chunk.setBlock(r-5, c+1, new Block(BlockMolds.leaf));
						chunk.reqBlockR(new int[] {r-4, 0}, new Block(BlockMolds.leaf));
						chunk.reqBlockR(new int[] {r-5, 0}, new Block(BlockMolds.leaf));
						chunk.treeOffset = 2;
					} else if (c == O.chunkSize-1) {
						chunk.reqBlockR(new int[] {r-4, 0}, new Block(BlockMolds.leaf));
						chunk.reqBlockR(new int[] {r-6, 0}, new Block(BlockMolds.leaf));
						chunk.reqBlockR(new int[] {r-5, 0}, new Block(BlockMolds.leaf));
						chunk.reqBlockR(new int[] {r-4, 1}, new Block(BlockMolds.leaf));
						chunk.reqBlockR(new int[] {r-5, 1}, new Block(BlockMolds.leaf));
						chunk.treeOffset = 3;
					}
				}
				c += 3;
			} else if (random.nextInt(shrubChance) == 1) {
				chunk.setBlock(r-1, c, new Block(BlockMolds.shrub));
			}
		}

	}

	public static void reconsile(Chunk chunk, int s, Chunk chunk2) {
		try {
			//System.out.println("[WorldGenerator] This chunk is:" + s);
			if (s == 0) {		//If the new chunk is to the left of the old chunk

				//For all the blocks the old one wanted to the left, add them to this chunk
				//System.out.println("[WorldGenerator] old Left: " + chunk2.reqLeft.size());
				for (int x = 0; x < chunk2.reqLeft.size(); x++) {
					chunk.setBlock(chunk2.reqLeft.get(x)[0], chunk2.reqLeft.get(x)[1], chunk2.reqLeftBlocks.get(x));
				}
				chunk2.reqLeft.clear();
				chunk2.reqLeftBlocks.clear();

				//For all the blocks this chunk wanted to the right, add them to the old chunk
				//System.out.println("[WorldGenerator] new right: " + chunk.reqRight.size());
				for (int x = 0; x < chunk.reqRight.size(); x++) {
					chunk2.setBlock(chunk.reqRight.get(x)[0], chunk.reqRight.get(x)[1], chunk.reqRightBlocks.get(x));
				}
				chunk.reqRight.clear();
				chunk.reqRightBlocks.clear();

			} else if (s == 2) {//If the new chunk is to the right of the old chunk

				//For all the blocks the old one wanted to the right, add them to this chunk
				for (int x = 0; x < chunk2.reqRight.size(); x++) {
					chunk.setBlock(chunk2.reqRight.get(x)[0], chunk2.reqRight.get(x)[1], chunk2.reqRightBlocks.get(x));
				}
				chunk2.reqRight.clear();
				chunk2.reqRightBlocks.clear();

				//For all the blocks this chunk wanted to the left, add them to the old chunk
				for (int x = 0; x < chunk.reqLeft.size(); x++) {
					chunk2.setBlock(chunk.reqLeft.get(x)[0], chunk.reqLeft.get(x)[1], chunk.reqLeftBlocks.get(x));
				}
				chunk.reqLeft.clear();
				chunk.reqLeftBlocks.clear();
			}
		} catch (ConcurrentModificationException e) {}
	}
}
