package com.matt.entity.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.matt.O;
import com.matt.block.Block;

/**
 * Every entity has a Model, which outlines where the entity does
 * and doesn't exist.  The model holds the information about it's
 * shape and size, and handles the movement for that entity
 *
 * @author Matthew Williams
 *
 */
public class Model {

	protected final double MAX_SPEED = 20.1;

	protected int[] pos;
	public double jump_height;
	public boolean built = false;
	public boolean outline = false;
	public int width, height, direction;
	public double yVel;
	public double move_speed, speed;
	public boolean on_ground = false;
	public ArrayList<ModelBlock> hit_box, sensors;
	public ArrayList<Color> display_colors;

	public Model(int x, int y) {
		this.hit_box = new ArrayList<>();	//Start up a list of hit_box model blocks
		this.sensors = new ArrayList<>(); //Start up a list of sensor model blocks
		this.display_colors = new ArrayList<>();

		this.outline = false;
		this.speed = 0;					//Set the speed as 0; will be overridden with subclasses
		this.pos = new int[] {x, y};	//Set the pos as the inputted x and y
		this.direction = 1;				//Set the direction to 1 (facing right)
		this.yVel = 0;					//Set yVelocity as 0
	}

	public Model(int x, int y, boolean outline) {
		this.hit_box = new ArrayList<>();	//Start up a list of hit_box model blocks
		this.sensors = new ArrayList<>(); //Start up a list of sensor model blocks
		this.display_colors = new ArrayList<>();

		this.outline = outline;
		this.speed = 0;					//Set the speed as 0; will be overridden with subclasses
		this.pos = new int[] {x, y};	//Set the pos as the inputted x and y
		this.direction = 1;				//Set the direction to 1 (facing right)
		this.yVel = 0;					//Set yVelocity as 0
	}

	public void print_pos() {
		System.out.println("[Model] Current Position: (" + this.pos[0] + ", " + this.pos[1] + ")");
	}

	public void moveX(double dx) {
		if ((dx < 0 && this.direction == 1) || (dx > 0 && this.direction == 0)) {
			flip();
		}
		this.pos[0] += dx;
		update_hitBox();
	}

	public void moveY(double dy) {
		this.pos[1] += dy;
		update_hitBox();
	}

	public void push(double x, double y, String source) {
		if (x != 0 || y != 0) {
			//System.out.println("[Model] Being pushed (" + (int)x + ", " + (int)y + ") by: " + source.toUpperCase());
			this.pos[0] += (int)x;
			this.pos[1] += (int)y;
			update_hitBox();
		}
	}

	/**
	 * Sets all sensors on this model to inactive, and checks to see
	 * if the sensors collide with any of the given blocks.  If so, that
	 * sensor is set to active.
	 * @param blocks Blocks immediately around this entity
	 */
	public void updateSensors(ArrayList<Block> blocks) {
		//Reset all the sensors
		for (ModelBlock sensor: sensors) {
			sensor.active = false;
		}

		//Set any sensors that collide with a block to active
		for (Block block: blocks) {
			for (ModelBlock sensor: sensors) {
				if (!sensor.active && sensor.rect.intersects(block.getRect())) {
					sensor.active = true;
				}
			}
		}
	}

	public void update_hitBox() {	//Updates each block in the hit_box
		for (ModelBlock block: this.hit_box) {
			block.update_position(this.direction);
		}
		for (ModelBlock block: this.sensors) {
			block.update_position(this.direction);
		}
	}

	public void display(Graphics g) {		//Outline The Rectangles in the Model

		for (int x = 0; x < this.hit_box.size(); x++) {
			//Set the box color
			g.setColor(this.display_colors.get(x));
			//Fill the box in
			g.fillRect(hit_box.get(x).rect.x, hit_box.get(x).rect.y, hit_box.get(x).rect.width, hit_box.get(x).rect.height);
			//If the box color was clear, set the border color to clear
			if (this.display_colors.get(x) == O.colorNull) {
				g.setColor(O.colorNull);
			} else {
				//Otherwise, if the color was not black, set the border color to black
				g.setColor(Color.black);
			}
			//border the box
			g.drawRect(hit_box.get(x).rect.x, hit_box.get(x).rect.y, hit_box.get(x).rect.width, hit_box.get(x).rect.height);
		}

		if (this.outline || O.entityOutlines) {
			g.setColor(Color.red);
			for (ModelBlock block: this.hit_box) {		//Body Boxes
				g.drawRect(block.rect.x, block.rect.y, block.rect.width, block.rect.height);
			}

			g.setColor(Color.blue);					//Sensor Rects
			for (ModelBlock block: this.sensors) {
				g.drawRect(block.rect.x, block.rect.y, block.rect.width, block.rect.height);
			}

			g.setColor(Color.black);		//Total pos box
			g.drawRect(this.pos[0], this.pos[1], this.width, this.height);
		}
	}

	public double check_vel(double start) {
		if (start > MAX_SPEED) {
			start = MAX_SPEED;
			//System.out.println("[Model] Limiting Entity Speed to " + O.MXS);
		} else if (start < -MAX_SPEED) {
			start = -MAX_SPEED;
			//System.out.println("[Model] Limiting Entity Speed to -" + O.MXS);
		}
		return start;
	}

	public void adjust_yVel(double y, String source) {
		this.yVel += y;
		this.yVel = check_vel(this.yVel);
		//System.out.println("[Model] Y_VEL adjusted by " + y + " by: " + source.toUpperCase());
	}

	public void set_yVel(double y, String source) {
		this.yVel = check_vel(y);
		//System.out.println("[Model] Y_VEL set to " + (int)y + " by: " + source.toUpperCase());
	}

	public void flip() {
		if (this.direction == 0) {
			this.direction = 1;
		} else {
			this.direction = 0;
		}
	}

	public void faceRight() {
		this.direction = 1;
	}

	public void faceLeft() {
		this.direction = 0;
	}

	public double getYVel() {
		return this.yVel;
	}

	public boolean collidesWith(Rectangle rect) {
		for (ModelBlock box: this.hit_box) {
			if (box.rect.intersects(rect)) {
				return true;
			}
		}
		return false;
	}

	public boolean collidesWith(Model model) {
		for (ModelBlock box: this.hit_box) {
			if (box.rect.intersects(model.hit_box.get(0).rect)) {
				return true;
			}
		}
		return false;
	}

	public boolean collidesWithX(int x) {
		if (this.pos[0] < x && this.pos[0] + this.width > x) {
			return true;
		} else {
			return false;
		}
	}

	public boolean collidesWithY(int y) {
		if (this.pos[1] <= y && this.pos[1] + this.height >= y) {
			return true;
		} else {
			return false;
		}
	}

	public void setSpeed(double s) {
		this.speed = s;
	}

	public void setPos(int[] pos, String source) {
		this.pos = pos;
		//System.out.println("[Model] Pos set to [" + pos[0] + ", " + pos[1] + "] by: " + source.toUpperCase());
		this.update_hitBox();
	}

	public int cx(double ratio) {
		return (int) (this.width * ratio);
	}

	public int cy(double ratio) {
		return (int) (this.height * ratio);
	}

	public int[] getPos() {
		return this.pos;
	}

	public int getX() {
		return this.pos[0];
	}

	public int getY() {
		return this.pos[1];
	}

	public void build() {
		this.build_basic();
	}

	protected void build_basic() {}
}

