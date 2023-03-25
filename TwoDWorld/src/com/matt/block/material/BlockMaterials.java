package com.matt.block.material;

/**
 * Stores the different block material types
 *
 * @author Matthew Williams
 *
 */
public class BlockMaterials {					//Hardness, HL, collides, toolTypes
	public static final BlockMaterial dirt = new BlockMaterial(10, 0, true, "hand, pickaxe");
	public static final BlockMaterial stone = new BlockMaterial(20, 1, true, "pickaxe");
	public static final BlockMaterial wood = new BlockMaterial(12, 0, false, "hand, axe");
	public static final BlockMaterial leaf = new BlockMaterial(4, 0, false, "hand, axe, pickaxe");
	public static final BlockMaterial bottom = new BlockMaterial(100, 2, true, "pickaxe");
	public static final BlockMaterial air = new BlockMaterial(0, 100, false, "none");
}
