package com.matt.item.material;

/**
 * Singleton class that holds the different Item Materials
 * @author mbwil
 *
 */
public class ItemMaterials {//         Durability count, HL, mining speed
	public static final ItemMaterial wood = new ItemMaterial(32, 1, 5);
	public static final ItemMaterial stone = new ItemMaterial(128, 2, 10);
	public static final ItemMaterial leaf = new ItemMaterial(4, 2, 30);
	public static final ItemMaterial hand = new ItemMaterial(-1, 0, 3);
}
