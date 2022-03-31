package com.matt.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

/**
 * Design Item for the Menu system (see docs for Menu)
 * Represents a block of text to be displayed on a screen
 * 
 * @author Matthew Williams
 *
 */
public class MenuLabel {
	
	MenuScreen screen;
	String text;
	public int extra;
	Font font;
	Color color, bcolor, underline_color;
	int x, y, w, h, Mu, Md, Ml, Mr, underline_height;
	boolean underline;
	
	public MenuLabel(MenuScreen screen, String text, int textSize) {
		this.screen = screen;
		this.text = text;
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
		this.font = new Font("Tahoma", Font.PLAIN, textSize);
		this.w = (int)(font.getStringBounds(text, frc).getWidth());
		this.h = (int)(font.getStringBounds(text, frc).getHeight());
	}
	
	public void setColor(Color color) {
		//Set the label's background color
		this.color = color;
	}
	
	public MenuLabel setPos(int x, int y) {
		//Set the position of the button.  -1 signifies center
		if (x == -1) {
			this.x = (this.screen.width - this.w) / 2;
		} else {
			this.x = x;
		}
		if (y == -1) {
			this.y = (this.screen.height - this.h) / 2;
		} else {
			this.y = y;
		}
		return this;
	}
	
	public MenuLabel setUnderline(Color color, int height) {
		this.underline = true;
		this.underline_color = color;
		this.underline_height = height;
		return this;
	}
	
	public void setMarginX(int x) {
		//Set the l & r margins
		this.Ml = x;
		this.Mr = x;
	}
	
	public void setMarginY(int y) {
		//Set the u & d margins
		this.Mu = y;
		this.Md = y;
	}
		
	
	public void setBColor(Color color) {
		//Set the borderColor for the label
		this.bcolor = color;
	}
	
	public void display(Graphics g) {
		if (this.color != null) {	//If this has a color
			g.setColor(this.color);	//Set the color and Fill the background for the label
			g.fillRect(this.screen.x + this.x - Ml, this.screen.y + this.y - Mu, this.w + Ml + Mr, this.h + Mu + Md);
		}
		if (this.bcolor != null) {	//If this has a border color
			g.setColor(this.bcolor);//Set the color and Fill the border for the label
			g.drawRect(this.screen.x + this.x - Ml, this.screen.y + this.y - Mu, this.w + Ml + Mr, this.h + Mu + Md);
		}
		if (this.color == Color.black) {	//If the background color is black
			g.setColor(Color.white);			//Set the text color as white
		} else {							//Otherwise,
			g.setColor(Color.black);			//Set the text color to black
		}
		g.setFont(this.font);	//Set the font
		g.drawString(this.text, this.screen.x + this.x, this.screen.y + this.y + this.h);	//Display the label's text at the size needed
		if (this.underline) {
			g.setColor(this.underline_color);
			g.fillRect(this.x + this.screen.x, this.y + this.h + this.screen.y + 2, this.w, this.underline_height);
		}
	}
}
