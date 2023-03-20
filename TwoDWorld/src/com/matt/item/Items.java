package com.matt.item;

import java.awt.Color;

import com.matt.O;
import com.matt.block.BlockMolds;
import com.matt.item.material.ItemMaterials;

/**
 * Singleton class that stores the different items in the game.
 * 
 * @author Matthew Williams
 *
 */
public class Items {
	//Item Items
	public static final Item stick = new Item(11, "Stick", Color.white);
	
	//Block Items
	public static final Item dirt = new Item(1, "Dirt", O.lightGreen, BlockMolds.dirt);
	public static final Item stone = new Item(2, "Stone Block", Color.gray, BlockMolds.stone);
	public static final Item log = new Item(3, "Log", O.brown, BlockMolds.wood);
	public static final Item leaf = new Item(4, "Leaf", O.leafGreen, BlockMolds.leaf);
	public static final Item bottom = new Item(5, "Bottom Block", O.offBlack, BlockMolds.bottom);
	public static final Item shrub = new Item(6, "Shrub", O.darkGreen, BlockMolds.shrub);
	//public static final Item woodChest = new Item(11, "Wooden Chest", O.alphaBlue, BlockMolds.woodChest);
	
	//Tool Items
	public static final ItemTool hand = new ItemTool(0, ItemMaterials.hand, "hand", Color.yellow, "hand, axe, pickaxe");
	public static final ItemTool woodPickaxe = new ItemTool(7, ItemMaterials.wood, "Wooden Pickaxe", O.darkPurple, "pickaxe");
	public static final ItemTool woodAxe = new ItemTool(8, ItemMaterials.wood, "Wooden Axe", O.darkPurple, "axe");
	public static final ItemTool stonePickaxe = new ItemTool(9, ItemMaterials.stone, "Stone Pickaxe", O.lightPurple, "pickaxe");
	public static final ItemTool stoneAxe = new ItemTool(10, ItemMaterials.stone, "Stone Axe", O.lightPurple, "axe");
}
