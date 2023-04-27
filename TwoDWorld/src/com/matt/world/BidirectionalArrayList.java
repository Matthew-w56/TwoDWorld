package com.matt.world;

import java.util.ArrayList;

//TODO: NEXT: Implement this in the World class.

/**
 * This class is meant to be used for the list of chunks in the World
 * class.  The idea is to have an array that can be indexed in both
 * positive and negative indices, allowing for the automatic conversion
 * of both positive and negative coordinates into chunk index positions.
 * 
 * @author Matthew Williams
 *
 * @param <T>  Type of object in the list
 */
public class BidirectionalArrayList<T> {
	
	ArrayList<T> left;
	ArrayList<T> right;
	private boolean initialized;
	
	public BidirectionalArrayList() {
		left = new ArrayList<T>();
		right = new ArrayList<T>();
		initialized = false;
	}
	
	public void addLeft(T obj) {
		if (initialized) {
			left.add(obj);
		} else {
			right.add(obj);
			initialized = true;
		}
	}
	
	public void addRight(T obj) {
		right.add(obj);
		if (!initialized) initialized = true;
	}
	
	public T get(int index) {
		if (index >= 0) {
			return right.get(index);
		} else {
			return left.get((-index) - 1);
		}
	}
	
}
