package com.matt.entity;

import com.matt.O;
import com.matt.entity.model.Model;

/**
 * Passive Entities are a type of Entity that behave
 * passively.  They mainly wander, choosing random
 * locations to go to next, and spend a good deal of
 * time stationary.
 * 
 * @author Matthew Williams
 *
 */
public class PassiveEntity extends Entity {
	
	public PassiveEntity(Model model) {
		super(model);			//Create this as a new entity
		this.sight = 1000;		//Set this entity's sight as 1000 px
		this.health = 10;		//Set the health as 10
		this.max_health = 10;	//Set the max health as 10
		this.wander = 0;
	}
	
	public void update() {
		if (O.shouldMove && !O.player.fullInv && !O.menu.inMenu) {
			this.update_vision();					//Update if the entity can see the player
			this.find_movement();					//Decide how to move
			this.update_physics();					//Update Forced Variables such as gravity
			this.move("passive entity (update)");	//Move
			this.update_validity();
		}
	}
	
	public void update_physics() {
		//Adjust the model's Y Velocity to account for gravity
		this.model.adjust_yVel(O.gravity, "passive entity (gravity)");
	}
	
	public boolean decideAttack() {
		return false;
	}
	
	public void find_movement() {
		if (this.waiting == 0) {
			if (this.wander == 0) {
				this.waiting = random.nextInt(300) + 100;
				this.set_wander();
			} else if (this.wander_dir == 1) {
				//System.out.println("[PassiveEntity] Moving right");
				this.model.move_speed = this.model.speed;
			} else if (this.wander_dir == 0) {
				//System.out.println("[PassiveEntity] Moving left");
				this.model.move_speed = -this.model.speed;
			}
		} else {
			this.model.move_speed = 0;
			this.waiting --;
			if (this.waiting == 0) {
				this.set_wander();
			}
		}
	}
}
