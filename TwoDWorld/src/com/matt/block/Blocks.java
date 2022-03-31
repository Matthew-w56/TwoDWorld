package com.matt.block;

import java.awt.Color;
import java.util.ArrayList;

import com.matt.O;
import com.matt.block.material.BlockMaterials;
import com.matt.item.Items;

/**
 * Singleton class that holds a master list of the different types of blocks in the world
 * 
 * @author Matthew Williams
 *
 */
public class Blocks {
	public static ArrayList<Block> blocksToTick = new ArrayList<Block>();
	
	public static Block air = new Block(0, BlockMaterials.air, O.skyBlue, true, "air");
	public static Block dirt = new Block(1, BlockMaterials.dirt, O.lightGreen, false, "dirt");
	public static Block stone = new Block(2, BlockMaterials.stone, Color.gray, false, "stone");
	public static Block wood = new Block(3, BlockMaterials.wood, O.brown, false, "wood");
	public static Block leaf = new Block(4, BlockMaterials.leaf, O.leafGreen, true, "leaf");
	public static Block bottom = new Block(5, BlockMaterials.bottom, O.offBlack, false, "bottom");
	public static Block shrub = new Block(6, BlockMaterials.leaf, O.darkGreen, true, "shrub");
	
	public static BlockChest woodChest = new BlockChest(7, BlockMaterials.wood, O.alphaBlue, false, "wood chest");
	
	public static void linkWithItems() {
		dirt.setDroppedItem(Items.dirt);
		stone.setDroppedItem(Items.stone);
		wood.setDroppedItem(Items.log);
		leaf.setDroppedItem(Items.leaf);
		bottom.setDroppedItem(Items.bottom);
		shrub.setDroppedItem(Items.shrub);
		System.out.println("[Blocks.link] Linked with items");
	}
	
	//Method that is run on regular intervals throughout the gameplay.
	public static void tick() {
		//Slowly heal all hurt blocks
		for (int i = 0; i < blocksToTick.size(); i++) {
			//Change to cap the top level, and remove from display() on Block
			blocksToTick.get(i).durability += O.healBlocksPerTick;
			if (blocksToTick.get(i).durability == blocksToTick.get(i).maxDurability) {
				blocksToTick.get(i).onTickList = false;
				blocksToTick.remove(i);
				i--;
			}
		}
	}
}
