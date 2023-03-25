package com.matt.block;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.matt.O;
import com.matt.block.material.BlockMaterial;
import com.matt.item.Item;

public class BlockMold {

	protected BlockMaterial material;
	protected Item droppedItem;
	protected Color color; //In future, this will be (image / display mode)
	protected boolean placeOver;
	protected int id, maxDurability;

	public BlockMold(int id, BlockMaterial blockMaterial, Color color, boolean placeOver, String name) {
		this.id = id;
		this.material = blockMaterial;
		this.maxDurability = blockMaterial.getHardness() * O.blockHardnessMultiplier;
		this.placeOver = placeOver;
		this.color = color;
	}

	public boolean getPlaceOver() {
		return this.placeOver;
	}

	public boolean getCollides() {
		//Removed a try/catch block to catch null pointers.  I assume the material being null
		//was the problem.
		if (this.material == null) return false;
		return this.material.getCollides();
	}

	public Color getColor() {
		return this.color;
	}

	public int getHarvestLevel() {
		return this.material.getHarvestLevel();
	}

	public Item getDroppedItem() {
		return this.droppedItem;
	}

	public void setDroppedItem(Item item) {
		this.droppedItem = item;
	}

	public ArrayList<String> getToolTypes() {
		return this.material.getToolTypes();
	}

	/**
	 * Returns the Integer ID of the block
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/*I don't like the nature of this creating a new block, but this should be in block
	 *public void setTo(Block block) {
		this.id = block.id;
		this.material = block.material;
		this.color = block.getColor();
		this.droppedItem = block.droppedItem;
		this.placeOver = block.placeOver;
		this.durability = block.durability;
		this.maxDurability = block.durability;
		this.rect = new Rectangle(-O.blockSize, 0, O.blockSize, O.blockSize);
	}*/

	/**
	 * Draws this block
	 * , with the specific instance of block's durability, and the
	 * x and y that the display manager passes to it.
	 * @param g Graphics object
	 * @param durability Integer durability of the block given
	 * @param x X pos on screen
	 * @param y Y pos on screen
	 */
	public void display(Graphics g, int durability, int x, int y) {
		g.setColor(this.color);
		g.fillRect(x, y, O.blockSize, O.blockSize);
		if (O.gridLines) {
			g.setColor(O.blockGridColor);
			g.drawRect(x, y, O.blockSize, O.blockSize);
		}
		if (durability > this.maxDurability) {
			durability = maxDurability;
		} else if (durability < this.maxDurability) {
			g.setColor(Color.black);
			g.fillRect(x + 5, y + 5, O.blockSize - 10, 10);
			int barLength = ((O.blockSize - 12) * durability) / this.maxDurability;
			g.setColor(O.lightBlue);
			g.fillRect(x + 6, y + 6, barLength, 8);
		}
	}
}
