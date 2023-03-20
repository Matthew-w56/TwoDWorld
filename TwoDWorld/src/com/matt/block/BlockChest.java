package com.matt.block;

import java.awt.Color;

import com.matt.block.material.BlockMaterial;

/**
 * Not yet implimented.  The idea is that it is a block with
 * an associated inventory, allowing storage blocks, as well
 * as other things that include a block having multiple items
 * associated with it.
 * 
 * @author Matthew Williams
 *
 */
public class BlockChest extends BlockMold {
	public BlockChest(int id, BlockMaterial blockMaterial, Color color, boolean placeOver, String name) {
		super(id, blockMaterial, color, placeOver, name);
	}
	
	@Override
	public void activate(Block b) {
		super.activate(b); //TODO
	}
	
	public void open() {
		//TODO
	}
}
