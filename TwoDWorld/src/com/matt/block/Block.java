package com.matt.block;

import java.awt.Rectangle;

import com.matt.O;
import com.matt.item.Item;

/**
 * The world is made of blocks, which have a material, associated item,
 * and a few other characteristics.  These are the base level for the
 * world's makeup
 *
 * @author Matthew Williams
 *
 */
public class Block {
	public BlockMold mold;
	public Rectangle rect;
	public int durability;
	public boolean onTickList = false;

	public Block(BlockMold mold, Rectangle rect) {
		setTo(mold);
		this.rect = new Rectangle(0, 0, O.blockSize, O.blockSize);
		setPos(rect.x, rect.y);
	}

	public Block(BlockMold mold) {
		this.setTo(mold);
		this.rect = new Rectangle(0, 0, O.blockSize, O.blockSize);
	}

	public void setPos(int x, int y) {
		this.rect.x = x;
		this.rect.y = y;
		O.blockSetPos++;
	}

	public Rectangle getRect() {
		return this.rect;
	}

	public Item getDroppedItem() {
		return this.mold.droppedItem;
	}

	public void setTo(BlockMold mold) {
		this.durability = mold.maxDurability;
		this.mold = mold;
	}

}
