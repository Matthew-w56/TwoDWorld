package com.matt;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.matt.creation.CreationSlot;
import com.matt.display.UIManager;
import com.matt.inventory.InventoryBackslot;
import com.matt.inventory.InventorySlot;
import com.matt.inventory.Slot;
import com.matt.item.Items;
import com.matt.menu.MenuButton;
import com.matt.menu.Screens;
import com.matt.world.WorldManager;

public class InputManager implements WindowListener, MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {
	
	WorldManager world_manager;
	UIManager ui_manager;
	Player player;
	Mouse mouse; //Currently only used to store what item the mouse is holding while in an inventory setting
	
	public InputManager(WorldManager world_manager, UIManager ui_manager, Mouse mouse) {
		this.world_manager = world_manager;
		this.ui_manager = ui_manager;
		this.player = world_manager.getPlayer();
		this.mouse = mouse;
	}
	
	private void dumpMouseHeldItem() {
		if (!player.fullInv && mouse.getItem() != null) {
			player.give(mouse.getItem(), mouse.getCount());
			mouse.clearItem();
		}
	}
	
	private void handleRightClickInInventory(Slot slot) {
		if (slot != null) {
			if (mouse.item == null && slot.getCount() > 0) {
				mouse.takeHalfItemsFromSlot(slot);
			} else {
				mouse.giveOneItemToSlot(slot);
			}
		}
	}
	
	private void handleLeftClickInInventory(Slot slot) {
		if (slot != null) {
			if (slot.getItem() == mouse.item) {
				mouse.giveAllItemsToSlot(slot);
			} else {
				mouse.swapItemsWithSlot(slot);
			}
		}
	}
	
	
	
	
	
	//FROM DisplayPanel.java
	@Override
	public void keyPressed(KeyEvent e) {
		//Respond to key press
		Player player = world_manager.getPlayer();
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			if (player.fullInv) {		//If you are in the inventory
				player.toggleInv();		//Exit the inventory
				dumpMouseHeldItem();
			} else if (!(player.fullInv || O.menu.inMenu)) {	//Else, If you are not in the inventory or menu
				O.menu.show(Screens.inGameMenu);//Show the menu
			} else if (O.menu.inMenu) {		//Else, If you are in the menu
				O.menu.hide(); 					//Hide the menu
			}
			break;
		
		case KeyEvent.VK_G:
			//Toggle the chunk lines
			O.chunkLines = !O.chunkLines;
			player.give(Items.stonePickaxe.getNew(), 1);
			break;
			
		case KeyEvent.VK_B:
			//Toggle the block grid lines
			O.gridLines = !O.gridLines;
			break;
			
		case KeyEvent.VK_E:
			//Toggle the inventory
			if (!O.menu.inMenu) {
				player.toggleInv();
			}
			break;
		
		case KeyEvent.VK_V:
			//Toggle the entity lines
			O.entityOutlines = !O.entityOutlines;
			break;
		
		case KeyEvent.VK_J:
			O.blockSetPos = 0;
			O.blockSetRect = 0;
			break;
		
		case KeyEvent.VK_K:
			System.out.println("[MyPanel] Set Pos: " + O.blockSetPos);
			System.out.println("[MyPanel] Set Rect: " + O.blockSetRect);
			System.out.println("[MyPanel] Total: " + O.blockSetPos + O.blockSetRect);
			break;
		
		case KeyEvent.VK_C:
			//Open the creation window
			if (!O.menu.inMenu) {
				O.creationWindow.toggle();
			}
			break;
		
		case KeyEvent.VK_T:
			//Toggles all togglers at once
			O.displayCircle = !O.displayCircle;
			O.entityOutlines = !O.entityOutlines;
			O.gridLines = !O.gridLines;
			O.chunkLines = !O.chunkLines;
			break;
		
		default:
			//Otherwise, pass the key to the player
			player.listen(e.getKeyCode(), true);
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		//TODO: I'm not huge about the player class doing input listening.  Look into it though, could be alright
		world_manager.getPlayer().listen(e.getKeyCode(), false);
	}
	
	@Override public void keyTyped(KeyEvent e) { /*Unused*/ }
	
	
	
	
	
	//From UIManager.java
	@Override public void windowClosing(WindowEvent e) 		{
		//Pass closing event on to the ui and world managers
		ui_manager.dispose();
		world_manager.close();
	}
	
	//These all just print their event right now, I'm curious when each is called
	@Override public void windowClosed(WindowEvent e)		{ System.out.println("[UIManager] Window Closed"); }
	@Override public void windowIconified(WindowEvent e)	{ System.out.println("[UIManager] Window Iconified"); }
	@Override public void windowDeiconified(WindowEvent e) 	{ System.out.println("[UIManager] Window Deiconified"); }
	@Override public void windowActivated(WindowEvent e) 	{ System.out.println("[UIManager] Window Activated"); }
	@Override public void windowDeactivated(WindowEvent e) 	{ System.out.println("[UIManager] Window Deactivated"); }
	@Override public void windowOpened(WindowEvent e)		{ System.out.println("[UIManager] Window Opened"); }

	//@Override public void mouseWheelMoved(MouseWheelEvent e) { }
	//@Override public void mouseClicked(MouseEvent e) { }
	//@Override public void mousePressed(MouseEvent e) { }
	//@Override public void mouseReleased(MouseEvent e) { }
	//@Override public void mouseEntered(MouseEvent e) { }
	//@Override public void mouseExited(MouseEvent e) { }
	//@Override public void mouseDragged(MouseEvent e) { }
	//@Override public void mouseMoved(MouseEvent e) { }
	
	
	
	
	
	
	
	//FROM MOUSE.java
	@Override
	public void mouseDragged(MouseEvent e) {
		//Move the positions of the mouse
		O.MX = e.getX();
		O.MY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		
		switch(e.getButton()) {
		case 1:
			O.mouseLeftDown = true;
			break;
		case 3:
			O.mouseRightDown = true;
			break;
		}
		
		if (player.fullInv) {
			
			Slot found_slot = null;
			if (e.getButton() == 1) {
				for (InventorySlot hslot: player.inventory.hotbar) {
					if (hslot.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {
						found_slot = hslot;
					}
				}
				if (found_slot == null) {
					for (InventoryBackslot[] brow: player.inventory.backInv) {
						for (InventoryBackslot bslot: brow) {
							if (bslot.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {
								found_slot = bslot;
							}
						}
					}
				}
				
				//If the mouse was clicked, but didn't hit a slot
				if (found_slot == null) {
					player.toggleInv();
					dumpMouseHeldItem();
				} else {
					handleLeftClickInInventory(found_slot);
				}
				
			} else if (e.getButton() == 3) {
				for (InventorySlot slot: player.inventory.hotbar) {
					if (slot.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {
						found_slot = slot;
					}
				}
				if (found_slot == null) {
					for (InventoryBackslot[] row: player.inventory.backInv) {
						for (InventoryBackslot slot: row) {
							if (slot.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {
								found_slot = slot;
							}
						}
					}
				}
				if (found_slot == null) {
					player.toggleInv();
					dumpMouseHeldItem();
				} else {
					handleRightClickInInventory(found_slot);
				}
			}
			
			
		} else if (O.menu.inMenu) {
			
			if (e.getButton() == 1) {		//Left Click in Menu
				if (Screens.activeScreen != null) {//If you are in a menu screen
					for (MenuButton b: Screens.activeScreen.buttons) {//For every button on the screen
						if (b.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {		//If the button is touching the mouse
							b.activate();							//Activate the button
						}
					}
				}
			}
			
		} else if (O.creationWindow.visible) {
			
			if (e.getButton() == 1) {			//Left Click   in  Creation Window
				CreationSlot endSlot = null;
				for (int i = 0; i < O.creationWindow.slots.size(); i++) {
					CreationSlot slot = O.creationWindow.slots.get(i);
					if (slot.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY))  {
						endSlot = slot;
					}
				}
				player.leftClick(endSlot);
			}
			
		} else {
			//Middle Click
			if (e.getButton() == 2) O.displayBox = !O.displayBox; //Toggle if the box for the cursor is displayed or not
			//Pass clicks on to the world. This is a quick click, not a held button.  That gets taken care of elsewhere.
			world_manager.handleClickAt(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY, e.getButton());
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!O.creationWindow.visible) {
			//If the rotation is negative
			if (e.getWheelRotation() < 0) {
				//Move the selected slot
				player.selected -= 1;
				//If the slot went over the edge
				if (player.selected < 0) {
					//Place it on the other side
					player.selected = O.hotCount-1;
				}
			//If the rotation is positive
			} else if (e.getWheelRotation() > 0) {
				//Move the selected slot
				player.selected += 1;
				//If the slot went over the edge
				if (player.selected > O.hotCount-1) {
					//Place it on the other side
					player.selected = 0;
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch(e.getButton()) {
		case 1:
			O.mouseLeftDown = false;
			break;
		case 3:
			O.mouseRightDown = false;
			break;
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		//Move the positions of the mouse
		O.MX = e.getX();
		O.MY = e.getY();
		
		//Highlight the buttons on the menu that the mouse is touching, if the player is in the menu
		if (O.menu.inMenu && Screens.activeScreen != null) {
			for (MenuButton b: Screens.activeScreen.buttons) {
				if (b.rect.contains(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY)) {
					b.current_color = b.selected_color;
				} else {
					b.current_color = b.primary_color;
				}
			}
		}
		
	}
	
	
}
