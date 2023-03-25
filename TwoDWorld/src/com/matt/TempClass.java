/*THIS entire section that is commented out represents the previous MOVE functionality.
	 * This is left here for future reference as the movement system is re-instated
	 *

	public void move(int dx, int dy) {
		//Update physics based on gravity and speed maximums and such
		updatePhysics();

		//try to move in the x position, and actually move what was successful
		moveX(tryX(dx));

		//try to move in the y position, and actually move what was successful
		moveY(tryY(dy));
	}

	public void updatePhysics() {
		//Take the toll of gravity on the vertical speed of the player
		O.verticalSpeed -= O.gravity;
		//VS is Current Vertical Speed
		//If the VS is over the Max Speed
		if (O.verticalSpeed > O.maximumSpeed) {
			//the VS is set as the Max Speed
			O.verticalSpeed = O.maximumSpeed;

		//If the VS is under the Min Speed
		} else if (O.verticalSpeed < -O.maximumSpeed) {
			//the VS is set as the Min Speed
			O.verticalSpeed = -O.maximumSpeed;
		}

		//If the up key is pressed and the player is on the ground
		if (O.PU && O.onGround) {
			//Set the Vertical Speed as the Jump Height
			O.verticalSpeed = O.jumpHeight;
			//Set the OnGround Variable as False
			O.onGround = false;
		}
	}

	public int tryX(int dx) {
		//Set up a testRect at the same position as the player, but not refering to the player
		Rectangle testRect = new Rectangle(player.rect);

		//Move the test rect
		testRect.x -= dx;

		//If the player is colliding with any blocks, set the position relative to the first block found
		ArrayList<Block> collide_list = getCollidingBlocks(testRect);
		if (collide_list.size() > 0) {
			Block block = collide_list.get(0);
			if (dx > 0) {
				testRect.x = block.getRect().x + O.blockSize;
			} else if (dx < 0) {
				testRect.x = block.getRect().x - O.playerWidth;
			}
		}

		//Return the distance moved, reversed because of the way that moving right, is the
		//Chunks moving left and vice-versa
		return -(testRect.x - player.rect.x);
	}

	public void moveX(int dx) {
		//System.out.println("[ChunkManager.moveX] Moving the world by X: " + dx);


		//Move all the chunks by (dx)
		for (Chunk ch: chunkListMiddle) {
			ch.rect.x += dx;
		}
		for (Entity e: O.world.middleEntities) {
			e.getModel().push(dx, 0, "Chunk Manager x");
		}
		//Update the player's X position
		O.X -= dx;
		//If dx had an effect, and tutorial isn't toggled yet, toggle it.
		if ((dx > 1 || dx < -1) && !O.tutProgress[0]) {
			O.tutProgress[0] = true;
		}

		//Set the leftmost Chunk as the leftmost Chunk
		Chunk leftChunk = getMin();
		//Set the rightmost Chunk as the rightmost Chunk
		Chunk rightChunk = getMax();

		//Update the y position of all the chunks
		O.chunkY = chunkListMiddle.get(0).rect.y;

		//If they hit the left point
		if (leftChunk.rect.intersects(points[0])) {
			//Move the chunk to the left list
			chunkToLeft(leftChunk);

			//Else, if they hit the right point
		} else if(rightChunk.rect.intersects(points[1])) {
			//Move the chunk to the right list
			chunkToRight(rightChunk);
		}

		//Set the X offset as the X position of the chunk % Block Size
		//O.movementOffsetX = chunkListMiddle.get(0).rect.x % O.blockSize;


		//Decide if you need to add new chunks
		//If the leftChunk is still off the screen a little to the left, you don't need a new chunk Left
		//If the rightChunk is still off the screen a little to the right, you don't need a new chunk Right
		if (!leftChunk.rect.intersects(off[0])) { //If chunk needed Left

			//If the list is empty, populate it
			if (chunkListLeft.size() == 0) {
				chunkListLeft.add(new Chunk(-O.chunkWidth, O.chunkY, 0, getMin()));
			}

			//add the most recent chunk in the left list to the middle
			chunkToMiddle(chunkListLeft.get(chunkListLeft.size()-1));

		} else if (!rightChunk.rect.intersects(off[1])) { //Chunk needed Right

			//If the list is empty, populate it
			if (chunkListRight.size() == 0) {
				chunkListRight.add(new Chunk(O.screenWidth, O.chunkY, 2, getMax()));
			}

			//add the most recent chunk in the right list to the middle
			chunkToMiddle(chunkListRight.get(chunkListRight.size()-1));
		}
		//For every chunk in the middle
		for (Chunk chunk: chunkListMiddle) {
			//If the chunk isn't at the expected y position, set it to the normalized y position
			if (chunk.rect.y != O.chunkY) {
				chunk.rect.y = O.chunkY;
				System.out.println("[ChunkManager.movex] Had to normalize Y position");

			}
			//Update the chunk's blocks
			//chunk.updateBlocks();
		}
	}

	public int tryY(int dy) {
		//Set a testRect as the same as player rect, without refering the player rect
		Rectangle testRect = new Rectangle(player.rect);

		//Move the test rect
		testRect.y -= dy;
		//Update onGround
		O.onGround = false;

		ArrayList<Block> collide_list = world.getCollidingBlocks(testRect);
		if (collide_list.size() > 0) {
			Block block = collide_list.get(0);
			if (dy > 0) {
				testRect.y = block.getRect().y + O.blockSize;
			} else if (dy < 0) {
				testRect.y = block.getRect().y - testRect.height;
				O.verticalSpeed = 0;
				O.onGround = true;
			}
		}

		//Return the distance moved, reversed because of the way Y positions are referenced, starting 0 at the top
		//rather than 0 at the bottom
		return -(testRect.y - player.rect.y);
	}

	public void moveY(int dy) {

		//Move every chunk by (dy)
		for (Chunk ch: chunkListMiddle) {
			ch.rect.y += dy;
		}
		for (Entity e: O.world.middleEntities) {
			e.getModel().push(0, dy, "Chunk Manager Y");
		}

		//If dy had an effect, and tutorial isn't toggled yet, toggle it.
		if (dy != 0 && O.tutProgress[0]) {O.tutProgress[1] = true;}

		//Update Y-related Variables
		O.Y += dy;
		O.chunkY += dy;
		O.movementOffsetY = O.chunkY % O.blockSize;

		for (Chunk chunk: chunkListMiddle) {
			//If the chunk isn't at the expected y position, set it to the normalized y position
			if (chunk.rect.y != O.chunkY) {
				chunk.rect.y = O.chunkY;
			}
			//Update the chunk's blocks
			chunk.updateBlocks();
		}
	}*/














