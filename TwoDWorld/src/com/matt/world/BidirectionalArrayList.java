package com.matt.world;

import java.util.ArrayList;
import java.util.Iterator;

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
public class BidirectionalArrayList<T> implements java.lang.Iterable<T> {
	
	private ArrayList<T> left;
	private ArrayList<T> right;
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
	
	public void removeLeft() {
		int l_size = left.size();
		if (l_size > 0) {
			left.remove(l_size-1);
		} else {
			int r_size = right.size();
			if (r_size > 0) {
				right.remove(0);
			}
		}
	}
	
	public void removeRight() {
		int r_size = right.size();
		if (r_size > 0) {
			right.remove(r_size-1);
		} else {
			int l_size = left.size();
			if (l_size > 0) {
				left.remove(0);
			}
		}
	}
	
	public T get(int index) {
		if (index >= 0) {
			return right.get(index);
		} else {
			return left.get((-index) - 1);
		}
	}
	
	/**
	 * Returns the minimum index for which there is an object
	 * in the array.  Can be negative.
	 * 
	 * @return Index of left-most object in array
	 */
	public int getMinIndex() {
		if (left.size() > 0) {
			return -left.size() + 1;
		} else {
			//If the right list is empty, 0 is a good default.
			//If the right list isn't empty, the min index would,
			//  by definition, be 0.
			return 0;
		}
	}
	
	/**
	 * 
	 */
	public int getMaxIndex() {
		if (right.size() > 0) {
			return right.size() - 1;
		} else {
			return 0;
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new BidirectionalArrayListIterator<T>(this);
	}
	
}

class BidirectionalArrayListIterator<T> implements Iterator<T> {
	
	private BidirectionalArrayList<T> list;
	private int index;
	private int maxIndex;
	
	public BidirectionalArrayListIterator (BidirectionalArrayList<T> obj) {
		this.list = obj;
		index = this.list.getMinIndex();
		maxIndex = this.list.getMaxIndex();
	}
	
	@Override
	public boolean hasNext() {
		return index < maxIndex;
	}

	@Override
	public T next() {
		index++;
		return this.list.get(index);
	}
	
}
