package com.matt.item;

import java.awt.Color;
import java.awt.Graphics;

import com.matt.O;
import com.matt.block.BlockMold;

/**
 * An Item is an inventory representation of something in the world.
 * An item can be a block, a tool, or just a material.
 *
 * @author Matthew Williams
 *
 */
public class Item {

	protected BlockMold placingBlock;
	protected Color color = Color.gray;
	protected String displayName = "Default Item";
	protected int id = -1, typeId = -1, stackSize = O.maxStackSize;

	public Item() {
		//Default item
	}

	public Item(int id, String name, Color color) {
		this.typeId = O.item;
		this.displayName = name;
		this.id = id;
		this.color = color;
	}

	public Item(int id, String name, Color color, BlockMold placingBlock) {
		this.typeId = O.placeableItem;
		this.displayName = name;
		this.id = id;
		this.color = color;
		this.placingBlock = placingBlock;
	}

	public Item(Item item) {
		this.typeId = item.getTypeId();
		this.displayName = item.getDisplayName();
		this.color = item.getColor();
		this.id = item.getId();
		this.placingBlock = item.getBlockMold();
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Item getNew() {
		return new Item(this);
	}

	public int getStackSize() {
		return this.stackSize;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public int getId() {
		return this.id;
	}

	public int getTypeId() {
		return this.typeId;
	}

	public BlockMold getBlockMold() {
		return this.placingBlock;
	}

	//Unimplemented
	public void use() {
		if (this.typeId == O.placeableItem) {
			//Place item as block, and remove item from inventory of player if successful
		}
	}

	public void display(Graphics g, int x, int y, double scale) {
		g.setColor(this.color);
		g.fillRect(x, y, (int)(O.itemWidth * scale), (int)(O.itemHeight * scale));
		g.setColor(Color.black);
		g.drawRect(x, y, (int)(O.itemWidth * scale), (int)(O.itemHeight * scale));
	}
}
