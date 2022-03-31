package com.matt.block;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.matt.O;
import com.matt.block.material.BlockMaterial;
import com.matt.entity.Entity;
import com.matt.item.Item;
import com.matt.item.ItemTool;

/**
 * The world is made of blocks, which have a material, associated item,
 * and a few other characteristics.  These are the base level for the
 * world's makeup
 * 
 * @author Matthew Williams
 *
 */
public class Block {

	public String name;
	protected BlockMaterial material;
	protected Item droppedItem;
	protected Rectangle rect;
	protected Color color;
	protected boolean placeOver, onTickList = false;
	protected int id, durability, maxDurability, barLength;
	
	public Block() {
		this.id = 0;
		this.rect = new Rectangle(0, 0, O.blockSize, O.blockSize);
		this.name = "Default/air";
	}
	
	public Block(Block block) {
		this.id = block.id;
		this.material = block.material;
		this.droppedItem = block.droppedItem;
		this.placeOver = block.placeOver;
		this.durability = block.durability;
		this.maxDurability = block.durability;
		this.rect = new Rectangle(0, 0, O.blockSize, O.blockSize);
		this.name = block.name;
		this.color = block.color;
	}
	
	public Block(int id, BlockMaterial blockMaterial, Color color, boolean placeOver, String name) {
		this.id = id;
		this.material = blockMaterial;
		this.durability = blockMaterial.getHardness() * O.blockHardnessMultiplier;
		this.maxDurability = blockMaterial.getHardness() * O.blockHardnessMultiplier;
		this.placeOver = placeOver;
		this.rect = new Rectangle(0, 0, O.blockSize, O.blockSize);
		this.name = name;
		this.color = color;
	}
	
	public boolean getPlaceOver() {
		return this.placeOver;
	}
	
	public boolean getCollides() {
		try {
			return this.material.getCollides();
		} catch (NullPointerException e) {
			return false;
		}
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
	
	public void activate() {
		boolean entity = true;
		for (Entity e: O.world.middleEntities) {
			if (e.getModel().hit_box.get(0).rect.intersects(this.rect)) {
				entity = false;
			}
		}
		Item holding = O.player.inventory.getSelectedItem();
		int distance = (int)Math.hypot(O.MX - O.player.midX, O.MY - O.player.midY);
		if (holding != null && holding.getTypeId() == O.placeableItem && 
				this.id != holding.getBlock().getId() && distance <= O.placeDistance &&
				entity && !this.rect.intersects(O.player.rect)) {
			//Place the block
			this.setTo(holding.getBlock());
			O.player.takeBySlot(O.player.selected, 1);
		}
	}
	
	public Rectangle getRect() {
		return this.rect;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setRect(Rectangle newRect) {
		this.rect = newRect;
	}
	
	public void setPos(int x, int y) {
		this.rect.x = x;
		this.rect.y = y;
	}
	
	public void hit(ItemTool tool) {
		boolean contains = false;
		for (String type: this.material.getToolTypes()) {
			if (tool.getToolTypes().contains(type)) {
				contains = true;
			}
		}
		if (contains) {
			this.durability -= tool.getUsePower();
		}
		if (this.durability <= 0) {
			this.destroy(tool);
		}
		if (!this.onTickList) {
			this.onTickList = true;
			Blocks.blocksToTick.add(this);
		}
	}
	
	public void destroy(ItemTool tool) {
		tool.decDurability();
		O.player.give(new Item(this.droppedItem), 1);
		this.setTo(Blocks.air);
	}
	
	public void setTo(Block block) {
		this.id = block.id;
		this.material = block.material;
		this.color = block.getColor();
		this.droppedItem = block.droppedItem;
		this.placeOver = block.placeOver;
		this.durability = block.durability;
		this.maxDurability = block.durability;
		this.rect = new Rectangle(-O.blockSize, 0, O.blockSize, O.blockSize);
		this.name = block.name;
	}
	
	public void display(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.rect.x, this.rect.y, this.rect.width, this.rect.height);
		if (O.gridLines) {
			g.setColor(O.blockGridColor);
			g.drawRect(this.rect.x, this.rect.y, this.rect.width, this.rect.height);
		}
		if (this.durability > this.maxDurability) {
			this.durability = maxDurability;
		} else if (this.durability < this.maxDurability) {
			g.setColor(Color.black);
			g.fillRect(this.rect.x + 5, this.rect.y + 5, this.rect.width - 10, 10);
			barLength = (int)(((this.rect.width - 10) * this.durability) / this.maxDurability);
			g.setColor(O.lightBlue);
			g.fillRect(this.rect.x + 6, this.rect.y + 6, barLength, 8);
		}
	}
}
