package com.matt;

import java.util.ConcurrentModificationException;

import com.matt.entity.Entity;

/**
 * Builds and handles the different threads in the game
 * 
 * @author Matthew Williams
 *
 */
public class Threads {
	//Thread that runs random effects and actions meant for testing, learning, and debugging.
	public static Thread CreatorConscience = new Thread(new Runnable() {
		@Override public void run() {
			System.out.println("[Conscience] Started");
			
			System.out.println("[Conscience] Sleeping");
		}
	});
	
	public static void startThreads() {
		painter.start();
		//Set should_move as false, as a time offset, and start moveThread
		O.shouldMove = false;
		moveThread.start();
	}
	
	public static Thread moveThread = new Thread(new Runnable() {
		@Override
		public void run() {
			System.out.println("[MoveThread] Started");
			while (O.going) {  //While the program is running
				if (O.shouldMove && !O.player.fullInv && !O.menu.inMenu && !O.creationWindow.visible) {    //If you Should Move
					try {
						//System.out.println("----------------[New Frame]----------------");
						//If the left key is pressed, set the speed as negative
						if (O.PL) {
							O.currentPlayerSpeed = O.playerSpeed;
						//If the right key is pressed, set the speed as positive
						} else if (O.PR) {
							O.currentPlayerSpeed = -O.playerSpeed;
						} else {
							//Otherwise, the speed is nothing
							O.currentPlayerSpeed = 0;
						}
						
						//Move with the speed both directions
						O.chunkManager.move((int)O.currentPlayerSpeed, (int)O.verticalSpeed);
						
						//Update all the entities
						for (Entity entity: O.world.middleEntities) {
							entity.update();
						}
						
					} catch (ConcurrentModificationException e) {}
					try {
						//Wait for next frame
						Thread.sleep(O.FPS);
					} catch (InterruptedException e) {}
					
				} else if (!O.shouldMove) {
					//If you should not move
					//Wait 1/10 second, and set it to true
					try {Thread.sleep(100);} catch (InterruptedException e) {}
					O.shouldMove = true;
				} else {
					try {
						//Wait for the next frame
						Thread.sleep(O.FPS);
					} catch (InterruptedException e) {}
				}
			}
			//Print out the end of moving thread
			System.out.println("[MoveThread] Shutting Down");
		}
	});
	
	public static Thread painter = new Thread(new Runnable() {
		@Override
		public void run() {
			System.out.println("[Painter] Started");
			while (O.going) {
				O.world.repaint();
				O.player.tickMouseWatcher();
				try {Thread.sleep(O.FPS);} catch (InterruptedException e) {}
			}
			System.out.println("[Painter] Shutting Down");
		}
	});
}
