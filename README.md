# TwoDWorld

This is a simple desktop game I built in my free time during Highschool.  The user is a rectangle in a world made of 2D blocks,
with a few major interesting aspects (especially for an un-trained highschool student):
- Dynamically Rendered World Chunks
   - The world is split up into large chunks, and only the chunks around the user are rendered
   - This allows the user to continue in one direction "forever", since the CPU doesn't have to process stached chunks
- Automatically Generated World
   - World features such as trees are generated algorithmically as the user continues exploring
- Custom Menu System
   - Unhappy with the jarring results of trying to overlay JavaFX or Swing, I built my own UI framework, including the defining, drawing, and creation of menus
   - Allows buttons to have results when clicked, which powered the in-game menus (see screenshots below)
   - Managed game state in stasis while menus were active
- Entities
   - The world starts up with one cow and one pig
   - I built an entire heirarchy of entity types and interfaces that allow the creation of a variety of entity types (animals, etc)
   - They wander the world, randomly deciding to pause or to walk in a direction towards some point
   - They jump when they need to get over something, and stop jumping when they can't get up to where they want
 
# Notable Classes

If you are interested in seeing a few slices of how this program was coded, see the following spots:


# Screenshots
1) Starting Screen
2) Exit Menu

![WorldStartShot](https://user-images.githubusercontent.com/26423158/161128988-4f7bd9f7-9a92-4f0f-a125-d9fdbce688c8.PNG)
![WorldExitMenuShot](https://user-images.githubusercontent.com/26423158/161129136-f0f21880-632b-4ab2-9e43-0853179a244e.PNG)
