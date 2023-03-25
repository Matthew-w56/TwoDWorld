package com.matt.entity;

import com.matt.O;

/**
 * Holds different variables for entities in a single space
 * to allow for easy debugging and tuning. Can later be
 * moved to their respective classes for cleanliness.
 *
 * I'm pretty sure EP stands for Entity_Panel
 *
 * @author Matthew Williams
 *
 */
public class EP {
	//Quadruped Variables
	public static int quad_width = 15;
	public static int quad_height = 10;
	public static int quad_jump_height = O.blockSize/3;

	//Pig Variables
	public static int pig_health = 6;
	public static double pig_stretch = 10;

	//Cow Variables
	public static int cow_health = 8;
	public static double cow_stretch = 12;



	//Biped Variables
	public static int bip_width = 9;
	public static int bip_height = 15;
	public static int bip_jump_height = O.blockSize / 2;

	//Person Variables
	public static int person_health = 15;
	public static double person_stretch = 10;
}
