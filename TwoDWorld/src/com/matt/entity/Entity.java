package com.matt.entity;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import com.matt.O;
import com.matt.entity.model.Model;
import com.matt.world.Chunk;
import com.matt.world.ChunkManager;

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
	public boolean is_wandering, sees_player, is_in_middle, is_walking, is_falling;
	
	public Entity(Model model) {
		this.model = model;	//Attatch the Model class to this entity
		this.sight = 0;		//Set the sight as 0, it is overridden in entity subclasses
		this.waiting = 0;	//Set the waiting time as 0 (measured in frames)
	}
	
	public abstract void update();	//Set an abstract class to be defined for subclasses
	
	public void update_validity() {
		if (this.find_chunks() == 0) {
			ArrayList<Chunk> list;
			if (this.model.getPos()[0] > 100) {
				list = ChunkManager.chunkListRight;
			} else {
				list = ChunkManager.chunkListLeft;
			}
			this.store_in_chunk(list.get(list.size()-1));
		}
	}
	
	//Add the entity to a list
	public void store_in_chunk(Chunk chunk) {
		//System.out.println("[Entity] Storing Entity in Side List");
		this.is_in_middle = false;				//Remove the middle toggle
		O.world.middleEntities.remove(this);	//Remove entity from the middle list
		chunk.addEntity(this);		//Add the entity to the chunk
		this.target_pos = new int[] {this.model.hit_box.get(0).rect.x - chunk.rect.x, this.model.hit_box.get(0).rect.y - chunk.rect.y};
									//Store the target pos for later reference
		this.home_chunk = chunk;	//Set the chunk as this entity's home_chunk
	}
	
	public void push_to_middle() {
		//System.out.println("[Entity] Retrieving Entity from Side List");
		this.is_in_middle = true;			//Set the middle toggle
		O.world.middleEntities.add(this);	//Add entity to world middle list
		if (this.home_chunk != null) {		//If the entity was in a chunk before:
			this.model.setPos(new int[] {this.home_chunk.rect.x + this.target_pos[0], this.home_chunk.rect.y + this.target_pos[1]}, "Entity (add_to_list)");
												//Set the entity's position as target position relative to chunk's position
			this.home_chunk = null;				//Forget old home_chunk
		}
	}
	
	public int find_chunks() {
		int found = 0;
		for (Chunk chunk: ChunkManager.chunkListMiddle) {
			if (this.model.hit_box.get(0).rect.intersects(chunk.rect)) {
				found++;
			}
		}
		return found;
	}
	
	public void update_vision() {
		//If the entity don't see the player, but it is close enough to
		if (!sees_player && O.findDistance((int)this.model.hit_box.get(0).rect.getCenterX(), (int)this.model.hit_box.get(0).rect.getCenterY(),
				(int)O.player.rect.getCenterX(), (int)O.player.rect.getCenterY()) <= sight) {
			//Set the sees_player to true
			//System.out.println("[Entity] Found Player");
			sees_player = true;
		//If the entity is too far to see the player, but still think it can
		} else if (sees_player && O.findDistance((int)this.model.hit_box.get(0).rect.getCenterX(), (int)this.model.hit_box.get(0).rect.getCenterY(),
				(int)O.player.rect.getCenterX(), (int)O.player.rect.getCenterY()) > sight) {
			//System.out.println("[Entity] Lost Player");
			sees_player = false;
		}
	}
	
	public void set_wander() {
		wander_dir = 1;
		if (random.nextInt(100) % 2 == 0) {
			wander_dir = 0;
		}
		this.wander = (int)(random.nextInt(600));
		is_wandering = true;
	}
	
	public void move(String source) {
		//Pass the move method to the model
		this.model.move(source);
		this.wander -= Math.abs(this.model.move_speed);
		if (decideAttack() || decideJump()) {}
	}
	
	public void display(Graphics g) {
		this.model.display(g);
	}
	
	public void push(double x, double y, String source) {
		//Pass the push method to the model
		this.model.push(x, y, source);
	}
	
	public abstract boolean decideAttack();
	
	public boolean decideJump() {
		if (this.model.on_ground && !this.model.sensors.get(0).active) {
			if (this.model.sensors.get(1).active || this.model.sensors.get(2).active) {	//If the low left sensor is active
				this.jump();
				return true;
			}
		}
		return false;
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
