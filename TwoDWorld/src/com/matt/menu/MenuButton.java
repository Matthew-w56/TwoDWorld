package com.matt.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import com.matt.Main;
import com.matt.O;

/**
 * Design Item for the Menu system (see docs for Menu)
 * Buttons have a position, size, color, and functionality.
 *
 * @author Matthew Williams
 *
 */
public class MenuButton {

	public String text;
	public Color current_color, primary_color, selected_color;
	public int x, y, text_width, text_height, function_type, dest_id;
	public int Ml = 10;
	public int Mu = 5;
	public int Mr = 10;
	public int Md = 5;
	public Color border_color = Color.black;
	MenuScreen screen;
	Font font;
	public Rectangle rect;

	public MenuButton(MenuScreen screen, String text) {
		this.build(screen, text, Color.gray);
	}

	public MenuButton(MenuScreen screen, String text, Color color) {
		this.build(screen, text, color);
	}


	public void build(MenuScreen screen, String text, Color color) {
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		this.font = new Font("Tahoma", Font.PLAIN, 20);
		this.text_width = (int)(font.getStringBounds(text, frc).getWidth());
		this.text_height = (int)(font.getStringBounds(text, frc).getHeight());

		this.text = text;
		this.current_color = color;
		this.primary_color = color;
		int red, green, blue;
		if (color.getRed() > 225) {
			red = 255;
		} else {red = color.getRed() + 30;}
		if (color.getGreen() > 255) {
			green = 255;
		} else {green = color.getGreen() + 30;}
		if (color.getBlue() > 255) {
			blue = 255;
		} else {blue = color.getBlue() + 30;}
		this.selected_color = new Color(red, green, blue);
		this.function_type = 0;
		this.screen = screen;
		this.rect = new Rectangle(this.x + this.screen.x, this.y + this.screen.y, this.text_width + this.Ml + this.Mr, this.text_height + this.Mu + this.Md);
	}

	public void setHeight(int y) {
		//Change the height, then update the rect of the button
		this.text_height = y;
		this.rect = new Rectangle(this.x + this.screen.x, this.y + this.screen.y, this.text_width + this.Ml + this.Mr, this.text_height + this.Mu + this.Md);
	}

	public int getHeight() {
		//Return the height
		return this.text_height + Mu + Md;
	}

	public MenuButton setPos(int x, int y) {
		//Set the position of the button.  -1 signifies center
		if (x == -1) {
			this.x = (this.screen.width - this.text_width) / 2;
		} else {
			this.x = x;
		}
		if (y == -1) {
			this.y = (this.screen.height - this.text_height) / 2;
		} else {
			this.y = y;
		}
		//Update the rect
		this.rect = new Rectangle(this.x + this.screen.x, this.y + this.screen.y, this.text_width + this.Ml + this.Mr, this.text_height + this.Mu + this.Md);
		return this;
	}

	public void setWidth(int x) {
		//Change the width of the button, then update the rect
		this.text_width = x;
		this.rect = new Rectangle(this.x + this.screen.x, this.y + this.screen.y, this.text_width + this.Ml + this.Mr, this.text_height + this.Mu + this.Md);
	}

	public void setID(int id) {
		//Set the ID, which is the the screen it leads to
		this.dest_id = id;
	}

	public void setFont(Font font) {
		//Update the font, then update the rect
		this.font = font;
	}

	public void setColor(Color color) {
		//Set the color
		this.current_color = color;
		this.primary_color = color;
		int red, green, blue;
		if (color.getRed() > 225) {
			red = 255;
		} else {red = color.getRed() + 30;}
		if (color.getGreen() > 255) {
			green = 255;
		} else {green = color.getGreen() + 30;}
		if (color.getBlue() > 255) {
			blue = 255;
		} else {blue = color.getBlue() + 30;}
		this.selected_color = new Color(red, green, blue);
	}

	public void setType(int t) {
		//Set the button type
		this.function_type = t;
	}

	public void setMargins(int l, int u, int r, int d) {
		//Set the margins as inputted, then update the rect
		this.Ml = l;
		this.Mu = u;
		this.Mr = r;
		this.Md = d;
		this.rect = new Rectangle(this.x + this.screen.x, this.y + this.screen.y, this.text_width + this.Ml + this.Mr, this.text_height + this.Mu + this.Md);
	}

	public void setColor2(Color c) {
		this.border_color = c;		//Color2 is the border color of the button
	}

	public void activate() {
		if (this.function_type == 0) {		//If the button opens a new screen
			this.screen.setDisplay(false);	//Hide this screen
			if (this.dest_id != -1) {				//As long as the id isn't -1, which indecates a close of the menu (inGame: to game | Out of game: Exit program)
				for (MenuScreen s: Screens.ScreenList) {	//Find the screen to display, and set it to display
					if (s.id == this.dest_id) {						//If you found the screen
						s.setDisplay(true);							//Set it as active
					}
				}
			} else {
				O.menu.hide();
			}
		} else if (this.function_type == 1) {	//If the button closes the game
			Main.going = false;
		}
	}

	public void draw(Graphics g) {
		g.setColor(this.current_color);
		g.fillRect(this.screen.x + this.x, this.screen.y + this.y, this.text_width + Mr + Ml, this.text_height + Mu + Md);
		g.setColor(this.border_color);
		g.drawRect(this.screen.x + this.x, this.screen.y + this.y, this.text_width + Mr + Ml, this.text_height + Mu + Md);
		if (this.current_color == Color.black) {
			g.setColor(Color.white);
		} else {
			g.setColor(Color.black);
		}
		g.setFont(this.font);
		g.drawString(this.text, this.screen.x + this.x + Ml, this.screen.y + this.y + this.text_height);
	}
}
