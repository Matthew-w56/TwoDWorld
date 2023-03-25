package com.matt.entity;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import com.matt.O;
import com.matt.block.Block;
import com.matt.entity.model.Model;
import com.matt.world.Chunk;

/**
 * Any moving object in the world is represented by an Entity.
 * Entities have an associated Model that stores the shape of
 * the animal/player/etc., but decisions about how to behave
 * and when to move are handled by the Entity class.
 *
 * @author Matthew Williams
 *
 */
public abstract class Entity {

	Model model;								//The entity's model
	public static Random random = new Random();	//Entity-Common random number generator
	public Chunk home_chunk;					//Chunk you are currently in; Null when in middle area
	public int[] target_pos;					//Stored as a position, in relative to home_chunk's position, for
												//Retaining position when put in/out of chunks and lists
	public int wander, wander_dir, waiting;		//The distance the entity wants to go to, and how long it is waiting until it starts to move
	int sight, health, max_health;				//Sight radius for player seeing, current health, and maximum health
	//List of boolean triggers and toggles for if the entity is wandering, sees the player, etc.
	public boolean is_wandering, is_in_middle, is_walking, is_falling;

	public Entity(Model model) {
		this.model = model;	//Attatch the Model class to this entity
		this.sight = 0;		//Set the sight as 0, it is overridden in entity subclasses
		this.waiting = 0;	//Set the waiting time as 0 (measured in frames)
	}

	public void setWander() {
		wander_dir = random.nextInt(100) % 2;
		this.wander = (random.nextInt(600));
		is_wandering = true;
	}

	public void display(Graphics g) {
		this.model.display(g);
	}

	public void push(double x, double y, String source) {
		//Pass the push method to the model
		this.model.push(x, y, source);
	}

	public void decideJump() {
		if (this.model.on_ground && !this.model.sensors.get(0).active && this.is_wandering) {
			if (this.model.sensors.get(1).active || this.model.sensors.get(2).active) {	//If the low left sensor is active
				this.jump();
			}
		}
	}

	public abstract void findMovement();

	public void updateSensors(ArrayList<Block> blocks) {
		this.model.updateSensors(blocks);
	}

	public void updatePhysics() {
		//Adjust the model's Y Velocity to account for gravity
		this.model.adjust_yVel(O.gravity, "passive entity (gravity)");
	}

	public void jump() {
		//Set the model's y velocity as the jump_height
		this.model.set_yVel(-this.model.jump_height, "Passive Entity (jump)");
	}

	public Model getModel() {
		//Return this entity's model
		return this.model;
	}
}
