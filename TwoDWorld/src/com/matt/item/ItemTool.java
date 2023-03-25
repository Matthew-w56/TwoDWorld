package com.matt.item;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.matt.O;
import com.matt.item.material.ItemMaterial;

/**
 * An Item Tool is an item that interacts differently
 * with the world than other items do.  Examples are
 * weapons, axes, shovels, etc.
 *
 * @author Matthew Williams
 *
 */
public class ItemTool extends Item {

	protected ItemMaterial material;
	protected ArrayList<String> toolTypes;
	private int durability;

	public ItemTool(int id, ItemMaterial itemMaterial, String name, Color color, String toolTypes) {
		super(id, name, color);
		this.typeId = O.toolItem;
		this.material = itemMaterial;
		this.durability = itemMaterial.getDurability();
		this.toolTypes = new ArrayList<>();
		for (String s: toolTypes.split(", ")) {
			this.toolTypes.add(s);
		}
		this.stackSize = 1;
	}

	public ItemTool(ItemTool tool) {
		this.id = tool.getId();
		this.displayName = tool.getDisplayName();
		this.color = tool.getColor();
		this.typeId = O.toolItem;
		this.material = tool.getMaterial();
		this.toolTypes = tool.getToolTypes();
		this.durability = material.getDurability();
		this.stackSize = 1;
	}

	@Override
	public ItemTool getNew() {
		return new ItemTool(this);
	}

	public ItemMaterial getMaterial() {
		return this.material;
	}

	/**
	 * Decreases this tool's durability, then returns
	 * whether the tool broke.
	 *
	 * @return True if the took broke, False otherwise
	 */
	public boolean decDurability() {
		if (this.durability != -1) {
			this.durability--;
			if (this.durability == 0) {
				return true;
			}
		}
		return false;
	}

	public int getCurrentDurability() {
		return this.durability;
	}

	public ArrayList<String> getToolTypes() {
		return this.toolTypes;
	}

	public int getHarvestLevel() {
		return this.material.getHarvestLevel();
	}

	public int getDurability() {
		return this.material.getDurability();
	}

	public int getUsePower() {
		return this.material.getUsePower();
	}

	@Override
	public void display(Graphics g, int x, int y, double scale) {
		g.setColor(this.color);
		g.fillRect(x, y, (int)(O.itemWidth * scale), (int)(O.itemHeight * scale));
	}
}
