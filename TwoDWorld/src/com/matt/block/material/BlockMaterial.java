package com.matt.block.material;

import java.util.ArrayList;

/**
 * Every Block has an associated BlockMaterial that dictates how the block
 * interacts with the world, and the player.  Block Material determines how
 * the block is mined, stepped on, and collected
 * 
 * @author Matthew Williams
 *
 */
public class BlockMaterial {
	protected ArrayList<String> toolTypes;
	protected boolean collides;
	protected int hardness, harvestLevel;
	
	public BlockMaterial(int hardness, int harvestLevel, boolean collides, String toolTypes) {
		this.hardness = hardness;
		this.harvestLevel = harvestLevel;
		this.collides = collides;
		this.toolTypes = new ArrayList<String>();
		for(String s: toolTypes.split(", ")) {
			this.toolTypes.add(s);
		}
	}
	
	public int getHarvestLevel() {
		return this.harvestLevel;
	}
	
	public int getHardness() {
		return this.hardness;
	}
	
	public ArrayList<String> getToolTypes() {
		return this.toolTypes;
	}
	
	public boolean getCollides() {
		return this.collides;
	}
}
