package com.matt.entity.entities;

import java.awt.Color;

import com.matt.entity.EP;
import com.matt.entity.PassiveEntity;
import com.matt.entity.model.QuadrupedModel;

/**
 * A Pig is a specific entity that has it's own model,
 * and wanders.  Is a passive entity, and a quadruped.
 *
 * @author Matthew Williams
 *
 */
public class Pig extends PassiveEntity {
	public Pig(int x, int y) {
		super(new QuadrupedModel(EP.pig_stretch, x, y, Color.pink, Color.red));
		this.getModel().build();
	}
}
