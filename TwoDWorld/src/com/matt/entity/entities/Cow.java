package com.matt.entity.entities;

import java.awt.Color;

import com.matt.entity.EP;
import com.matt.entity.PassiveEntity;
import com.matt.entity.model.ModelBlock;
import com.matt.entity.model.QuadrupedModel;

/**
 * A Cow is an entity with a specific model, that wanders
 * and acts as a passive entity and a quadruped.
 *
 * @author Matthew Williams
 *
 */
public class Cow extends PassiveEntity {
	public Cow(int x, int y) {
		super(new QuadrupedModel(EP.cow_stretch, x, y, Color.white, Color.black) {
			@Override public void build() {
				this.build_basic();
				this.hit_box.add(new ModelBlock(this, cx(.13), cy(.3), cx(.06), cy(.12)));		//Spot #1  (Top spot)
				this.display_colors.add(Color.black);
				this.hit_box.add(new ModelBlock(this, cx(.34), cy(.32), cx(.2), cy(.11)));		//Spot #2
				this.display_colors.add(Color.black);
				this.hit_box.add(new ModelBlock(this, cx(.27), cy(.5), cx(.09), cy(.21)));		//Spot #3
				this.display_colors.add(Color.black);
				this.hit_box.add(new ModelBlock(this, cx(.5), cy(.47), cx(.23), cy(.24)));		//Spot #4
				this.display_colors.add(Color.black);
				this.hit_box.add(new ModelBlock(this, cx(.1), cy(.5), cx(.1), cy(.1)));			//Spot #5 (Small Spot)
				this.display_colors.add(Color.black);
			}
		});
		this.getModel().build();
	}
}
