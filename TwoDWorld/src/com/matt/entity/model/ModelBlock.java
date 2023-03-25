package com.matt.entity.model;

import java.awt.Rectangle;

/**
 * Models are made out of ModelBlocks, which are basically just
 * Rectangle objects, that are capable of turning off (or
 * "inactive") when the entity is not longer in rendering range.
 *
 * @author Matthew Williams
 *
 */
public class ModelBlock {
	public int[] pos;
	public Rectangle rect;
	Model model;
	public boolean active;

	public ModelBlock(Model model, double x, double y, double width, double height) {
		this.pos = new int[] {(int)x, (int)y};
		this.model = model;
		this.active = false;
		this.rect = new Rectangle(model.getX() + (int)x, model.getY() + (int)y, (int)width, (int)height);
	}

	public void update_position(int direction) {
		if (direction == 1) {
			this.rect = new Rectangle(model.getX() + this.pos[0], model.getY() + this.pos[1], this.rect.width, this.rect.height);
		} else if (direction == 0) {
			this.rect = new Rectangle(model.getX() + model.width - this.pos[0] - this.rect.width,
					                  model.getY() + this.pos[1], this.rect.width, this.rect.height);
		}
	}
}
