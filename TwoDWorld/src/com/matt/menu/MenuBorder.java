package com.matt.menu;

import java.awt.Color;
import java.awt.Graphics;

import com.matt.O;

/**
 * Design Item for the Menu system (see docs for Menu)
 * Represents a border that can be displayed on the edges of a screen
 * 
 * @author Matthew Williams
 *
 */
public class MenuBorder {
	
	MenuScreen screen;
	int x, y, length, height, width;
	Color color;
	
	public MenuBorder(int x, int y, int length, int height) {
		this.build(x, y, length, height, O.margin, Color.black);
	}
	
	public MenuBorder(int x, int y, int length, int height, int width) {
		this.build(x, y, length, height, width, Color.black);
	}
	
	public MenuBorder(int x, int y, int length, int height, int width, Color color) {
		this.build(x, y, length, height, width, color);
	}
	
	public MenuBorder(MenuScreen screen) {
		this.build(screen.x, screen.y, screen.getWidth(), screen.getHeight(), O.margin, Color.black);
	}
	
	public MenuBorder(MenuScreen screen, int width) {
		this.build(screen.x, screen.y, screen.getWidth(), screen.getHeight(), width, Color.black);
	}
	
	public MenuBorder(MenuScreen screen, int width, Color color) {
		this.build(screen.x, screen.y, screen.getWidth(), screen.getHeight(), width, color);
	}
	
	public void build(int x, int y, int length, int height, int width, Color color) {
		this.x = x;
		this.y = y;
		this.length = length;
		this.height = height;
		this.width = width;
		this.color = color;
	}
	
	public void display(Graphics g) {
		g.setColor(this.color);
		g.fillRect(x, y, length, width);
		g.fillRect(x, y, width, height);
		g.fillRect(x + length - width, y, width, height);
		g.fillRect(x, y+height, length, width);
	}
}
