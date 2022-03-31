package com.matt.item.material;

/**
 * Every item tool has an itemMaterial that stores information
 * about how it behaves, and how it interacts with blocks
 * 
 * @author Matthew Williams
 *
 */
public class ItemMaterial {
	
	private int durability, harvestLevel, usePower;
	
	public ItemMaterial(int durability, int harvestLevel, int usePower) {
		this.durability = durability;
		this.harvestLevel = harvestLevel;
		this.usePower = usePower;
	}
	
	public int getDurability() {
		return this.durability;
	}
	
	public int getHarvestLevel() {
		return this.harvestLevel;
	}
	
	public int getUsePower() {
		return this.usePower;
	}
}
