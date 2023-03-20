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
public class BlockMolds {
	public static ArrayList<Block> blocksToTick = new ArrayList<Block>();
	
	public static final BlockMold air = new BlockMold(0, BlockMaterials.air, O.skyBlue, true, "air");
	public static final BlockMold dirt = new BlockMold(1, BlockMaterials.dirt, O.lightGreen, false, "dirt");
	public static final BlockMold stone = new BlockMold(2, BlockMaterials.stone, Color.gray, false, "stone");
	public static final BlockMold wood = new BlockMold(3, BlockMaterials.wood, O.brown, false, "wood");
	public static final BlockMold leaf = new BlockMold(4, BlockMaterials.leaf, O.leafGreen, true, "leaf");
	public static final BlockMold bottom = new BlockMold(5, BlockMaterials.bottom, O.offBlack, false, "bottom");
	public static final BlockMold shrub = new BlockMold(6, BlockMaterials.leaf, O.darkGreen, true, "shrub");
	
	//public static final BlockChest woodChest = new BlockChest(7, BlockMaterials.wood, O.alphaBlue, false, "wood chest");
	
	public static void linkWithItems() {
		dirt.setDroppedItem(Items.dirt);
		stone.setDroppedItem(Items.stone);
		wood.setDroppedItem(Items.log);
		leaf.setDroppedItem(Items.leaf);
		bottom.setDroppedItem(Items.bottom);
		shrub.setDroppedItem(Items.shrub);
		System.out.println("[Blocks.linkWithItems] Linked with items");
	}
	
	//Method that is run on regular intervals throughout the gameplay.
	public static void tick() {
		//Slowly heal all hurt blocks
		for (int i = 0; i < blocksToTick.size(); i++) {
			//Heal the block, and if it is at max durability remove itself from the list
			blocksToTick.get(i).durability += O.healBlocksPerTick;
			if (blocksToTick.get(i).durability == blocksToTick.get(i).mold.maxDurability) {
				blocksToTick.get(i).onTickList = false;
				blocksToTick.remove(i);
				i--;
			}
		}
	}
}
