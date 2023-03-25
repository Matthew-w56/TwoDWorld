package com.matt.entity.model;


import java.awt.Color;

import com.matt.O;
import com.matt.entity.EP;

/**
 * Quadruped Models are the basic outlined model for any
 * four-legged entity (which is all that exists for now).
 *
 * @author Matthew Williams
 *
 */
public class QuadrupedModel extends Model {

	Color color1, color2;
	double stretch;

	public QuadrupedModel(double stretch, int x, int y, Color color1, Color color2) {
		super(x, y);
		setSpeed(2);
		this.stretch = stretch;
		this.jump_height = EP.quad_jump_height;
		this.color1 = color1;
		this.color2 = color2;
		this.width = (int)(EP.quad_width * stretch);
		this.height = (int)(EP.quad_height * stretch);
	}

	@Override
	public void build() {
		this.build_basic();
	}

	@Override
	protected void build_basic() {	//Built facing right
		this.hit_box.add(new ModelBlock(this, cx(.06), 0, (width - 2 * cx(.06)), height));	//Movemnt Hitbox
		this.display_colors.add(O.colorNull);

		this.hit_box.add(new ModelBlock(this, 0, cy(.45), cx(.1), cy(.1)));					//Tail
		this.display_colors.add(this.color2);

		this.hit_box.add(new ModelBlock(this, cx(.18), cy(.7), cx(.1), cy(.3)));			//Back Leg
		this.display_colors.add(this.color2);

		this.hit_box.add(new ModelBlock(this, cx(.65), cy(.7), cx(.1), cy(.3)));			//Front Leg
		this.display_colors.add(this.color2);

		this.hit_box.add(new ModelBlock(this, cx(.06), cy(.3), cx(.76), cy(.45)));			//Body
		this.display_colors.add(this.color1);

		this.hit_box.add(new ModelBlock(this, cx(.77), -cy(.095), cx(.05), cy(.12)));		//Ear Inside
		this.display_colors.add(this.color2);

		this.hit_box.add(new ModelBlock(this, cx(.77), -cy(.1), cx(.03), cy(.12)));			//Ear Outside
		this.display_colors.add(this.color1);

		this.hit_box.add(new ModelBlock(this, cx(.73), 0, cx(.26), cy(.4)));				//Head
		this.display_colors.add(this.color1);

		this.hit_box.add(new ModelBlock(this, cx(.93), cy(.1), cx(.03), cy(.05)));			//Eye
		this.display_colors.add(Color.black);

		//-----------------------------------------------[Differing line between Hit_box, and Sensors]------------------------------------------------------------

		this.sensors.add(new ModelBlock(this, -1, 0, width + 2, height - O.blockSize - 1));					//Sensor for checking one block up			(0)
		this.sensors.add(new ModelBlock(this, -1, height - O.blockSize + 1, 2, O.blockSize - 1));			//Sensor for checking block to the left  	(1)
		this.sensors.add(new ModelBlock(this, width - 1, height - O.blockSize + 1, 2, O.blockSize - 1));	//Sensor for checking block to the right	(2)
		this.built = true;
	}
}
